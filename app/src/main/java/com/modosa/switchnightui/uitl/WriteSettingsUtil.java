package com.modosa.switchnightui.uitl;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.modosa.switchnightui.R;

/**
 * WriteSettings 工具类
 *
 * @author dadaewq
 */
public class WriteSettingsUtil {
    public static final int YES = UiModeManager.MODE_NIGHT_YES;
    public static final int NO = UiModeManager.MODE_NIGHT_NO;
    private static final String KEY_MODE = "ui_night_mode";

    /**
     * need permission {@link android.Manifest.permission#WRITE_SECURE_SETTINGS},
     */

    public static boolean isNightMode(Context context) {
        int mNightMode = -1;

        try {
            mNightMode = Settings.Secure.getInt(context.getContentResolver(),
                    WriteSettingsUtil.KEY_MODE, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (2 == mNightMode);

    }

    static void putKey(Context context, int nightmode, OnEnableAccessibilityListener listener) {
        String msg;
        try {
            if (Settings.Secure.putInt(context.getContentResolver(), KEY_MODE, nightmode)) {
                msg = String.format(
                        Build.VERSION.SDK_INT < Build.VERSION_CODES.M ?
                                context.getString(R.string.yes2old) : context.getString(R.string.yes2),
                        nightmode == 2 ?
                                context.getString(R.string.on) : context.getString(R.string.off)
                );

                listener.onSuccess(msg);
            } else {
                msg = String.format(context.getString(R.string.failmethod), "2");
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

