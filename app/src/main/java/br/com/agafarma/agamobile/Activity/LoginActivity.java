package br.com.agafarma.agamobile.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import br.com.agafarma.agamobile.Data.LoginDAO;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.WebApi.LoginApi;

public class LoginActivity extends AppCompatActivity {

    public static ProgressDialog ringProgressDialog;
    private EditText mCPFView;
    private EditText mSenhaView;
    private Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mCPFView = (EditText) findViewById(R.id.txtCpf);
        mSenhaView = (EditText) findViewById(R.id.txtSenha);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        //mCPFView.setText("97349704020");
        mSenhaView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    ValidaLogin();
                    return true;
                }
                return false;
            }
        });

        mCPFView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    ValidaLogin();
                    return true;
                }
                return false;
            }
        });


        btnEntrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Entrar();
            }
        });
    }

    private boolean ValidaLogin() {
        this.mCPFView.setError(null);
        this.mSenhaView.setError(null);
        String cpf = this.mCPFView.getText().toString();
        String senha = this.mSenhaView.getText().toString();
        boolean bool = true;
        if (TextUtils.isEmpty(senha)) {
            this.mSenhaView.setError("Informa a senha");
            bool = false;
        }
        if (TextUtils.isEmpty(cpf)) {
            this.mCPFView.setError("Informe seu CPF");
            bool = false;
        }
        return bool;
    }

    private void Entrar() {
        if (ValidaLogin()) {
            SessionModel.Usuario.CpfCnpj = this.mCPFView.getText().toString();
            SessionModel.Usuario.Senha = this.mSenhaView.getText().toString();
            ringProgressDialog = new Diverso().TempoTela(this);

            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LoginApi loginApi = new LoginApi();
                            try {
                                if (loginApi.Entrar(LoginActivity.this)) {
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    new LoginDAO(LoginActivity.this).Inserir(SessionModel.Usuario);
                                    LoginActivity.this.startActivity(intent);
                                    LoginActivity.ringProgressDialog.dismiss();
                                } else {
                                    LoginActivity.ringProgressDialog.dismiss();
                                    new Dialog(LoginActivity.this).ShowAlertOK("Atenção", "CPF/CNPJ ou Senha Invalida");
                                }
                                return;
                            } catch (IOException localIOException) {
                                localIOException.printStackTrace();
                                LoginActivity.ringProgressDialog.dismiss();
                                Diverso.Error(getApplicationContext());
                            }
                        }
                    });
                }
            }).start();
        }
    }

}

