package br.com.agafarma.agamobile.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.agafarma.agamobile.Data.AvisoDAO;
import br.com.agafarma.agamobile.Data.QuestionarioDAO;
import br.com.agafarma.agamobile.Data.QuestionarioRespostaDAO;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaModel;
import br.com.agafarma.agamobile.Util.DataHora;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.Util.Notificacao;
import br.com.agafarma.agamobile.WebApi.AvisoApi;
import br.com.agafarma.agamobile.WebApi.QuestionarioApi;

public class NotificacaoService extends Service {
    private static final String DEBUG_TAG = "NotificacaoService";
    private Context context;

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
        Notificar();
        return super.onStartCommand(paramIntent, paramInt1, paramInt2);
    }


    private void Notificar() {
        AvisoModel aviso = new AvisoModel();
        List<AvisoModel> listaAviso = new ArrayList();
        aviso.Tipo = 2;
        try {
            listaAviso = new AvisoDAO(getApplicationContext()).BuscarNotificacao();
            for (AvisoModel item : listaAviso) {
                item.DataNotificacao = new DataHora(new Date()).addMinute(30);
                //item.DataNotificacao = new Date();
                Log.i(DEBUG_TAG, "Nova Data de Notificacao: " + item.DataNotificacao);

                new AvisoDAO(getApplicationContext()).Atualizar(item);
                aviso = item;
            }
            Notificacao notificacao = new Notificacao();

            Log.i(DEBUG_TAG, "Aplicacao esta Rodando: " + Diverso.isAplicativoRodando(getApplicationContext()));

            //if (listaAviso.size() > 0 && !Diverso.isAplicativoRodando(getApplicationContext())) {
            if (listaAviso.size() > 1)
                notificacao.Mostrar(getApplicationContext(), "Agafarma Aviso", "Você possui " + listaAviso.size() + " avisos não lidos", "Info", 0);
            else if (listaAviso.size() == 1)
                notificacao.Mostrar(getApplicationContext(), "Agafarma Aviso", aviso.Descricao, "Info", aviso.IdAviso);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
