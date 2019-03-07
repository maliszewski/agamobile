package br.com.agafarma.agamobile.Util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class VerificaBackground extends AsyncTask<Context, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Context... contexts) {
        final Context context = contexts[0].getApplicationContext();

        return isAppExecutando(context);
    }

    private boolean isAppExecutando(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        final String packegeName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                    appProcess.processName.equals(packegeName))
                return true;
        }
        return false;
    }
}
