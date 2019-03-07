package br.com.agafarma.agamobile.WebApi;

import android.content.Context;
import android.graphics.Bitmap;
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
import br.com.agafarma.agamobile.Data.QuestionarioDAO;
import br.com.agafarma.agamobile.Data.QuestionarioRespostaFotoDAO;
import br.com.agafarma.agamobile.Interface.RestFull;
import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaFotoModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaModel;
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

public class QuestionarioApi {
    private static final String DEBUG_TAG = "QuestionarioApi";

    public QuestionarioApi() {
    }


    public List<QuestionarioModel> BuscarQuestionario(Context mContext)
            throws IOException {
        List<QuestionarioModel> listaQuestionarioModel = new ArrayList();
        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Call<List<QuestionarioModel>> callResultService = new Retrofit.Builder().baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
                .create(RestFull.class).GetQuestionario();
        /*
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        callResultService.enqueue(new Callback<List<QuestionarioModel>>() {
            @Override
            public void onResponse(Call<List<QuestionarioModel>> call, Response<List<QuestionarioModel>> response) {
                Log.i(DEBUG_TAG, "onResponse");
                Log.i(DEBUG_TAG, "Envio de leitura avisos com sucesso");
            }

            @Override
            public void onFailure(Call<List<QuestionarioModel>> call, Throwable t) {
                Log.i(DEBUG_TAG, "Falha de leitura avisos");
            }
        });
        */



        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        List<QuestionarioModel> lq = callResultService.execute().body();
        if (lq != null && lq.size() > 0) {
            listaQuestionarioModel = lq;
        }
        Log.i(DEBUG_TAG, "BuscarQuestionario OK");


        return listaQuestionarioModel;
    }

    public void EnviaQuestionarioResposta(Context mContext, List<QuestionarioRespostaModel> paramList, Boolean envioFoto)
            throws IOException, ParseException {

        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Call<Boolean> callResultService = new Retrofit.Builder().baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
                .create(RestFull.class)
                .PostResposta(paramList);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        callResultService.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.i(DEBUG_TAG, "onResponse");

                for (QuestionarioRespostaModel questionarioRespostaModel : paramList) {
                    if (questionarioRespostaModel.IdQuestionarioResposta > 0) {
                        QuestionarioModel q = new QuestionarioModel();
                        q.IdQuestionario = questionarioRespostaModel.IdQuestionario;
                        try {
                            new QuestionarioDAO().Deletar(mContext, q);
                            Log.i(DEBUG_TAG, "Questionario Deletado: " + questionarioRespostaModel.IdQuestionario);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }

                if (!envioFoto && HomeActivity.carregando != null){
                    HomeActivity.carregando.setVisibility(View.INVISIBLE);
                }

                Log.i(DEBUG_TAG, "Envio de respostas com sucesso");
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.i(DEBUG_TAG, "Falha no envio das respostas");
            }
        });
    }

    public void BuscaQuestionarioCancelado(Context mContext) throws IOException {

        List<QuestionarioModel> listaQuestionarioModel = new ArrayList();

        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Call<List<QuestionarioModel>> callResultService = new Retrofit.Builder().baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
                .create(RestFull.class)
                .GetQuestionarioCancelado();


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        List<QuestionarioModel> lq = callResultService.execute().body();
        if (lq.size() > 0) {
            listaQuestionarioModel = lq;
        }

        Log.i(DEBUG_TAG, "BuscaQuestionarioCancelado OK  QT: " + listaQuestionarioModel.size());

        for (QuestionarioModel qM : listaQuestionarioModel) {
            try {
                new QuestionarioDAO(mContext).Deletar(qM.IdQuestionario);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    public void UploadFile(Context mContext, List<QuestionarioRespostaFotoModel> listaFotos) throws IOException {



        OkHttpClient httpClient = new Autorizacao().GerarCabecalho(mContext);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        RestFull service = new Retrofit.Builder().baseUrl(Autorizacao.BuscaURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient).build()
                .create(RestFull.class);

        final int[] i = {0};
        for (QuestionarioRespostaFotoModel qRFoto : listaFotos) {

            File file = Image.CreateFileTemp(mContext, qRFoto.Imagem);

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Call<ResponseBody> call = service.UpLoadImagePregunta(qRFoto.IdQuestionario, qRFoto.IdQuestionarioPergunta, body);

            StrictMode.setThreadPolicy(policy);
            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    i[0]++;
                    Log.i(DEBUG_TAG, "onResponse Fotos Enviadas com Sucesso de: " + i[0] + ":" + listaFotos.size());
                    if (listaFotos.size() == i[0] && HomeActivity.carregando != null){
                        HomeActivity.carregando.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i(DEBUG_TAG, "onFailure Erro no envio de foto:  " + t.getMessage());
                    i[0]++;
                    if (listaFotos.size() == i[0] && HomeActivity.carregando != null){
                        HomeActivity.carregando.setVisibility(View.INVISIBLE);
                    }
                }

            });
        }
    }

}