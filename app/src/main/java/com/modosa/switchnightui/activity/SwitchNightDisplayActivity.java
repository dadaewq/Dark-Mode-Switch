package com.modosa.switchnightui.activity;


import android.os.Build;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.SwitchDisplayUtil;

/**
 * @author dadaewq
 */
public class SwitchNightDisplayActivity extends AbstractSwitchDisplayActivity {

    public SwitchNightDisplayActivity() {
        key = SwitchDisplayUtil.NIGHT_DISPLAY_ACTIVATED;
        lowestSdk = Build.VERSION_CODES.LOLLIPOP;
        nameLowestSdk = "5.0";
        shortcutId = "SwitchNightDisplay";
        shortcutLongLabelId = R.string.title_night_display;
        iconId = R.drawable.ic_qs_night_display;
    }
}
