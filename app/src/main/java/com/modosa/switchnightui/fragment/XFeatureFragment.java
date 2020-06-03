package com.modosa.switchnightui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

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
        x_mobileqq_config = findPreference("x_mobileqq_config");
        x_wechat_config = findPreference("x_wechat_config");

        x_mobileqq_config.setOnPreferenceClickListener(this);
        x_wechat_config.setOnPreferenceClickListener(this);
        findPreference("view_hook_config").setOnPreferenceClickListener(this);

        refresh();
    }


    private void refresh() {
        x_mobileqq_config.setSummary(spUtil.getString("x_mobileqq_config"));
        x_wechat_config.setSummary(spUtil.getString("x_wechat_config"));
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
            case "x_mobileqq_config":
            case "x_wechat_config":
                showDialogCustomHook(preferenceKey);
                break;
            case "view_hook_config":
                OpUtil.launchCustomTabsUrl(context, "https://dadaewq.gitee.io/tutorials/config/hook_config.html");
                break;
            default:
        }

        return false;
    }

    private void showDialogCustomHook(String preferenceKey) {

        final EditText valueOfpreferenceKey = new EditText(context);
        valueOfpreferenceKey.setHint("类名:方法名,方法名,方法名");
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
            String value = valueOfpreferenceKey.getText().toString().replaceAll(" ", "").replaceAll("：", ":").replaceAll("，", ",");
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


