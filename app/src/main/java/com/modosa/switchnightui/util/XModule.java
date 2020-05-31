package com.modosa.switchnightui.util;


import androidx.annotation.Keep;

import com.modosa.switchnightui.BuildConfig;
import com.modosa.switchnightui.activity.MainActivity;

import java.lang.reflect.Method;

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

        if ("com.tencent.mobileqq".equals(loadPackageParam.packageName)) {
            //play 8.2.9_1353
            hookQQDarkMode(loadPackageParam.classLoader, "ayhx", "a");

            //non-play 8.3.6_1406
            hookQQDarkMode(loadPackageParam.classLoader, "bbom", "a");
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

    }

    private void hookQQDarkMode(ClassLoader classLoader, String className, String methodName) {
        try {
            Method[] methods = XposedHelpers.findMethodsByExactParameters(XposedHelpers.findClass(className, classLoader), boolean.class);
            for (Method method : methods) {
                if (methodName.equals(method.getName())) {
                    XposedBridge.hookMethod(
                            method,
                            XC_MethodReplacement.returnConstant(true)
                    );
                }
            }
        } catch (Exception e) {
            XposedBridge.log("" + e);
        }
    }
}
