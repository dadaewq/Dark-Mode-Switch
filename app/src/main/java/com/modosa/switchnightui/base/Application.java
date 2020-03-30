package com.modosa.switchnightui.base;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.modosa.switchnightui.activity.MainActivity;
import com.modosa.switchnightui.util.SpUtil;

/**
 * @author dadaewq
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (new SpUtil(this).getBoolean(MainActivity.SP_KEY_ENABLE_BUG_REPORT, true)) {
            String scretCode = null;


            AppCenter.start(this, scretCode, Analytics.class, Crashes.class);
        }

    }

}
