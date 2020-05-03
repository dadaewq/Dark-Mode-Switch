package com.modosa.switchnightui.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.base.BaseActivity;
import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SwitchBatterySaverUtil;

/**
 * @author dadaewq
 */
@SuppressWarnings("FieldCanBeLocal")
public class SwitchBatterySaverActivity extends BaseActivity {

    private final String shortcutId = "SwitchBatterySaver";
    private final int shortcutLongLabelId = R.string.name_switch_battery_saver;
    private final int shortcutShortLabelId = R.string.action_switch;
    private final int iconId = R.drawable.ic_qs_battery_saver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SwitchBatterySaverUtil switchBatterySaverUtil = new SwitchBatterySaverUtil(this);

        if (Intent.ACTION_CREATE_SHORTCUT.equals(getIntent().getAction())) {
            createShortCut();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                switchBatterySaverUtil.switchBatterySaver();
            } else {
                OpUtil.showToast1(this, String.format(
                        getString(R.string.tip_switch_what1_need_android_what2),
                        getString(R.string.title_battery_saver),
                        "5.0"
                ));
            }

            finish();
        }
    }

    private void createShortCut() {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {

            Intent intent = new Intent(new Intent(Intent.ACTION_VIEW))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setClass(this, getClass());

            ShortcutInfoCompat shortcut = new ShortcutInfoCompat.Builder(this, shortcutId)
                    .setLongLabel(getString(shortcutLongLabelId))
                    .setShortLabel(getString(shortcutShortLabelId))
                    .setIcon(IconCompat.createWithResource(this, iconId))
                    .setIntent(intent)
                    .build();

            Intent pinnedShortcutCallbackIntent = ShortcutManagerCompat.createShortcutResultIntent(this, shortcut);
            setResult(RESULT_OK, pinnedShortcutCallbackIntent);
            finish();
        }
    }

}
