package com.modosa.switchnightui.activity;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.SpUtil;
import com.modosa.switchnightui.util.SwitchDarkModeUtil;
import com.modosa.switchnightui.util.WriteSettingsUtil;

import java.util.Objects;

/**
 * @author dadaewq
 */
public class SwitchDarkModeActivity extends AbstractSwitchActivity {

    private SpUtil spUtil;
    private SwitchDarkModeUtil switchDarkModeUtil;
    private boolean enable;

    public SwitchDarkModeActivity() {
        shortcutId = "SwitchDarkMode";
        shortcutLongLabelId = R.string.title_dark_mode;
        iconId = R.mipmap.ic_launcher_dark_mode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        spUtil = new SpUtil(this);
        switchDarkModeUtil = new SwitchDarkModeUtil(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    void switchMethod() {
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
