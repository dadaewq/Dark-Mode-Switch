package com.modosa.switchnightui;


import android.app.Activity;
import android.app.UiModeManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import java.util.Objects;

public class ShortcutActivity extends Activity {

    private final int yes = UiModeManager.MODE_NIGHT_YES;
    private final int no = UiModeManager.MODE_NIGHT_NO;
    private UiModeManager uiModeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);

        if ("android.intent.action.CREATE_SHORTCUT".equals(getIntent().getAction())) {
            createShortCut();
        } else {
            changeUI1();
        }
    }

    private void createShortCut() {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {
            Intent intent = new Intent(new Intent(Intent.ACTION_VIEW))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setClass(this, getClass());

            ShortcutInfoCompat shortcut = new ShortcutInfoCompat.Builder(this, "changeui1")
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

    private void changeUI1() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int i = yes;
                if (Objects.requireNonNull(uiModeManager).getNightMode() == yes) {
                    i = no;
                }
                uiModeManager.setNightMode(i);
                if (uiModeManager.getNightMode() == i) {
                    if (yes == i) {
                        showToast(getString(R.string.DarkModeOn));
                    } else {
                        showToast(getString(R.string.DarkModeOff));
                    }

                } else {
                    showToast(getString(R.string.no1));
                }
            }
        }, 0);
        finish();
    }

    private void showToast(final String text) {
        Toast.makeText(ShortcutActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
