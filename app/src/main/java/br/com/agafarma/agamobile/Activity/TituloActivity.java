package br.com.agafarma.agamobile.Activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.agafarma.agamobile.Model.BoletoModel;
import br.com.agafarma.agamobile.Model.CircularModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.Model.TituloModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Autorizacao;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.Util.ToolBar;
import br.com.agafarma.agamobile.WebApi.CircularApi;
import br.com.agafarma.agamobile.WebApi.FinanceiroApi;

public class TituloActivity extends AppCompatActivity {

    private TextView txtTitulo;
    private TextView txtDataVencimento;
    private TextView txtValor;
    private TextView txtLinhaDigitavel;
    private static ProgressDialog ringProgressDialog;
    private CoordinatorLayout coordinatorLayout;
    private WebView pWebview;
    private static String TAG = "TituloActivity";
    public static BoletoModel boleto;
    public static TituloModel titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titulo);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);

        txtTitulo = (TextView) findViewById(R.id.boleto_titulo_parcela);
        txtDataVencimento = (TextView) findViewById(R.id.boleto_data_vencimento);
        txtValor = (TextView) findViewById(R.id.boleto_valor);
        txtLinhaDigitavel = (TextView) findViewById(R.id.boleto_linha_digitavel);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        Carregar();

    }


    private void Carregar() {
        if (ringProgressDialog == null || !ringProgressDialog.isShowing()) {
            ringProgressDialog = new Diverso().TempoTela(this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CarregaTitulo();
                            TituloActivity.ringProgressDialog.dismiss();
                            String mens = "";

                            Date d = new Date();
                            if (titulo.DataVencimento.getTime() > d.getTime())
                                mens = "Segunda via foi enviado para seu EMAIL e o Codigo de Barra ja foi Copiado...";
                            else
                                mens = "Estamos redirecionado para atualizar sua dara de vencimento";

                            Snackbar.make(coordinatorLayout, mens, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                        }
                    });

                }
            }).start();
        }
    }


    private void CarregaTitulo() {
        try {
            if (titulo.TipoEmpresa != 3)
                boleto = new FinanceiroApi().BuscarBoleto(this, titulo.Sequencia);
            else
                boleto = new FinanceiroApi().BuscarBoletoAgaCredi(this, SessionModel.Loja.LojaId);

        } catch (IOException e) {
            e.printStackTrace();
            ringProgressDialog.dismiss();
            Diverso.Error(getApplicationContext());
        }

        String t = "";
        if (titulo.Parcela != null && !titulo.Parcela.isEmpty())
            t = String.format("%.0f", titulo.Titulo) + "/" + titulo.Parcela;
        else
            t = String.format("%.0f", titulo.Titulo);

        if (titulo.TipoEmpresa == 3)
            t = SessionModel.Loja.RazaoSocial;

        txtTitulo.setText(t);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtDataVencimento.setText(sdf.format(titulo.DataVencimento));
        txtValor.setText(String.format("%.2f", titulo.Saldo));
        txtLinhaDigitavel.setText(boleto.LinhaDigitavel);
        if (boleto.LinhaDigitavel != null && boleto.LinhaDigitavel.length() > 0) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("linha", boleto.LinhaDigitavel);
            clipboard.setPrimaryClip(clip);
        }

        pWebview = (WebView) findViewById(R.id.boleto_webview);

        String url = "";

        if (!boleto.BoletoEmDia) {
            url = boleto.UrlAtualizarBoleto;
        } else {
            url = Autorizacao.BuscaURL() + boleto.NomeArquivo;
        }
        pWebview.getSettings().setJavaScriptEnabled(true);
        pWebview.loadUrl(url);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


}
