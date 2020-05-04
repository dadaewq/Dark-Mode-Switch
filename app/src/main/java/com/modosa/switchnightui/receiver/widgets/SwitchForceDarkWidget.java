package com.modosa.switchnightui.receiver.widgets;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.activity.SwitchForceDarkActivity;

/**
 * Implementation of SwitchForceDarkWidget functionality.
 *
 * @author dadaewq
 */
public class SwitchForceDarkWidget extends AbstractSwitchWidget {

    public SwitchForceDarkWidget() {
        imgId = R.id.img_switch_force_dark;
        layoutId = R.layout.app_widget_switch_force_dark;
        myClass = SwitchForceDarkActivity.class;
    }

}


