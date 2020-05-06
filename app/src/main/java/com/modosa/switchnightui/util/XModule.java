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

    }
}
