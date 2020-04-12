package com.modosa.switchnightui.fragment;

import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.activity.AboutActivity;
import com.modosa.switchnightui.activity.TimingSwitchActivity;
import com.modosa.switchnightui.util.OpUtil;

/**
 * @author dadaewq
 */
public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
    private Context context;
    private SwitchPreferenceCompat stableMode;
    private UiModeManager uiModeManager;
    private AlertDialog alertDialog;
    private boolean enableStableMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_settings, rootKey);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void init() {
        uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        initView();

    }

    private void initView() {
        stableMode = findPreference("stablemode");
        assert stableMode != null;
        stableMode.setOnPreferenceClickListener(v -> {
            if (stableMode.isChecked()) {
                showDialogEnableStableMode();
            }
            return true;
        });


        findPreference("timeup").setOnPreferenceClickListener(this);
        findPreference("switchcarmode").setOnPreferenceClickListener(this);
        findPreference("instructions_before_use").setOnPreferenceClickListener(this);
        findPreference("help").setOnPreferenceClickListener(this);
        findPreference("about").setOnPreferenceClickListener(this);


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            findPreference("excludeFromRecents").setVisible(false);
        }

    }

    private void showDialogEnableStableMode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.title_dialog_EnableStableMode)
                .setMessage(R.string.message_dialog_EnableStableMode)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> enableStableMode = true);

        alertDialog = builder.create();
        alertDialog.setOnDismissListener(dialog -> {
            stableMode.setChecked(enableStableMode);
            enableStableMode = false;
        });
        OpUtil.showAlertDialog(context, alertDialog);
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


        switch (preference.getKey()) {
            case "switchcarmode":
                if (uiModeManager != null) {
                    OpUtil.showToast0(context, R.string.switchcarmode);
                    if (Configuration.UI_MODE_TYPE_CAR == uiModeManager.getCurrentModeType()) {
                        uiModeManager.disableCarMode(0);
                    } else {
                        uiModeManager.enableCarMode(2);
                    }
                }
                break;
            case "timeup":
                OpUtil.startMyClass(context, TimingSwitchActivity.class);
                break;
            case "instructions_before_use":
                alertDialog = OpUtil.createDialogConfirmPrompt(context);
                OpUtil.showDialogConfirmPrompt(context, alertDialog);
                break;
            case "help":
                OpUtil.launchCustomTabsUrl(context, "https://dadaewq.gitee.io/tutorials/switchnightui.html");
                break;
            case "about":
                OpUtil.startMyClass(context, AboutActivity.class);
                break;
            default:
        }

        return false;
    }
}


