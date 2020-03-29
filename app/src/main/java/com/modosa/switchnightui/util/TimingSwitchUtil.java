package com.modosa.switchnightui.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.modosa.switchnightui.receiver.TimingSwitchReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author dadaewq
 */
public class TimingSwitchUtil {

    public static final String ENABLE_TIMING_SWITCH = "enableTimingSwitch";
    public static final String SP_KEY_TIMEON = "timeOn";
    public static final String SP_KEY_TIMEOFF = "timeOff";
    public static final String ENABLE_TIMING_SWITCH2 = "enableTimingSwitch2";
    public static final String SP_KEY_TIMEON2 = "timeOn2";
    public static final String SP_KEY_TIMEOFF2 = "timeOff2";
    private final Context context;
    private final AlarmManager alarmManager;
    private final SpUtil spUtil;

    public TimingSwitchUtil(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        spUtil = new SpUtil(context);
    }

    private PendingIntent getPendingIntent(int nightModeOrForceDark) {
        Intent intent = new Intent(context, TimingSwitchReceiver.class);
        if (nightModeOrForceDark == UiModeManager.MODE_NIGHT_YES || nightModeOrForceDark == UiModeManager.MODE_NIGHT_NO) {
            intent.putExtra("nightMode", nightModeOrForceDark);
        } else {
            intent.putExtra("froceDark", nightModeOrForceDark);
        }


        return PendingIntent.getBroadcast(context, nightModeOrForceDark, intent, 0);
    }

    public void setAllSwitchAlarm() {
        setSingleSwitchAlarm(UiModeManager.MODE_NIGHT_YES);
        setSingleSwitchAlarm(UiModeManager.MODE_NIGHT_NO);
    }

    public void setAllSwitchAlarm2() {
        setSingleSwitchAlarm(22);
        setSingleSwitchAlarm(11);
    }

    public void setSingleSwitchAlarm(int nightModeOrForceDark) {
        long triggerAtMillis = getTriggerAtMillis(nightModeOrForceDark);
        if (triggerAtMillis != -1) {
            setAlarm(triggerAtMillis, getPendingIntent(nightModeOrForceDark));
        }
    }

    private long getTriggerAtMillis(int nightModeOrForceDark) {

        String time = null;
        switch (nightModeOrForceDark) {
            case UiModeManager.MODE_NIGHT_YES:
                time = spUtil.getString(SP_KEY_TIMEON);
                break;
            case UiModeManager.MODE_NIGHT_NO:
                time = spUtil.getString(SP_KEY_TIMEOFF);
                break;
            case 22:
                time = spUtil.getString(SP_KEY_TIMEON2);
                break;
            case 11:
                time = spUtil.getString(SP_KEY_TIMEOFF2);
                break;
            default:
        }

        int hour, minute;
        if (time == null) {
            return -1;
        } else {
            String[] times = time.split(":");
            hour = Integer.parseInt(times[0]);
            minute = Integer.parseInt(times[1]);
        }

        Calendar now = Calendar.getInstance();
        Calendar calendarSwitch = Calendar.getInstance();
        calendarSwitch.set(Calendar.HOUR_OF_DAY, hour);
        calendarSwitch.set(Calendar.MINUTE, minute);
        calendarSwitch.set(Calendar.SECOND, 0);
        calendarSwitch.set(Calendar.MILLISECOND, 0);

        if (calendarSwitch.before(now)) {
            calendarSwitch.add(Calendar.DATE, 1);
        }

        return calendarSwitch.getTimeInMillis();

    }


    private void setAlarm(long triggerAtMillis, PendingIntent pendingIntent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Log.e("setAlarm", "" + sdf.format(new Date(triggerAtMillis)));
    }
}
