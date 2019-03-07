package br.com.agafarma.agamobile.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class Tarefa {
    private static final String DEBUG_TAG = "Tarefa";

    public static ArrayList<String> alarmIntens = new ArrayList<String>();

    public static void Notificacao(Context context, long intervalo) {
        String intentName = "Alarm_" + String.valueOf(intervalo);
        if (!alarmIntens.contains(intentName)) {
            Cancelar(context);
            alarmIntens.add(intentName);
            Intent ishintent = new Intent(context, NotificacaoService.class);
            PendingIntent pintent = PendingIntent.getService(context, 0, ishintent, 0);
            AlarmManager alarm = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            alarm.cancel(pintent);
            if (intervalo > 0)
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (intervalo * 60000), pintent);
            else
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (5 * 60000), pintent);

            Log.i(DEBUG_TAG, "Criado ping: " + intentName);

        }
    }

    public static void Cancelar(Context c) {
        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        for (String iterator : alarmIntens) {
            Log.i(DEBUG_TAG, "Cancelar ping: " + iterator);
            Intent intent = new Intent(iterator);
            PendingIntent pi = PendingIntent.getBroadcast(c, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(pi);
        }
    }
}
