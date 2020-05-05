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
        shortcutLongLabelId = R.string.title_force_dark;
        iconId = R.mipmap.ic_launcher_force_dark;
    }


    @Override
    void switchMethod() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            new SwitchForceDarkUtil(this).switchForceDark();
        } else {
            showSwitchTipNeedSdk(R.string.title_force_dark, "10");
        }
    }
}
