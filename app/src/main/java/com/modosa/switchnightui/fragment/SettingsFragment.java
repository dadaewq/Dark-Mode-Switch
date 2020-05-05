package com.modosa.switchnightui.fragment;

import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.activity.AboutActivity;
import com.modosa.switchnightui.activity.TimingSwitchActivity;
import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SwitchBatterySaverUtil;
import com.modosa.switchnightui.util.SwitchDisplayUtil;
import com.modosa.switchnightui.util.oneplus.OpSwitchForceDarkUtil;

/**
 * @author dadaewq
 */
@SuppressWarnings("ConstantConditions")

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
    public static final String SP_KEY_PERMANENT_NOTIFICATION = "permanent_notification";
    private final String[] displayKeys = new String[]{SwitchDisplayUtil.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED, SwitchDisplayUtil.GRAYSCALE, SwitchDisplayUtil.NIGHT_DISPLAY_ACTIVATED};
    private Context context;
    private SwitchPreferenceCompat stableMode;
    private SwitchPreferenceCompat carMode;
    private SwitchPreferenceCompat batterySaver;
    private SwitchPreferenceCompat permanentNotification;
    private SwitchPreferenceCompat[] switchPreferenceCompats;
    private UiModeManager uiModeManager;
    private AlertDialog alertDialog;
    private boolean enableStableMode = false;
    private SwitchBatterySaverUtil switchBatterySaverUtil;
    private SwitchDisplayUtil switchDisplayUtil;
    private Handler handler;


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
        batterySaver = findPreference("battery_saver");
        assert batterySaver != null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            switchBatterySaverUtil = new SwitchBatterySaverUtil(context);
            batterySaver.setOnPreferenceClickListener(v -> {
                boolean shouldOpen = batterySaver.isChecked();
                String message = switchBatterySaverUtil.setBatterySaver(shouldOpen);

                if (message != null) {
                    OpUtil.showToast1(context, message);
                } else {
                    handler = new Handler();
                    handler.postDelayed(() -> {
                        boolean newPowerMode = switchBatterySaverUtil.isPowerSaveMode();
                        batterySaver.setChecked(newPowerMode);

                        if (newPowerMode != shouldOpen) {
                            OpUtil.showToast0(context, R.string.tip_cannotSwitchBatterySaver);
                        }

                    }, 300);

                }

                return true;
            });
        } else {
            batterySaver.setVisible(false);
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
        permanentNotification = findPreference(SP_KEY_PERMANENT_NOTIFICATION);
        assert permanentNotification != null;
        permanentNotification.setOnPreferenceClickListener(v -> {
            if (permanentNotification.isChecked()) {
                OpUtil.addPermanentNotification(context);
            } else {
                OpUtil.cancelPermanentNotification(context);
            }
            return true;
        });

        switchDisplayUtil = new SwitchDisplayUtil(context);

//        0颜色反转,
//        1灰度,
//        2夜间模式, SDK25
        switchPreferenceCompats = new SwitchPreferenceCompat[displayKeys.length];

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initDisplay(0);
            initDisplay(1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                initDisplay(2);
            } else {
                findPreference(displayKeys[2]).setVisible(false);
            }
        } else {
            findPreference("system_display").setVisible(false);
        }

        findPreference("timeup").setOnPreferenceClickListener(this);
        findPreference("instructions_before_use").setOnPreferenceClickListener(this);
        findPreference("help").setOnPreferenceClickListener(this);
        findPreference("about").setOnPreferenceClickListener(this);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            findPreference("excludeFromRecents").setVisible(false);
        }
        //一加设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && OpSwitchForceDarkUtil.isOnePlus(context)) {
            findPreference("oneplus_settings").setVisible(true);
            SwitchPreferenceCompat oneplusSynchronizeTheme = findPreference("oneplus_synchronize_theme");
            SwitchPreferenceCompat oneplusUseColorfulTheme = findPreference("oneplus_use_colorful_theme");
            if (!oneplusSynchronizeTheme.isChecked()) {
                oneplusUseColorfulTheme.setEnabled(false);
            }
            oneplusSynchronizeTheme.setOnPreferenceChangeListener((preference, newValue) -> {
                oneplusUseColorfulTheme.setEnabled((boolean) newValue);
                return true;
            });

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

    private void initDisplay(int keyIndes) {
        switchPreferenceCompats[keyIndes] = findPreference(displayKeys[keyIndes]);

        switchPreferenceCompats[keyIndes].setOnPreferenceClickListener(v -> {
            boolean shouldOpen = switchPreferenceCompats[keyIndes].isChecked();
            String message = switchDisplayUtil.putSettingsSecureInt(displayKeys[keyIndes], shouldOpen);

            switchPreferenceCompats[keyIndes].setChecked(switchDisplayUtil.isDisplayKeyEnabled(displayKeys[keyIndes]));

            if (message != null) {
                OpUtil.showToast1(context, message);
            }
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            batterySaver.setChecked(switchBatterySaverUtil.isPowerSaveMode());
            switchPreferenceCompats[0].setChecked(switchDisplayUtil.isDisplayKeyEnabled(displayKeys[0]));
            switchPreferenceCompats[1].setChecked(switchDisplayUtil.isDisplayKeyEnabled(displayKeys[1]));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                switchPreferenceCompats[2].setChecked(switchDisplayUtil.isDisplayKeyEnabled(displayKeys[2]));
            }
        }
        carMode.setChecked(Configuration.UI_MODE_TYPE_CAR == uiModeManager.getCurrentModeType());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
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


