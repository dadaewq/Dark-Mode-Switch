package com.modosa.switchnightui.receiver.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Abstract of SwitchWidget functionality.
 *
 * @author dadaewq
 */
abstract public class AbstractSwitchWidget extends AppWidgetProvider {

    static int imgId;
    static int layoutId;
    static Class myClass;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);

        views.setOnClickPendingIntent(
                imgId,
                PendingIntent.getActivity(context, imgId, new Intent(context, myClass), 0)
        );

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

}

