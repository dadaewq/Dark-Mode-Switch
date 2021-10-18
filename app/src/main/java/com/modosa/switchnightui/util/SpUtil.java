package com.modosa.switchnightui.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.preference.PreferenceManager;

import com.modosa.switchnightui.BuildConfig;

/**
 * @author dadaewq
 */
public class SpUtil {
    private final SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SpUtil(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sharedPreferences = context.createDeviceProtectedStorageContext().getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences", Context.MODE_PRIVATE);
        } else {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    private SharedPreferences.Editor getEditor() {
        if (editor == null) {
            editor = sharedPreferences.edit();
        }
        return editor;
    }

    public int getWorkMode() {
        return sharedPreferences.getInt("method", -1);
    }

    public void putWorkMode(int checked) {
        getEditor().putInt("method", checked).apply();
    }

    public boolean isStableMode() {
        String stablemode = "stablemode";
        return sharedPreferences.getBoolean(stablemode, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public boolean getFalseBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    void putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void putString(String key, String value) {
        getEditor().putString(key, value).apply();
    }
}
