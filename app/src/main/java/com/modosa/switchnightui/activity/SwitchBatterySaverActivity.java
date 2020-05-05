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
        shortcutLongLabelId = R.string.title_battery_saver;
        iconId = R.mipmap.ic_launcher_battery_saver;
    }


    @Override
    void switchMethod() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            new SwitchBatterySaverUtil(this).switchBatterySaverWithResult();
        } else {
            showSwitchTipNeedSdk(R.string.title_battery_saver, "5.0");
        }
    }

}
