package com.modosa.switchnightui.util;

import static com.modosa.switchnightui.util.OpUtil.BLANK;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.modosa.switchnightui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dadaewq
 */
public class SwitchBatterySaverUtil {
    private static final String KEY_LOW_POWER_MODE = "low_power";
    private static final String KEY_LOW_POWER_MODE_STICKY = "low_power_sticky";
    private static final String CMD_SETTINGS_PUT_GLOBAL = "settings put global ";
    private final Context context;
    private final ContentResolver resolver;
    private final PowerManager powerManager;

    public SwitchBatterySaverUtil(Context context) {
        this.context = context;
        resolver = context.getContentResolver();
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    public boolean isPowerSaveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return powerManager.isPowerSaveMode();
        } else {
            return false;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void switchBatterySaverWithResult() {
        boolean shouldOpen = !isPowerSaveMode();
        String message = setBatterySaver(shouldOpen);


        if (message != null) {
            OpUtil.showToast1(context, message);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                boolean newPowerMode = isPowerSaveMode();
                String msg = OpUtil.getTipStr1IsStr2(context,
                        context.getString(R.string.title_battery_saver),
                        context.getString(newPowerMode ?
                                R.string.tip_on :
                                R.string.tip_off
                        )
                );
                if (newPowerMode != shouldOpen) {
                    OpUtil.showToast0(context, context.getString(R.string.tip_cannotSwitchBatterySaver) + "\n" + msg);
                } else {
                    OpUtil.showToast1(context, msg);
                }
                handler.removeCallbacksAndMessages(null);
            }, 300);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public String setBatterySaver(boolean shouldOpen) {
        if (shouldOpen) {
            return setBatterySaver(1);
        } else {
            return setBatterySaver(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private String setBatterySaver(int newPowerMode) {
        String message = null;
        int oldPowerMode = Settings.Global.getInt(resolver, KEY_LOW_POWER_MODE, -1);
        try {
            if (oldPowerMode == newPowerMode) {
                if (newPowerMode == 1) {
                    Settings.Global.putInt(resolver, KEY_LOW_POWER_MODE, 0);
                } else {
                    Settings.Global.putInt(resolver, KEY_LOW_POWER_MODE, 1);
                }
            }

            Settings.Global.putInt(resolver, KEY_LOW_POWER_MODE, newPowerMode);
            Settings.Global.putInt(resolver, KEY_LOW_POWER_MODE_STICKY, newPowerMode);
        } catch (Exception e) {
            message = e + "";
            Log.e("Exception", e + "");
            if (message.contains("WRITE_SETTINGS")) {
                message = context.getString(R.string.tip_needpermission_WRITE_SETTINGS);
                @SuppressLint("InlinedApi") Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + context.getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException ignore) {
                }
            } else if (message.contains("WRITE_SECURE_SETTINGS")) {
                String cmd = "adb shell pm grant " + context.getPackageName() + " android.permission.WRITE_SECURE_SETTINGS";
                OpUtil.copyCmd(context, cmd);
                message = String.format(context.getString(R.string.tip_needpermission_WRITE_SECURE_SETTINGS), cmd);

            }
            if (setBatterySaverWithRoot(newPowerMode, oldPowerMode)) {
                message = null;
            }
        }
        return message;
    }

    private boolean setBatterySaverWithRoot(int newPowerMode, int oldPowerMode) {
        boolean isSu = false;
        String[] checkRoot = ShellUtil.execWithRoot("exit");
        if ("0".equals(checkRoot[3])) {
            String cmd;

            List<String> cmds;

            if (oldPowerMode == newPowerMode) {
                if (newPowerMode == 1) {
                    cmd = CMD_SETTINGS_PUT_GLOBAL + KEY_LOW_POWER_MODE + BLANK + 0;
                } else {
                    cmd = CMD_SETTINGS_PUT_GLOBAL + KEY_LOW_POWER_MODE + BLANK + 1;
                }
                ShellUtil.exec(cmd, true);
            }
            cmds = new ArrayList<>();
            cmd = CMD_SETTINGS_PUT_GLOBAL + KEY_LOW_POWER_MODE + BLANK + newPowerMode;
            cmds.add(cmd);
            cmd = CMD_SETTINGS_PUT_GLOBAL + KEY_LOW_POWER_MODE_STICKY + BLANK + newPowerMode;
            cmds.add(cmd);
            ShellUtil.execWithRoot(cmds);
            isSu = true;
        }
        return isSu;
    }

}
