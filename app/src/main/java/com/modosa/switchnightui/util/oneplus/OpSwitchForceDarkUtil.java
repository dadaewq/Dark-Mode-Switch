package com.modosa.switchnightui.util.oneplus;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.modosa.switchnightui.util.ShellUtil;
import com.modosa.switchnightui.util.SwitchDarkModeUtil;

import java.util.ArrayList;
import java.util.List;

import static com.modosa.switchnightui.util.OpUtil.BLANK;

/**
 * @author dadaewq
 */
public class OpSwitchForceDarkUtil {
    static final String CMD_SETPROP_THEME = "setprop persist.sys.theme ";
    private static final String KEY_OP_FORCE_DARK_ENTIRE_WORLD = "op_force_dark_entire_world";
    private static final String KEY_AOSP_FORCE_DARK_MODE = "aosp_force_dark_mode";
    private static final String KEY_OP_FORCE_DARK_MODE = "op_force_dark_mode";
    private static final String CMD_SETTINGS_PUT_SECURE = "settings put secure ";
    private final Context context;

    public OpSwitchForceDarkUtil(Context context) {
        this.context = context;
    }

    public static boolean isOnePlus(Context context) {
        return Build.BRAND.toLowerCase().contains("oneplus") || Settings.Secure.getInt(context.getContentResolver(), OPThemeUtils.KEY_ORIGIN_DARK_MODE_ACTION, -2) != -2;
    }

    public static boolean isOnePlusForceDark(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), KEY_AOSP_FORCE_DARK_MODE, -2) == 1;
    }

    public boolean isForceDark() {
        return Settings.Secure.getInt(context.getContentResolver(), KEY_AOSP_FORCE_DARK_MODE, -2) == 1;
    }

    /**
     * One Plus Force Dark
     *
     * @param enable is enable force dark
     * @return isSu
     */
    public boolean setForceDark(boolean enable) {

        String[] checkRoot = ShellUtil.execWithRoot("exit");
        if ("0".equals(checkRoot[3])) {
            String cmd;
            if (enable) {
                List<String> cmds = new ArrayList<>();
                cmd = CMD_SETTINGS_PUT_SECURE + KEY_AOSP_FORCE_DARK_MODE + BLANK + 1;
                cmds.add(cmd);

                int getOemBlackMode = Settings.System.getInt(context.getContentResolver(), OPThemeUtils.KEY_DARK_MODE_ACTION, 0);

                cmd = OPThemeUtils.CMD_SETTINGS_PUT_SYSTEM + OPThemeUtils.KEY_ORIGIN_DARK_MODE_ACTION + BLANK + getOemBlackMode;
                cmds.add(cmd);
                ShellUtil.execWithRoot(cmds);

                OPThemeUtils.setCurrentBasicColorMode(1);

            } else {
                cmd = CMD_SETTINGS_PUT_SECURE + KEY_AOSP_FORCE_DARK_MODE + BLANK + 0;
                ShellUtil.execWithRoot(cmd);

                int oneplusTheme = Settings.System.getInt(context.getContentResolver(), OPThemeUtils.KEY_ORIGIN_DARK_MODE_ACTION, 0);

                if (oneplusTheme == 0) {
                    OPThemeUtils.enableLightThemes(context);
                } else if (oneplusTheme == 1) {
                    OPThemeUtils.enableDarkThemes(context);
                } else {
                    OPThemeUtils.enableColorfulThemes(context);
                }
            }
            int oneplusTheme = Settings.System.getInt(context.getContentResolver(), OPThemeUtils.KEY_ORIGIN_DARK_MODE_ACTION, 0);

            setNightMode(oneplusTheme, enable);
            return true;
        } else {
            return false;
        }


    }

    public boolean switchForceDark() {

        return setForceDark(!isForceDark());

    }

    private void setNightMode(int oneplusTheme, boolean enable) {
        String cmd;
        List<String> cmds = new ArrayList<>();

        if (enable) {
            cmd = SwitchDarkModeUtil.SERVICE_CALL_UIMODE + 2;
            cmds.add(cmd);

            cmd = CMD_SETPROP_THEME + 2;
            cmds.add(cmd);

            cmd = CMD_SETTINGS_PUT_SECURE + KEY_OP_FORCE_DARK_MODE + BLANK + 1;
            cmds.add(cmd);

            cmd = CMD_SETTINGS_PUT_SECURE + KEY_OP_FORCE_DARK_ENTIRE_WORLD + BLANK + 1;
            cmds.add(cmd);

        } else {
            if (oneplusTheme == 1) {
                cmd = SwitchDarkModeUtil.SERVICE_CALL_UIMODE + 2;
                cmds.add(cmd);

                cmd = CMD_SETPROP_THEME + 2;
                cmds.add(cmd);

            } else {
                cmd = SwitchDarkModeUtil.SERVICE_CALL_UIMODE + 1;
                cmds.add(cmd);

                cmd = CMD_SETPROP_THEME + 1;
                cmds.add(cmd);

            }
            cmd = CMD_SETTINGS_PUT_SECURE + KEY_OP_FORCE_DARK_MODE + BLANK + 0;
            cmds.add(cmd);

            cmd = CMD_SETTINGS_PUT_SECURE + KEY_OP_FORCE_DARK_ENTIRE_WORLD + BLANK + 0;
            cmds.add(cmd);

        }
        ShellUtil.execWithRoot(cmds);

    }

}
