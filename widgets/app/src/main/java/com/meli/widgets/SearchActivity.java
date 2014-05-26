package com.meli.widgets;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.meli.items.Item;
import com.meli.utils.CacheManager;
import com.meli.utils.FindTask;
import com.meli.utils.IObserver;
import com.meli.utils.ImageDownloader;
import com.meli.utils.ImageTask;
import com.meli.utils.MeliService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends Activity {

    private final static String TAG = SearchActivity.class.getSimpleName();

    private final static int SEARCH_LIMIT = 5;
    private static int offset = 0;

    int mAppWidgetId;

    private static List<Item> items;
    private static ArrayList<String> results;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        detectAction();
        items = new ArrayList<Item>();
        results = new ArrayList<String>();
    }


    private void detectAction() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras == null) {
            finish();
            return;
        }
        mAppWidgetId = intent.getIntExtra(FeaturesItemsWidgetProvider.APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        String action = intent.getAction();
        if(action == FeaturesItemsWidgetProvider.ACTION_HOT) {
            loadHotItems();
        } else {
            finish();
        }

    }

    private void loadHotItems() {
        if(CategoryActivity.getSelectedCategoryId() != -1) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("category",  CategoryActivity.getSelectedCategory());
            params.put("limit",  String.valueOf(SEARCH_LIMIT));
            params.put("offset", String.valueOf(offset));
            FindTask findTask = new FindTask(this, MeliService.SEARCH_HOT_END_POINT, new IObserver() {
                @Override
                public void update(Object data) {
                    parseItemResponse((String) data);
                }
            });
            findTask.execute(params);
        } else {
            //TODO: call Category activity
        }
    }

    private void parseItemResponse(String response) {
        JSONObject jsonResponse;
        JSONArray jsonArray;
        JSONObject jsonItem;
        Item item;
        try {
            results.add(response);
            jsonResponse = new JSONObject(response);
            jsonArray = jsonResponse.getJSONArray("results");
            for(int i=0; i < jsonArray.length(); i++) {
                jsonItem = jsonArray.getJSONObject(i);

                String id = jsonItem.getString("id");
                item = new Item(id, jsonItem.getString("title"),
                        jsonItem.getString("price"),
                        jsonItem.getString("listing_mode"),
                        jsonItem.getInt("sold_quantity"));
                items.add(item);
                Bitmap bufferedThumbnail = CacheManager.getInstance().getImage(id);
                if(bufferedThumbnail == null) {
                    ImageDownloader.getInstance().startDownload(id, jsonItem.getString("thumbnail"), new IObserver() {
                        @Override
                        public void update(Object data) {
                            ImageTask imageTask = (ImageTask)data;
                            Item item = new Item(imageTask.getId());
                            int index = items.indexOf(item);
                            if(index != -1) {
                                item = items.get(index);
                                item.setThumbnail(imageTask.getImageBitmap());
                                //TODO: notify widget that image was downloaded;
                            } else {
                                Log.e(TAG, "item not found");
                            }
                        }
                    });
                } else {
                    item.setThumbnail(bufferedThumbnail);
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i(TAG, "Number of entries " + items.size());
        updateViewContent();
    }

    private void updateViewContent() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), FeaturesItemsWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewItems);
//        Intent intent = new Intent(this,FeaturesItemsWidgetProvider.class);
//        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{mAppWidgetId});
//        sendBroadcast(intent);
        finish();
    }

    public static List<Item> getItems() {
        return items;
    }

    public static void setItems(List<Item> items) {
        SearchActivity.items = items;
    }
}
