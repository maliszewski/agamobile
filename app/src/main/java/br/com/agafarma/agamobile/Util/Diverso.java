package br.com.agafarma.agamobile.Util;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.agafarma.agamobile.Activity.ErrorActivity;
import br.com.agafarma.agamobile.Activity.HomeActivity;

public class Diverso {
    public static ArrayList<View> getViewsByTag(ViewGroup root, String tag) {
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }

        }
        return views;
    }

    public ProgressDialog TempoTela(Context paramContext) {
        Log.i("progressWait", "Tempo de tela");
        ProgressDialog result = ProgressDialog.show(paramContext, "Aviso...", "Aguarde por favor...");
        result.setCancelable(false);
        result.setIndeterminate(true);
        result.setCanceledOnTouchOutside(false);
        //result.setCancelable(true);
        result.show();
        return result;
    }

    public static boolean isOnline(Context paramContext) {
        ConnectivityManager manager = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static boolean isWifiConnected(Context paramContext) {
        ConnectivityManager manager = (ConnectivityManager) paramContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAplicativoRodando(Context context) {
        ApplicationInfo info = context.getApplicationInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (ActivityManager.RunningAppProcessInfo obj:procInfos) {
                Log.i("Diverso", "Aplicacao esta Rodando: " + obj.processName);
                return true;
            }
        }
        return false;
    }

    public static void  Error(Context mContext){
        Intent intent = new Intent(mContext, ErrorActivity.class);
        mContext.startActivity(intent);
    }
}
