package com.modosa.switchnightui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.uitl.OpUtil;
import com.modosa.switchnightui.uitl.SwitchUtil;

/**
 * @author dadaewq
 */
public class SwitchForceDarkActivity extends Activity {

    private OpUtil opUtil;
    private SwitchUtil switchUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Intent.ACTION_CREATE_SHORTCUT.equals(getIntent().getAction())) {
            createShortCut();
        } else {
            opUtil = new OpUtil(this);
            switchUtil = new SwitchUtil(this, null);
            switchForceDark();
            finish();
        }
    }

    private void switchForceDark() {

        String msg = "";
        boolean isSu, isForceDark;
        if (opUtil.isOp()) {
            isSu = opUtil.switchForceDark();
            if (!isSu) {
                switchUtil.showToast(getString(R.string.no_root));
                return;
            }
            isForceDark = opUtil.isForceDark();
        } else {
            isSu = switchUtil.switchForceDark();
            if (!isSu) {
                msg = getString(R.string.no_root) + "\n";
            }
            isForceDark = switchUtil.isForceDark();
        }
        if (isForceDark) {
            switchUtil.showToast(msg + getString(R.string.ForceDarkOn));
        } else {
            switchUtil.showToast(msg + getString(R.string.ForceDarkOff));
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
