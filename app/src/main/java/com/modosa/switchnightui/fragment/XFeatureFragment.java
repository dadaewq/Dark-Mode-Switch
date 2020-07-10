package com.modosa.switchnightui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.modosa.switchnightui.BuildConfig;
import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.AppInfoUtil;
import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SpUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Executors;


/**
 * @author dadaewq
 */
@SuppressWarnings("ConstantConditions")
public class XFeatureFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
    private Context context;
    private SpUtil spUtil;


    private Preference x_mobileqq_config;
    private Preference x_wechat_config;
    private Preference x_iflytek_input_config;
    private Preference x_caij_see_config;
    private PreferenceCategory x_experimental;
    private Preference x_custom_return1_config;
    private Preference x_custom_return0_config;

    private boolean isOpSuccess = false;
    private AlertDialog alertDialog;

    private MyHandler mHandler;
    private int successNumber = 0;

    public static String readParse(String urlPath) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len;
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inStream = conn.getInputStream();
        while ((len = inStream.read(data)) != -1) {
            outStream.write(data, 0, len);
        }
        inStream.close();
        return new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new MyHandler(this);
        init();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_xfeature, rootKey);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void init() {
        spUtil = new SpUtil(context);

        Preference check_xfeature = findPreference("check_xfeature");
        if (check_xfeature != null) {
            check_xfeature.setSummary("v" + BuildConfig.VERSION_NAME + "（" + BuildConfig.VERSION_CODE + "）" + "\n(当前版本默认支持解除限制的有QQ_8.3.9、Play QQ_8.2.9、微信_7.0.16、讯飞输入法_9.1.9696、See_1.5.6.15，更多版本请按需设置自定义配置)");
            if (hook2ReturnTrue()) {
                check_xfeature.setTitle(R.string.check_xfeature_ok);
                check_xfeature.setIcon(R.drawable.ic_passed);
            }
        }
        x_mobileqq_config = findPreference("x_mobileqq_config");
        x_wechat_config = findPreference("x_wechat_config");
        x_iflytek_input_config = findPreference("x_iflytek_input_config");
        x_caij_see_config = findPreference("x_caij_see_config");
        SwitchPreferenceCompat x_enable_experimental = findPreference("x_enable_experimental");
        x_experimental = findPreference("x_experimental");
        x_custom_return1_config = findPreference("x_custom_return1_config");
        x_custom_return0_config = findPreference("x_custom_return0_config");

        x_mobileqq_config.setOnPreferenceClickListener(this);
        x_wechat_config.setOnPreferenceClickListener(this);
        x_iflytek_input_config.setOnPreferenceClickListener(this);
        x_caij_see_config.setOnPreferenceClickListener(this);
        x_enable_experimental.setOnPreferenceChangeListener((preference, newValue) -> {
            x_experimental.setVisible((boolean) newValue);
            return true;
        });
        x_experimental.setVisible(x_enable_experimental.isChecked());
        x_custom_return1_config.setOnPreferenceClickListener(this);
        x_custom_return0_config.setOnPreferenceClickListener(this);

        findPreference("view_hook_config").setOnPreferenceClickListener(this);
        findPreference("update_hook_config").setOnPreferenceClickListener(this);
        refresh();
    }

    private void refresh() {
        x_mobileqq_config.setSummary(spUtil.getString("x_mobileqq_config"));
        x_wechat_config.setSummary(spUtil.getString("x_wechat_config"));
        x_iflytek_input_config.setSummary(spUtil.getString("x_iflytek_input_config"));
        x_caij_see_config.setSummary(spUtil.getString("x_caij_see_config"));
        x_custom_return1_config.setSummary(spUtil.getString("x_custom_return1_config"));
        x_custom_return0_config.setSummary(spUtil.getString("x_custom_return0_config"));
    }

    @Keep
    private boolean hook2ReturnTrue() {
//        如果需要hook，不要注释下一行
        Log.i("hook2ReturnTrue", ": ");

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        String preferenceKey = preference.getKey();
        switch (preferenceKey) {
            case "view_hook_config":
                OpUtil.launchCustomTabsUrl(context, "https://dadaewq.gitee.io/tutorials/config/hook_config.html");
                break;
            case "update_hook_config":
                updateHookConfig();
                break;
            case "x_mobileqq_config":
            case "x_wechat_config":
                showDialogTencentHook(preferenceKey);
                break;
            case "x_iflytek_input_config":
                showDialogIflytekInputHook(preferenceKey);
                break;
            case "x_caij_see_config":
                showDialogCaijSeeHook(preferenceKey);
                break;
            case "x_custom_return1_config":
            case "x_custom_return0_config":
                showDialogCtomHook(preferenceKey);
                break;
            default:
        }

        return false;
    }

    private void updateHookConfig() {
        successNumber = 0;
        Executors.newSingleThreadExecutor().execute(() -> {
            Message msg = mHandler.obtainMessage();
            msg.arg1 = 9;
            try {
                String configs = readParse("https://gitee.com/dadaewq/Tutorials/raw/master/config/hook_config.json");
                Object Obconfigs = new JSONObject(configs).opt("custom_configs");
                Log.d("Obconfigs", ": " + Obconfigs.toString());
                if (Obconfigs == null) {
                    msg.arg1 = 6;
                    mHandler.sendMessage(msg);
                    return;
                }
                HashMap<String, String>[] hashMaps = new HashMap[4];

                for (int i = 0; i < hashMaps.length; i++) {
                    hashMaps[i] = new HashMap<>(2);
                }

                hashMaps[0].put("pref_key", "x_mobileqq");
                hashMaps[0].put("pkgName", "com.tencent.mobileqq");
                hashMaps[1].put("pref_key", "x_wechat");
                hashMaps[1].put("pkgName", "com.tencent.mm");
                hashMaps[2].put("pref_key", "x_iflytek_input");
                hashMaps[2].put("pkgName", "com.iflytek.inputmethod");
                hashMaps[3].put("pref_key", "x_caij_see");
                hashMaps[3].put("pkgName", "com.caij.see");

                for (int i = 0; i < 2; i++) {
                    String pref_key = hashMaps[i].get("pref_key");
                    if (spUtil.getBoolean(pref_key, true)) {
                        try {
                            String pkgName = hashMaps[i].get("pkgName");
                            String config = ((JSONObject) ((JSONObject) Obconfigs).get(pkgName)).getString(AppInfoUtil.getAppVersions(context, pkgName));
                            spUtil.putString(pref_key + "_config", config);
                            successNumber++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                for (int i = 2; i < 4; i++) {
                    Log.d("forupdateHookConfig", hashMaps[i].get("pkgName"));
                    String pref_key = hashMaps[i].get("pref_key");
                    if (spUtil.getBoolean(pref_key, true)) {
                        try {
                            String pkgName = hashMaps[i].get("pkgName");
                            String config = ((JSONObject) ((JSONObject) Obconfigs).get(pkgName)).getString(AppInfoUtil.getAppVersion(context, pkgName));
                            spUtil.putString(pref_key + "_config", config);
                            successNumber++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                msg.arg1 = 6;
            }

            mHandler.sendMessage(msg);
        });
    }

    private void showDialogTencentHook(String preferenceKey) {

        final EditText valueOfpreferenceKey = new EditText(context);
        if ("x_wechat_config".equals(preferenceKey)) {
            valueOfpreferenceKey.setHint("类名:方法名,方法名,...或类名;变量名");
        } else {
            valueOfpreferenceKey.setHint("类名:方法名,方法名,...");
        }
        valueOfpreferenceKey.setText(spUtil.getString(preferenceKey));
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("自定义HOOK")
                .setView(valueOfpreferenceKey)
                .setNeutralButton("关闭", null)
                .setNegativeButton("清空", null)
                .setPositiveButton("保存", null);
        if ("x_wechat_config".equals(preferenceKey)) {
            builder.setTitle("自定义HOOK-微信");
        } else {
            builder.setTitle("自定义HOOK-QQ");
        }
        alertDialog = builder.create();
        OpUtil.showAlertDialog(context, alertDialog);

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String value = valueOfpreferenceKey.getText().toString().replaceAll("\\s*", "").replace("：", ":").replace("，", ",");
            if ("x_wechat_config".equals(preferenceKey)) {
                value = value.replace("；", ";");
            }
            valueOfpreferenceKey.setText(value);
            opPreferenceValueFromDialog(preferenceKey, value, AlertDialog.BUTTON_POSITIVE);
        });
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> valueOfpreferenceKey.setText(null));
        alertDialog.setOnDismissListener(dialog -> {
            if (isOpSuccess) {
                isOpSuccess = false;
                refresh();
            }
        });
    }


    private void showDialogIflytekInputHook(String preferenceKey) {

        final EditText valueOfpreferenceKey = new EditText(context);
        valueOfpreferenceKey.setHint("类名");

        valueOfpreferenceKey.setText(spUtil.getString(preferenceKey));
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("自定义HOOK")
                .setView(valueOfpreferenceKey)
                .setNeutralButton("关闭", null)
                .setNegativeButton("清空", null)
                .setPositiveButton("保存", null);


        alertDialog = builder.create();
        OpUtil.showAlertDialog(context, alertDialog);

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String value = valueOfpreferenceKey.getText().toString().replaceAll("\\s*", "");

            valueOfpreferenceKey.setText(value);
            opPreferenceValueFromDialog(preferenceKey, value, AlertDialog.BUTTON_POSITIVE);
        });
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> valueOfpreferenceKey.setText(null));
        alertDialog.setOnDismissListener(dialog -> {
            if (isOpSuccess) {
                isOpSuccess = false;
                refresh();
            }
        });
    }

    private void showDialogCaijSeeHook(String preferenceKey) {

        final EditText valueOfpreferenceKey = new EditText(context);
        valueOfpreferenceKey.setHint("类名:方法名");

        valueOfpreferenceKey.setText(spUtil.getString(preferenceKey));
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("自定义HOOK")
                .setView(valueOfpreferenceKey)
                .setNeutralButton("关闭", null)
                .setNegativeButton("清空", null)
                .setPositiveButton("保存", null);


        alertDialog = builder.create();
        OpUtil.showAlertDialog(context, alertDialog);

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String value = valueOfpreferenceKey.getText().toString().replaceAll("\\s*", "").replace("：", ":");

            valueOfpreferenceKey.setText(value);
            opPreferenceValueFromDialog(preferenceKey, value, AlertDialog.BUTTON_POSITIVE);
        });
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> valueOfpreferenceKey.setText(null));
        alertDialog.setOnDismissListener(dialog -> {
            if (isOpSuccess) {
                isOpSuccess = false;
                refresh();
            }
        });
    }

    private void showDialogCtomHook(String preferenceKey) {

        final EditText valueOfpreferenceKey = new EditText(context);
        valueOfpreferenceKey.setHint("包名:类名:方法名,方法名,...;包名:类名:方法名,方法名,...");
        valueOfpreferenceKey.setText(spUtil.getString(preferenceKey));
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("自定义HOOK")
                .setView(valueOfpreferenceKey)
                .setNeutralButton("关闭", null)
                .setNegativeButton("清空", null)
                .setPositiveButton("保存", null);

        alertDialog = builder.create();
        OpUtil.showAlertDialog(context, alertDialog);

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String value = valueOfpreferenceKey.getText().toString().replaceAll("\\s*", "").replace("；", ";").replace("：", ":").replace("，", ",");
            valueOfpreferenceKey.setText(value);
            opPreferenceValueFromDialog(preferenceKey, value, AlertDialog.BUTTON_POSITIVE);
        });
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> valueOfpreferenceKey.setText(null));
        alertDialog.setOnDismissListener(dialog -> {
            if (isOpSuccess) {
                isOpSuccess = false;
                refresh();
            }
        });
    }

    private void opPreferenceValueFromDialog(String preferenceKey, String value, int whichButton) {

        isOpSuccess = false;

        try {
            if (whichButton == AlertDialog.BUTTON_POSITIVE) {
                spUtil.putString(preferenceKey, value);
                isOpSuccess = true;
            }
        } catch (Exception ignore) {
            isOpSuccess = false;
        }

    }

    private static class MyHandler extends Handler {

        private final WeakReference<XFeatureFragment> wrFragment;

        MyHandler(XFeatureFragment fragment) {
            this.wrFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (wrFragment.get() == null) {
                return;
            }
            XFeatureFragment xFeatureFragment = wrFragment.get();

            if (msg.arg1 == 9) {
                OpUtil.showToast0(xFeatureFragment.context, "成功更新" + xFeatureFragment.successNumber + "项自定义配置");
                xFeatureFragment.refresh();
            } else if (msg.arg1 == 6) {
                OpUtil.showToast0(xFeatureFragment.context, "更新失败");
            }
        }
    }
}


