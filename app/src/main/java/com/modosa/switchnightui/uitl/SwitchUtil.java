package com.modosa.switchnightui.uitl;

import android.app.UiModeManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.modosa.switchnightui.R;

import java.util.Objects;

/**
 * @author dadaewq
 */
public class SwitchUtil {

    private final UiModeManager uiModeManager;
    private final Context context;


    public SwitchUtil(Context context, UiModeManager uiModeManager) {
        this.context = context;
        this.uiModeManager = uiModeManager;
    }

    public boolean switch1(final int nightmode) {
        boolean av = true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            uiModeManager.enableCarMode(2);
        }
        int want = WriteSettingsUtil.YES;
        if (WriteSettingsUtil.YES == nightmode) {
            uiModeManager.setNightMode(want);
        } else {
            want = WriteSettingsUtil.NO;
            uiModeManager.setNightMode(want);
        }
        if (uiModeManager.getNightMode() == want) {
            av = false;
        }
        return av;
    }


    public void switch2(int nightmode) {
        WriteSettingsUtil.putKey(context, nightmode, new WriteSettingsUtil.OnEnableAccessibilityListener() {
            @Override
            public void onSuccess(String t) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (TelephonyManager.CALL_STATE_IDLE == ((TelephonyManager) Objects.requireNonNull(context.getSystemService(Context.TELEPHONY_SERVICE))).getCallState()) {
                        if (Configuration.UI_MODE_TYPE_CAR == uiModeManager.getCurrentModeType()) {
                            uiModeManager.disableCarMode(0);
                        } else {
                            uiModeManager.enableCarMode(2);
                            uiModeManager.disableCarMode(0);
                        }
                        return;
                    }
                }
                showToast(t);
            }

            @Override
            public void onFailed(String t) {
                if (t.contains("WRITE_SECURE_SETTINGS")) {
                    String cmd = "adb shell pm grant " + context.getPackageName() + " android.permission.WRITE_SECURE_SETTINGS";
                    copyCmd(cmd);
                    showToast(String.format(context.getString(R.string.needpermission), cmd));

                } else {
                    showToast(t);
                }
            }
        });

    }


    public boolean switch3(int nightmode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            uiModeManager.enableCarMode(2);
        }
        String cmd = "service call uimode 4 i32 " + nightmode;

        String[] checkRoot = ShellUtil.execWithRoot("exit");

        if ("0".equals(checkRoot[3])) {
            ShellUtil.execWithRoot(cmd);
            return false;
        } else {
            ShellUtil.exec(cmd, false);
            return true;
        }

    }

    public boolean isForceDark() {
        String getforce = "getprop debug.hwui.force_dark";
        String[] result = ShellUtil.exec(getforce, false);

        return Boolean.valueOf(result[0]);
    }


    public boolean switchForceDark() {
        String setforce = "setprop debug.hwui.force_dark ";

        String[] checkRoot = ShellUtil.execWithRoot("exit");

        boolean isforce = isForceDark();
        if ("0".equals(checkRoot[3])) {

            ShellUtil.execWithRoot(setforce + !isforce);
            if (isForceDark()) {
                showToast(context.getString(R.string.force_darkOn));
                return true;
            } else {
                showToast(context.getString(R.string.force_darkOff));
                return false;
            }
        } else {
            ShellUtil.exec(setforce + !isforce, false);

            String msg = context.getString(R.string.no_root);
            if (isForceDark()) {
                msg += "\n" + context.getString(R.string.force_darkOn);
                showToast(msg);
                return true;
            } else {
                msg += "\n" + context.getString(R.string.force_darkOff);
                showToast(msg);
                return false;
            }
        }

    }


    private void copyCmd(CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, text);
        assert clipboard != null;
        clipboard.setPrimaryClip(clipData);
    }

    public void showToast(final String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
