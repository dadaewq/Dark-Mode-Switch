package com.modosa.switchnightui.activity;


import android.os.Build;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.SwitchDisplayUtil;

/**
 * @author dadaewq
 */
public class SwitchGrayScaleActivity extends AbstractSwitchDisplayActivity {


    public SwitchGrayScaleActivity() {
        key = SwitchDisplayUtil.GRAYSCALE;
        lowestSdk = Build.VERSION_CODES.LOLLIPOP;
        nameLowestSdk = "5.0";
        shortcutId = "SwitchGrayScale";
        shortcutLongLabelId = R.string.title_gray_scale;
        iconId = R.drawable.ic_qs_gray_scale_48dp;
    }
}
