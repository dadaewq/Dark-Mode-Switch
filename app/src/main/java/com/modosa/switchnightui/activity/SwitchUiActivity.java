package com.modosa.switchnightui.activity;


import android.app.Activity;
import android.app.UiModeManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.uitl.SpUtil;
import com.modosa.switchnightui.uitl.SwitchUtil;
import com.modosa.switchnightui.uitl.WriteSettingsUtil;

import java.util.Objects;

/**
 * @author dadaewq
 */
public class SwitchUiActivity extends Activity {

    private SpUtil spUtil;
    private SwitchUtil switchUtil;
    private int want = -1;
    private UiModeManager uiModeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        spUtil = new SpUtil(this);
        switchUtil = new SwitchUtil(this, uiModeManager);

        if (Intent.ACTION_CREATE_SHORTCUT.equals(getIntent().getAction())) {
            createShortCut();
        } else {
            want = WriteSettingsUtil.YES;
            if (Objects.requireNonNull(uiModeManager).getNightMode() == WriteSettingsUtil.YES) {
                want = WriteSettingsUtil.NO;
            }
            switchUi(want);
            finish();
        }
    }

    private void createShortCut() {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {

            Intent intent = new Intent(new Intent(Intent.ACTION_VIEW))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setClass(this, getClass());

            ShortcutInfoCompat shortcut = new ShortcutInfoCompat.Builder(this, "SwitchUi")
                    .setLongLabel(getString(R.string.app_name))
                    .setShortLabel(getString(R.string.action_switch))
                    .setIcon(IconCompat.createWithResource(this, R.drawable.ic_brightness_2_black_24dp))
                    .setIntent(intent)
                    .build();

            Intent pinnedShortcutCallbackIntent = ShortcutManagerCompat.createShortcutResultIntent(this, shortcut);
            setResult(RESULT_OK, pinnedShortcutCallbackIntent);
            finish();
        }
    }


    private void switchUi(int want) {
        if (want != WriteSettingsUtil.NO) {
            want = WriteSettingsUtil.YES;
        }
        String msg = "";

        int method = spUtil.getMethod();
        if (method == 2) {
            if (WriteSettingsUtil.isNightMode(this)) {
                switchUtil.switch2(WriteSettingsUtil.NO);
            } else {
                switchUtil.switch2(WriteSettingsUtil.YES);
            }
            return;
        } else if (method == 3) {
            if (switchUtil.switch3(want)) {
                msg = getString(R.string.no_root) + "\n";
            }
        } else {

            if (switchUtil.switch1(want)) {
                msg = String.format(getString(R.string.failmethod), "1") + "\n";
            }
        }
        if (uiModeManager.getNightMode() == WriteSettingsUtil.YES) {
            msg += getString(R.string.DarkModeOn);
        } else {
            msg += getString(R.string.DarkModeOff);
        }
        switchUtil.showToast0(msg);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newconfig) {
        super.onConfigurationChanged(newconfig);

        if (spUtil.isStableMode()) {
            switchUi(want);
        } else {
            finish();
        }
    }
}
