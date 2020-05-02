package com.modosa.switchnightui.util;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.modosa.switchnightui.R;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class BatterySaverUtil {
    private static final String KEY_LOW_POWER_MODE = "low_power";
    private static final String KEY_LOW_POWER_MODE_STICKY = "low_power_sticky";
    private static final String CMD_SETTINGS_PUT_GLOBAL = "settings put global ";
    private Context context;
    private ContentResolver resolver;

    public BatterySaverUtil(Context context) {
        this.context = context;
        resolver = context.getContentResolver();
    }

    public String setBatterySaver(boolean isOpen) {
        if (isOpen) {
            return setBatterySaver(1);
        } else {
            return setBatterySaver(0);
        }

    }

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
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
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

    private boolean setBatterySaverWithRoot(int newPowerMode, int nowPowerMode) {
        boolean isSu = false;
        String[] checkRoot = ShellUtil.execWithRoot("exit");
        if ("0".equals(checkRoot[3])) {
            String cmd;

            List<String> cmds;

            if (nowPowerMode == newPowerMode) {
                if (newPowerMode == 1) {
                    cmd = CMD_SETTINGS_PUT_GLOBAL + KEY_LOW_POWER_MODE + " " + 0;
                    ShellUtil.exec(cmd, true);
                } else {
                    cmd = CMD_SETTINGS_PUT_GLOBAL + KEY_LOW_POWER_MODE + " " + 1;
                    ShellUtil.exec(cmd, true);
                }
            }
            cmds = new ArrayList<>();
            cmd = CMD_SETTINGS_PUT_GLOBAL + KEY_LOW_POWER_MODE + " " + newPowerMode;
            cmds.add(cmd);
            cmd = CMD_SETTINGS_PUT_GLOBAL + KEY_LOW_POWER_MODE_STICKY + " " + newPowerMode;
            cmds.add(cmd);
            ShellUtil.execWithRoot(cmds);
            isSu = true;
        }
        return isSu;
    }

}
