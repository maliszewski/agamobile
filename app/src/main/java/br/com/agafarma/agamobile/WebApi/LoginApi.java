package br.com.agafarma.agamobile.WebApi;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import br.com.agafarma.agamobile.Activity.ListaContatoActivity;
import br.com.agafarma.agamobile.Interface.RestFull;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.Model.UsuarioModel;
import br.com.agafarma.agamobile.Util.Autorizacao;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.Diverso;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginApi {
    private static final String DEBUG_TAG = "LoginApi";

    public boolean Entrar(Context mContext) throws IOException {

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

        Call<UsuarioModel> callResultService = service.GetLogin(Autorizacao.Versao);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        UsuarioModel usuario = callResultService.execute().body();

        if (usuario != null &&  usuario.UsuarioId > 0) {
            SessionModel.Usuario = usuario;
            if (SessionModel.Usuario.LojaId > 0)
                SessionModel.Loja = new LojaApi().BuscarLoja(mContext, SessionModel.Usuario.LojaId);
            return true;
        } else {
            SessionModel.Usuario = new UsuarioModel();
            return false;

        }

    }

}
