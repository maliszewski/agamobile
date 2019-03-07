package br.com.agafarma.agamobile.WebApi;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.agafarma.agamobile.Activity.SplashActivity;
import br.com.agafarma.agamobile.Data.AvisoDAO;
import br.com.agafarma.agamobile.Data.QuestionarioDAO;
import br.com.agafarma.agamobile.Interface.RestFull;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaModel;
import br.com.agafarma.agamobile.Service.Tarefa;
import br.com.agafarma.agamobile.Util.Autorizacao;
import br.com.agafarma.agamobile.Util.DataHora;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvisoApi {
    private static final String DEBUG_TAG = "AvisoApi";

    public AvisoApi() {
    }


    public boolean BuscaAviso(Context mContext)
            throws IOException {
        List<AvisoModel> listaAvisoModel = new ArrayList();
        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Call<List<AvisoModel>> callResultService = new Retrofit.Builder().baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
                .create(RestFull.class).GetAviso();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        List<AvisoModel> lista = callResultService.execute().body();
        if (lista != null && lista.size() > 0) {
            listaAvisoModel = lista;
        }
        Log.i(DEBUG_TAG, "BuscarAviso OK: " + listaAvisoModel.size());

        for (AvisoModel aviso : listaAvisoModel) {

            new AvisoDAO(mContext).Deletar(aviso.IdAviso);
            new AvisoDAO(mContext).Inserir(aviso);

        }

        return true;
    }


    public void EnviaLeitura(Context mContext, List<AvisoModel> paramList)
            throws IOException, ParseException {

        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Call<Boolean> callResultService = new Retrofit.Builder().baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
                .create(RestFull.class)
                .PostAvisoResposta(paramList);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        callResultService.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.i(DEBUG_TAG, "onResponse");

                for (AvisoModel avisoModel : paramList) {
                    if (avisoModel.IdAviso > 0) {
                        new AvisoDAO(mContext).Deletar(avisoModel.IdAviso);
                        Log.i(DEBUG_TAG, "Aviso Deletado: " + avisoModel.IdAviso);

                    }
                }
                Log.i(DEBUG_TAG, "Envio de leitura avisos com sucesso");
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.i(DEBUG_TAG, "Falha de leitura avisos");
            }
        });
    }
}
