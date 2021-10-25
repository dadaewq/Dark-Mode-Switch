package com.modosa.switchnightui.util;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.modosa.switchnightui.R;

/**
 * @author dadaewq
 */
public class SwitchDisplayUtil {
    public static final String ACCESSIBILITY_DISPLAY_INVERSION_ENABLED = "accessibility_display_inversion_enabled";
    public static final String GRAYSCALE = "grayscale";
    public static final String NIGHT_DISPLAY_ACTIVATED = "night_display_activated";
    private static final String ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED = "accessibility_display_daltonizer_enabled";
    private static final String ACCESSIBILITY_DISPLAY_DALTONIZER = "accessibility_display_daltonizer";
    private static final String CMD_SETTINGS_PUT_SECURE = "settings put secure ";

    private final Context context;
    private final ContentResolver resolver;

    public SwitchDisplayUtil(Context context) {
        this.context = context;
        resolver = context.getContentResolver();
    }


    public void switchDisplayKeyWithResult(String key, String lablel) {
        String message = putSettingsSecureInt(key, !isDisplayKeyEnabled(key));

        if (message != null) {
            OpUtil.showToast1(context, message);
        } else {
            OpUtil.showToast1(context, String.format(context.getString(R.string.tip_str1_is_str2),
                    lablel,
                    context.getString(isDisplayKeyEnabled(key) ? R.string.tip_on : R.string.tip_off)
            ));
        }
    }

    public boolean isDisplayKeyEnabled(String key) {
        switch (key) {
            case ACCESSIBILITY_DISPLAY_INVERSION_ENABLED:
                return Settings.Secure.getInt(resolver, ACCESSIBILITY_DISPLAY_INVERSION_ENABLED, 0) != 0;
            case NIGHT_DISPLAY_ACTIVATED:
                return Settings.Secure.getInt(resolver, NIGHT_DISPLAY_ACTIVATED, 0) == 1;
            case GRAYSCALE:
                boolean enabled = Settings.Secure.getInt(resolver, ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 0) != 0;

                boolean isGrayscale = Settings.Secure.getInt(resolver, ACCESSIBILITY_DISPLAY_DALTONIZER, -1) == 0;

                return enabled && isGrayscale;
            default:
                return false;
        }
    }


    public String putSettingsSecureInt(String key, boolean shouldOpen) {
        if (GRAYSCALE.equals(key)) {
            String msg;
            if (shouldOpen) {
                msg = putSettingsSecureInt(ACCESSIBILITY_DISPLAY_DALTONIZER, 0);
                if (msg == null) {
                    return putSettingsSecureInt(ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 1);
                } else {
                    return msg;
                }
            } else {
                return putSettingsSecureInt(ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 0);
            }
        } else {
            if (shouldOpen) {
                return putSettingsSecureInt(key, 1);
            } else {
                return putSettingsSecureInt(key, 0);
            }
        }
    }

    private String putSettingsSecureInt(String key, int value) {
        String message = null;
        try {
            Settings.Secure.putInt(resolver, key, value);
        } catch (Exception e) {
            message = e + "";
            Log.e("Exception", e + "");
            if (message.contains("WRITE_SETTINGS")) {
                message = context.getString(R.string.tip_needpermission_WRITE_SETTINGS);
                @SuppressLint("InlinedApi")
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + context.getPackageName()))
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException ignore) {
                }
            } else if (message.contains("WRITE_SECURE_SETTINGS")) {
                String cmd = "adb shell pm grant " + context.getPackageName() + " android.permission.WRITE_SECURE_SETTINGS";
                OpUtil.copyCmd(context, cmd);
                message = String.format(context.getString(R.string.tip_needpermission_WRITE_SECURE_SETTINGS), cmd);

            }
            if (putSettingsSecureIntWithRoot(key, value)) {
                message = null;
            }
        }
        return message;
    }

    private boolean putSettingsSecureIntWithRoot(String key, int value) {
        boolean isSu = false;
        String[] checkRoot = ShellUtil.execWithRoot("exit");
        if ("0".equals(checkRoot[3])) {
            String cmd = CMD_SETTINGS_PUT_SECURE + key + OpUtil.BLANK + value;
            ShellUtil.execWithRoot(cmd);
            isSu = true;
        }
        return isSu;
    }
}
