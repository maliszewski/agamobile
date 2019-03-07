package br.com.agafarma.agamobile.Util;

import android.content.Context;
import android.util.Base64;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import br.com.agafarma.agamobile.Data.LoginDAO;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.Model.UsuarioModel;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Autorizacao {
    public OkHttpClient GerarCabecalho(Context mContext) throws IOException {
        if (SessionModel.Usuario == null) {
            SessionModel.Usuario = new LoginDAO(mContext).Buscar();
        }

        String credentials = SessionModel.Usuario.CpfCnpj.replace(".", "").replace("-", "").replace("/", "") + ":" + SessionModel.Usuario.Senha;
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                // Request customization: add request headers
                                Request.Builder requestBuilder = original.newBuilder()
                                        .header("Authorization", basic)
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        return okClient;
    }

    public static final String  Versao = "1";

    public static String BuscaURL() {
        //Agafarma Producao
        //return "http://mobile.agafarma.com.br:8085";

        //Agafarma HomeOffice
        //return " http://192.168.0.11";

        //Agafarma HomeOffice 2
        return " http://192.168.0.29";


        //Agafarma Teste
        //return "http://testemobile.agafarma.com.br";
    }
    public static String BuscaURLPortal() {
        return "http://portal.agafarma.com.br";
    }
    public static String BuscaBrokerMQTT() {
        //Agafarma HomeOffice
        //return "192.168.0.11";
        //Agafarma test
        return "testbroker.agafarma.com.br";
        //return "138.36.107.58";
        //return "192.168.0.29";

    }
}
