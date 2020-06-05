package com.modosa.switchnightui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
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
import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SpUtil;


/**
 * @author dadaewq
 */
@SuppressWarnings("ConstantConditions")
public class XFeatureFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
    private Context context;
    private SpUtil spUtil;


    private Preference x_mobileqq_config;
    private Preference x_wechat_config;
    private PreferenceCategory x_experimental;
    private Preference x_custom_return1_config;
    private Preference x_custom_return0_config;

    private boolean isOpSuccess = false;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            check_xfeature.setSummary("v" + BuildConfig.VERSION_NAME + "（" + BuildConfig.VERSION_CODE + "）");
            if (hook2ReturnTrue()) {
                check_xfeature.setTitle(R.string.check_xfeature_ok);
            }
        }
        x_mobileqq_config = findPreference("x_mobileqq_config");
        x_wechat_config = findPreference("x_wechat_config");
        SwitchPreferenceCompat x_enable_experimental = findPreference("x_enable_experimental");
        x_experimental = findPreference("x_experimental");
        x_custom_return1_config = findPreference("x_custom_return1_config");
        x_custom_return0_config = findPreference("x_custom_return0_config");

        x_mobileqq_config.setOnPreferenceClickListener(this);
        x_wechat_config.setOnPreferenceClickListener(this);
        x_enable_experimental.setOnPreferenceChangeListener((preference, newValue) -> {
            x_experimental.setVisible((boolean) newValue);
            return true;
        });
        x_experimental.setVisible(x_enable_experimental.isChecked());
        x_custom_return1_config.setOnPreferenceClickListener(this);
        x_custom_return0_config.setOnPreferenceClickListener(this);

        findPreference("view_hook_config").setOnPreferenceClickListener(this);

        refresh();
    }


    private void refresh() {
        x_mobileqq_config.setSummary(spUtil.getString("x_mobileqq_config"));
        x_wechat_config.setSummary(spUtil.getString("x_wechat_config"));
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
            case "x_mobileqq_config":
            case "x_wechat_config":
                showDialogTencentHook(preferenceKey);
                break;
            case "x_custom_return1_config":
            case "x_custom_return0_config":
                showDialogCtomHook(preferenceKey);
                break;
            default:
        }

        return false;
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

}


