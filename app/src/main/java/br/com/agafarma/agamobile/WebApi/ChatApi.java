package br.com.agafarma.agamobile.WebApi;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.agafarma.agamobile.Activity.HomeActivity;
import br.com.agafarma.agamobile.Data.AvisoDAO;
import br.com.agafarma.agamobile.Data.ContatoDAO;
import br.com.agafarma.agamobile.Data.MqttMensagemDAO;
import br.com.agafarma.agamobile.Interface.RestFull;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.ContatoChatModel;
import br.com.agafarma.agamobile.Model.MqttMensagemModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaFotoModel;
import br.com.agafarma.agamobile.Util.Autorizacao;
import br.com.agafarma.agamobile.Util.Image;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatApi {
    private static final String DEBUG_TAG = "ChatApi";

    public void SincronizaContatos(Context mContext) throws IOException {
        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        RestFull service = new Retrofit.Builder().baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
                .create(RestFull.class);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Call<List<ContatoChatModel>> call = service.GetContato();

        StrictMode.setThreadPolicy(policy);
        call.enqueue(new Callback<List<ContatoChatModel>>() {

            @Override
            public void onResponse(Call<List<ContatoChatModel>> call, Response<List<ContatoChatModel>> response) {
                Log.i(DEBUG_TAG, "onResponse");

                new Thread(new Runnable() {
                    public void run() {
                        if (response.body().size() > 0) {
                            new ContatoDAO(mContext).Deletar();
                            for (ContatoChatModel item : response.body()) {
                                new ContatoDAO(mContext).Inserir(item);
                            }
                        }

                        List<MqttMensagemModel> lista = new MqttMensagemDAO(mContext).ListaIdGrupoUsuario();

                        for (MqttMensagemModel item : lista) {
                            try {
                                ContatoChatModel filtro = new ContatoChatModel();
                                filtro.Tipo = item.Tipo;
                                if (item.Tipo == 2) {
                                    filtro.IdGrupoMqtt = item.IdGrupoMqtt;
                                    if (new ContatoDAO(mContext).Buscar(filtro).size() == 0) {
                                        new MqttMensagemDAO(mContext).Deletar(item);
                                        Log.i(DEBUG_TAG, "Apagada a conversa Grupo: " + item.IdGrupoMqtt);
                                    }
                                } else if (item.Tipo == 1) {
                                    filtro.IdUsuario = item.IdUsuario;
                                    if (new ContatoDAO(mContext).Buscar(filtro).size() == 0) {
                                        new MqttMensagemDAO(mContext).Deletar(item);
                                        Log.i(DEBUG_TAG, "Apagada a conversa Usuario: " + item.IdUsuario);

                                    }
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i(DEBUG_TAG, "Contatos Sincronizados com sucesso");

                    }
                }).start();


            }

            @Override
            public void onFailure(Call<List<ContatoChatModel>> call, Throwable t) {

                Log.i(DEBUG_TAG, "Contatos Sincronizados com ERRO");

            }

        });

    }

}
