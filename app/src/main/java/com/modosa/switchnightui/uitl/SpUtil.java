package com.modosa.switchnightui.uitl;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author dadaewq
 */
public class SpUtil {
    private final String stablemode = "stablemode";
    private final SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SpUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public int getMethod() {
        return sharedPreferences.getInt("method", -1);
    }

    public void putMethod(int checked) {
        editor = sharedPreferences.edit();
        editor.putInt("method", checked);
        editor.apply();
    }

    public boolean isStableMode() {
        return sharedPreferences.getBoolean(stablemode, false);
    }


    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value) {
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void putString(String key, String value) {
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void switchStableMode(boolean isstablemode) {
        editor = sharedPreferences.edit();
        if (isstablemode) {
            editor.putBoolean(stablemode, false);
        } else {
            editor.putBoolean(stablemode, true);
        }
        editor.apply();
    }
}
