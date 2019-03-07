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

import br.com.agafarma.agamobile.Data.AvisoDAO;
import br.com.agafarma.agamobile.Interface.RestFull;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.CircularModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.Util.Autorizacao;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CircularApi {
    private static final String DEBUG_TAG = "CircularApi";

    public CircularApi() {
    }


    public List<CircularModel> BuscaNaoLida(Context mContext)
            throws IOException {
        List<CircularModel> listaCircularModel = new ArrayList();
        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Call<List<CircularModel>> callResultService = new Retrofit.Builder().baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
                .create(RestFull.class).GetCircularNaoLida(SessionModel.Usuario.LojaId);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        List<CircularModel> lista = callResultService.execute().body();
        if (lista != null && lista.size() > 0) {
            listaCircularModel = lista;
        }
        Log.i(DEBUG_TAG, "BuscaNaoLida OK: " + listaCircularModel.size());


        return listaCircularModel;
    }

    public void SalvarLida(Context mContext, int idCircular)
            throws IOException {
        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Call<Boolean> callResultService = new Retrofit.Builder().baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
                .create(RestFull.class).PostCircularLida(idCircular);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        Boolean retorno = callResultService.execute().body();
        Log.i(DEBUG_TAG, "SalvarLida OK");

    }
}
