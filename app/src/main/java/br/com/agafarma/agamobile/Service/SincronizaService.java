package br.com.agafarma.agamobile.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.agafarma.agamobile.Activity.HomeActivity;
import br.com.agafarma.agamobile.Data.AvisoDAO;
import br.com.agafarma.agamobile.Data.QuestionarioDAO;
import br.com.agafarma.agamobile.Data.QuestionarioRespostaDAO;
import br.com.agafarma.agamobile.Data.QuestionarioRespostaFotoDAO;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaFotoModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaModel;
import br.com.agafarma.agamobile.Util.Autorizacao;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.WebApi.AvisoApi;
import br.com.agafarma.agamobile.WebApi.QuestionarioApi;

public class SincronizaService extends Service {
    private static final String DEBUG_TAG = "SincronizaService";
    private static boolean Sincronizando = false;
    private Context context;

    public SincronizaService() {
    }


    public void onCreate() {
        Log.i(DEBUG_TAG, "onCreate OK");
        this.context = this;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(DEBUG_TAG, "onBind OK");
        return null;
    }


    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        Log.i(DEBUG_TAG, "onStartCommand OK");
        if (!Sincronizando) {
            Handler mHandler = new Handler();
            mHandler.post(new Runnable() {
                public void run() {
                    new Sincroniza().execute(new Object[0]);
                }

            });
        }

        return super.onStartCommand(paramIntent, paramInt1, paramInt2);
    }


    public class Sincroniza extends AsyncTask<Object[], Object, Object> {
        Boolean paraSaida = false;

        @Override
        protected void onPostExecute(Object object) {
            Log.i(DEBUG_TAG, "Finalizando ");
            if (paraSaida && HomeActivity.carregando != null) {
                HomeActivity.carregando.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        protected Object doInBackground(Object[]... objects) {

            Sincronizando = true;
            Log.i(DEBUG_TAG, "Iniciando ");
            try {

                // Limpa Questionario Vencidos
                new QuestionarioDAO(context).LimpaQuestionariosVencidos();

                // Busca Novos Questionarios
                List<QuestionarioModel> listaQuestionarioModel = new QuestionarioApi().BuscarQuestionario(context);
                Log.i(DEBUG_TAG, "Buscou questionarios: " + listaQuestionarioModel.size());

                new QuestionarioDAO().Sincroniza(context, listaQuestionarioModel);
                Log.i(DEBUG_TAG, "Gravou questionarios: " + listaQuestionarioModel.size());

                new QuestionarioApi().BuscaQuestionarioCancelado(context);
                Log.i(DEBUG_TAG, "Questionarios Cancelados" );


                QuestionarioModel questionarioModel = new QuestionarioModel();
                questionarioModel.Status = 3;

                List<QuestionarioRespostaFotoModel> listaRespostaFoto = new QuestionarioRespostaFotoDAO(context).Buscar(questionarioModel);
                //List<QuestionarioRespostaFotoModel> listaRespostaFoto = new ArrayList();
                if (listaRespostaFoto.size() > 0) {
                    new QuestionarioApi().UploadFile(context, listaRespostaFoto);
                    Log.i(DEBUG_TAG, "Fotos Enviadas: ");
                    Thread.sleep(listaRespostaFoto.size() * (60 * 500));
                }

                /// Envio de Resposta
                List<QuestionarioRespostaModel> listaResposta = new QuestionarioRespostaDAO(context).Buscar(questionarioModel);
                Log.i(DEBUG_TAG, "Buscou respostas para envio : " + listaResposta.size());

                if (listaResposta.size() > 0) {
                    new QuestionarioApi().EnviaQuestionarioResposta(context, listaResposta, listaRespostaFoto.size() > 0);
                    Log.i(DEBUG_TAG, "Respostas Enviadas");
                }

                paraSaida = listaResposta.size() == 0 && listaRespostaFoto.size() == 0;

                // Busca Aviso
                new AvisoApi().BuscaAviso(context);
                Log.i(DEBUG_TAG, "Buscar Aviso Sincronizados");

                AvisoModel aviso = new AvisoModel();
                aviso.Tipo = 3;
                List<AvisoModel> listaAviso = new AvisoDAO(context).Buscar(aviso);
                Log.i(DEBUG_TAG, "Buscou aviso para envio : " + listaAviso.size());
                if (listaAviso.size() > 0) {
                    new AvisoApi().EnviaLeitura(context, listaAviso);
                    Log.i(DEBUG_TAG, "Leituras de aviso Enviadas");
                }

                // Inicia o servico de notificacao
                Log.i(DEBUG_TAG, "Iniciado servico de envio de notificacao ");
                Tarefa.Notificacao(context, 1);



            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(DEBUG_TAG, "doInBackground OK");
            Sincronizando = false;
            return "Sucesso";
        }
    }
}
