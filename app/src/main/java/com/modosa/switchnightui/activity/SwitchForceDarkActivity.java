package com.modosa.switchnightui.activity;


import android.os.Build;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.SwitchForceDarkUtil;

/**
 * @author dadaewq
 */
public class SwitchForceDarkActivity extends AbstractSwitchActivity {

    public SwitchForceDarkActivity() {
        shortcutId = "SwitchForceDark";
        shortcutLongLabelId = R.string.name_switch_force_dark;
        iconId = R.drawable.ic_qs_force_dark;
    }


    @Override
    void switchMethod() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            new SwitchForceDarkUtil(this).switchForceDark();
        } else {
            showTipNeedSdk(R.string.title_force_dark, "10");
        }
    }
}
