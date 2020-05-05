package com.modosa.switchnightui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.base.BaseActivity;
import com.modosa.switchnightui.util.OpUtil;

/**
 * @author dadaewq
 */
abstract public class AbstractSwitchActivity extends BaseActivity {

    String shortcutId;
    int shortcutLongLabelId;
    int iconId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Intent.ACTION_CREATE_SHORTCUT.equals(getIntent().getAction())) {
            createShortCut();
        } else {
            switchMethod();
        }
        finish();
    }

    void showSwitchTipNeedSdk(int what1, String what2) {
        OpUtil.showToast1(this, String.format(
                getString(R.string.tip_switch_what1_need_android_what2),
                getString(what1),
                what2
        ));
    }

    /**
     * 切换的具体方法
     */
    abstract void switchMethod();

    private void createShortCut() {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {

            Intent intent = new Intent(new Intent(Intent.ACTION_VIEW))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setClass(this, getClass());
            String label = getString(shortcutLongLabelId);
            ShortcutInfoCompat shortcut = new ShortcutInfoCompat.Builder(this, shortcutId)
                    .setLongLabel(label)
                    .setShortLabel(label)
                    .setIcon(IconCompat.createWithResource(this, iconId))
                    .setIntent(intent)
                    .build();

            Intent pinnedShortcutCallbackIntent = ShortcutManagerCompat.createShortcutResultIntent(this, shortcut);
            setResult(RESULT_OK, pinnedShortcutCallbackIntent);
        }
    }

}
