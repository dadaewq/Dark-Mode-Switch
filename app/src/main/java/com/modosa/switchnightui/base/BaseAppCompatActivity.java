package com.modosa.switchnightui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.modosa.switchnightui.fragment.SettingsFragment;
import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SpUtil;
import com.modosa.switchnightui.util.TimingSwitchUtil;

/**
 * @author dadaewq
 */
abstract public class BaseAppCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        SpUtil spUtil = new SpUtil(context);

        if (spUtil.getFalseBoolean(SettingsFragment.SP_KEY_PERMANENT_NOTIFICATION)) {
            OpUtil.addPermanentNotification(context);
        }

        if (spUtil.getFalseBoolean(TimingSwitchUtil.ENABLE_TIMING_SWITCH)) {
            new TimingSwitchUtil(getApplicationContext()).setAllSwitchAlarm();
        }

        if (spUtil.getFalseBoolean(TimingSwitchUtil.ENABLE_TIMING_SWITCH2)) {
            new TimingSwitchUtil(getApplicationContext()).setAllSwitchAlarm2();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
