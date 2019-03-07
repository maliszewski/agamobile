package br.com.agafarma.agamobile.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import br.com.agafarma.agamobile.Data.LoginDAO;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.Model.UsuarioModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Service.SincronizaService;
import br.com.agafarma.agamobile.Service.Tarefa;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.WebApi.LoginApi;

public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarLogin();
            }
        }, 2000);
    }

    private void mostrarLogin() {
        SessionModel.Usuario = new UsuarioModel();
        LoginApi localLoginApi = new LoginApi();
        Intent localIntent = new Intent(this, LoginActivity.class);
        try {
            UsuarioModel localUsuarioModel = new LoginDAO(this).Buscar();
            Intent intent = new Intent(SplashActivity.this,
                    LoginActivity.class);

            if (localUsuarioModel.UsuarioId > 0) {
                Tarefa.Notificacao(SplashActivity.this, 1);
                SessionModel.Usuario = localUsuarioModel;
                if (Diverso.isOnline(this)) {
                    if (localLoginApi.Entrar(SplashActivity.this)) {
                        if (SessionModel.Usuario.Mensagem != null && SessionModel.Usuario.Mensagem.length() > 0 && SessionModel.Usuario.Status.equals("N")) {
                            new Dialog(SplashActivity.this).ShowAlertOK("Atenção", SessionModel.Usuario.Mensagem, true);
                            return;
                        }

                        if (Diverso.isWifiConnected(this)) {
                            Intent intentService = new Intent(this, SincronizaService.class);
                            SplashActivity.this.startService(intentService);
                        }
                        intent = new Intent(this, HomeActivity.class);
                    } else {
                        new LoginDAO(this).Deletar(SessionModel.Usuario);
                    }
                } else if (!Diverso.isOnline(this) && localUsuarioModel.LojaId == 0) {
                    intent = new Intent(this, HomeActivity.class);
                }
            }
            startActivity(intent);
            finish();
            return;
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
            Diverso.Error(getApplicationContext());
        }
    }
}
