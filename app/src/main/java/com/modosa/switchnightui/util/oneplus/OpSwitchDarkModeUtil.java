package com.modosa.switchnightui.util.oneplus;

import static com.modosa.switchnightui.util.OpUtil.BLANK;

import android.content.Context;

import com.modosa.switchnightui.util.ShellUtil;
import com.modosa.switchnightui.util.SpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author dadaewq
 */
public class OpSwitchDarkModeUtil {
    private final Context context;

    public OpSwitchDarkModeUtil(Context context) {
        this.context = context;
    }

    public void setDarkMode(boolean enable) {
        List<String> cmds;
        int theme = 0;
        if (enable) {
            theme = 1;
        } else if (new SpUtil(context).getFalseBoolean("oneplus_use_colorful_theme")) {
            theme = 2;
        }
        cmds = getOneplusCommands(theme);
        ShellUtil.execWithRoot(cmds);
        enableThemes(theme);
    }

    private List<String> getOneplusCommands(int theme) {
        switch (theme) {
            case 1:
                return getOneplusCommands(1, 2);
            case 2:
                return getOneplusCommands(2, 1);
            default:
                return getOneplusCommands(0, 1);
        }
    }

    private List<String> getOneplusCommands(int parm1, int parm2) {
        String cmd;
        List<String> cmds = new ArrayList<>();
        cmd = OPThemeUtils.CMD_SETTINGS_PUT_SYSTEM + OPThemeUtils.KEY_DARK_MODE_ACTION + BLANK + parm1;
        cmds.add(cmd);

        cmd = OPThemeUtils.CMD_SETTINGS_PUT_SYSTEM + OPThemeUtils.KEY_ORIGIN_DARK_MODE_ACTION + BLANK + parm1;
        cmds.add(cmd);

        cmd = OpSwitchForceDarkUtil.CMD_SETPROP_THEME + parm2;
        cmds.add(cmd);

        cmd = OPThemeUtils.CMD_SETPROP_THEME_STATUS + parm1;
        cmds.add(cmd);

        return cmds;
    }

    private void enableThemes(int theme) {
        OpTheme opTheme = new OpTheme(context);
        HashMap<String, String> map = new HashMap<>();
        switch (theme) {
            case 1:
                map.clear();
                map.put(OPThemeUtils.OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR, OPThemeUtils.OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR_WHITE);
                opTheme.disableTheme(map);
                map.put(OPThemeUtils.OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR, OPThemeUtils.OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR_BLCAK);
                opTheme.enableTheme(map);
                break;
            case 2:
                map.clear();
                map.put(OPThemeUtils.OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR, OPThemeUtils.OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR_WHITE);
                opTheme.disableTheme(map);
                map.put(OPThemeUtils.OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR, OPThemeUtils.OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR_BLCAK);
                opTheme.disableTheme(map);
                break;
            default:
                map.put(OPThemeUtils.OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR, OPThemeUtils.OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR_BLCAK);
                opTheme.disableTheme(map);
                map.clear();
                map.put(OPThemeUtils.OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR, OPThemeUtils.OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR_WHITE);
                opTheme.enableTheme(map);
        }
    }

}
