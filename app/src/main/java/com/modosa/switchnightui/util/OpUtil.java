package com.modosa.switchnightui.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.UiModeManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.NotificationCompat;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.activity.MainActivity;
import com.modosa.switchnightui.activity.SwitchDarkModeActivity;
import com.modosa.switchnightui.receiver.TimingSwitchReceiver;
import com.modosa.switchnightui.service.NotificationService;

/**
 * @author dadaewq
 */
public class OpUtil {
    public static final String BLANK = " ";
    public static final String SP_KEY_ENABLE_BUG_REPORT = "enableBugReport";
    public final static String CONFIRM_PROMPT = "ConfirmPrompt01";
    private static final String SP_KEY_EXCLUDE_FROM_RECENTS = "excludeFromRecents";

    public static void showAlertDialog(Context context, AlertDialog alertDialog) {
        Window window = alertDialog.getWindow();
        if (window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.Background, null)));
            } else {
                window.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.Background)));
            }
            window.setBackgroundDrawableResource(R.drawable.alertdialog_background);
        }

        if (!((Activity) context).isFinishing()) {
            alertDialog.show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.rBackground, null));
                alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(context.getResources().getColor(R.color.rBackground, null));
            } else {
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.rBackground));
                alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(context.getResources().getColor(R.color.rBackground));
            }
        }
    }

    public static boolean isScreenOn(Context context) {

        PowerManager powerManager = (PowerManager)
                context.getSystemService(Context.POWER_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return powerManager.isInteractive();
        } else {
            return powerManager.isScreenOn();
        }
    }

    static void copyCmd(Context context, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, text);
        assert clipboard != null;
        clipboard.setPrimaryClip(clipData);
    }

    public static void startMyClass(Context context, Class myClass) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setClass(context, myClass);
            context.startActivity(intent);
        } catch (Exception e) {
            showToast1(context, "" + e);
        }
    }

    public static void showTipNeedSdk(Context context, int what1, String what2) {
        showToast1(context, String.format(
                context.getString(R.string.tip_switch_what1_need_android_what2),
                context.getString(what1),
                what2
        ));
    }

    public static String getTipStr1IsStr2(Context context, String what1, String what2) {
        return String.format(
                context.getString(R.string.tip_str1_is_str2),
                what1,
                what2
        );
    }

    public static Notification getPermanentNotification(Context context) {
        String channelId = R.string.title_permanent_notification + "";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        if (notificationManager != null) {
            notification = new NotificationCompat.Builder(context, channelId)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_qs_dark_mode)
                    .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0))
                    .setNotificationSilent()
                    .addAction(0, context.getString(R.string.on), getSwitchPendingIntent(context, UiModeManager.MODE_NIGHT_YES))
                    .addAction(0, context.getString(R.string.off), getSwitchPendingIntent(context, UiModeManager.MODE_NIGHT_NO))
                    .build();


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, context.getString(R.string.title_permanent_notification), NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            // 设置常驻Flag
            notification.flags = Notification.FLAG_ONGOING_EVENT;

        }
        return notification;
    }


    public static void addPermanentNotification(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, NotificationService.class));
            } else {
                context.startService(new Intent(context, NotificationService.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancelPermanentNotification(Context context) {
        context.stopService(new Intent(context, NotificationService.class));
    }

    private static PendingIntent getSwitchPendingIntent(Context context, int darkMode) {

        Intent intent = new Intent(context, SwitchDarkModeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("darkMode", darkMode);
        try {
            return PendingIntent.getActivity(context, darkMode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } catch (Exception e) {
            e.printStackTrace();
            return getSwitchPendingIntent1(context, darkMode);
        }
    }

    private static PendingIntent getSwitchPendingIntent1(Context context, int darkMode) {
        Intent intent = new Intent(context, TimingSwitchReceiver.class)
                .putExtra("darkMode", darkMode);
        return PendingIntent.getBroadcast(context, darkMode + 2, intent, 0);
    }


    public static AlertDialog createDialogConfirmPrompt(Context context) {
        SpUtil spUtil = new SpUtil(context);
        View checkBoxView = View.inflate(context, R.layout.confirm_checkbox, null);

        CheckBox checkboxBugreport = checkBoxView.findViewById(R.id.confirm_checkboxBugreport);
        CheckBox checkBox1 = checkBoxView.findViewById(R.id.confirm_checkbox1);
        CheckBox checkBox2 = checkBoxView.findViewById(R.id.confirm_checkbox2);
        CheckBox checkBox3 = checkBoxView.findViewById(R.id.confirm_checkbox3);
        CheckBox checkBox4 = checkBoxView.findViewById(R.id.confirm_checkbox4);
        CheckBox checkBoxLast = checkBoxView.findViewById(R.id.confirm_checkbox_last);
        checkboxBugreport.setText(R.string.checkbox_enable_bugReport);
        checkBox1.setText(R.string.checkbox1_instructions_before_use);
        checkBox2.setText(R.string.checkbox2_instructions_before_use);
        checkBox3.setText(R.string.checkbox3_instructions_before_use);
        checkBox4.setText(R.string.checkbox4_instructions_before_use);
        checkBoxLast.setText(R.string.checkbox_last_instructions_before_use);

        checkboxBugreport.setChecked(spUtil.getBoolean(SP_KEY_ENABLE_BUG_REPORT, true));
        checkBox2.setEnabled(false);
        checkBox3.setEnabled(false);
        checkBox4.setEnabled(false);
        checkBoxLast.setEnabled(false);

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

        checkBox4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkBoxLast.setChecked(false);
            checkBoxLast.setEnabled(isChecked);
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.title_instructions_before_use)
                .setView(checkBoxView)
                .setPositiveButton(android.R.string.cancel, null)
                .setNeutralButton(android.R.string.ok, (dialog, which) -> {
                    boolean hasBothConfirm = false;
                    if (checkBox1.isChecked() && checkBox2.isChecked() && checkBox4.isChecked() && checkBoxLast.isChecked()) {
                        hasBothConfirm = true;
                    }
                    spUtil.putBoolean(CONFIRM_PROMPT, hasBothConfirm);
                });

        return builder.create();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void finishAndRemoveTask(Activity activity) {
        boolean removeTask = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && new SpUtil(activity).getFalseBoolean(SP_KEY_EXCLUDE_FROM_RECENTS)) {
            removeTask = true;
        }
        if (removeTask) {
            activity.finishAndRemoveTask();
        } else {
            activity.finish();
        }
    }


    public static void showDialogConfirmPrompt(Context context, AlertDialog alertDialog) {

        OpUtil.showAlertDialog(context, alertDialog);

        Button button = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        button.setEnabled(false);

        CountDownTimer timer = new CountDownTimer(30000, 1000) {
            final String oK = context.getString(android.R.string.ok);

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                button.setText(oK + "(" + millisUntilFinished / 1000 + "s" + ")");
            }

            @Override
            public void onFinish() {
                button.setText(oK);
                button.setEnabled(true);
            }
        };
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        timer.start();

        View alertTitle = null;
        try {
            alertTitle = alertDialog.findViewById(context.getResources().getIdentifier("alertTitle", "id", "android"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != alertTitle) {
            alertTitle.setOnLongClickListener(v -> {
                ViewGroup viewGroup = alertDialog.findViewById(R.id.checkboxParrent);
                if (null != viewGroup) {
                    for (int i = 0; i < viewGroup.getChildCount(); i++) {
                        ((CheckBox) viewGroup.getChildAt(i)).setChecked(true);
                    }
                }

                timer.cancel();
                timer.onFinish();
                return false;
            });
        }
    }

    public static void launchCustomTabsUrl(Context context, String url) {
        try {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build();

            customTabsIntent.launchUrl(context, Uri.parse(url));
        } catch (Exception e) {
            showToast1(context, "" + e);
        }
    }

    public static void openUrl(Context context, String url) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url.trim())));
        } catch (Exception e) {
            showToast1(context, "" + e);
        }
    }

    public static void showToast0(Context context, final String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast0(Context context, final int stringId) {
        Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast1(Context context, final String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showToast1(Context context, final int stringId) {
        Toast.makeText(context, stringId, Toast.LENGTH_LONG).show();
    }
}
