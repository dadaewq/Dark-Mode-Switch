package com.modosa.switchnightui.base;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.modosa.switchnightui.util.SpUtil;
import com.modosa.switchnightui.util.TimingSwitchUtil;

/**
 * @author dadaewq
 */
abstract public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        if (new SpUtil(context).getFalseBoolean(TimingSwitchUtil.ENABLE_TIMING_SWITCH)) {
            new TimingSwitchUtil(getApplicationContext()).setAllSwitchAlarm();
        }

        if (new SpUtil(context).getFalseBoolean(TimingSwitchUtil.ENABLE_TIMING_SWITCH2)) {
            new TimingSwitchUtil(getApplicationContext()).setAllSwitchAlarm2();
        }
    }
}
