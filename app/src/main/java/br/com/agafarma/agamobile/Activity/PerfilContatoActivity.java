package br.com.agafarma.agamobile.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.io.IOException;

import br.com.agafarma.agamobile.Model.LojaModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.Util.MaskEditUtil;
import br.com.agafarma.agamobile.Util.ToolBar;
import br.com.agafarma.agamobile.WebApi.LojaApi;

public class PerfilContatoActivity extends AppCompatActivity {

    private static String TAG = "PerfilContatoActivity";
    private EditText txtContato;
    private EditText txtEmail;
    private EditText txtDDD;
    private EditText txtTelefone;
    private EditText txtTeleEntrega;
    private EditText txtTeleEntregaHorario;
    private EditText txtTeleEntregaTelefone;
    private Switch swTeleEntrega;
    private Button btnSalvar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_contato);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);

        txtContato = (EditText) findViewById(R.id.txt_contato);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtDDD = (EditText) findViewById(R.id.txt_ddd);
        txtTelefone = (EditText) findViewById(R.id.txt_telefone);
        txtTeleEntregaHorario = (EditText) findViewById(R.id.txt_tele_entrega_horario);
        txtTeleEntregaTelefone = (EditText) findViewById(R.id.txt_tele_entrega_telefone);
        swTeleEntrega = (Switch) findViewById(R.id.sw_tele_entrega);
        btnSalvar = (Button) findViewById(R.id.btn_salvar);

        txtTelefone.addTextChangedListener(MaskEditUtil.mask(txtTelefone, "####-####"));
        txtTeleEntregaTelefone.addTextChangedListener(MaskEditUtil.mask(txtTeleEntregaTelefone, "####-####"));
        txtTeleEntregaHorario.addTextChangedListener(MaskEditUtil.mask(txtTeleEntregaHorario, "##:## - ##:##"));
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean alterar = CarregaSessaoLoja();

                ProgressDialog progressDialog = new Diverso().TempoTela(PerfilContatoActivity.this);
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
                                        alterado = new LojaApi().SalvarLoja(PerfilContatoActivity.this, SessionModel.Loja);
                                        progressDialog.dismiss();
                                        new Dialog(PerfilContatoActivity.this).ShowAlertOK("Atenção", "Dados Salvo com Sucesso", true);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                        Diverso.Error(getApplicationContext());
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    new Dialog(PerfilContatoActivity.this).ShowAlertOK("Atenção", "Dados Salvo com Sucesso", true);
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
        txtContato.setText(SessionModel.Loja.Contato);
        txtEmail.setText(SessionModel.Loja.Email);
        txtDDD.setText(String.valueOf(SessionModel.Loja.DDD));
        txtTelefone.setText(SessionModel.Loja.Fone);
        txtTeleEntregaHorario.setText(SessionModel.Loja.TeleEntregaHorario);
        txtTeleEntregaTelefone.setText(SessionModel.Loja.TeleEntregaFone);
        swTeleEntrega.setChecked(SessionModel.Loja.TeleEntrega.equals("S"));
    }

    private boolean CarregaSessaoLoja() {
        boolean result = false;

        if (!SessionModel.Loja.Contato.equals(txtContato.getText().toString())) {
            SessionModel.Loja.Contato = txtContato.getText().toString();
            result = true;
        }
        if (!SessionModel.Loja.Email.equals(txtEmail.getText().toString())) {
            SessionModel.Loja.Email = txtEmail.getText().toString();
            result = true;
        }
        if (SessionModel.Loja.DDD != Integer.valueOf(txtDDD.getText().toString())) {
            SessionModel.Loja.DDD = Integer.valueOf(txtDDD.getText().toString());
            result = true;
        }
        if (!SessionModel.Loja.Fone.equals(txtTelefone.getText().toString())) {
            SessionModel.Loja.Fone = txtTelefone.getText().toString();
            result = true;
        }
        if (!SessionModel.Loja.TeleEntregaHorario.equals(txtTeleEntregaHorario.getText().toString())) {
            SessionModel.Loja.TeleEntregaHorario = txtTeleEntregaHorario.getText().toString();
            result = true;
        }


        if (!SessionModel.Loja.TeleEntregaFone.equals(txtTeleEntregaTelefone.getText().toString())) {
            SessionModel.Loja.TeleEntregaFone = txtTeleEntregaTelefone.getText().toString();
            result = true;
        }

        String tele = "S";
        if (swTeleEntrega.isChecked())
            tele = "S";
        else
            tele = "N";

        if (!tele.equals(SessionModel.Loja.TeleEntrega)) {
            SessionModel.Loja.TeleEntrega = tele;
            result = true;
        }

        return result;

    }
}
