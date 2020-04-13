package com.modosa.switchnightui.util;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XModule implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if ("com.coolapk.market".equals(loadPackageParam.packageName)) {
            try {
                XposedHelpers.findAndHookMethod("com.coolapk.market.AppSetting", loadPackageParam.classLoader, "shouldDisableXposed", XC_MethodReplacement.returnConstant(false));

//                XposedHelpers.findAndHookMethod("com.coolapk.market.util.XposedUtils", loadPackageParam.classLoader, "disableXposed", XC_MethodReplacement.returnConstant(true));
            } catch (Exception e) {
                XposedBridge.log("" + e);
            }

            try {
                XposedHelpers.findAndHookMethod("com.coolapk.market.util.NightModeHelper", loadPackageParam.classLoader, "isThisRomSupportSystemTheme", XC_MethodReplacement.returnConstant(true));

            } catch (Exception e) {
                XposedBridge.log("" + e);
            }
        }
    }
}