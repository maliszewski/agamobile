package br.com.agafarma.agamobile.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import android.util.Log;

import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import br.com.agafarma.agamobile.Activity.HomeActivity;
import br.com.agafarma.agamobile.Activity.ListaMqttActivity;
import br.com.agafarma.agamobile.Activity.MqttMensageActivity;
import br.com.agafarma.agamobile.Data.ContatoDAO;
import br.com.agafarma.agamobile.Data.LoginDAO;
import br.com.agafarma.agamobile.Data.MqttMensagemDAO;
import br.com.agafarma.agamobile.Model.ContatoChatModel;
import br.com.agafarma.agamobile.Model.MqttMensagemModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.Model.UsuarioModel;
import br.com.agafarma.agamobile.Util.Autorizacao;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.Util.Notificacao;
import br.com.agafarma.agamobile.Util.VerificaBackground;
import br.com.agafarma.agamobile.WebApi.ChatApi;

public class MqttService extends Service implements MqttCallback {
    private static final String DEBUG_TAG = "MqttService";

    private static final String MQTT_URL_FORMATO = "tcp://%s:%d";
    private static final Integer MQTT_PORTA = 1883;
    private static final String MQTT_BROKER = Autorizacao.BuscaBrokerMQTT();

    private static final String MQTT_HANDLER_THREAD = "MQTTService[" + DEBUG_TAG + "]";
    private static final String ACAO_CONECTAR_MQTT = DEBUG_TAG + ".CONECTAR_MQTT";
    private static final String ACAO_DESCONECTAR_MQTT = DEBUG_TAG + ".DESCONECTAR";
    private static final String ACAO_ANALISAR_SERVICO_MQTT = DEBUG_TAG + ".ANALISAR_SERVICO";
    private static final String ACAO_RECONECTAR_MQTT = DEBUG_TAG + ".RECONECTAR";
    private static final String ACAO_ENVIAR_MENSAGEM_MQTT = DEBUG_TAG + ".ENVIAR_MENSAGEM";
    private static final String ACAO_INSCREVER_TOPICO_MQTT = DEBUG_TAG + ".INSCREVER_TOPICO";
    private static final String ACAO_PARAR_MQTT = DEBUG_TAG + ".PARAR";
    private static final String ACAO_SINCRONIZA_CONTATO = DEBUG_TAG + ".SINCRONIZA_CONTATO";

    private static Integer KEPPALIVE = 120;


    private static boolean STATUS_SERVICO_MQTT = false;
    private static boolean STATUS_RECEIVER = false;

    private MqttDefaultFilePersistence mDataStore;
    private String mDeviceId;
    private AlarmManager mAlarme;
    private ConnectivityManager mConectividade;
    private MqttClient mqttClient;
    private MqttConnectOptions mqttOpt;


    public static void acaoIniciarServico(Context ctx) {
        Intent intent = new Intent(ctx, MqttService.class);
        intent.setAction(ACAO_CONECTAR_MQTT);
        ctx.startService(intent);
    }

    public static void acaoSincronizaContato(Context ctx) {
        Intent intent = new Intent(ctx, MqttService.class);
        intent.setAction(ACAO_SINCRONIZA_CONTATO);
        ctx.startService(intent);
    }

    public static void acaoEnviarMensagem(Context ctx, MqttMensagemModel mensagem) {
        Intent intent = new Intent(ctx, MqttService.class);
        intent.setAction(ACAO_ENVIAR_MENSAGEM_MQTT);
        ctx.startService(intent);
    }

