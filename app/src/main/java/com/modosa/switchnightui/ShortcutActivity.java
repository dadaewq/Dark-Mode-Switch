package com.modosa.switchnightui;


import android.app.Activity;
import android.app.UiModeManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.Objects;

public class ShortcutActivity extends Activity {

    private final int yes = UiModeManager.MODE_NIGHT_YES;
    private final int no = UiModeManager.MODE_NIGHT_NO;
    private UiModeManager uiModeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        changeUI1();

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
