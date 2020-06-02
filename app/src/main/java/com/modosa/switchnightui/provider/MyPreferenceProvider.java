package com.modosa.switchnightui.provider;

import android.content.Context;
import android.content.SharedPreferences;

import com.crossbowffs.remotepreferences.RemotePreferenceProvider;
import com.crossbowffs.remotepreferences.RemotePreferences;
import com.modosa.switchnightui.util.Constants;

public class MyPreferenceProvider extends RemotePreferenceProvider {

    private static SharedPreferences spInstance;


    public MyPreferenceProvider() {
        super(Constants.AUTHORITY, new String[]{Constants.PREF_FILE});
    }

    public static SharedPreferences getRemoteSharedPreference(Context context) {
        if (spInstance == null) {
            synchronized (MyPreferenceProvider.class) {
                if (spInstance == null) {
                    spInstance = new RemotePreferences(context, Constants.AUTHORITY, Constants.PREF_FILE);
                }
            }
        }
        return spInstance;
    }
}
