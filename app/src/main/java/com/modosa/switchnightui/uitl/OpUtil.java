package com.modosa.switchnightui.uitl;

import android.content.Context;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

public class OpUtil {
    public static final String KEY_OP_FORCE_DARK_ENTIRE_WORLD = "op_force_dark_entire_world";
    private static final String KEY_AOSP_FORCE_DARK_MODE = "aosp_force_dark_mode";
    private static final String KEY_OP_FORCE_DARK_MODE = "op_force_dark_mode";
    private static final String CMD_SETTINGS_PUT_SECURE = "settings put secure ";
    private static final String CMD_SETPROP_THEME = "setprop persist.sys.theme ";

    private final Context context;

    public OpUtil(Context context) {
        this.context = context;
    }

    public boolean isOp() {
        return Settings.Secure.getInt(context.getContentResolver(), OPThemeUtils.KEY_ORIGIN_DARK_MODE_ACTION, -2) != -2;
    }

    public boolean isForceDark() {
        return Settings.Secure.getInt(context.getContentResolver(), KEY_AOSP_FORCE_DARK_MODE, -2) == 1;

    }

    public boolean switchForceDark() {

        String[] checkRoot = ShellUtil.execWithRoot("exit");
        if ("0".equals(checkRoot[3])) {
            String cmd;
            boolean isEnabled;
            if (isForceDark()) {
                isEnabled = false;
                cmd = CMD_SETTINGS_PUT_SECURE + KEY_AOSP_FORCE_DARK_MODE + OPThemeUtils.BLANK + 0;
                ShellUtil.execWithRoot(cmd);

                int oneplusTheme = Settings.System.getInt(context.getContentResolver(), OPThemeUtils.KEY_ORIGIN_DARK_MODE_ACTION, 0);

                if (oneplusTheme == 0) {
                    OPThemeUtils.enableLightThemes(context);
                } else if (oneplusTheme == 1) {
                    OPThemeUtils.enableDarkThemes(context);
                } else {
                    OPThemeUtils.enableColorfulThemes(context);
                }
            } else {
                isEnabled = true;
                List<String> cmds = new ArrayList<>();
                cmd = CMD_SETTINGS_PUT_SECURE + KEY_AOSP_FORCE_DARK_MODE + OPThemeUtils.BLANK + 1;
                cmds.add(cmd);

                int getOemBlackMode = Settings.System.getInt(context.getContentResolver(), OPThemeUtils.KEY_DARK_MODE_ACTION, 0);

                cmd = OPThemeUtils.CMD_SETTINGS_PUT_SYSTEM + OPThemeUtils.KEY_ORIGIN_DARK_MODE_ACTION + OPThemeUtils.BLANK + getOemBlackMode;
                cmds.add(cmd);

                ShellUtil.execWithRoot(cmds);

                OPThemeUtils.setCurrentBasicColorMode(1);
            }
            int oneplusTheme = Settings.System.getInt(context.getContentResolver(), OPThemeUtils.KEY_ORIGIN_DARK_MODE_ACTION, 0);

            setNightMode(oneplusTheme, isEnabled);
            return true;
        } else {
            return false;
        }


    }

    private void setNightMode(int oneplusTheme, boolean enable) {
        String cmd;
        List<String> cmds = new ArrayList<>();

        if (enable) {
            cmd = SwitchUtil.SERVICE_CALL_UIMODE + 2;
            cmds.add(cmd);

            cmd = CMD_SETPROP_THEME + 2;
            cmds.add(cmd);

            cmd = CMD_SETTINGS_PUT_SECURE + KEY_OP_FORCE_DARK_MODE + OPThemeUtils.BLANK + 1;
            cmds.add(cmd);

            cmd = CMD_SETTINGS_PUT_SECURE + KEY_OP_FORCE_DARK_ENTIRE_WORLD + OPThemeUtils.BLANK + 1;
            cmds.add(cmd);

        } else {
            if (oneplusTheme == 1) {
                cmd = SwitchUtil.SERVICE_CALL_UIMODE + 2;
                cmds.add(cmd);

                cmd = CMD_SETPROP_THEME + 2;
                cmds.add(cmd);

            } else {
                cmd = SwitchUtil.SERVICE_CALL_UIMODE + 1;
                cmds.add(cmd);

                cmd = CMD_SETPROP_THEME + 1;
                cmds.add(cmd);

            }
            cmd = CMD_SETTINGS_PUT_SECURE + KEY_OP_FORCE_DARK_MODE + OPThemeUtils.BLANK + 0;
            cmds.add(cmd);

            cmd = CMD_SETTINGS_PUT_SECURE + KEY_OP_FORCE_DARK_ENTIRE_WORLD + OPThemeUtils.BLANK + 0;
            cmds.add(cmd);

        }
        ShellUtil.execWithRoot(cmds);

    }

}
