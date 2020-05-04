package com.modosa.switchnightui.receiver.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.activity.SwitchBatterySaverActivity;
import com.modosa.switchnightui.util.OpUtil;

/**
 * Implementation of SwitchBatterySaverWidget functionality.
 *
 * @author dadaewq
 */
public class SwitchBatterySaverWidget extends AppWidgetProvider {

    private static final String WIDGET_ACTION = "com.modosa.switchnightui.appwidget.switch_battery_saver";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_switch_battery_saver);

        views.setOnClickPendingIntent(R.id.img_switch_battery_saver, getPendingIntent(context));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static PendingIntent getPendingIntent(Context context) {

        Intent intent = new Intent()
                .setClass(context, SwitchForceDarkWidget.class)
                .setAction(WIDGET_ACTION)
                .setData(Uri.parse("id:" + R.id.img_switch_battery_saver));
//        //设置data域的时候，把控件id一起设置进去，
//        // 因为在绑定的时候，是将同一个id绑定在一起的，所以哪个控件点击，发送的intent中data中的id就是哪个控件的id
//
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void startActivity(Context context) {
        OpUtil.startMyClass(context, SwitchBatterySaverActivity.class);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (WIDGET_ACTION.equals(intent.getAction())) {
            startActivity(context);
        }
    }

}

