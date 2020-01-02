package com.modosa.switchnightui;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;

class CheckUtil {
    private final Context context;
    private final UiModeManager uiModeManager;

    // How to use
    //                new CheckUtil(getApplicationContext(), uiModeManager).check();

    public CheckUtil(Context context, UiModeManager uiModeManager) {
        this.uiModeManager = uiModeManager;
        this.context = context;
    }

    private boolean isNightModeUnlocked() {
        try {
            @SuppressLint("DiscouragedPrivateApi")
            Method misNightModeLocked = uiModeManager.getClass().getDeclaredMethod("isNightModeLocked");
            misNightModeLocked.setAccessible(true);
            return !(boolean) misNightModeLocked.invoke(uiModeManager);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean canModify() {
        return context.checkCallingOrSelfPermission("android.permission.MODIFY_DAY_NIGHT_MODE") == PackageManager.PERMISSION_GRANTED;
    }

    public boolean check() {
        boolean r1 = isNightModeUnlocked();
        boolean r2 = canModify();
        Toast.makeText(context, isNightModeUnlocked() + " " + canModify(), Toast.LENGTH_SHORT).show();
        Log.e("unlock+perm ", isNightModeUnlocked() + " " + canModify());
        return r1 || r2;
    }
}
