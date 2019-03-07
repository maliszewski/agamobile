package br.com.agafarma.agamobile.WebApi;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.agafarma.agamobile.Activity.PerfilContatoActivity;
import br.com.agafarma.agamobile.Interface.RestFull;
import br.com.agafarma.agamobile.Model.LojaModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.Model.UsuarioModel;
import br.com.agafarma.agamobile.Util.Autorizacao;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.Diverso;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LojaApi {
    private static final String DEBUG_TAG = "LojaApi";

    public LojaModel BuscarLoja(Context mContext, int idLoja) throws IOException {

        LojaModel loja = new LojaModel();
        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit;
        retrofit = builder.client(httpClient).build();

        RestFull service = retrofit.create(RestFull.class);

        Call<LojaModel> callResultService = service.GetLoja(idLoja);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        loja = callResultService.execute().body();

        return loja;
    }

    public boolean SalvarLoja(Context mContext, LojaModel loja)
            throws IOException {

        Boolean retorno = false;

        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Call<Boolean> callResultService = new Retrofit.Builder().baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
                .create(RestFull.class).PostLoja(loja);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        retorno = callResultService.execute().body();

        Log.i(DEBUG_TAG, "SalvarLoja OK");
        return retorno;
    }

}
