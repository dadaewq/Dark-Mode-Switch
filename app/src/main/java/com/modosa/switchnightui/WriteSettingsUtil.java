package com.modosa.switchnightui;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;


/**
 * Change工具类
 */
class WriteSettingsUtil {
    private static final String KEY_MODE = "ui_night_mode";

    /**
     * need permission {@link android.Manifest.permission#WRITE_SECURE_SETTINGS},
     */

    static boolean isNightMode(Context context) {
        int mNightMode = -1;

        try {
            mNightMode = Settings.Secure.getInt(context.getContentResolver(),
                    WriteSettingsUtil.KEY_MODE, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (2 == mNightMode);

    }

    static void putKey(Context context, OnEnableAccessibilityListener listener) {
        String msg;
        try {
            int ui_mode = 2;
            if (isNightMode(context)) {
                ui_mode = 1;
            }
            if (Settings.Secure.putInt(context.getContentResolver(), KEY_MODE, ui_mode)) {
                msg = context.getString(R.string.yes2);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    msg = context.getString(R.string.yes2old);
                }
                listener.onSuccess(msg);
            } else {
                msg = context.getString(R.string.no2);
                listener.onFailed(msg);
            }

        } catch (Exception e) {
            msg = String.format(context.getString(R.string.error), "\n\n" + e);
            listener.onFailed(msg);
        }
    }

    public interface OnEnableAccessibilityListener {
        void onSuccess(String t);

        void onFailed(String t);
    }
}

