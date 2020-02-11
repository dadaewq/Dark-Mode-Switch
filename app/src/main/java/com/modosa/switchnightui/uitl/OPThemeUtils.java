package com.modosa.switchnightui.uitl;

import android.content.Context;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

final class OPThemeUtils {

    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR = "oneplus_basiccolor";
    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_DYNAMICFONT = "oneplus_dynamicfont";
    private static final String KEY_ORIGIN_DARK_MODE_ACTION = "origin_oem_black_mode";
    private static final String KEY_DARK_MODE_ACTION = "oem_black_mode";
    private static final String OEM_BLACK_MODE_ACCENT_COLOR = "oem_black_mode_accent_color";
    private static final String OEM_WHITE_MODE_ACCENT_COLOR = "oem_white_mode_accent_color";
    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_AODNOTIFICATION = "oneplus_aodnotification";
    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_AODNOTIFICATION_GOLD = "gold";
    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_AODNOTIFICATION_PURPLE = "purple";
    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_AODNOTIFICATION_RED = "red";
    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR_BLCAK = "black";
    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR_WHITE = "white";
    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_DYNAMICFONT_NOTOSANS = "1";
    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_DYNAMICFONT_SLATE_OF_ONEPLUS_STYLE = "2";
    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE = "oneplus_shape";
    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE_CIRCLE = "circle";
    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE_ROUNDEDRECT = "roundedrect";
    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE_SQUIRCLE = "squircle";
    private static final String OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE_TEARDROP = "teardrop";

    private static final String OP_OEM_FONT_MODE = "oem_font_mode";
    private static final String OEM_BLACK_MODE_ACCENT_COLOR_INDEX = "oem_black_mode_accent_color_index";
    private static final String OEM_WHITE_MODE_ACCENT_COLOR_INDEX = "oem_white_mode_accent_color_index";

    private static final String OP_PRIMARY_DEFAULT_LIGHT = "#ff2196f3";
    private static final String OP_PRIMARY_DEFAULT_DARK = "#ff42a5f5";
    private static final String KEY_AOD_CLOCK_STYLE = "aod_clock_style";
    private static final int CLOCK_TYPE_ANALOG = 1;
    private static final int CLOCK_TYPE_DEFAULT = 0;
    private static final int CLOCK_TYPE_MINIMALISM = 2;


    private static final String OP_CONSTANTS_ONEPLUS_ACCENT_COLOR = "oneplus_accent_color";
    private static final String OP_CONSTANTS_OP_CUSTOM_HORIZON_LIGHT_ANIMATION_STYLE_KEY = "op_custom_horizon_light_animation_style";
    private static final String OP_CONSTANTS_OP_CUSTOM_UNLOCK_ANIMATION_STYLE_KEY = "op_custom_unlock_animation_style";

    private static final String CMD_SETTINGS_PUT_SYSTEM = "settings put system ";
    private static final String CMD_SETPROP_THEME_STATUS = "setprop persist.sys.theme.status ";
    private static final String CMD_SETPROP_THEME_ACCENTCOLOR = "setprop persist.sys.theme.accentcolor ";
    private static final String SERVICE_CALL_UIMODE = "service call uimode 4 i32 ";
    private static final String BLANK = " ";


    static void setCurrentBasicColorMode(int colorMode) {
        List<String> cmds = new ArrayList<>();
        String cmd;
        cmd = CMD_SETTINGS_PUT_SYSTEM + KEY_DARK_MODE_ACTION + BLANK + colorMode;
        cmds.add(cmd);

        cmd = CMD_SETPROP_THEME_STATUS + colorMode;
        cmds.add(cmd);

        ShellUtil.execWithRoot(cmds);
    }


    private static void setCurrentShape(int shapeMode) {
        String cmd = CMD_SETTINGS_PUT_SYSTEM + OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE + BLANK + shapeMode;
        ShellUtil.execWithRoot(cmd);
    }

    private static void setCurrentFont(int font) {
        String cmd = CMD_SETTINGS_PUT_SYSTEM + OP_OEM_FONT_MODE + BLANK + font;
        ShellUtil.execWithRoot(cmd);
    }


    private static void setCurrentHorizonLight(int horizoLight) {
        String cmd = CMD_SETTINGS_PUT_SYSTEM + OP_CONSTANTS_OP_CUSTOM_HORIZON_LIGHT_ANIMATION_STYLE_KEY + BLANK + horizoLight;
        ShellUtil.execWithRoot(cmd);
    }

