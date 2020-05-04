package com.modosa.switchnightui.receiver.widgets;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.activity.SwitchInvertColorsActivity;

/**
 * Implementation of SwitchInvertColorsWidget functionality.
 *
 * @author dadaewq
 */
public class SwitchInvertColorsWidget extends AbstractSwitchWidget {

    public SwitchInvertColorsWidget() {
        imgId = R.id.img_switch_invert_colors;
        layoutId = R.layout.app_widget_switch_invert_colors;
        myClass = SwitchInvertColorsActivity.class;
    }

}

