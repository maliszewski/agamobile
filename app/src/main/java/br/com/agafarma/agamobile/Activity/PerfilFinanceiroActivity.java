package br.com.agafarma.agamobile.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.ToolBar;

public class PerfilFinanceiroActivity extends AppCompatActivity {

    private static String TAG = "PerfilFinanceiroActivity";
    private EditText txtStatusCredito;
    private EditText txtLimiteCredito;
    private EditText txtSaldo;
    private EditText txtEmNegociacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_financeiro);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);

        txtStatusCredito = (EditText) findViewById(R.id.txt_status_financeiro);
        txtLimiteCredito = (EditText) findViewById(R.id.txt_limite);
        txtSaldo = (EditText) findViewById(R.id.txt_saldo);
        txtEmNegociacao= (EditText) findViewById(R.id.txt_em_negociacao);

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

    private void Carrega(){
        txtStatusCredito.setText(SessionModel.Loja.GetSituacaoCredito());
        txtLimiteCredito.setText(SessionModel.Loja.LimiteCredito.toString());
        txtSaldo.setText(SessionModel.Loja.SaldoDevedor.toString());
        txtEmNegociacao.setText(SessionModel.Loja.GetEmNegociacao());
    }
}