    private static void disableAllThemes(Context context) {
        HashMap<String, String> map = new HashMap<>();
        OpTheme opTheme = new OpTheme(context);
        setCurrentBasicColorMode(2);
        map.clear();
        setCurrentFont(1);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_DYNAMICFONT, OP_CUSTOMIZATION_THEME_ONEPLUS_DYNAMICFONT_SLATE_OF_ONEPLUS_STYLE);
        opTheme.disableTheme(map);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_DYNAMICFONT, OP_CUSTOMIZATION_THEME_ONEPLUS_DYNAMICFONT_NOTOSANS);
        opTheme.disableTheme(map);
        map.clear();
        setCurrentShape(1);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE, OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE_SQUIRCLE);
        opTheme.disableTheme(map);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE, OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE_CIRCLE);
        opTheme.disableTheme(map);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE, OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE_TEARDROP);
        opTheme.disableTheme(map);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE, OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE_ROUNDEDRECT);
        opTheme.disableTheme(map);
        map.clear();
        setCurrentHorizonLight(0);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_AODNOTIFICATION, OP_CUSTOMIZATION_THEME_ONEPLUS_AODNOTIFICATION_GOLD);
        opTheme.disableTheme(map);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_AODNOTIFICATION, OP_CUSTOMIZATION_THEME_ONEPLUS_AODNOTIFICATION_RED);
        opTheme.disableTheme(map);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_AODNOTIFICATION, OP_CUSTOMIZATION_THEME_ONEPLUS_AODNOTIFICATION_PURPLE);
        opTheme.disableTheme(map);
    }

    static void enableLightThemes(Context context) {
        String cmd;
        if (Settings.System.getInt(context.getContentResolver(), KEY_ORIGIN_DARK_MODE_ACTION, 0) != 0) {
            cmd = CMD_SETTINGS_PUT_SYSTEM + KEY_ORIGIN_DARK_MODE_ACTION + BLANK + 0;
            ShellUtil.execWithRoot(cmd);
        }
        disableAllThemes(context);
        HashMap<String, String> map = new HashMap<>();
        OpTheme opTheme = new OpTheme(context);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR, OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR_BLCAK);
        opTheme.disableTheme(map);
        map.clear();
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR, OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR_WHITE);
        opTheme.enableTheme(map);
        enableAospDarkTheme(false);
        setCurrentBasicColorMode(0);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_DYNAMICFONT, OP_CUSTOMIZATION_THEME_ONEPLUS_DYNAMICFONT_NOTOSANS);
        setCurrentFont(1);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE, OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE_CIRCLE);
        setCurrentShape(1);
        opTheme.enableTheme(map);
        setCurrentHorizonLight(0);

        List<String> cmds = new ArrayList<>();

        cmd = CMD_SETTINGS_PUT_SYSTEM + OP_CONSTANTS_ONEPLUS_ACCENT_COLOR + BLANK + OP_PRIMARY_DEFAULT_LIGHT;
        cmds.add(cmd);

        cmd = CMD_SETPROP_THEME_ACCENTCOLOR + OP_PRIMARY_DEFAULT_LIGHT.replace("#", "");
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OEM_BLACK_MODE_ACCENT_COLOR + BLANK + OP_PRIMARY_DEFAULT_LIGHT;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OEM_WHITE_MODE_ACCENT_COLOR + BLANK + OP_PRIMARY_DEFAULT_DARK;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OEM_BLACK_MODE_ACCENT_COLOR_INDEX + BLANK + 0;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OEM_WHITE_MODE_ACCENT_COLOR_INDEX + BLANK + 0;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OP_CONSTANTS_OP_CUSTOM_UNLOCK_ANIMATION_STYLE_KEY + BLANK + 0;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + KEY_AOD_CLOCK_STYLE + BLANK + CLOCK_TYPE_DEFAULT;
        cmds.add(cmd);

        ShellUtil.execWithRoot(cmds);
    }

    static void enableDarkThemes(Context context) {
        String cmd;
        if (Settings.System.getInt(context.getContentResolver(), KEY_ORIGIN_DARK_MODE_ACTION, 0) != 1) {
            cmd = CMD_SETTINGS_PUT_SYSTEM + KEY_ORIGIN_DARK_MODE_ACTION + BLANK + 0;
            ShellUtil.exec(cmd, true);
        }
        disableAllThemes(context);
        HashMap<String, String> map = new HashMap<>();
        OpTheme opTheme = new OpTheme(context);
        enableAospDarkTheme(true);
        setCurrentBasicColorMode(1);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_DYNAMICFONT, OP_CUSTOMIZATION_THEME_ONEPLUS_DYNAMICFONT_NOTOSANS);
        setCurrentFont(1);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE, OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE_ROUNDEDRECT);
        setCurrentShape(2);
        map.put(OP_CUSTOMIZATION_THEME_ONEPLUS_AODNOTIFICATION, OP_CUSTOMIZATION_THEME_ONEPLUS_AODNOTIFICATION_PURPLE);
        setCurrentHorizonLight(3);
        opTheme.enableTheme(map);

        List<String> cmds = new ArrayList<>();

        cmd = CMD_SETTINGS_PUT_SYSTEM + OP_CONSTANTS_ONEPLUS_ACCENT_COLOR + BLANK + OP_PRIMARY_DEFAULT_DARK;
        cmds.add(cmd);

        cmd = CMD_SETPROP_THEME_ACCENTCOLOR + OP_PRIMARY_DEFAULT_DARK.replace("#", "");
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OEM_BLACK_MODE_ACCENT_COLOR + BLANK + OP_PRIMARY_DEFAULT_LIGHT;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OEM_WHITE_MODE_ACCENT_COLOR + BLANK + OP_PRIMARY_DEFAULT_DARK;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OEM_BLACK_MODE_ACCENT_COLOR_INDEX + BLANK + 0;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OEM_WHITE_MODE_ACCENT_COLOR_INDEX + BLANK + 0;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OP_CONSTANTS_OP_CUSTOM_UNLOCK_ANIMATION_STYLE_KEY + BLANK + 1;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + KEY_AOD_CLOCK_STYLE + BLANK + CLOCK_TYPE_ANALOG;
        cmds.add(cmd);

        ShellUtil.execWithRoot(cmds);
    }

    static void enableColorfulThemes(Context context) {
        String cmd;
        if (Settings.System.getInt(context.getContentResolver(), KEY_ORIGIN_DARK_MODE_ACTION, 0) != 2) {
            cmd = CMD_SETTINGS_PUT_SYSTEM + KEY_ORIGIN_DARK_MODE_ACTION + BLANK + 2;
            ShellUtil.execWithRoot(cmd);
        }
        enableAospDarkTheme(false);
        disableAllThemes(context);
        new HashMap();
        OpTheme opTheme = new OpTheme(context);
        HashMap<String, String> disableMap = new HashMap<>();
        disableMap.put(OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR, OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR_WHITE);
        opTheme.disableTheme(disableMap);
        disableMap.put(OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR, OP_CUSTOMIZATION_THEME_ONEPLUS_BASICCOLOR_BLCAK);
        opTheme.disableTheme(disableMap);
        setCurrentBasicColorMode(2);
        HashMap<String, String> mapEnable = new HashMap<>();
        OpTheme opThemeEnable = new OpTheme(context);
        mapEnable.put(OP_CUSTOMIZATION_THEME_ONEPLUS_DYNAMICFONT, OP_CUSTOMIZATION_THEME_ONEPLUS_DYNAMICFONT_SLATE_OF_ONEPLUS_STYLE);
        setCurrentFont(2);
        mapEnable.put(OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE, OP_CUSTOMIZATION_THEME_ONEPLUS_SHAPE_SQUIRCLE);
        setCurrentShape(4);
        mapEnable.put(OP_CUSTOMIZATION_THEME_ONEPLUS_AODNOTIFICATION, OP_CUSTOMIZATION_THEME_ONEPLUS_AODNOTIFICATION_GOLD);
        setCurrentHorizonLight(2);
        opThemeEnable.enableTheme(mapEnable);

        List<String> cmds = new ArrayList<>();

        cmd = CMD_SETTINGS_PUT_SYSTEM + OP_CONSTANTS_ONEPLUS_ACCENT_COLOR + BLANK + OP_PRIMARY_DEFAULT_LIGHT;
        cmds.add(cmd);

        cmd = CMD_SETPROP_THEME_ACCENTCOLOR + OP_PRIMARY_DEFAULT_LIGHT.replace("#", "");
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OEM_BLACK_MODE_ACCENT_COLOR + BLANK + OP_PRIMARY_DEFAULT_LIGHT;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OEM_WHITE_MODE_ACCENT_COLOR + BLANK + OP_PRIMARY_DEFAULT_DARK;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OEM_BLACK_MODE_ACCENT_COLOR_INDEX + BLANK + 0;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OEM_WHITE_MODE_ACCENT_COLOR_INDEX + BLANK + 0;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + OP_CONSTANTS_OP_CUSTOM_UNLOCK_ANIMATION_STYLE_KEY + BLANK + 2;
        cmds.add(cmd);

        cmd = CMD_SETTINGS_PUT_SYSTEM + KEY_AOD_CLOCK_STYLE + BLANK + CLOCK_TYPE_MINIMALISM;
        cmds.add(cmd);

        ShellUtil.execWithRoot(cmds);
    }

    private static void enableAospDarkTheme(boolean enable) {
        String cmd = SERVICE_CALL_UIMODE + (enable ? 2 : 0);
        ShellUtil.execWithRoot(cmd);
    }

}
