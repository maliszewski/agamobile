package br.com.agafarma.agamobile.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.service.textservice.SpellCheckerService;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.agafarma.agamobile.Adapter.PerfilAdapter;
import br.com.agafarma.agamobile.Model.MenuModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.Util.MaskEditUtil;
import br.com.agafarma.agamobile.Util.ToolBar;
import br.com.agafarma.agamobile.WebApi.LojaApi;

public class PerfilGeralActivity extends AppCompatActivity {

    private static String TAG = "PerfilGeralActivity";

    private EditText txtCNPJ;
    private EditText txtRazaoSocial;
    private EditText txtNomeFantasia;
    private EditText txtInscricaoEstadual;

    private EditText txtCPFProprietario;
    private EditText txtNomePropeitario;
    private EditText txtTipoLoja;
    private EditText txtFarmaciaPopular;
    private EditText txtCooperada;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_geral);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);
        txtCNPJ = (EditText) findViewById(R.id.txt_cnpj);
        txtRazaoSocial = (EditText) findViewById(R.id.txt_razao_social);
        txtNomeFantasia = (EditText) findViewById(R.id.txt_nome_fantasia);
        txtInscricaoEstadual = (EditText) findViewById(R.id.txt_inscricao_estadual);

        txtCPFProprietario = (EditText) findViewById(R.id.txt_cpf_proprietario);
        txtNomePropeitario = (EditText) findViewById(R.id.txt_nome_proprietario);
        txtTipoLoja = (EditText) findViewById(R.id.txt_tipo_loja);
        txtFarmaciaPopular = (EditText) findViewById(R.id.txt_farmacia_popular);
        txtCooperada = (EditText) findViewById(R.id.txt_cooperada);

        txtCNPJ.addTextChangedListener(MaskEditUtil.mask(txtCNPJ, "##.###.###/####-##"));
        txtCPFProprietario.addTextChangedListener(MaskEditUtil.mask(txtCPFProprietario, "###.###.###-##"));
        btnSalvar = (Button) findViewById(R.id.btn_salvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean alterar = CarregaSessaoLoja();

                ProgressDialog progressDialog = new Diverso().TempoTela(PerfilGeralActivity.this);
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
                                        alterado = new LojaApi().SalvarLoja(PerfilGeralActivity.this, SessionModel.Loja);
                                        progressDialog.dismiss();
                                        new Dialog(PerfilGeralActivity.this).ShowAlertOK("Atenção", "Dados Salvo com Sucesso", true);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                        Diverso.Error(getApplicationContext());
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    new Dialog(PerfilGeralActivity.this).ShowAlertOK("Atenção", "Dados Salvo com Sucesso", true);

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
        txtCNPJ.setText(SessionModel.Loja.Cnpj);
        txtRazaoSocial.setText(SessionModel.Loja.RazaoSocial);
        txtNomeFantasia.setText(SessionModel.Loja.NomeFantasia);
        txtInscricaoEstadual.setText(SessionModel.Loja.InscricaoEstadual);
        txtCPFProprietario.setText(SessionModel.Loja.CpfProprietario1);
        txtNomePropeitario.setText(SessionModel.Loja.NomeProprietario1);
        txtTipoLoja.setText(SessionModel.Loja.GetTipo());
        txtFarmaciaPopular.setText(SessionModel.Loja.GetFarmaciaPopular());
        txtCooperada.setText(SessionModel.Loja.GetCooperada());
    }

    private boolean CarregaSessaoLoja() {
        boolean result = false;

        if (!SessionModel.Loja.Cnpj.equals(txtCNPJ.getText().toString())) {
            SessionModel.Loja.Cnpj = txtCNPJ.getText().toString();
            result = true;
        }
        if (!SessionModel.Loja.RazaoSocial.equals(txtRazaoSocial.getText().toString())) {
            SessionModel.Loja.RazaoSocial = txtRazaoSocial.getText().toString();
            result = true;
        }
        if (!SessionModel.Loja.NomeFantasia.equals(txtNomeFantasia.getText().toString())) {
            SessionModel.Loja.NomeFantasia = txtNomeFantasia.getText().toString();
            result = true;
        }
        if (!SessionModel.Loja.InscricaoEstadual.equals(txtInscricaoEstadual.getText().toString())) {
            SessionModel.Loja.InscricaoEstadual = txtInscricaoEstadual.getText().toString();
            result = true;
        }
        if (!SessionModel.Loja.CpfProprietario1.equals(txtCPFProprietario.getText().toString())) {
            SessionModel.Loja.CpfProprietario1 = txtCPFProprietario.getText().toString();
            result = true;
        }
        if (!SessionModel.Loja.NomeProprietario1.equals(txtNomePropeitario.getText().toString())) {
            SessionModel.Loja.NomeProprietario1 = txtNomePropeitario.getText().toString();
            result = true;
        }
        return result;
    }

}
