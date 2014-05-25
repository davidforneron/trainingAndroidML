package com.meli.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class FeaturesItemsWidgetProvider extends AppWidgetProvider {


    public static final String ACTION_CATEGORIES = "CATEGORIES";
    public static final String ACTION_HOT = "HOT";
    public static final String ACTION_TREND = "TREND";
    public static final String ACTION_SEARCH = "SEARCH";
    public static final String ACTION_FEATURES = "FEATURES";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent();
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            ComponentName category = new ComponentName("com.meli.widgets", "com.meli.widgets.CategoryActivity");
            ComponentName search = new ComponentName("com.meli.widgets", "com.meli.widgets.SearchActivity");
            // Get the layout for the App Widget and attach the on-click listeners
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);

            intent.setComponent(search);
            intent.setAction(ACTION_HOT);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.btnHot, pendingIntent);

            intent.setAction(ACTION_TREND);
            intent.setComponent(search);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.btnTrend, pendingIntent);

            intent.setAction(ACTION_FEATURES);
            intent.setComponent(search);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.btnFeatures, pendingIntent);

            intent.setAction(ACTION_SEARCH);
            intent.setComponent(search);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.btnSearch, pendingIntent);

            intent.setAction(ACTION_CATEGORIES);
            intent.setComponent(category);
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
