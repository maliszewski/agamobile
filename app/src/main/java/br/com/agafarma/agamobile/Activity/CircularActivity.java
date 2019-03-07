package br.com.agafarma.agamobile.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import br.com.agafarma.agamobile.Data.AvisoDAO;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.CircularModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Service.SincronizaService;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.Util.ToolBar;
import br.com.agafarma.agamobile.WebApi.CircularApi;

public class CircularActivity extends AppCompatActivity {

    private TextView pNumero;
    private TextView pAssunto;
    private WebView pWebview;
    private static String TAG = "CircularActivity";
    public static CircularModel circular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);
        pNumero = (TextView) findViewById(R.id.circular_numero);
        pAssunto = (TextView) findViewById(R.id.circular_assunto);
        pWebview = (WebView) findViewById(R.id.circular_webview);
        String url = "http://drive.google.com/viewerng/viewer?embedded=true&url=" + String.valueOf(Uri.parse(circular.Anexo));
        pWebview.getSettings().setJavaScriptEnabled(true);
        pWebview.loadUrl(url);
        pNumero.setText(circular.Numero + "/" + circular.Ano);
        pAssunto.setText(circular.Assunto);


        try {
            new CircularApi().SalvarLida(CircularActivity.this, circular.IdCircular);
        } catch (IOException e) {
            e.printStackTrace();
            Diverso.Error(getApplicationContext());
        }

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
