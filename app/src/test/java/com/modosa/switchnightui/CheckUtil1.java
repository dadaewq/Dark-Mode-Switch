package com.modosa.switchnightui;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.Context;
import android.content.pm.PackageManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class CheckUtil1 {
    private final Context context;
    private final UiModeManager uiModeManager;

    // How to use
    //                new CheckUtil(getApplicationContext(), uiModeManager).check();

    public CheckUtil1(Context context, UiModeManager uiModeManager) {
        this.uiModeManager = uiModeManager;
        this.context = context;
    }

    public static void setNightMode() {
        try {
            @SuppressLint({"DiscouragedPrivateApi", "PrivateApi"})
            Class clazz = ClassLoader.getSystemClassLoader().loadClass(
                    "com.android.server.UiModeManagerService");
            Method misNightModeLocked = clazz.getDeclaredMethod("isNightModeLocked");
            misNightModeLocked.setAccessible(true);
//            return (boolean) misNightModeLocked.invoke(uiModeManager);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String isNightModeUnlocked() {
        try {
            @SuppressLint("DiscouragedPrivateApi")
            Method misNightModeLocked = uiModeManager.getClass().getDeclaredMethod("isNightModeLocked");
            misNightModeLocked.setAccessible(true);
            return "" + !(boolean) misNightModeLocked.invoke(uiModeManager);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Exception";
    }

    public String isNightModeLocked1() {
        try {
            @SuppressLint({"DiscouragedPrivateApi", "PrivateApi"})
            Class clazz = ClassLoader.getSystemClassLoader().loadClass(
                    "com.android.server.UiModeManagerService$9");
            Field field = uiModeManager.getClass().getField("mService");

            Method misNightModeLocked = clazz.getDeclaredMethod("isNightModeLocked");
            misNightModeLocked.setAccessible(true);
            return "" + !(boolean) misNightModeLocked.invoke(field);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Exception";
    }

    public String isNightModeLocked2() {
        try {
            @SuppressLint({"DiscouragedPrivateApi", "PrivateApi"})

            Field field = uiModeManager.getClass().getField("mService");

            @SuppressWarnings("JavaReflectionMemberAccess") Method misNightModeLocked = field.getClass().getDeclaredMethod("isNightModeLocked");
            misNightModeLocked.setAccessible(true);
            return "" + !(boolean) misNightModeLocked.invoke(field);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Exception";
    }

    public boolean hasPermission() {
        return context.checkCallingOrSelfPermission("android.permission.MODIFY_DAY_NIGHT_MODE") == PackageManager.PERMISSION_GRANTED;
    }

//    public boolean check() {
//        boolean r1 = isNightModeUnlocked();
//        boolean r2 = hasPermission();
//        Toast.makeText(context, isNightModeUnlocked() + " " + hasPermission(), Toast.LENGTH_SHORT).show();
//        Log.e("unlock+perm ", isNightModeUnlocked() + " " + hasPermission());
//        return r1 || r2;
//    }

}

//配置变更

//    <activity
//    android:name=".MyActivity"
//    android:configChanges="uiMode" />


//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        Log.e("new_uiMode", newConfig.uiMode + "");
//        Log.e("mask", Configuration.UI_MODE_NIGHT_MASK + "");
//        int currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
//        switch (currentNightMode) {
//            case Configuration.UI_MODE_NIGHT_NO:
//                Log.e("NO", Configuration.UI_MODE_NIGHT_NO + "");
//                break;
//            case Configuration.UI_MODE_NIGHT_YES:
//                Log.e("YES", Configuration.UI_MODE_NIGHT_YES + "");
//                break;
//        }
//    }