    public void onCreate() {
        Log.i(DEBUG_TAG, "onCreate OK");
        HandlerThread thread = new HandlerThread(MQTT_HANDLER_THREAD);
        thread.start();

        mDataStore = new MqttDefaultFilePersistence(getCacheDir().getAbsolutePath());
        mAlarme = (AlarmManager) getSystemService(ALARM_SERVICE);
        mConectividade = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(DEBUG_TAG, "onBind OK");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {

        super.onStartCommand(intent, flag, startId);

        if (intent == null || intent.getAction() == null) {
            analisarServico();
            Log.i(DEBUG_TAG, "Serviço iniciado sem proposito Entao ANALISO");
        } else {
            String acao = intent.getAction();
            Log.i(DEBUG_TAG, "onStartCommand: " + acao);

            switch (acao) {
                case ACAO_CONECTAR_MQTT:
                    iniciarConectandoServico();
                    break;
                case ACAO_DESCONECTAR_MQTT:
                    desConectarMqtt();
                    break;
                case ACAO_RECONECTAR_MQTT:
                    reConectarServico();
                    break;
                case ACAO_ANALISAR_SERVICO_MQTT:
                    analisarServico();
                    break;
                case ACAO_ENVIAR_MENSAGEM_MQTT:
                    enviarMensage();
                    break;
                case ACAO_INSCREVER_TOPICO_MQTT:
                    UsuarioModel usuario = new LoginDAO(getApplicationContext()).Buscar();
                    inscreverTopicoMqtt(usuario);
                    break;
                case ACAO_PARAR_MQTT:
                    pararServico();
                    break;
                case ACAO_SINCRONIZA_CONTATO:
                    sincronizaContato();
                    break;
                default:
                    Log.i(DEBUG_TAG, "Escolha de inicialização improcedente: " + acao);
                    break;

            }
        }
        return START_STICKY;
    }

    private synchronized void enviarMensage() {
        if (mqttClient == null || !mqttClient.isConnected()) {
            Log.i(DEBUG_TAG, "MQTT Desconetado, então conecta");
            conectarMqtt();
            registrarReceiver();
        }
        MqttMensagemModel filtro = new MqttMensagemModel();
        filtro.Status = 3;
        List<MqttMensagemModel> listaMensage = new MqttMensagemDAO(getApplicationContext()).Buscar(filtro, false);

        for (MqttMensagemModel msg : listaMensage) {
            if ((mqttClient != null && mqttClient.isConnected()) && msg != null) {
                int idUsuario = msg.IdUsuario;
                String nomeUsuario = msg.NomeUsuario;
                Gson gson = new Gson();
                msg.NomeUsuario = SessionModel.Usuario.Nome;
                msg.IdUsuario = SessionModel.Usuario.UsuarioId;
                String jsonMensage = gson.toJson(msg);

                MqttMessage mqttMessage = new MqttMessage();
                mqttMessage.setPayload(jsonMensage.getBytes());
                mqttMessage.setQos(1);
                mqttMessage.setRetained(false);
                try {
                    mqttClient.publish(msg.Topico, mqttMessage);
                    msg.Status = 4;
                    msg.NomeUsuario = nomeUsuario;
                    msg.IdUsuario = idUsuario;

                    new MqttMensagemDAO(getApplicationContext()).Atualizar(msg);
                    Log.i(DEBUG_TAG, "Mensagem Enviada com Sucesso: " + msg.Mensagem);

                } catch (MqttException e) {
                    Log.i(DEBUG_TAG, "Erro ao enviar Mensagem");
                    e.printStackTrace();
                    desConectarMqtt();
                } catch (NullPointerException ex) {
                    Log.i(DEBUG_TAG, "Erro ao enviar Mensagem");
                    ex.printStackTrace();
                    desConectarMqtt();
                }
            }
        }
    }

    private void sincronizaContato() {
        try {
            new ChatApi().SincronizaContatos(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void iniciarConectandoServico() {
        if (STATUS_SERVICO_MQTT) {
            Log.i(DEBUG_TAG, "Serviço do MQTT ja INICIADO");
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    conectarMqtt();
                    registrarReceiver();
                }
            }, 30000);
        }
    }

    private void conectarMqtt() {

        try {
            ContatoChatModel filtroContato = new ContatoChatModel();
            List<ContatoChatModel> listaContato = new ContatoDAO(getApplicationContext()).Buscar(filtroContato);
            if (listaContato.size() == 0){
                sincronizaContato();
                iniciarAnaliseMqtt();
                return;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!STATUS_SERVICO_MQTT) {
            UsuarioModel usuario = new LoginDAO(getApplicationContext()).Buscar();

            String urlMQTT = String.format(Locale.US, MQTT_URL_FORMATO, MQTT_BROKER, MQTT_PORTA);
            Log.i(DEBUG_TAG, "Url do MQTT: " + urlMQTT);
            mDeviceId = String.valueOf(usuario.UsuarioId);

            if (mDataStore != null) {
                try {
                    mqttClient = new MqttClient(urlMQTT, mDeviceId, mDataStore);
                } catch (MqttException e) {
                    Log.i(DEBUG_TAG, "Erro ao conectar no client do MQTT");
                    e.printStackTrace();
                    desConectarMqtt();
                }
            }
            try {

                String credentials = usuario.CpfCnpj.replace(".", "").replace("-", "").replace("/", "");

                mqttOpt = new MqttConnectOptions();

                mqttOpt.setCleanSession(false);
                mqttOpt.setUserName(credentials);
                mqttOpt.setPassword(usuario.Senha.toCharArray());
                mqttOpt.setKeepAliveInterval(KEPPALIVE);
                mqttOpt.setConnectionTimeout(05);

                if (Diverso.isOnline(getApplicationContext()) && isConetado()) {
                    if (mqttClient != null && !mqttClient.isConnected()) {
                        mqttClient.connect(mqttOpt);
                        mqttClient.setCallback(MqttService.this);
                        Log.i(DEBUG_TAG, "Conectado com Sucesso");
                        inscreverTopicoMqtt(usuario);

                    }
                } else {
                    Log.i(DEBUG_TAG, "Não possui internet para conectar");
                    desConectarMqtt();
                }

            } catch (MqttException e) {
                Log.i(DEBUG_TAG, "Erro ao tentar conectar o MQTT");
                e.printStackTrace();
                desConectarMqtt();
            }


        } else {
            Log.i(DEBUG_TAG, "Serviço do MQTT ja INICIADO");
        }
    }

    private void desConectarMqtt() {
        STATUS_SERVICO_MQTT = false;
        Log.i(DEBUG_TAG, "Inicado o Desconetar MQTT");
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
                Log.i(DEBUG_TAG, "MQTT Desconetado");
            }
        } catch (MqttException e) {
            Log.i(DEBUG_TAG, "ERRO ao desconectar o MQTT");
            e.printStackTrace();
        }
        mqttClient = null;
        desRegistrarReceiver();
    }

