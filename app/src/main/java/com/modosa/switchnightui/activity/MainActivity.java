package com.modosa.switchnightui.activity;

import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.uitl.OpUtil;
import com.modosa.switchnightui.uitl.SpUtil;
import com.modosa.switchnightui.uitl.SwitchUtil;
import com.modosa.switchnightui.uitl.WriteSettingsUtil;


/**
 * @author dadaewq
 */
public class MainActivity extends AppCompatActivity {

    private final static String confirmPrompt = "ConfirmPrompt";
    private OpUtil opUtil;
    private SwitchUtil switchUtil;
    private SpUtil spUtil;
    private TextView status;
    private UiModeManager uiModeManager;
    private RadioGroup radioGroup1;
    private int want = -1;
    private boolean isstablemode;
    private boolean isOnePlus;
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
        opUtil = new OpUtil(this);
        spUtil = new SpUtil(this);
        isOnePlus = opUtil.isOp();
    }

    private void confirmPrompt() {
        if (!spUtil.getBoolean(confirmPrompt)) {
            showDialogConfirmPrompt();
        }
    }

    private void setView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (spUtil.isStableMode()) {
            setTitle("* " + getString(R.string.app_name));

        } else {
            setTitle(getString(R.string.app_name));

        }
        on = findViewById(R.id.on);
        off = findViewById(R.id.off);


        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        switchUtil = new SwitchUtil(this, uiModeManager);
        radioGroup1 = findViewById(R.id.radioGroup1);

        RadioButton radioButton2 = findViewById(R.id.radioButton2);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            radioButton2.append(" (" + getString(R.string.tips_method2) + ")");
        }
        int checked = spUtil.getMethod();

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
            int method;
            switch (checkedId) {
                case R.id.radioButton2:
                    method = 2;
                    break;
                case R.id.radioButton3:
                    method = 3;
                    break;
                default:
                    method = 1;
            }
            spUtil.putMethod(method);

        });

        on.setOnClickListener(v -> {
            want = WriteSettingsUtil.YES;
            switchui();
        });

        off.setOnClickListener(v -> {
            want = WriteSettingsUtil.NO;
            switchui();
        });

        status = findViewById(R.id.textView);
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
            boolean isForceDark;
            if (isOnePlus) {
                isForceDark = opUtil.isForceDark();
            } else {
                isForceDark = switchUtil.isForceDark();
            }
            if (isForceDark) {
                forceDark.setTitle(R.string.ForceDarkOn);
            } else {
                forceDark.setTitle(R.string.ForceDarkOff);
            }
        }

        MenuItem menuItem2 = menu.findItem(R.id.switchstablemode);
        if (spUtil.isStableMode()) {
            isstablemode = true;
            menuItem2.setTitle(R.string.StableModeOn);
        } else {
            isstablemode = false;
            menuItem2.setTitle(R.string.StableModeOff);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            recreate();

        } else if (id == R.id.switchcarmode) {
            switchUtil.showToast0(R.string.switchcarmode);
            if (Configuration.UI_MODE_TYPE_CAR == uiModeManager.getCurrentModeType()) {
                uiModeManager.disableCarMode(0);
            } else {
                uiModeManager.enableCarMode(2);
            }
            return true;
        } else if (id == R.id.switchstablemode) {

            spUtil.switchStableMode(isstablemode);

            if (spUtil.isStableMode()) {
                switchUtil.showToast0(getString(R.string.StableModeOn) + getString(R.string.tip_StableModeOn));
                setTitle("* " + getString(R.string.app_name));
                item.setTitle(R.string.StableModeOn);
            } else {
                switchUtil.showToast0(R.string.StableModeOff);
                setTitle(getString(R.string.app_name));
                item.setTitle(R.string.StableModeOff);
            }
            return true;
        } else if (id == R.id.switchforcedark) {

            boolean isSu, isForceDark;
            String msg = "";
            if (isOnePlus) {
                isSu = opUtil.switchForceDark();
                if (!isSu) {
                    switchUtil.showToast0(R.string.no_root);
                    return true;
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
                item.setTitle(R.string.ForceDarkOn);
                switchUtil.showToast0(msg + getString(R.string.ForceDarkOn));
            } else {
                item.setTitle(R.string.ForceDarkOff);
                switchUtil.showToast0(msg + getString(R.string.ForceDarkOff));
            }

            return true;

        } else if (id == R.id.timeup) {
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setClass(this, TimeUpActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchui() {
        String msg = "";
        if (want != WriteSettingsUtil.NO) {
            want = WriteSettingsUtil.YES;
        }
        switch (radioGroup1.getCheckedRadioButtonId()) {
            case R.id.radioButton2:
                switchUtil.switch2(want);
                break;
            case R.id.radioButton3:
                if (switchUtil.switch3(want)) {
                    msg = getString(R.string.no_root);
                }
                break;
            default:
                if (switchUtil.switch1(want)) {
                    msg = String.format(getString(R.string.failmethod), "1");
                }
        }
        if (!"".equals(msg)) {
            switchUtil.showToast0(msg);
        }
        refreshStatus();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    private void showDialogConfirmPrompt() {

        View checkBoxView = View.inflate(this, R.layout.confirm_checkbox, null);

        CheckBox checkBox1 = checkBoxView.findViewById(R.id.confirm_checkbox1);
        CheckBox checkBox2 = checkBoxView.findViewById(R.id.confirm_checkbox2);
        checkBox1.setText(R.string.checkbox1_instructions_before_use);
        checkBox2.setText(R.string.checkbox2_instructions_before_use);
        checkBox2.setEnabled(false);
        checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkBox2.setChecked(false);
            checkBox2.setEnabled(isChecked);
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.title_instructions_before_use)
                .setView(checkBoxView)
                .setNeutralButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    boolean hasBothConfirm = false;
                    if (checkBox1.isChecked() && checkBox2.isChecked()) {
                        hasBothConfirm = true;
                    }
                    spUtil.putBoolean(confirmPrompt, hasBothConfirm);
                });

        alertDialog = builder.create();
        OpUtil.showAlertDialog(this, alertDialog);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.rBackground));
        alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.rBackground));

    }

    private void refreshStatus() {
        if (uiModeManager.getNightMode() == WriteSettingsUtil.YES) {
            status.setText(R.string.DarkModeOn);
            if (!WriteSettingsUtil.isNightMode(this)) {
                status.append("*");
            }
            on.setChecked(true);
        } else {
            status.setText(R.string.DarkModeOff);
            if (WriteSettingsUtil.isNightMode(this)) {
                status.append("*");
            }
            off.setChecked(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStatus();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newconfig) {
        super.onConfigurationChanged(newconfig);
        if (spUtil.isStableMode()) {
            if (off.isChecked()) {
                want = 1;
            } else {
                want = 2;
            }
            switchui();
        } else {
            recreate();
        }
        refreshStatus();
    }

}
