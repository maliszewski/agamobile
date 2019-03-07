package br.com.agafarma.agamobile.Util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

import br.com.agafarma.agamobile.Activity.AvisoActivity;
import br.com.agafarma.agamobile.Activity.HomeActivity;
import br.com.agafarma.agamobile.Activity.ListaAvisoActivity;
import br.com.agafarma.agamobile.Activity.ListaMqttActivity;
import br.com.agafarma.agamobile.R;

public class Notificacao {
    @SuppressLint("ResourceAsColor")
    public void Mostrar(Context ctx, String title, String text, String typeInfo, Integer type) {

        Bundle bundle = new Bundle();

        Intent intent = new Intent(ctx, ListaAvisoActivity.class);
        if (type > 0)
            intent = new Intent(ctx, AvisoActivity.class);

        bundle.putString("idAviso", String.valueOf(type));
        intent.putExtras(bundle);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap bm = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_luz_black_24dp);

        NotificationCompat.Builder b = new NotificationCompat.Builder(ctx);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.agafarma_notificacao)
                .setLargeIcon(bm)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(text)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo(typeInfo);
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, b.build());
    }
    public void MostrarMqtt(Context ctx, String nomeUsuario, String mensagem) {

        Bundle bundle = new Bundle();

        Intent intent = new Intent(ctx, ListaMqttActivity.class);
        //Intent intent = new Intent(ctx, HomeActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap bm = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_luz_black_24dp);

        NotificationCompat.Builder b = new NotificationCompat.Builder(ctx);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.agafarma_notificacao)
                .setLargeIcon(bm)
                .setTicker(nomeUsuario)
                .setContentTitle(nomeUsuario)
                .setContentText(mensagem)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, b.build());
    }

}