    private boolean isConetado() {
        NetworkInfo info = mConectividade.getActiveNetworkInfo();
        return (info == null) ? false : info.isConnected();
    }

    private final BroadcastReceiver mConectividadeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(DEBUG_TAG, "Alterado Conexão com Internet");
            if (isConetado()) {
                Log.i(DEBUG_TAG, "Internet Ligada");
            } else {
                Log.i(DEBUG_TAG, "Internet DesLigada");
                desConectarMqtt();
            }

        }
    };

    private void registrarReceiver() {
        try {
            if (STATUS_RECEIVER) {
                registerReceiver(mConectividadeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                STATUS_RECEIVER = true;
            }
        } catch (Exception e) {
            STATUS_RECEIVER = false;
            Log.i(DEBUG_TAG, "Erro ao registrar serviço");
            e.printStackTrace();

        }
    }

    private void desRegistrarReceiver() {

        try {
            if (STATUS_RECEIVER) {
                unregisterReceiver(mConectividadeReceiver);
                STATUS_RECEIVER = false;
            }
        } catch (Exception e) {
            STATUS_RECEIVER = false;
            Log.i(DEBUG_TAG, "Erro ao desregistrar serviço");
            e.printStackTrace();

        }

    }

    private void reConectarServico() {
        if (mqttClient != null && mqttClient.isConnected()) {
            desConectarMqtt();
        }
        conectarMqtt();
    }

    private synchronized void analisarServico() {

        Log.i(DEBUG_TAG, "Analisando o Serviço");

        if (mqttClient != null && !mqttClient.isConnected()) {
            desConectarMqtt();
            Log.i(DEBUG_TAG, "Não esta conectado ao MQTT então PARAR");
        }

        if (mqttClient == null || !STATUS_SERVICO_MQTT) {

            conectarMqtt();
            registrarReceiver();
            Log.i(DEBUG_TAG, "Estava parado iniciar novamente");

        }
    }

    private void iniciarAnaliseMqtt() {


        Log.i(DEBUG_TAG, "Iniciado a Analise do Servico");
        int keep = 6000; // 2 * (1000 * 60);
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MqttService.class);
        intent.setAction(ACAO_ANALISAR_SERVICO_MQTT);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);

        mAlarme.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + keep, keep, pendingIntent);

    }

    private void inscreverTopicoMqtt(UsuarioModel usuario) {
        iniciarAnaliseMqtt();

        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttClient.subscribe("agamqtt/geral", 1);
                mqttClient.subscribe("agamqtt/" + usuario.UsuarioId, 1);
                List<ContatoChatModel> listaContatoChatModel = new ContatoDAO(getApplicationContext()).BuscarGrupo();
                for (ContatoChatModel item : listaContatoChatModel) {
                    mqttClient.subscribe(item.Topico, 1);
                    Log.i(DEBUG_TAG, "Topico: " + item.Topico);
                }
                STATUS_SERVICO_MQTT = true;
                Log.i(DEBUG_TAG, "Inscrição nos Topicos Realizada com Sucesso");
                enviarMensage();

            } catch (MqttException e) {
                desConectarMqtt();
                Log.i(DEBUG_TAG, "Erro ao realizar inscrição nos topicos");
                e.printStackTrace();
            }
        }


    }

    private void pararServico() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MqttService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        mAlarme.cancel(pendingIntent);

    }


    @Override
    public void connectionLost(Throwable cause) {
        Log.i(DEBUG_TAG, "Perdeu Conexão");
        desConectarMqtt();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        try {
            Log.i(DEBUG_TAG, "Recebe mensagens do MQTT");
            Log.i(DEBUG_TAG, "Topico: " + topic + " Mensagem: " + new String(message.getPayload()) + " QoS:" + message.getQos());

            boolean isAppRodando = new VerificaBackground().execute(getApplication()).get();
            MqttMensagemModel mqttMensagemModel;

            JSONObject jObj = new JSONObject(new String(message.getPayload()));
            mqttMensagemModel = new MqttMensagemModel(jObj, topic);

            if (mqttMensagemModel == null || mqttMensagemModel.Mensagem == null || mqttMensagemModel.Mensagem.isEmpty())
                return;

            UsuarioModel usuario = new LoginDAO(getApplicationContext()).Buscar();
            if (mqttMensagemModel.Tipo == 2 && mqttMensagemModel.IdUsuario == usuario.UsuarioId)
                return;

            ContatoChatModel filtroContato = new ContatoChatModel();
            if (mqttMensagemModel.Tipo == 1)
                filtroContato.IdUsuario = mqttMensagemModel.IdUsuario;
            else if (mqttMensagemModel.Tipo == 2)
                filtroContato.IdGrupoMqtt = mqttMensagemModel.IdGrupoMqtt;

            List<ContatoChatModel> listaContato = new ContatoDAO(getApplicationContext()).Buscar(filtroContato);
            if (listaContato.size() > 0)
                mqttMensagemModel.Topico = listaContato.get(0).Topico;
            else
                return;


            if (MqttMensageActivity.StatusActivity && mqttMensagemModel.Tipo == 2 &&
                    mqttMensagemModel.IdGrupoMqtt > 0 &&
                    mqttMensagemModel.IdGrupoMqtt == MqttMensageActivity.IdGrupoMqtt) {

                Log.i(DEBUG_TAG, "Tipo: Grupo");
                MqttMensageActivity.MostraMensagem(mqttMensagemModel);

            } else if ((MqttMensageActivity.StatusActivity && mqttMensagemModel.Tipo == 1 &&
                    mqttMensagemModel.IdUsuario == MqttMensageActivity.IdUsuario)) {

                Log.i(DEBUG_TAG, "Tipo: Chat");
                MqttMensageActivity.MostraMensagem(mqttMensagemModel);
            } else if ((MqttMensageActivity.StatusActivity && mqttMensagemModel.Tipo == 3 &&
                    MqttMensageActivity.Tipo == mqttMensagemModel.Tipo &&
                    mqttMensagemModel.IdUsuario == MqttMensageActivity.IdUsuario)) {

                Log.i(DEBUG_TAG, "Tipo: Alerta");
                MqttMensageActivity.MostraMensagem(mqttMensagemModel);

            } else {

                if (mqttMensagemModel.Tipo == 2)
                    new Notificacao().MostrarMqtt(getApplicationContext(), mqttMensagemModel.NomeGrupoMqtt, mqttMensagemModel.Mensagem);
                else
                    new Notificacao().MostrarMqtt(getApplicationContext(), mqttMensagemModel.NomeUsuario, mqttMensagemModel.Mensagem);

            }
            new MqttMensagemDAO(getApplicationContext()).Inserir(mqttMensagemModel);

            if (ListaMqttActivity.StatusActivity){
                ListaMqttActivity.Atualizar();
            }

        } catch (JSONException je) {
            Log.i(DEBUG_TAG, "Erro ao recerber mensagem JSONException");
            je.printStackTrace();
        } catch (Exception e) {
            Log.i(DEBUG_TAG, "Erro ao recerber mensagem GRAVE");
            e.printStackTrace();
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.i(DEBUG_TAG, "Ping");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(DEBUG_TAG, "onDestroy!");

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }
}
