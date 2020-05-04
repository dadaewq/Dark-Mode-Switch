package com.modosa.switchnightui.receiver.widgets;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.activity.SwitchNightDisplayActivity;

/**
 * Implementation of SwitchNightDisplayWidget functionality.
 *
 * @author dadaewq
 */
public class SwitchNightDisplayWidget extends AbstractSwitchWidget {

    public SwitchNightDisplayWidget() {
        imgId = R.id.img_switch_night_display;
        layoutId = R.layout.app_widget_switch_night_display;
        myClass = SwitchNightDisplayActivity.class;
    }

}

