package com.modosa.switchnightui.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.modosa.switchnightui.util.SpUtil;
import com.modosa.switchnightui.util.SwitchDarkModeUtil;
import com.modosa.switchnightui.util.SwitchForceDarkUtil;
import com.modosa.switchnightui.util.TimingSwitchUtil;

import java.util.Objects;

/**
 * @author dadaewq
 */
public class TimingSwitchReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("Intent", intent + "");
        SpUtil spUtil = new SpUtil(context);

        //Switch Dark Mode
        if (spUtil.getFalseBoolean(TimingSwitchUtil.ENABLE_TIMING_SWITCH)) {
            if (intent != null && intent.hasExtra("nightMode")) {
                int nightMode = Objects.requireNonNull(intent.getExtras()).getInt("nightMode");
                new SwitchDarkModeUtil(context).setDarkModeWithResult(nightMode);

            }
            new TimingSwitchUtil(context).setAllSwitchAlarm();
        }

        //Switch Force Dark
        if (spUtil.getFalseBoolean(TimingSwitchUtil.ENABLE_TIMING_SWITCH2)) {
            if (intent != null && intent.hasExtra("froceDark")) {
                int froceDark = Objects.requireNonNull(intent.getExtras()).getInt("froceDark");
                new SwitchForceDarkUtil(context).setForceDark(froceDark);
            }
            new TimingSwitchUtil(context).setAllSwitchAlarm2();
        }

    }
}
