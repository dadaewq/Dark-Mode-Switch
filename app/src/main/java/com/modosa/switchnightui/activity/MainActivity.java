package com.modosa.switchnightui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.base.BaseActivity;
import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SpUtil;
import com.modosa.switchnightui.util.SwitchDarkModeUtil;
import com.modosa.switchnightui.util.SwitchForceDarkUtil;
import com.modosa.switchnightui.util.WriteSettingsUtil;


/**
 * @author dadaewq
 */
public class MainActivity extends BaseActivity {
    public static final String SP_KEY_ENABLE_BUG_REPORT = "enableBugReport";
    private final static String CONFIRM_PROMPT = "ConfirmPrompt01";


    private SwitchDarkModeUtil switchDarkModeUtil;
    private SwitchForceDarkUtil switchForceDarkUtil;

    private SpUtil spUtil;
    private TextView status;
    private UiModeManager uiModeManager;
    private RadioGroup radioGroup1;
    private int want = -1;
    private boolean isstablemode;
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
        if (!spUtil.getFalseBoolean(CONFIRM_PROMPT)) {
            showDialogConfirmPrompt();
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

        if (spUtil.isStableMode()) {
            setTitle("* " + getString(R.string.app_name));

        } else {
            setTitle(getString(R.string.app_name));

        }
        on = findViewById(R.id.on);
        off = findViewById(R.id.off);


        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
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
            want = UiModeManager.MODE_NIGHT_YES;
            switchDarkMode();

        });

        off.setOnClickListener(v -> {
            want = UiModeManager.MODE_NIGHT_NO;
            switchDarkMode();
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
        MenuItem carMode = menu.findItem(R.id.switchcarmode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            carMode.setVisible(true);
            MenuItem forceDark = menu.findItem(R.id.switchforcedark);
            forceDark.setVisible(true);

            if (switchForceDarkUtil.isForceDark()) {
                forceDark.setTitle(R.string.ForceDarkOn);
            } else {
                forceDark.setTitle(R.string.ForceDarkOff);
            }
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            carMode.setVisible(true);
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
            OpUtil.showToast0(this, R.string.switchcarmode);
            if (Configuration.UI_MODE_TYPE_CAR == uiModeManager.getCurrentModeType()) {
                uiModeManager.disableCarMode(0);
            } else {
                uiModeManager.enableCarMode(2);
            }
            return true;
        } else if (id == R.id.switchstablemode) {

            spUtil.switchStableMode(isstablemode);

            if (spUtil.isStableMode()) {
                OpUtil.showToast0(this, getString(R.string.StableModeOn) + getString(R.string.tip_StableModeOn));
                setTitle("* " + getString(R.string.app_name));
            } else {
                OpUtil.showToast0(this, R.string.StableModeOff);
                setTitle(getString(R.string.app_name));
            }
            return true;
        } else if (id == R.id.switchforcedark) {
            switchForceDarkUtil.switchForceDark();
            return true;

        } else if (id == R.id.timeup) {
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setClass(this, TimingSwitchActivity.class);
            startActivity(intent);
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

    private void showDialogConfirmPrompt() {

        View checkBoxView = View.inflate(this, R.layout.confirm_checkbox, null);

        CheckBox checkboxBugreport = checkBoxView.findViewById(R.id.confirm_checkboxBugreport);
        CheckBox checkBox1 = checkBoxView.findViewById(R.id.confirm_checkbox1);
        CheckBox checkBox2 = checkBoxView.findViewById(R.id.confirm_checkbox2);
        CheckBox checkBox3 = checkBoxView.findViewById(R.id.confirm_checkbox3);
        CheckBox checkBox4 = checkBoxView.findViewById(R.id.confirm_checkbox4);
        checkboxBugreport.setText(R.string.checkbox_enable_bugReport);
        checkBox1.setText(R.string.checkbox1_instructions_before_use);
        checkBox2.setText(R.string.checkbox2_instructions_before_use);
        checkBox3.setText(R.string.checkbox3_instructions_before_use);
        checkBox4.setText(R.string.checkbox4_instructions_before_use);

        checkboxBugreport.setChecked(spUtil.getBoolean(SP_KEY_ENABLE_BUG_REPORT, true));
        checkBox2.setEnabled(false);
        checkBox3.setEnabled(false);
        checkBox4.setEnabled(false);

        checkboxBugreport.setOnCheckedChangeListener((buttonView, isChecked) -> spUtil.putBoolean(SP_KEY_ENABLE_BUG_REPORT, checkboxBugreport.isChecked()));

        checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkBox2.setChecked(false);
            checkBox2.setEnabled(isChecked);
        });

        checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkBox3.setChecked(false);
            checkBox3.setEnabled(isChecked);
        });

        checkBox3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkBox4.setChecked(false);
            checkBox4.setEnabled(isChecked);
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.title_instructions_before_use)
                .setView(checkBoxView)
                .setPositiveButton(android.R.string.no, null)
                .setNeutralButton(android.R.string.yes, (dialog, which) -> {
                    boolean hasBothConfirm = false;
                    if (checkBox1.isChecked() && checkBox2.isChecked() && checkBox3.isChecked() && checkBox4.isChecked()) {
                        hasBothConfirm = true;
                    }
                    spUtil.putBoolean(CONFIRM_PROMPT, hasBothConfirm);
                });

        alertDialog = builder.create();
        OpUtil.showAlertDialog(this, alertDialog);


        Button button = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        button.setEnabled(false);

        CountDownTimer timer = new CountDownTimer(10000, 1000) {
            final String oK = getString(android.R.string.ok);

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                button.setText(oK + "(" + millisUntilFinished / 1000 + "s" + ")");
            }

            @Override
            public void onFinish() {
                button.setText(oK);
                button.setEnabled(true);
//                button.setClickable(true);
            }
        };
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        timer.start();


    }

    private void refreshStatus() {
        if (uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
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
                want = UiModeManager.MODE_NIGHT_NO;
            } else {
                want = UiModeManager.MODE_NIGHT_YES;
            }
            switchDarkModeUtil.setDarkModeWithTip(want);
        } else {
            recreate();
        }
        refreshStatus();
    }

}
