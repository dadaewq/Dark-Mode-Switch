package com.modosa.switchnightui;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;


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
        String msg = "";
        try {
            if (isNightMode(context)) {
                if (Settings.Secure.putInt(context.getContentResolver(), KEY_MODE, 1)) {
                    msg = context.getString(R.string.yes2);
                    listener.onSuccess(msg);
                } else {
                    msg = context.getString(R.string.no2);
                    listener.onFailed(msg);
                }
            } else {
                if (Settings.Secure.putInt(context.getContentResolver(), KEY_MODE, 2)) {
                    msg = context.getString(R.string.yes2);
                    listener.onSuccess(msg);
                } else {
                    msg = context.getString(R.string.no2);
                    listener.onFailed(msg);
                }
            }
        } catch (Exception e) {
            msg = String.format(context.getString(R.string.error), "\n\n" + e);
            listener.onFailed(msg);
        } finally {
            Log.e("putKey", msg);
        }
    }

    public interface OnEnableAccessibilityListener {
        void onSuccess(String t);

        void onFailed(String t);
    }
}

