package com.meli.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by DAVID on 19/05/2014.
 */
public class WidgetsUtils {

    public static void createSpinnerDialog(Activity activity, String title, String [] options, final SpinnerListener widgetListener) {

        AlertDialog.Builder b = new AlertDialog.Builder(activity);
        b.setTitle(title);
        b.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                widgetListener.update(which);
            }
        });
        b.show();
    }

}
