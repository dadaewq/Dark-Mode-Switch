package com.modosa.switchnightui.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Window;
import android.widget.Toast;

import com.modosa.switchnightui.R;

/**
 * @author dadaewq
 */
public class OpUtil {
    public static void showAlertDialog(Context context, AlertDialog alertDialog) {
        Window window = alertDialog.getWindow();
        if (window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.Background, null)));
            } else {
                window.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.Background)));
            }
            window.setBackgroundDrawableResource(R.drawable.alertdialog_background);
        }

        if (!((Activity) context).isFinishing()) {
            alertDialog.show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.rBackground, null));
            alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(context.getResources().getColor(R.color.rBackground, null));
        } else {
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.rBackground));
            alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(context.getResources().getColor(R.color.rBackground));
        }
    }

    static void copyCmd(Context context, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, text);
        assert clipboard != null;
        clipboard.setPrimaryClip(clipData);
    }

    public static void showToast0(Context context, final String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast0(Context context, final int stringId) {
        Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast1(Context context, final String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showToast1(Context context, final int stringId) {
        Toast.makeText(context, stringId, Toast.LENGTH_LONG).show();
    }
}
