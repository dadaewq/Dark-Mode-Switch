package com.modosa.switchnightui.util;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Keep;

import com.modosa.switchnightui.BuildConfig;
import com.modosa.switchnightui.activity.MainActivity;
import com.modosa.switchnightui.fragment.XFeatureFragment;
import com.modosa.switchnightui.provider.MyPreferenceProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.setStaticBooleanField;
import static de.robv.android.xposed.XposedHelpers.setStaticIntField;
import static de.robv.android.xposed.XposedHelpers.setStaticObjectField;

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
                initPreferencesWithCallHook(this::hookCoolapk);
                break;
            case Constants.PACKAGE_NAME_DINGTALK:
            case Constants.PACKAGE_NAME_DINGTALK_GLOBAL:
                initPreferencesWithCallHook(this::hookDingTalk);
                break;
            case Constants.PACKAGE_NAME_FLYTEK_INPUTMETHOD:
                initPreferencesWithCallHook(() -> hookCustomIflytekInput(() -> {
                    //non-play 9.1.9696
                    hookIflytekInput("app.apl");
                }));
                break;
            case Constants.PACKAGE_NAME_CAIJ_SEE:
                initPreferencesWithCallHook(() -> hookCustomCaijSee(() -> {
                    //1.5.6.15
                    hookCaijSee("com.caij.see.o0Oo0Oo0", "Wwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
                }));
                break;
//            case Constants.PACKAGE_NAME_GBOARD:
//                initPreferencesWithCallHook(() -> hookCustomBoard(() -> {
//                    //1.5.6.6
//                    hookBoard("dur", "d");
//                }));
//                break;
            case Constants.PACKAGE_NAME_MOBILEQQ:
                initPreferencesWithCallHook(() -> hookCustomTencent("x_mobileqq", () -> {
                    //non-play 8.3.9_1424
                    hookReturnBooleanWithmethodName(loadPackageParam.classLoader, "bdbg", "a", true);

                    //play 8.2.9_1353
                    hookReturnBooleanWithmethodName(loadPackageParam.classLoader, "ayhx", "a", true);
                }));
                break;
            case Constants.PACKAGE_NAME_WECHAT:
                initPreferencesWithCallHook(() -> hookCustomTencent("x_wechat", () -> {
                    //non-play 7.0.16_1700

//                    new config
//                    hookTencentBrandApi("com.tencent.mm.ui.ai", "FSW");
//
//                    old config
                    hookReturnBooleanWithmethodNames(loadPackageParam.classLoader, "com.tencent.mm.ui.aj", new String[]{"fhI", "fhJ", "fhL"}, true);


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
        initPreferencesWithCallHook(() -> {
            hookCustom("x_custom_return1", true);
            hookCustom("x_custom_return0", false);
        });
    }


    private void initPreferencesWithCallHook(CallHook callHook) {

        try {

            findAndHookMethod(Application.class, "attach",
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

//        findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
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

        boolean useHook = true;

        if (sharedPreferences != null && !sharedPreferences.getBoolean("x_coolapk", true)) {
            useHook = false;
        }

        if (useHook) {
            try {
                findAndHookMethod("com.coolapk.market.AppSetting", loadPackageParam.classLoader,
                        "shouldDisableXposed",
                        XC_MethodReplacement.returnConstant(false)
                );

                findAndHookMethod("com.coolapk.market.util.XposedUtils", loadPackageParam.classLoader,
                        "disableXposed",
                        XC_MethodReplacement.returnConstant(true)
                );
            } catch (Exception e) {
                XposedBridge.log("" + e);
            }

            try {
                findAndHookMethod("com.coolapk.market.util.NightModeHelper", loadPackageParam.classLoader,
                        "isThisRomSupportSystemTheme",
                        XC_MethodReplacement.returnConstant(true)
                );
            } catch (Exception e) {
                XposedBridge.log("hookCoolapk e：" + e);
            }
        }
    }

    private void hookDingTalk() {

        boolean useHook = true;

        if (sharedPreferences != null && !sharedPreferences.getBoolean("x_dingtalk", true)) {
            useHook = false;
        }

        if (useHook) {
            try {
                findAndHookMethod("com.alibaba.android.dingtalkui.dark.ThemeHelper", loadPackageParam.classLoader,
                        "d",
                        XC_MethodReplacement.returnConstant(true)
                );

            } catch (Exception e) {
                XposedBridge.log("hookDingTalk e：" + e);
            }
        }
    }


    private void hookCustomTencent(String key, CallHook callHook) {
        boolean useDefault = true;

        String x_tencent_config;
        if (sharedPreferences != null) {
            //不解除限制
            if (!sharedPreferences.getBoolean(key, true)) {
                return;
            } else {
                //获取自定义
                x_tencent_config = sharedPreferences.getString(key + "_config", "");
                Log.e("x_tencent_config", key + "_config——" + x_tencent_config);

                try {
                    if (!"".equals(x_tencent_config)) {
                        x_tencent_config = x_tencent_config.replaceAll("\\s*", "").replace("：", ":").replace("，", ",");
                        int length = x_tencent_config.length();
                        if ("x_wechat".equals(key)) {
                            x_tencent_config = x_tencent_config.replace("；", ";");
                            if (x_tencent_config.endsWith(";")) {
                                x_tencent_config = x_tencent_config.substring(0, length - 1);
                            }
                            if (x_tencent_config.contains(";")) {
                                String[] brandConfig = x_tencent_config.split(";");
                                if (brandConfig.length >= 2) {
                                    hookTencentBrandApi(brandConfig[0], brandConfig[1]);
                                }
                                return;
                            }
                        }


                        if (x_tencent_config.endsWith(",")) {
                            x_tencent_config = x_tencent_config.substring(0, length - 1);
                        }
                        if (length > 2) {
                            String[] splits = x_tencent_config.split(":");
                            if (splits.length >= 2) {
                                String className = splits[0];
                                String[] methodNames = splits[1].split(",");
                                //只有当自定义的配置没有明显错误的时候才不不适用默认的Hook
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

    private void hookTencentBrandApi(String clazzName, String brandVariable) {
        try {
            String brands = Build.BRAND.toLowerCase() + "&8, other&8";
            Log.e("brand_api", brands);
            setStaticObjectField(
                    XposedHelpers.findClass(clazzName, loadPackageParam.classLoader),
                    brandVariable,
                    brands
            );
        } catch (Exception e) {
            Log.e("Exception", "hook brand_api : ");
        }
    }


    private void hookCustomIflytekInput(CallHook callHook) {
        boolean useDefault = true;

        String x_iflytek_input_config;
        if (sharedPreferences != null) {
            String key = "x_iflytek_input";
            //不解除限制
            if (!sharedPreferences.getBoolean(key, true)) {
                return;
            } else {
                //获取自定义
                x_iflytek_input_config = sharedPreferences.getString(key + "_config", "");
                Log.e("x_iflytek_input_config", key + "_config——" + x_iflytek_input_config);

                try {
                    if (!"".equals(x_iflytek_input_config)) {
                        x_iflytek_input_config = x_iflytek_input_config.replaceAll("\\s*", "");

                        if (x_iflytek_input_config.length() > 2) {
                            useDefault = false;
                            hookIflytekInput(x_iflytek_input_config);
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

    private void hookIflytekInput(String clazzName) {

        try {
            findAndHookMethod("com.iflytek.inputmethod.depend.config.settings.Settings", loadPackageParam.classLoader,
                    "isDarkModeAdaptOpen",
                    XC_MethodReplacement.returnConstant(true)
            );

        } catch (Exception e) {
            XposedBridge.log("" + e);
        }

        String[] staticObjectFields = new String[]{"a", "b", "c", "d", "e", "f"};
        int a, b, c;
        Field d;

        try {
            setStaticBooleanField(
                    XposedHelpers.findClass(clazzName, loadPackageParam.classLoader),
                    staticObjectFields[4],
                    true
            );
        } catch (Exception e) {
            XposedBridge.log("hookIflytekInput e：" + e);
        }
        try {
            setStaticObjectField(
                    XposedHelpers.findClass(clazzName, loadPackageParam.classLoader),
                    staticObjectFields[5],
                    true
            );
        } catch (Exception e) {
            XposedBridge.log("hookIflytekInput e：" + e);
        }

        try {
            Field declaredField = Configuration.class.getDeclaredField("UI_MODE_NIGHT_YES");
            declaredField.setAccessible(true);
            a = declaredField.getInt(null);
            Field declaredField2 = Configuration.class.getDeclaredField("UI_MODE_NIGHT_NO");
            declaredField2.setAccessible(true);
            b = declaredField2.getInt(null);
            Field declaredField3 = Configuration.class.getDeclaredField("UI_MODE_NIGHT_MASK");
            declaredField3.setAccessible(true);
            c = declaredField3.getInt(null);
            d = Configuration.class.getDeclaredField("uiMode");
            d.setAccessible(true);

            setStaticIntField(
                    XposedHelpers.findClass(clazzName, loadPackageParam.classLoader),
                    staticObjectFields[0],
                    a
            );
            setStaticIntField(
                    XposedHelpers.findClass(clazzName, loadPackageParam.classLoader),
                    staticObjectFields[1],
                    b
            );
            setStaticIntField(
                    XposedHelpers.findClass(clazzName, loadPackageParam.classLoader),
                    staticObjectFields[2],
                    c
            );

            setStaticObjectField(
                    XposedHelpers.findClass(clazzName, loadPackageParam.classLoader),
                    staticObjectFields[3],
                    d
            );
        } catch (Exception e) {
            XposedBridge.log("hookIflytekInput e：" + e);
        }

    }


    private void hookCustomCaijSee(CallHook callHook) {
        boolean useDefault = true;

        String x_caij_see_config;
        if (sharedPreferences != null) {
            String key = "x_caij_see";
            //不解除限制
            if (!sharedPreferences.getBoolean(key, true)) {
                return;
            } else {
                //获取自定义
                x_caij_see_config = sharedPreferences.getString(key + "_config", "");
                Log.e("x_caij_see_config", key + "_config——" + x_caij_see_config);

                try {
                    if (!"".equals(x_caij_see_config)) {
                        x_caij_see_config = x_caij_see_config.replaceAll("\\s*", "").replace("：", ":");

                        if (x_caij_see_config.length() > 15) {
                            String[] x_config1 = x_caij_see_config.split(":");
                            String className, methodName;
                            if (x_config1.length >= 2) {
                                className = x_config1[0];
                                methodName = x_config1[1];
                                useDefault = false;
                                hookCaijSee(className, methodName);
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

    private void hookCaijSee(String className, String methodName) {

        try {
            findAndHookMethod(className, loadPackageParam.classLoader, methodName,
                    Context.class,
                    XC_MethodReplacement.returnConstant(true)
            );
        } catch (Exception e) {
            XposedBridge.log("" + e);
        }
    }

    private void hookCustomBoard(CallHook callHook) {
        boolean useDefault = true;

        String x_gboard_config;
        if (sharedPreferences != null) {
            String key = "x_gboard";
            //不解除限制
            if (!sharedPreferences.getBoolean(key, true)) {
                return;
            } else {
                //获取自定义
                x_gboard_config = sharedPreferences.getString(key + "_config", "");
                Log.e("x_gboard_config", key + "_config——" + x_gboard_config);

                try {
                    if (!"".equals(x_gboard_config)) {
                        x_gboard_config = x_gboard_config.replaceAll("\\s*", "").replace("：", ":");

                        if (x_gboard_config.length() > 15) {
                            String[] x_config1 = x_gboard_config.split(":");
                            String className, methodName;
                            if (x_config1.length >= 2) {
                                className = x_config1[0];
                                methodName = x_config1[1];
                                useDefault = false;
                                hookCaijSee(className, methodName);
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

    private void hookBoard(String className, String methodName) {

        try {
            Log.e("isdark", ":" + (context != null) + " | " + context);
            boolean isdark = (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
            Log.e("isdark config", ":" + (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK));
            Log.e("isdark", ":" + isdark);
            findAndHookMethod(className, loadPackageParam.classLoader, methodName,
                    XC_MethodReplacement.returnConstant(isdark)
            );
        } catch (Exception e) {
            XposedBridge.log("" + e);
        }
    }

    private void hookMyself() {
        try {
//            findAndHookMethod(MainActivity.class.getName(), loadPackageParam.classLoader,
//                    "hookTitleReturnString",
//                    XC_MethodReplacement.returnConstant("Xposed")
//            );
            findAndHookMethod(MainActivity.class.getName(), loadPackageParam.classLoader,
                    "hook2ReturnTrue",
                    XC_MethodReplacement.returnConstant(true)
            );
            findAndHookMethod(XFeatureFragment.class.getName(), loadPackageParam.classLoader,
                    "hook2ReturnTrue",
                    XC_MethodReplacement.returnConstant(true)
            );
        } catch (Exception e) {
            XposedBridge.log("" + e);
        }
    }

    private void hookCustom(String key, boolean defaultValue) {

        String x_custom_configs;
        String packageName;
        String className;
        String[] methodNames;

        if (sharedPreferences != null && sharedPreferences.getBoolean("x_enable_experimental", false) && sharedPreferences.getBoolean(key, false)) {
            //获取自定义
            x_custom_configs = sharedPreferences.getString(key + "_config", "");
            Log.e("x_custom_configs", key + "_config——" + x_custom_configs);
            if (!"".equals(x_custom_configs)) {
                x_custom_configs = x_custom_configs.replaceAll("\\s*", "").replace("；", ";").replace("：", ":").replace("，", ",");
                for (String x_config : x_custom_configs.split(";")) {
                    if (x_config.length() >= 7) {
                        try {
                            String[] x_config1 = x_config.split(":");
                            if (x_config1.length >= 3) {
                                packageName = x_config1[0];
                                if (packageName.equals(loadPackageParam.packageName)) {
                                    className = x_config1[1];
                                    methodNames = x_config1[2].split(",");
                                    hookReturnBooleanWithmethodNames(loadPackageParam.classLoader, className, methodNames, defaultValue);
                                }

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

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
            findAndHookMethod(className, classLoader,
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
