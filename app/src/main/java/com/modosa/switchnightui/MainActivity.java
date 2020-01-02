package com.modosa.switchnightui;

import android.app.UiModeManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

/**
 * @author dadaewq
 */
public class MainActivity extends AppCompatActivity {

    private final int yes = UiModeManager.MODE_NIGHT_YES;
    private final int no = UiModeManager.MODE_NIGHT_NO;
    private UiModeManager uiModeManager;

    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        status = findViewById(R.id.textView);
        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        refreshStatus();
    }

    public void onClick1(@SuppressWarnings("unused") View view) {
        changeUI1();
        refreshStatus();
    }

    public void onClick2(@SuppressWarnings("unused") View view) {
        changeUI2();
        refreshStatus();
    }

    private void refreshStatus() {
        if (WriteSettingsUtil.isNightMode(this)) {
            status.setText(R.string.DarkModeOn);
        } else {
            status.setText(R.string.DarkModeOff);
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
                    showToast(getString(R.string.yes1));
                } else {
                    showToast(getString(R.string.no1));
                }
            }
        }, 0);
    }

    private void changeUI2() {
        WriteSettingsUtil.putKey(this, new WriteSettingsUtil.OnEnableAccessibilityListener() {
            @Override
            public void onSuccess(String t) {
                showToast(t);
            }

            @Override
            public void onFailed(String t) {
                if (t.contains("WRITE_SECURE_SETTINGS")) {
                    String CMD = "adb shell pm grant " + getPackageName() + " android.permission.WRITE_SECURE_SETTINGS";
                    copyCMD(CMD);
                    showToast(String.format(getString(R.string.needpermission), CMD));

                } else {
                    showToast(t);
                }
            }
        });

    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshStatus();
    }

    private void copyCMD(CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, text);
        Objects.requireNonNull(clipboard).setPrimaryClip(clipData);
    }

}
