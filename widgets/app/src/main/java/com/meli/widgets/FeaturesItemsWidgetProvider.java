package com.meli.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class FeaturesItemsWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.meli.widgets", "com.meli.widgets.MainActivity"));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            // Get the layout for the App Widget and attach the on-click listeners
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);
            intent.setAction("HOT");
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.btnHot, pendingIntent);

            intent.setAction("TREND");
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.btnTrend, pendingIntent);

            intent.setAction("FEATURES");
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.btnFeatures, pendingIntent);

            intent.setAction("SEARCH");
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.btnSearch, pendingIntent);

            intent.setAction("CATEGORIES");
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.textCategories, pendingIntent);

            updateWidgetMain(views);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    public void updateWidgetMain(RemoteViews views) {
        views.setTextViewText(R.id.textCategories,"");
    }
}
