package com.modosa.switchnightui.util.oneplus;

import android.content.Context;
import android.provider.Settings;

import com.modosa.switchnightui.util.ShellUtil;

import static com.modosa.switchnightui.util.OpUtil.BLANK;

/**
 * @author dadaewq
 */
public class OpSwitchDarkModeUtil {

    public static void setDarkMode(Context context, boolean enable) {
        String cmd;
        if (enable) {
            OPThemeUtils.enableDarkThemes(context);
            cmd = OpSwitchForceDarkUtil.CMD_SETPROP_THEME + 2;
        } else {
            int getOemBlackMode = Settings.System.getInt(context.getContentResolver(), OPThemeUtils.KEY_DARK_MODE_ACTION, 0);

            if (getOemBlackMode == 1) {
                getOemBlackMode = 2;
            }

            cmd = OPThemeUtils.CMD_SETTINGS_PUT_SYSTEM + OPThemeUtils.KEY_ORIGIN_DARK_MODE_ACTION + BLANK + getOemBlackMode;
            ShellUtil.execWithRoot(cmd);

            int oneplusTheme = Settings.System.getInt(context.getContentResolver(), OPThemeUtils.KEY_ORIGIN_DARK_MODE_ACTION, 0);

            if (oneplusTheme == 2) {
                OPThemeUtils.enableColorfulThemes(context);
            } else {
                OPThemeUtils.enableLightThemes(context);
            }

            cmd = OpSwitchForceDarkUtil.CMD_SETPROP_THEME + 1;
        }
        ShellUtil.execWithRoot(cmd);
    }

}
