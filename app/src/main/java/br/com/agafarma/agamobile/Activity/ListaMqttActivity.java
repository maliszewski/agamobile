package br.com.agafarma.agamobile.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.util.List;

import br.com.agafarma.agamobile.Adapter.AvisoAdapter;
import br.com.agafarma.agamobile.Adapter.MqttAdapter;
import br.com.agafarma.agamobile.Data.AvisoDAO;
import br.com.agafarma.agamobile.Data.MqttMensagemDAO;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.MqttMensagemModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Service.MqttService;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.ToolBar;

public class ListaMqttActivity extends AppCompatActivity {

    Toolbar barra;
    private static Activity activity;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<MqttMensagemModel> data;
    private Button btnPesquisarContato;
    public static Boolean StatusActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_mqtt);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        btnPesquisarContato = (Button) findViewById(R.id.btn_contato);
        btnPesquisarContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ListaContatoActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        MqttService.acaoSincronizaContato(ListaMqttActivity.this);

        activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Atualizar();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public static void Atualizar() {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MqttMensagemDAO mqttDAO = new MqttMensagemDAO(activity.getApplicationContext());
                MqttMensagemModel mqttFiltro = new MqttMensagemModel();
                mqttFiltro.Status = 1;
                data = mqttDAO.BuscarAgrupado();

                if (data != null && data.size() > 0) {
                    adapter = new MqttAdapter(activity.getApplicationContext(), data);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        StatusActivity = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        StatusActivity = true;
    }

}
