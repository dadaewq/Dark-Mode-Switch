package com.modosa.switchnightui.util.oneplus;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;

class OpTheme {

    private final Context mContext;

    OpTheme(Context context) {
        this.mContext = context;
    }

    void enableTheme(HashMap<String, String> categoryMap) {
        if (true) {
            return;
        }
        try {
            Intent intent = new Intent("android.settings.oneplus_theme_enable");
            intent.putExtra("category_map", categoryMap);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.mContext.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("OpTheme", "Exception " + e);
        }
    }

    void disableTheme(HashMap<String, String> categoryMap) {
        if (true) {
            return;
        }
        try {
            Intent intent = new Intent("android.settings.oneplus_theme_disable");
            intent.putExtra("category_map", categoryMap);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.mContext.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("OpTheme", "Exception " + e);
        }
    }
}
