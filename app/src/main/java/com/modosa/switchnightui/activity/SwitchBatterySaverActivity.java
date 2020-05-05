package com.modosa.switchnightui.activity;


import android.os.Build;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.SwitchBatterySaverUtil;

/**
 * @author dadaewq
 */
public class SwitchBatterySaverActivity extends AbstractSwitchActivity {


    public SwitchBatterySaverActivity() {
        shortcutId = "SwitchBatterySaver";
        shortcutLongLabelId = R.string.name_switch_battery_saver;
        iconId = R.drawable.ic_qs_battery_saver_48dp;
    }


    @Override
    void switchMethod() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            new SwitchBatterySaverUtil(this).switchBatterySaverWithResult();
        } else {
            showTipNeedSdk(R.string.title_battery_saver, "5.0");
        }
    }

}
