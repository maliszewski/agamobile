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
import br.com.agafarma.agamobile.Util.ToolBar;
import br.com.agafarma.agamobile.WebApi.LojaApi;

public class PerfilEncarteActivity extends AppCompatActivity {

    private static String TAG = "PerfilEncarteActivity";
    private EditText txtQuantidadeEncarte;
    private EditText txtQuantidadeEncarteExcedente;
    private EditText txtQuantidadeFolheto;
    private EditText txtQuantidadeFolhetoExcedente;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_encarte);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);

        txtQuantidadeEncarte = (EditText) findViewById(R.id.txt_qt_encarte);
        txtQuantidadeEncarteExcedente = (EditText) findViewById(R.id.txt_qt_encarte_excedente);
        txtQuantidadeFolheto = (EditText) findViewById(R.id.txt_qt_folheto);
        txtQuantidadeFolhetoExcedente = (EditText) findViewById(R.id.txt_qt_folheto_Excedente);
        btnSalvar = (Button) findViewById(R.id.btn_salvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean alterar = CarregaSessaoLoja();

                ProgressDialog progressDialog = new Diverso().TempoTela(PerfilEncarteActivity.this);
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
                                        alterado = new LojaApi().SalvarLoja(PerfilEncarteActivity.this, SessionModel.Loja);
                                        progressDialog.dismiss();
                                        new Dialog(PerfilEncarteActivity.this).ShowAlertOK("Atenção", "Dados Salvo com Sucesso", true);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                        Diverso.Error(getApplicationContext());
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    new Dialog(PerfilEncarteActivity.this).ShowAlertOK("Atenção", "Dados Salvo com Sucesso", true);
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
        txtQuantidadeEncarte.setText(String.valueOf(SessionModel.Loja.QuantidadeEncarte));
        txtQuantidadeEncarteExcedente.setText(String.valueOf(SessionModel.Loja.QuantidadeExcedente));
        txtQuantidadeFolheto.setText(String.valueOf(SessionModel.Loja.QuantidadeEncarte));
        txtQuantidadeFolhetoExcedente.setText(String.valueOf(SessionModel.Loja.QuantidadeFolhetoExcedente));
    }

    private boolean CarregaSessaoLoja() {
        boolean result = false;
        if (SessionModel.Loja.QuantidadeExcedente != Integer.valueOf(txtQuantidadeEncarteExcedente.getText().toString())) {
            SessionModel.Loja.QuantidadeExcedente = Integer.valueOf(txtQuantidadeEncarteExcedente.getText().toString());
            result = true;
        }
        if (SessionModel.Loja.QuantidadeFolhetoExcedente != Integer.valueOf(txtQuantidadeFolhetoExcedente.getText().toString())) {
            SessionModel.Loja.QuantidadeFolhetoExcedente = Integer.valueOf(txtQuantidadeFolhetoExcedente.getText().toString());
            result = true;
        }

        return result;
    }
}
