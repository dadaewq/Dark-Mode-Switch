package com.modosa.switchnightui.receiver.widgets;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.activity.SwitchBatterySaverActivity;

/**
 * Implementation of SwitchBatterySaverWidget functionality.
 *
 * @author dadaewq
 */
public class SwitchBatterySaverWidget extends AbstractSwitchWidget {

    public SwitchBatterySaverWidget() {
        imgId = R.id.img_switch_battery_saver;
        layoutId = R.layout.app_widget_switch_battery_saver;
        myClass = SwitchBatterySaverActivity.class;
    }

}

