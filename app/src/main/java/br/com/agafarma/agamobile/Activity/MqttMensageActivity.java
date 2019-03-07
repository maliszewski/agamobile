package br.com.agafarma.agamobile.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.agafarma.agamobile.Adapter.MqttChatAdapter;
import br.com.agafarma.agamobile.Data.MqttMensagemDAO;
import br.com.agafarma.agamobile.Model.MqttMensagemModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Service.MqttService;
import br.com.agafarma.agamobile.Util.ToolBar;

public class MqttMensageActivity extends AppCompatActivity {

    private static String TAG = "MqttMensageActivity";
    private static Activity activity;
    private static MqttChatAdapter adapter;
    private static ListView listaMensage;
    private Button btnEnviar;
    private EditText txtMensage;

    public static int IdUsuario = 0;
    public static String NomeUsuario = "";
    public static int IdGrupoMqtt = 0;
    public static String NomeGrupoMqtt = "";
    public static String Topico = "";

    public static int Tipo = 0;

    public static Boolean StatusActivity = false;
    public LinearLayout barraEnvioMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt_mensage);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);

        btnEnviar = (Button) findViewById(R.id.btn_enviar);
        txtMensage = (EditText) findViewById(R.id.txt_mensage);
        listaMensage = (ListView) findViewById(R.id.lista_mensage);
        barraEnvioMensagem = (LinearLayout) findViewById(R.id.barra_envio_mensagem);

        activity = this;
        if (getIntent().getExtras().getString("idUsuario") != null) {
            IdUsuario = Integer.valueOf(getIntent().getExtras().getString("idUsuario")).intValue();
        }
        if (getIntent().getExtras().getString("nomeUsuario") != null) {
            NomeUsuario = getIntent().getExtras().getString("nomeUsuario");
        }
        if (getIntent().getExtras().getString("idGrupoMqtt") != null) {
            IdGrupoMqtt = Integer.valueOf(getIntent().getExtras().getString("idGrupoMqtt")).intValue();
        }
        if (getIntent().getExtras().getString("nomeGrupoMqtt") != null) {
            NomeGrupoMqtt = getIntent().getExtras().getString("nomeGrupoMqtt");
        }
        if (getIntent().getExtras().getString("tipo") != null) {
            Tipo = Integer.valueOf(getIntent().getExtras().getString("tipo")).intValue();
        }
        if (getIntent().getExtras().getString("topico") != null) {
            Topico = getIntent().getExtras().getString("topico");
        }

        if (Tipo == 3) {
            barraEnvioMensagem.setVisibility(View.INVISIBLE);
        } else {
            barraEnvioMensagem.setVisibility(View.VISIBLE);
        }

        if (Tipo == 1) {
            getSupportActionBar().setTitle(NomeUsuario);
        } else if (Tipo == 2) {
            getSupportActionBar().setTitle(NomeGrupoMqtt);
        } else if (Tipo == 3) {
            getSupportActionBar().setTitle("Alerta");
        }


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtMensage.getText().toString().length() == 0) {
                    return;
                }
                MqttMensagemModel mqttMensagemModel = new MqttMensagemModel();
                mqttMensagemModel.IdMqttMensagem = 0;
                mqttMensagemModel.IdReferencia = 0;
                mqttMensagemModel.Tipo = Tipo;
                mqttMensagemModel.Link = "";
                mqttMensagemModel.Titulo = "";
                mqttMensagemModel.Mensagem = txtMensage.getText().toString();
                mqttMensagemModel.IdUsuario = IdUsuario;
                mqttMensagemModel.NomeUsuario = NomeUsuario;
                mqttMensagemModel.Status = 3;

                if (IdGrupoMqtt > 0) {
                    mqttMensagemModel.Topico = Topico;
                } else {
                    mqttMensagemModel.Topico = Topico;
                }

                mqttMensagemModel.IdGrupoMqtt = IdGrupoMqtt;
                mqttMensagemModel.NomeGrupoMqtt = NomeGrupoMqtt;

                new MqttMensagemDAO(getApplicationContext()).Inserir(mqttMensagemModel);
                MqttMensageActivity.MostraMensagem(mqttMensagemModel);

                MqttService.acaoEnviarMensagem(getApplicationContext(), mqttMensagemModel);
                txtMensage.setText("");
            }

        });
        CarregarHistorico();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void MostraMensagem(final MqttMensagemModel msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.Adicionar(msg);
                adapter.notifyDataSetChanged();
                mostraUltimo();
            }
        });
    }

    private static void mostraUltimo() {
        listaMensage.setSelection(listaMensage.getCount() - 1);
    }

    private void CarregarHistorico() {
        if (adapter != null)
            adapter.Limpar();
        List<MqttMensagemModel> listaMsg = new ArrayList<MqttMensagemModel>();
        MqttMensagemModel filtro = new MqttMensagemModel();
        if (Tipo == 2)
            filtro.IdGrupoMqtt = IdGrupoMqtt;
        else
            filtro.IdUsuario = IdUsuario;

        filtro.Tipo = Tipo;
        listaMsg = new MqttMensagemDAO(this).Buscar(filtro, false);
        adapter = new MqttChatAdapter(this, new ArrayList<MqttMensagemModel>());
        listaMensage.setAdapter(adapter);
        for (int i = 0; i < listaMsg.size(); i++) {
            MostraMensagem(listaMsg.get(i));
            if (listaMsg.get(i).Status == 1) {
                MqttMensagemModel item = listaMsg.get(i);
                item.Status = 2;
                new MqttMensagemDAO(this).Atualizar(listaMsg.get(i));
            }
        }
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
