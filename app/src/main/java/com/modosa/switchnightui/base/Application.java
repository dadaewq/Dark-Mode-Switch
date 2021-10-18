package com.modosa.switchnightui.base;

import android.os.Build;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.modosa.switchnightui.util.OpUtil;
import com.modosa.switchnightui.util.SpUtil;

/**
 * @author dadaewq
 */
@SuppressWarnings("WeakerAccess")
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (new SpUtil(this).getBoolean(OpUtil.SP_KEY_ENABLE_BUG_REPORT, true)) {
            String scretCode = null;
            //TODO


            AppCenter.start(this, scretCode, Analytics.class, Crashes.class);
        }

    }

}
