package com.modosa.switchnightui.receiver.widgets;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.activity.SwitchDarkModeActivity;

/**
 * Implementation of SwitchDarkModeWidget functionality.
 *
 * @author dadaewq
 */
public class SwitchDarkModeWidget extends AbstractSwitchWidget {

    public SwitchDarkModeWidget() {
        imgId = R.id.img_switch_dark_mode;
        layoutId = R.layout.app_widget_switch_dark_mode;
        myClass = SwitchDarkModeActivity.class;
    }

}


