package com.modosa.switchnightui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Keep;
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
    private TextView nameDarkmode;

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
            alertDialog.setOnDismissListener(dialog -> refreshStatus());
        }
    }

    @Keep
    private String hookTitleReturnString() {
//        如果需要hook，不要注释下一行
        Log.i("hook2ReturnTrue", ": ");

        return getString(R.string.title_dark_mode);
    }

    @SuppressLint("SetTextI18n")
    private void setView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.alertdialog_background);
        }

        nameDarkmode = findViewById(R.id.name_dark_mode);
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

    private void showDialogEnableStableMode(int need) {
        if (!spUtil.getFalseBoolean(OpUtil.CONFIRM_PROMPT)) {
            Context context = this;
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.title_second_confirm)
                    .setMessage(R.string.message_second_confirm)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        want = need;
                        switchDarkMode();
                    });

            alertDialog = builder.create();
            alertDialog.setOnDismissListener(dialog -> refreshStatus());
            OpUtil.showAlertDialog(context, alertDialog);
        } else {
            want = need;
            switchDarkMode();
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

        on.setOnClickListener(v -> showDialogEnableStableMode(UiModeManager.MODE_NIGHT_YES));

        off.setOnClickListener(v -> showDialogEnableStableMode(UiModeManager.MODE_NIGHT_NO));

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

            MenuItem forceDark = menu.findItem(R.id.switch_force_dark);
            forceDark.setVisible(true);

            if (switchForceDarkUtil.isForceDark()) {
                forceDark.setTitle(R.string.tip_on_force_dark);
            } else {
                forceDark.setTitle(R.string.tip_off_force_dark);
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
        } else if (id == R.id.switch_force_dark) {
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
        nameDarkmode.setText(hookTitleReturnString());
        if (!spUtil.getFalseBoolean(OpUtil.CONFIRM_PROMPT)) {
            nameDarkmode.append(" ?");
        }
        if (spUtil.isStableMode()) {
            setTitle("* " + getString(R.string.app_name));
        } else {
            setTitle(getString(R.string.app_name));
        }

        boolean isSecureSettingsNightMode = WriteSettingsUtil.isNightMode(this);

        String suffixOn = OpUtil.BLANK, suffixOff = OpUtil.BLANK;
        if (uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
            on.setChecked(true);
            if (!isSecureSettingsNightMode) {
                suffixOff = "*";
            }
        } else {
            off.setChecked(true);
            if (isSecureSettingsNightMode) {
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
