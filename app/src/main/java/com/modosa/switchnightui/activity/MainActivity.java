package com.modosa.switchnightui.activity;

import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.base.BaseAppCompatActivity;
import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SpUtil;
import com.modosa.switchnightui.util.SwitchDarkModeUtil;
import com.modosa.switchnightui.util.SwitchForceDarkUtil;
import com.modosa.switchnightui.util.WriteSettingsUtil;


/**
 * @author dadaewq
 */
public class MainActivity extends BaseAppCompatActivity {

    private SwitchDarkModeUtil switchDarkModeUtil;
    private SwitchForceDarkUtil switchForceDarkUtil;

    private SpUtil spUtil;
    private UiModeManager uiModeManager;
    private RadioGroup radioGroup1;
    private int want = -1;
    private RadioButton on, off;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        confirmPrompt();
        setView();
        setListener();
        refreshStatus();
    }

    private void init() {
        spUtil = new SpUtil(this);

        switchDarkModeUtil = new SwitchDarkModeUtil(this);
        switchForceDarkUtil = new SwitchForceDarkUtil(this);

    }

    private void confirmPrompt() {
        if (!spUtil.getFalseBoolean(OpUtil.CONFIRM_PROMPT)) {
            alertDialog = OpUtil.createDialogConfirmPrompt(this);
            OpUtil.showDialogConfirmPrompt(this, alertDialog);
        }
    }

    private void setView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.alertdialog_background);
        }

        on = findViewById(R.id.on);
        off = findViewById(R.id.off);

        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        radioGroup1 = findViewById(R.id.radioGroup1);

        RadioButton radioButton2 = findViewById(R.id.radioButton2);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            radioButton2.append(" (" + getString(R.string.tips_workmode2) + ")");
        }
        int checked = spUtil.getWorkMode();

        switch (checked) {
            case 2:
                radioGroup1.check(R.id.radioButton2);
                break;
            case 3:
                radioGroup1.check(R.id.radioButton3);
                break;
            default:
                radioGroup1.check(R.id.radioButton1);
        }
    }

    private void setListener() {
        radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            int workMode;
            switch (checkedId) {
                case R.id.radioButton2:
                    workMode = 2;
                    break;
                case R.id.radioButton3:
                    workMode = 3;
                    break;
                default:
                    workMode = 1;
            }
            spUtil.putWorkMode(workMode);

        });

        on.setOnClickListener(v -> {
            want = UiModeManager.MODE_NIGHT_YES;
            switchDarkMode();

        });

        off.setOnClickListener(v -> {
            want = UiModeManager.MODE_NIGHT_NO;
            switchDarkMode();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            MenuItem forceDark = menu.findItem(R.id.switchforcedark);
            forceDark.setVisible(true);

            if (switchForceDarkUtil.isForceDark()) {
                forceDark.setTitle(R.string.ForceDarkOn);
            } else {
                forceDark.setTitle(R.string.ForceDarkOff);
            }
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            recreate();
            return true;
        } else if (id == R.id.switchforcedark) {
            switchForceDarkUtil.switchForceDark();
            return true;
        } else if (id == R.id.settings) {
            OpUtil.startMyClass(this, SettingsActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void switchDarkMode() {
        switchDarkModeUtil.setDarkModeWithTip(want);
        refreshStatus();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    private void refreshStatus() {
        if (spUtil.isStableMode()) {
            setTitle("* " + getString(R.string.app_name));
        } else {
            setTitle(getString(R.string.app_name));
        }


        String suffixOn = " ", suffixOff = " ";
        if (uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
            on.setChecked(true);
            if (!WriteSettingsUtil.isNightMode(this)) {
                suffixOff = "*";
            }
        } else {
            off.setChecked(true);
            if (WriteSettingsUtil.isNightMode(this)) {
                suffixOn = "*";
            }
        }
        on.setText(R.string.on);
        off.setText(R.string.off);
        on.append(suffixOn);
        off.append(suffixOff);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStatus();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        OpUtil.finishAndRemoveTask(this);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newconfig) {
        super.onConfigurationChanged(newconfig);

        if (spUtil.isStableMode()) {
            if (want != -1) {
                if (off.isChecked()) {
                    want = UiModeManager.MODE_NIGHT_NO;
                } else {
                    want = UiModeManager.MODE_NIGHT_YES;
                }
                switchDarkModeUtil.setDarkModeWithTip(want);
            }
        } else {
            recreate();
        }
        refreshStatus();
    }

}
