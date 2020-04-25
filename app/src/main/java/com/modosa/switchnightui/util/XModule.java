package com.modosa.switchnightui.util;


import androidx.annotation.Keep;

import com.modosa.switchnightui.BuildConfig;
import com.modosa.switchnightui.activity.MainActivity;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author dadaewq
 */
@Keep
@SuppressWarnings("WeakerAccess")
public class XModule implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {


        if ("com.coolapk.market".equals(loadPackageParam.packageName)) {
            try {
                XposedHelpers.findAndHookMethod("com.coolapk.market.AppSetting", loadPackageParam.classLoader,
                        "shouldDisableXposed",
                        XC_MethodReplacement.returnConstant(false)
                );

                XposedHelpers.findAndHookMethod("com.coolapk.market.util.XposedUtils", loadPackageParam.classLoader,
                        "disableXposed",
                        XC_MethodReplacement.returnConstant(true)
                );
            } catch (Exception e) {
                XposedBridge.log("" + e);
            }

            try {
                XposedHelpers.findAndHookMethod("com.coolapk.market.util.NightModeHelper", loadPackageParam.classLoader,
                        "isThisRomSupportSystemTheme",
                        XC_MethodReplacement.returnConstant(true)
                );
            } catch (Exception e) {
                XposedBridge.log("" + e);
            }
        }


        if (BuildConfig.APPLICATION_ID.equals(loadPackageParam.packageName)) {
            try {
                XposedHelpers.findAndHookMethod(MainActivity.class.getName(), loadPackageParam.classLoader,
                        "hookTitleReturnString",
                        XC_MethodReplacement.returnConstant("Xposed")
                );
            } catch (Exception e) {
                XposedBridge.log("" + e);
            }
        }


        //        if ("com.mihotel.shareto".equals(loadPackageParam.packageName)) {
//            try {
//                XposedHelpers.findAndHookMethod("com.mihotel.shareto.activity.MainActivity", loadPackageParam.classLoader,
//                        "hook2ReturnTrue",
//                        XC_MethodReplacement.returnConstant(true)
//                );
//
//            } catch (Exception e) {
//                XposedBridge.log("" + e);
//            }
//        }


//        if ("android".equals(loadPackageParam.packageName)) {

//        try {
//            XposedHelpers.findAndHookMethod("android.app.UiModeManager", loadPackageParam.classLoader, "isNightModeLocked",
//                    XC_MethodReplacement.returnConstant(false));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//            try {
//                XposedHelpers.findAndHookMethod("android.app.UiModeManager", loadPackageParam.classLoader, "getNightMode",
//                        XC_MethodReplacement.returnConstant(666));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


//        try {
//            XposedHelpers.findAndHookMethod("com.android.server.UiModeManagerService", loadPackageParam.classLoader, "isNightModeLocked",
//                    XC_MethodReplacement.returnConstant(true));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//            try {
//                XposedHelpers.findAndHookMethod("com.android.server.UiModeManagerService$6", loadPackageParam.classLoader, "isNightModeLocked",
//                        XC_MethodReplacement.returnConstant(false));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


//        useless
//        try {
//            XposedHelpers.findAndHookMethod(ContextWrapper.class.getName(), loadPackageParam.classLoader, "checkCallingOrSelfPermission",
//                    String.class,
//                    new XC_MethodHook() {
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            super.beforeHookedMethod(param);
//                            if("android.permission.MODIFY_DAY_NIGHT_MODE".equals(param.args[0])){
//                                param.setResult(PackageManager.PERMISSION_GRANTED);
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }
}