package com.modosa.switchnightui.fragment;

import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.activity.AboutActivity;
import com.modosa.switchnightui.activity.TimingSwitchActivity;
import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SwitchBatterySaverUtil;

/**
 * @author dadaewq
 */
@SuppressWarnings("ConstantConditions")

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
    public static final String SP_KEY_PERMANENT_NOTIFICATION = "permanentNotification";
    private Context context;
    private SwitchPreferenceCompat stableMode;
    private SwitchPreferenceCompat carMode;
    private SwitchPreferenceCompat battery_saver;
    private SwitchPreferenceCompat permanent_notification;
    private UiModeManager uiModeManager;
    private AlertDialog alertDialog;
    private boolean enableStableMode = false;
    private SwitchBatterySaverUtil switchBatterySaverUtil;


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
        //省电模式
        battery_saver = findPreference("battery_saver");
        assert battery_saver != null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            switchBatterySaverUtil = new SwitchBatterySaverUtil(context);
            battery_saver.setOnPreferenceClickListener(v -> {
                boolean shouldOpen = battery_saver.isChecked();
                String message = switchBatterySaverUtil.setBatterySaver(shouldOpen);

                boolean newPowerMode = switchBatterySaverUtil.isPowerSaveMode();
                battery_saver.setChecked(newPowerMode);
                if (message == null) {
                    if (newPowerMode != shouldOpen) {
                        OpUtil.showToast0(context, R.string.tip_cannotSwitchBatterySaver);
                    }
                } else {
                    OpUtil.showToast1(context, message);
                }

                return true;
            });
        } else {
            battery_saver.setVisible(false);
        }

        //驾驶模式
        carMode = findPreference("carmode");
        assert carMode != null;
        carMode.setOnPreferenceClickListener(v -> {
            if (carMode.isChecked()) {
                uiModeManager.enableCarMode(2);
            } else {
                uiModeManager.disableCarMode(0);
            }
            return true;
        });

        //稳定切换深色模式
        stableMode = findPreference("stablemode");
        assert stableMode != null;
        stableMode.setOnPreferenceClickListener(v -> {
            if (stableMode.isChecked()) {
                showDialogEnableStableMode();
            }
            return true;
        });

        //常驻通知
        permanent_notification = findPreference("permanent_notification");
        assert permanent_notification != null;
        permanent_notification.setOnPreferenceClickListener(v -> {
            if (permanent_notification.isChecked()) {
                OpUtil.addPermanentNotification(context);
            } else {
                OpUtil.cancelPermanentNotification(context);
            }
            return true;
        });

        findPreference("timeup").setOnPreferenceClickListener(this);
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
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            battery_saver.setChecked(switchBatterySaverUtil.isPowerSaveMode());
        }
        carMode.setChecked(Configuration.UI_MODE_TYPE_CAR == uiModeManager.getCurrentModeType());
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


