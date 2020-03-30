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
import com.modosa.switchnightui.util.SwitchForceDarkUtil;

/**
 * @author dadaewq
 */
public class SwitchForceDarkActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Intent.ACTION_CREATE_SHORTCUT.equals(getIntent().getAction())) {
            createShortCut();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                new SwitchForceDarkUtil(this).switchForceDark();
            } else {
                OpUtil.showToast1(this, R.string.tip_need_sdk29);
            }
            finish();
        }
    }

    private void createShortCut() {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {

            Intent intent = new Intent(new Intent(Intent.ACTION_VIEW))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setClass(this, getClass());

            ShortcutInfoCompat shortcut = new ShortcutInfoCompat.Builder(this, "SwitchForceDark")
                    .setLongLabel(getString(R.string.app_name))
                    .setShortLabel(getString(R.string.action_switch))
                    .setIcon(IconCompat.createWithResource(this, R.drawable.ic_brightness_3_black_24dp))
                    .setIntent(intent)
                    .build();

            Intent pinnedShortcutCallbackIntent = ShortcutManagerCompat.createShortcutResultIntent(this, shortcut);
            setResult(RESULT_OK, pinnedShortcutCallbackIntent);
            finish();
        }
    }
}
