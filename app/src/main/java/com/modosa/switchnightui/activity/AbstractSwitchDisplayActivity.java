package com.modosa.switchnightui.activity;


import android.os.Build;

import com.modosa.switchnightui.util.SwitchDisplayUtil;

/**
 * @author dadaewq
 */
abstract public class AbstractSwitchDisplayActivity extends AbstractSwitchActivity {
    String key;
    int lowestSdk;
    String nameLowestSdk;

    @Override
    void switchMethod() {
        if (Build.VERSION.SDK_INT >= lowestSdk) {
            new SwitchDisplayUtil(this).switchDisplayKeyWithResult(key, getString(shortcutLongLabelId));
        } else {
            showSwitchTipNeedSdk(shortcutLongLabelId, nameLowestSdk);
        }
    }
}
