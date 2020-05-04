package com.modosa.switchnightui.util.oneplus;

import com.modosa.switchnightui.util.ShellUtil;

import java.util.ArrayList;
import java.util.List;

import static com.modosa.switchnightui.util.OpUtil.BLANK;

/**
 * @author dadaewq
 */
public class OpSwitchDarkModeUtil {

    public static void setDarkMode(boolean enable) {
        String cmd;
        List<String> cmds = new ArrayList<>();
        if (enable) {
            cmd = OPThemeUtils.CMD_SETTINGS_PUT_SYSTEM + OPThemeUtils.KEY_ORIGIN_DARK_MODE_ACTION + BLANK + 1;
            cmds.add(cmd);

            cmd = OPThemeUtils.CMD_SETTINGS_PUT_SYSTEM + OPThemeUtils.KEY_DARK_MODE_ACTION + BLANK + 1;
            cmds.add(cmd);
        } else {
            cmd = OPThemeUtils.CMD_SETTINGS_PUT_SYSTEM + OPThemeUtils.KEY_ORIGIN_DARK_MODE_ACTION + BLANK + 0;
            cmds.add(cmd);

            cmd = OPThemeUtils.CMD_SETTINGS_PUT_SYSTEM + OPThemeUtils.KEY_DARK_MODE_ACTION + BLANK + 0;
            cmds.add(cmd);
        }
        ShellUtil.execWithRoot(cmds);
    }

}
