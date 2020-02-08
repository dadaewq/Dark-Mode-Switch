package com.modosa.switchnightui.receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.activity.SwitchUiActivity;

/**
 * Implementation of App Widget functionality.
 *
 * @author dadaewq
 */
public class SwitchUiAppWidget extends AppWidgetProvider {

    private static final String WIDGET_ACTION = "com.modosa.switchnightui.appwidget.switchui";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_switchui);

        views.setOnClickPendingIntent(R.id.img_ui, getPendingIntent(context));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static PendingIntent getPendingIntent(Context context) {

        Intent intent = new Intent()
                .setClass(context, SwitchUiAppWidget.class)
                .setAction(WIDGET_ACTION)
                .setData(Uri.parse("id:" + R.id.img_ui));

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

        Intent intent = new Intent(new Intent(Intent.ACTION_VIEW))
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setClass(context.getApplicationContext(), SwitchUiActivity.class);

        //设置data域的时候，把控件id一起设置进去，
        // 因为在绑定的时候，是将同一个id绑定在一起的，所以哪个控件点击，发送的intent中data中的id就是哪个控件的id

        context.startActivity(intent);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (WIDGET_ACTION.equals(intent.getAction())) {
            startActivity(context);
        }
    }

}

