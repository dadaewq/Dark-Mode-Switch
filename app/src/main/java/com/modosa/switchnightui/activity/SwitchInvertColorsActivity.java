package com.modosa.switchnightui.activity;


import android.os.Build;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.SwitchDisplayUtil;

/**
 * @author dadaewq
 */
public class SwitchInvertColorsActivity extends AbstractSwitchDisplayActivity {

    public SwitchInvertColorsActivity() {
        key = SwitchDisplayUtil.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED;
        lowestSdk = Build.VERSION_CODES.LOLLIPOP;
        nameLowestSdk = "5.0";
        shortcutId = "SwitchInvertColors";
        shortcutLongLabelId = R.string.title_invert_colors;
        iconId = R.mipmap.ic_launcher_invert_colors;
    }
}
