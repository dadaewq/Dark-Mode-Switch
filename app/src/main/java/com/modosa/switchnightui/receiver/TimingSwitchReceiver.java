package com.modosa.switchnightui.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.modosa.switchnightui.fragment.SettingsFragment;
import com.modosa.switchnightui.util.OpUtil;
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
        SpUtil spUtil = new SpUtil(context);

        if (new SpUtil(context).getFalseBoolean(SettingsFragment.SP_KEY_PERMANENT_NOTIFICATION)) {
            OpUtil.addPermanentNotification(context);
        }

        if (intent != null && intent.hasExtra("darkMode")) {
            int nightMode = Objects.requireNonNull(intent.getExtras()).getInt("darkMode");
            new SwitchDarkModeUtil(context).setDarkModeWithResult(nightMode);
        } else {
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
}
