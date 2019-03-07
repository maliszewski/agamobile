package br.com.agafarma.agamobile.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootAndroidReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context c, Intent i) {
        Log.i("BootAndroidReceiver", "onReceive");
        Tarefa.Notificacao(c, 30);
        MqttService.acaoIniciarServico(c);
    }
}

