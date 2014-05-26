package com.meli.widgets.remoteview;

/**
 * Created by DAVID on 25/05/2014.
 */

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.meli.items.Item;
import com.meli.widgets.R;
import com.meli.widgets.SearchActivity;

import java.util.List;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 *
 */
public class ItemListProvider implements RemoteViewsService.RemoteViewsFactory {
    private Context context = null;
    private int appWidgetId;
    private List<Item> itemList;
    public ItemListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        itemList = SearchActivity.getItems();
    }

    /*
    *Similar to getView of Adapter where instead of View
    *we return RemoteViews
    *
    */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.list_item_row);
        Item item =  itemList.get(position);
        remoteView.setTextViewText(R.id.textTitle, item.getTitle());
        remoteView.setTextViewText(R.id.textPrice, item.getPrice());
        return remoteView;
    }


    @Override
    public int getCount() {
        return itemList != null ? itemList.size() : 0;
    }


    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}