package com.modosa.switchnightui.util;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Keep;

import com.modosa.switchnightui.BuildConfig;
import com.modosa.switchnightui.activity.MainActivity;
import com.modosa.switchnightui.provider.MyPreferenceProvider;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
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

    Context context;
    SharedPreferences sharedPreferences;
    XC_LoadPackage.LoadPackageParam loadPackageParam;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        this.loadPackageParam = loadPackageParam;

        String loadPackageName = loadPackageParam.packageName;

        switch (loadPackageName) {
            case Constants.PACKAGE_NAME_COOLAPK:
                hookCoolapk();
                break;
            case Constants.PACKAGE_NAME_MOBILEQQ:
                initPreferencesWithCallHook(() -> hookCustom("x_mobileqq", () -> {
                    //non-play 8.3.6_1406
                    hookReturnBooleanWithmethodName(loadPackageParam.classLoader, "bbom", "a", true);

                    //play 8.2.9_1353
                    hookReturnBooleanWithmethodName(loadPackageParam.classLoader, "ayhx", "a", true);
                }));
                break;
            case Constants.PACKAGE_NAME_WECHAT:
                initPreferencesWithCallHook(() -> hookCustom("x_wechat", () -> {
                    //non-play 7.0.15_1680
                    hookReturnBooleanWithmethodNames(loadPackageParam.classLoader, "com.tencent.mm.ui.ai", new String[]{"eRY", "eRZ", "eSb"}, true);


//                if (!eRV() || eRX() || ((!eSb() && !eRW()) || !eRY())) {return false}
//            hookWeChatDarkMode(loadPackageParam.classLoader, "com.tencent.mm.ui.ai", "eRU",true);

//            clicfg_dark_mode_on
//            hookWeChatDarkMode(loadPackageParam.classLoader, "com.tencent.mm.ui.ai", "eRV",true);
//
//            clicfg_dark_mode_unused_on
//            hookWeChatDarkMode(loadPackageParam.classLoader, "com.tencent.mm.ui.ai", "eRW",true);

//            clicfg_dark_mode_disable_device
//            hookWeChatDarkMode(loadPackageParam.classLoader, "com.tencent.mm.ui.ai", "eRX",false);

//            clicfg_dark_mode_brand_api
//                    hookWeChatDarkMode(loadPackageParam.classLoader, "com.tencent.mm.ui.ai", "eRY", true);
//
//            dark_mode_follow_system
//                    hookWeChatDarkMode(loadPackageParam.classLoader, "com.tencent.mm.ui.ai", "eRZ", true);
//
//            dark_mode_follow_system
//            hookWeChatDarkMode(loadPackageParam.classLoader, "com.tencent.mm.ui.ai", "eSa",true);
//
//            dark_mode_used
//                    hookWeChatDarkMode(loadPackageParam.classLoader, "com.tencent.mm.ui.ai", "eSb", true);

                    //play 7.0.13_1621

//edxposed 使用以下hook会导致微信卡死
//                    hookReturnBooleanWithmethodNames(loadPackageParam.classLoader,"com.tencent.mm.ui.ag",new String[]{"eJp","eJq","eJr"},true);


                }));
                break;
            case BuildConfig.APPLICATION_ID:
                hookMyself();
                break;
            default:
        }
    }


    private void initPreferencesWithCallHook(CallHook callHook) {

        try {

            XposedHelpers.findAndHookMethod(Application.class, "attach",
                    Context.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            context = (Context) param.args[0];
                            sharedPreferences = MyPreferenceProvider.getRemoteSharedPreference(context);

                            callHook.call();
                        }
                    });
        } catch (Exception e) {
            Log.e("Exception", "initPreferencesWithCallHook : ");
            XposedBridge.log("" + e);
        }

//        XposedHelpers.findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
//            //
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) {
//
//                final Activity activity = (Activity) param.thisObject;
//                context = activity;
//                sharedPreferences = MyPreferenceProvider.getRemoteSharedPreference(context);
//
//                callHook.call();
//            }
//        });
    }


    private void hookCoolapk() {
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


    private void hookCustom(String key, CallHook callHook) {
        boolean useDefault = true;

        String x_configs;
        if (sharedPreferences != null) {
            //不解除限制
            if (!sharedPreferences.getBoolean(key, true)) {
                return;
            } else {
                //获取自定义
                x_configs = sharedPreferences.getString(key + "_config", "");
                Log.e("x_configs", key + "_config——" + x_configs);

                try {
                    if (!"".equals(x_configs)) {
                        x_configs = x_configs.replaceAll("\\s*", "").replace("：", ":").replace("，", ",");
                        int length = x_configs.length();

                        if (x_configs.endsWith(",")) {
                            x_configs = x_configs.substring(0, length - 1);
                        }
                        if (length > 2) {
                            String[] splits = x_configs.split(":");
                            if (splits.length >= 2) {
                                String className = splits[0];
                                String[] methodNames = splits[1].split(",");
                                useDefault = false;
                                hookReturnBooleanWithmethodNames(loadPackageParam.classLoader, className, methodNames, true);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        Log.e("useDefault", ": " + useDefault);
        if (useDefault) {
            callHook.call();
        }
    }


    private void hookMyself() {
        try {
//            XposedHelpers.findAndHookMethod(MainActivity.class.getName(), loadPackageParam.classLoader,
//                    "hookTitleReturnString",
//                    XC_MethodReplacement.returnConstant("Xposed")
//            );
            XposedHelpers.findAndHookMethod(MainActivity.class.getName(), loadPackageParam.classLoader,
                    "hook2ReturnTrue",
                    XC_MethodReplacement.returnConstant(true)
            );
        } catch (Exception e) {
            XposedBridge.log("" + e);
        }
    }


    private void hookReturnBooleanWithmethodNames(ClassLoader classLoader, String className, String[] methodNames, boolean booleanVlaue) {

        Method[] methods = XposedHelpers.findMethodsByExactParameters(XposedHelpers.findClass(className, classLoader), boolean.class);

        for (String methodName : methodNames) {
            try {
                for (Method method : methods) {
                    if (methodName.equals(method.getName())) {
                        XposedBridge.hookMethod(
                                method,
                                XC_MethodReplacement.returnConstant(booleanVlaue)
                        );
                    }
                }
            } catch (Exception e) {
                XposedBridge.log("" + e);
            }
        }
    }

    private void hookReturnBooleanWithmethodName(ClassLoader classLoader, String className, String methodName, boolean booleanVlaue) {
        hookReturnBooleanWithmethodNames(classLoader, className, new String[]{methodName}, booleanVlaue);
    }


    private void findAndHookMethodReturnBoolean(ClassLoader classLoader, String className, String methodName, boolean booleanVlaue) {
        try {
            XposedHelpers.findAndHookMethod(className, classLoader,
                    methodName,
                    XC_MethodReplacement.returnConstant(booleanVlaue)
            );

        } catch (Exception e) {
            XposedBridge.log("" + e);
        }
    }

    interface CallHook {
        /**
         * CallHook 接口
         */
        void call();
    }
}
