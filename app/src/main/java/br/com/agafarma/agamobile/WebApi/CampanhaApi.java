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
import br.com.agafarma.agamobile.Model.CampanhaModel;
import br.com.agafarma.agamobile.Util.Autorizacao;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CampanhaApi {
    private static final String DEBUG_TAG = "CampanhaApi";

    public CampanhaApi() {
    }


    public List<CampanhaModel> BuscaCampanhaHome(Context mContext, int idLoja)
            throws IOException {
        List<CampanhaModel> listaCampanhaModel = new ArrayList();
        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Call<List<CampanhaModel>> callResultService = new Retrofit.Builder().baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
                .create(RestFull.class).GetCampanha(idLoja);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        List<CampanhaModel> lista = callResultService.execute().body();
        if (lista != null && lista.size() > 0) {
            listaCampanhaModel = lista;
        }
        Log.i(DEBUG_TAG, "BuscarCampanha OK: " + listaCampanhaModel.size());


        return listaCampanhaModel;
    }


}
