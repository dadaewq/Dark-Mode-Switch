package com.modosa.switchnightui.activity;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.base.BaseActivity;
import com.modosa.switchnightui.util.SpUtil;
import com.modosa.switchnightui.util.SwitchDarkModeUtil;
import com.modosa.switchnightui.util.WriteSettingsUtil;

import java.util.Objects;

/**
 * @author dadaewq
 */
public class SwitchDarkModeActivity extends BaseActivity {

    private SpUtil spUtil;
    private SwitchDarkModeUtil switchDarkModeUtil;
    private boolean enable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spUtil = new SpUtil(this);
        switchDarkModeUtil = new SwitchDarkModeUtil(this);

        if (Intent.ACTION_CREATE_SHORTCUT.equals(getIntent().getAction())) {
            createShortCut();
        } else {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("darkMode")) {
                int nightMode = Objects.requireNonNull(intent.getExtras()).getInt("darkMode");
                switchDarkModeUtil.setDarkModeWithResult(nightMode);
            } else {
                if (new SpUtil(this).getWorkMode() == 2) {
                    enable = !WriteSettingsUtil.isNightMode(this);
                } else {
                    enable = !switchDarkModeUtil.isDarkMode();
                }
                switchDarkModeUtil.setDarkModeWithResult(enable);
            }

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

    @Override
    public void onConfigurationChanged(@NonNull Configuration newconfig) {
        super.onConfigurationChanged(newconfig);

        if (spUtil.isStableMode()) {
            switchDarkModeUtil.setDarkModeWithResult(enable);
        } else {
            finish();
        }
    }
}
