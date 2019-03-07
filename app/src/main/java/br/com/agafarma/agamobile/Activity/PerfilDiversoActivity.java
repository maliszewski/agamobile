package br.com.agafarma.agamobile.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import java.io.IOException;

import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.Util.MaskEditUtil;
import br.com.agafarma.agamobile.Util.ToolBar;
import br.com.agafarma.agamobile.WebApi.LojaApi;

public class PerfilDiversoActivity extends AppCompatActivity {

    private static String TAG = "PerfilDiversoActivity";
    private RadioGroup rbtGrupoRegiao;
    private RadioGroup rbtGrupoCluster;
    private RadioGroup rbtGrupoPadraoLoja;
    private RadioGroup rbtGrupoTamanho;
    private EditText txtSistemaERP;
    private EditText txtMetragem;
    private EditText txtNumeroFuncionario;
    private EditText txtMetragemVenda;
    private Button btnSalvar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_diverso);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);

        rbtGrupoRegiao = (RadioGroup) findViewById(R.id.rbtGrupoRegiao);
        rbtGrupoCluster = (RadioGroup) findViewById(R.id.rbtGrupoCluster);
        rbtGrupoPadraoLoja = (RadioGroup) findViewById(R.id.rbtGrupoPadraoLoja);
        rbtGrupoTamanho = (RadioGroup) findViewById(R.id.rbtGrupoTamanho);

        txtSistemaERP = (EditText) findViewById(R.id.txt_sistema_erp);
        txtMetragem = (EditText) findViewById(R.id.txt_metragem);
        txtMetragemVenda = (EditText) findViewById(R.id.txt_metragem_venda);

        txtNumeroFuncionario = (EditText) findViewById(R.id.txt_numero_funcioanrio);
        btnSalvar = (Button) findViewById(R.id.btn_salvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean alterar = CarregaSessaoLoja();

                ProgressDialog progressDialog = new Diverso().TempoTela(PerfilDiversoActivity.this);
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
                                        alterado = new LojaApi().SalvarLoja(PerfilDiversoActivity.this, SessionModel.Loja);
                                        progressDialog.dismiss();
                                        new Dialog(PerfilDiversoActivity.this).ShowAlertOK("Atenção", "Dados Salvo com Sucesso", true);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                        Diverso.Error(getApplicationContext());
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    new Dialog(PerfilDiversoActivity.this).ShowAlertOK("Atenção", "Dados Salvo com Sucesso", true);
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
        txtSistemaERP.setText(SessionModel.Loja.SistemaERP);
        txtMetragem.setText(SessionModel.Loja.Metragem.toString());
        txtMetragemVenda.setText(SessionModel.Loja.MetragemVenda.toString());
        txtNumeroFuncionario.setText(String.valueOf(SessionModel.Loja.NumeroFuncionario));
        setRBTSelecionado(rbtGrupoRegiao, SessionModel.Loja.Regiao);
        setRBTSelecionado(rbtGrupoCluster, SessionModel.Loja.Cluster);
        setRBTSelecionado(rbtGrupoPadraoLoja, SessionModel.Loja.PadraoLoja);
        setRBTSelecionado(rbtGrupoTamanho, SessionModel.Loja.Tamanho);

    }


    private boolean CarregaSessaoLoja() {
        boolean result = false;


        if (SessionModel.Loja.Regiao != getRBTSelecionado(rbtGrupoRegiao)) {
            SessionModel.Loja.Regiao = getRBTSelecionado(rbtGrupoRegiao);
            result = true;
        }

        if (SessionModel.Loja.SistemaERP == null) {
            SessionModel.Loja.SistemaERP = new String();
        }

        if (!SessionModel.Loja.SistemaERP.equals(txtSistemaERP.getText().toString())) {
            SessionModel.Loja.SistemaERP = txtSistemaERP.getText().toString();
            result = true;
        }
        if (SessionModel.Loja.Metragem != Double.valueOf(txtMetragem.getText().toString())) {
            SessionModel.Loja.Metragem = Double.valueOf(txtMetragem.getText().toString());
            result = true;
        }
        if (SessionModel.Loja.MetragemVenda != Double.valueOf(txtMetragemVenda.getText().toString())) {
            SessionModel.Loja.MetragemVenda = Double.valueOf(txtMetragemVenda.getText().toString());
            result = true;
        }

        if (SessionModel.Loja.NumeroFuncionario != Integer.valueOf(txtNumeroFuncionario.getText().toString())) {
            SessionModel.Loja.NumeroFuncionario = Integer.valueOf(txtNumeroFuncionario.getText().toString());
            result = true;
        }

        if (SessionModel.Loja.Cluster != getRBTSelecionado(rbtGrupoCluster)) {
            SessionModel.Loja.Cluster = getRBTSelecionado(rbtGrupoCluster);
            result = true;
        }
        if (SessionModel.Loja.PadraoLoja != getRBTSelecionado(rbtGrupoPadraoLoja)) {
            SessionModel.Loja.PadraoLoja = getRBTSelecionado(rbtGrupoPadraoLoja);
            result = true;
        }
        if (SessionModel.Loja.Tamanho != getRBTSelecionado(rbtGrupoTamanho)) {
            SessionModel.Loja.Tamanho = getRBTSelecionado(rbtGrupoTamanho);
            result = true;
        }

        return result;
    }

    private int getRBTSelecionado(RadioGroup rbtGrupo) {
        RadioButton rbtButton;

        int selectedId = rbtGrupo.getCheckedRadioButtonId();

        rbtButton = (RadioButton) findViewById(selectedId);

        int id = rbtButton.getId();

        int value = 0;
        if (id == R.id.rbtRegiao1)
            value = 1;
        else if (id == R.id.rbtRegiao2)
            value = 2;
        else if (id == R.id.rbtRegiao3)
            value = 3;
        else if (id == R.id.rbtRegiao4)
            value = 4;
        else if (id == R.id.rbtRegiao5)
            value = 5;
        else if (id == R.id.rbtPremium)
            value = 1;
        else if (id == R.id.rbtPremiumCompacta)
            value = 2;
        else if (id == R.id.rbtBasica)
            value = 3;
        else if (id == R.id.rbtBasicaCompacta)
            value = 4;
        else if (id == R.id.rbtTotal)
            value = 1;
        else if (id == R.id.rbtNenhum)
            value = 2;
        else if (id == R.id.rbtEspacoClinico)
            value = 3;
        else if (id == R.id.rbtFachada)
            value = 4;
        else if (id == R.id.rbtLayoutInterno)
            value = 5;
        else if (id == R.id.rbtPequeno)
            value = 1;
        else if (id == R.id.rbtMedio)
            value = 2;
        else if (id == R.id.rbtGrande)
            value = 3;

        return value;
    }

    private void setRBTSelecionado(RadioGroup rbtGrupo, int value) {
        RadioButton rbtB = null;
        int id = rbtGrupo.getId();
        if (id == R.id.rbtGrupoRegiao) {
            if (value == 1)
                rbtB = (RadioButton) findViewById(R.id.rbtRegiao1);
            else if (value == 2)
                rbtB = (RadioButton) findViewById(R.id.rbtRegiao2);
            else if (value == 3)
                rbtB = (RadioButton) findViewById(R.id.rbtRegiao3);
            else if (value == 4)
                rbtB = (RadioButton) findViewById(R.id.rbtRegiao4);
            else if (value == 5)
                rbtB = (RadioButton) findViewById(R.id.rbtRegiao5);
        } else if (id == R.id.rbtGrupoCluster) {
            if (value == 1)
                rbtB = (RadioButton) findViewById(R.id.rbtPremium);
            else if (value == 2)
                rbtB = (RadioButton) findViewById(R.id.rbtPremiumCompacta);
            else if (value == 3)
                rbtB = (RadioButton) findViewById(R.id.rbtBasica);
            else if (value == 4)
                rbtB = (RadioButton) findViewById(R.id.rbtBasicaCompacta);

        } else if (id == R.id.rbtGrupoPadraoLoja) {
            if (value == 1)
                rbtB = (RadioButton) findViewById(R.id.rbtTotal);
            else if (value == 2)
                rbtB = (RadioButton) findViewById(R.id.rbtNenhum);
            else if (value == 3)
                rbtB = (RadioButton) findViewById(R.id.rbtEspacoClinico);
            else if (value == 4)
                rbtB = (RadioButton) findViewById(R.id.rbtFachada);
            else if (value == 5)
                rbtB = (RadioButton) findViewById(R.id.rbtLayoutInterno);

        } else if (id == R.id.rbtGrupoTamanho) {
            if (value == 1)
                rbtB = (RadioButton) findViewById(R.id.rbtPequeno);
            else if (value == 2)
                rbtB = (RadioButton) findViewById(R.id.rbtMedio);
            else if (value == 3)
                rbtB = (RadioButton) findViewById(R.id.rbtGrande);

        }
        if (rbtB != null)
            rbtB.setChecked(true);

    }

}
