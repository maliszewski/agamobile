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
import br.com.agafarma.agamobile.Model.BoletoModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.Model.TituloModel;
import br.com.agafarma.agamobile.Util.Autorizacao;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FinanceiroApi {
    private static final String DEBUG_TAG = "FinanceiroApi";

    public List<TituloModel> ListarBoleto(Context mContext, int idLoja)
            throws IOException {
        List<TituloModel> listaTituloModel = new ArrayList();
        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Call<List<TituloModel>> callResultService = new Retrofit.Builder().baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
                .create(RestFull.class).GetListaTitulo(idLoja);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        List<TituloModel> lista = callResultService.execute().body();

        if (lista != null && lista.size() > 0) {
            listaTituloModel = lista;
        }

        return listaTituloModel;
    }

    public BoletoModel BuscarBoleto(Context mContext, int idSequencia)
            throws IOException {
        BoletoModel boletoModel = new BoletoModel();
        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Call<BoletoModel> callResultService = new Retrofit.Builder().baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
                .create(RestFull.class).GetBoleto(idSequencia, SessionModel.Usuario.LojaId);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        BoletoModel b = callResultService.execute().body();

        if (b != null) {
            boletoModel = b;
        }
        return boletoModel;
    }

    public BoletoModel BuscarBoletoAgaCredi(Context mContext, int idLoja)
            throws IOException {
        BoletoModel boletoModel = new BoletoModel();
        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Call<BoletoModel> callResultService = new Retrofit.Builder().baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
                .create(RestFull.class).GetBoletoAgaCredi(idLoja);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        BoletoModel b = callResultService.execute().body();
        if (b != null) {
            boletoModel = b;
        }

        return boletoModel;
    }

}
