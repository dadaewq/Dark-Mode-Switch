package com.modosa.switchnightui.uitl;

import android.content.Context;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

public class OpUtil {
    private static final String KEY_AOSP_FORCE_DARK_MODE = "aosp_force_dark_mode";
    private static final String KEY_OP_FORCE_DARK_ENTIRE_WORLD = "op_force_dark_entire_world";
    private static final String KEY_OP_FORCE_DARK_MODE = "op_force_dark_mode";
    private static final String KEY_OEM_BLACK_MODE = "oem_black_mode";
    private static final String KEY_ORIGIN_OEM_BLACK_MODE = "origin_oem_black_mode";
    private static final String CMD_SETTINGS_PUT_SECURE = "settings put secure ";
    private static final String CMD_SETTINGS_PUT_SYSTEM = "settings put system ";
    private static final String CMD_SETPROP_THEME = "setprop persist.sys.theme ";
    private static final String SERVICE_CALL_UIMODE = "service call uimode 4 i32 ";
    private static final String BLANK = " ";
    private final Context context;


    public OpUtil(Context context) {
        this.context = context;
    }

    public boolean isOp() {
        return Settings.Secure.getInt(context.getContentResolver(), KEY_OP_FORCE_DARK_ENTIRE_WORLD, -2) != -2;
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
                cmd = CMD_SETTINGS_PUT_SECURE + KEY_AOSP_FORCE_DARK_MODE + BLANK + 0;
                ShellUtil.execWithRoot(cmd);

                int oneplusTheme = Settings.System.getInt(context.getContentResolver(), KEY_ORIGIN_OEM_BLACK_MODE, 0);

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
                cmd = CMD_SETTINGS_PUT_SECURE + KEY_AOSP_FORCE_DARK_MODE + BLANK + 1;
                cmds.add(cmd);

                int getOemBlackMode = Settings.System.getInt(context.getContentResolver(), KEY_OEM_BLACK_MODE, 0);

                cmd = CMD_SETTINGS_PUT_SYSTEM + getOemBlackMode;
                cmds.add(cmd);

                ShellUtil.execWithRoot(cmds);

                OPThemeUtils.setCurrentBasicColorMode(1);
            }
            int oneplusTheme = Settings.System.getInt(context.getContentResolver(), KEY_ORIGIN_OEM_BLACK_MODE, 0);

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
            cmd = SERVICE_CALL_UIMODE + 2;
            cmds.add(cmd);

            cmd = CMD_SETPROP_THEME + 2;
            cmds.add(cmd);

            cmd = CMD_SETTINGS_PUT_SECURE + KEY_OP_FORCE_DARK_ENTIRE_WORLD + BLANK + 1;
            cmds.add(cmd);

            cmd = CMD_SETTINGS_PUT_SECURE + KEY_OP_FORCE_DARK_MODE + BLANK + 1;
            cmds.add(cmd);

        } else {
            if (oneplusTheme == 1) {
                cmd = SERVICE_CALL_UIMODE + 2;
                cmds.add(cmd);

                cmd = CMD_SETPROP_THEME + 2;
                cmds.add(cmd);

            } else {
                cmd = SERVICE_CALL_UIMODE + 1;
                cmds.add(cmd);

                cmd = CMD_SETPROP_THEME + 1;
                cmds.add(cmd);

            }
            cmd = CMD_SETTINGS_PUT_SECURE + KEY_OP_FORCE_DARK_ENTIRE_WORLD + BLANK + 0;
            cmds.add(cmd);

            cmd = CMD_SETTINGS_PUT_SECURE + KEY_OP_FORCE_DARK_MODE + BLANK + 0;
            cmds.add(cmd);

        }
        ShellUtil.execWithRoot(cmds);

    }

}
