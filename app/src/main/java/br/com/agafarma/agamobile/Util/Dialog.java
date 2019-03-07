package br.com.agafarma.agamobile.Util;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import br.com.agafarma.agamobile.R;

public class Dialog {
    Context mContext;

    public Dialog(Context context) {
        this.mContext = context;
    }

    public void ShowSettingsAlertGPS(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
                dialog.cancel();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void ShowSettingsAlertConect(int title, int message, final boolean finish) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                mContext.startActivity(intent);
                dialog.cancel();
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (finish)
                    ((Activity) mContext).finish();
            }
        });
        alertDialog.show();
    }

    public void ShowAlertOK(String title, int message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public void ShowAlertOK(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public void ShowAlertOK(String title, String message, final Boolean finish) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (finish)
                    ((Activity) mContext).finish();
            }
        });

        alertDialog.show();
    }



    public void ShowAlertTicketOK(int title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) mContext).finish();
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


}
