package br.com.agafarma.agamobile.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.Util.MaskEditUtil;
import br.com.agafarma.agamobile.Util.ToolBar;
import br.com.agafarma.agamobile.WebApi.LojaApi;

public class PerfilLocalizacaoActivity extends AppCompatActivity {

    private static String TAG = "PerfilLocalizacaoActivity";
    private EditText txtEndereco;
    private EditText txtBairro;
    private EditText txtCidade;
    private EditText txtCEP;
    private EditText txtEstado;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_localizacao);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);

        txtEndereco = (EditText) findViewById(R.id.txt_endereco);
        txtBairro = (EditText) findViewById(R.id.txt_bairro);
        txtCidade = (EditText) findViewById(R.id.txt_cidade);
        txtCEP = (EditText) findViewById(R.id.txt_cep);
        txtEstado = (EditText) findViewById(R.id.txt_estado);

        txtCEP.addTextChangedListener(MaskEditUtil.mask(txtCEP, "#####-###"));
        btnSalvar = (Button) findViewById(R.id.btn_salvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean alterar = CarregaSessaoLoja();

                ProgressDialog progressDialog = new Diverso().TempoTela(PerfilLocalizacaoActivity.this);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(25);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                boolean alterado = false;

                                if (alterar) {
                                    try {
                                        alterado = new LojaApi().SalvarLoja(PerfilLocalizacaoActivity.this, SessionModel.Loja);
                                        progressDialog.dismiss();
                                        new Dialog(PerfilLocalizacaoActivity.this).ShowAlertOK("Atenção", "Dados Salvo com Sucesso", true);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                        Diverso.Error(getApplicationContext());
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    new Dialog(PerfilLocalizacaoActivity.this).ShowAlertOK("Atenção", "Dados Salvo com Sucesso", true);
                                }
                            }
                        });
                    }
                }).start();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Carrega();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void Carrega() {
        txtEndereco.setText(SessionModel.Loja.Endereco);
        txtBairro.setText(SessionModel.Loja.Bairro);
        txtCidade.setText(SessionModel.Loja.Cidade);
        txtCEP.setText(SessionModel.Loja.Cep);
        txtEstado.setText(SessionModel.Loja.Estado);
    }

    private boolean CarregaSessaoLoja() {
        boolean result = false;

        if (!SessionModel.Loja.Endereco.equals(txtEndereco.getText().toString())) {
            SessionModel.Loja.Endereco = txtEndereco.getText().toString();
            result = true;
        }

        if (!SessionModel.Loja.Bairro.equals(txtBairro.getText().toString())) {
            SessionModel.Loja.Bairro = txtBairro.getText().toString();
            result = true;
        }

        if (!SessionModel.Loja.Cidade.equals(txtCidade.getText().toString())) {
            SessionModel.Loja.Cidade = txtCidade.getText().toString();
            result = true;
        }

        if (!SessionModel.Loja.Cep.equals(txtCEP.getText().toString())) {
            SessionModel.Loja.Cep = txtCEP.getText().toString();
            result = true;
        }

        if (!SessionModel.Loja.Estado.equals(txtEstado.getText().toString())) {
            SessionModel.Loja.Estado = txtEstado.getText().toString();
            result = true;
        }

        return result;
    }

}
