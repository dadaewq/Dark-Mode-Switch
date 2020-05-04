package com.modosa.switchnightui.receiver.widgets;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.activity.SwitchGrayScaleActivity;

/**
 * Implementation of SwitchGrayScaleWidget functionality.
 *
 * @author dadaewq
 */
public class SwitchGrayScaleWidget extends AbstractSwitchWidget {

    public SwitchGrayScaleWidget() {
        imgId = R.id.img_switch_gray_scale;
        layoutId = R.layout.app_widget_switch_gray_scale;
        myClass = SwitchGrayScaleActivity.class;
    }

}

