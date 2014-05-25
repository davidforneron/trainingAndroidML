package com.meli.widgets;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.meli.utils.FindTask;
import com.meli.utils.IObserver;
import com.meli.utils.MeliService;
import com.meli.utils.SpinnerListener;
import com.meli.utils.WidgetsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryActivity extends Activity {

    private final static String TAG = MainActivity.class.getSimpleName();

    int mAppWidgetId;
    private static List <String> categoriesNames;
    private static List <String> categoriesId;
    private static int selectedCategory = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        detectAction();
    }

    private void detectAction() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras == null) {
            finish();
            return;
        }
        String action = intent.getAction();
        if(action == FeaturesItemsWidgetProvider.ACTION_CATEGORIES) {
            loadCategories();
        } else {
            finish();
        }
    }

    private void loadCategories() {
        if(categoriesNames == null) {
            categoriesNames = new ArrayList<String>();
            categoriesId = new ArrayList<String>();
            FindTask findTask = new FindTask(this, MeliService.SEARCH_CATEGORIES_END_POINT, new IObserver() {
                @Override
                public void update(Object data) {
                    parseCategoryResponse((String) data);
                }
            });
            findTask.execute(new HashMap<String, String>());
        } else {
            createDialog();
        }
    }


    private void parseCategoryResponse(String response) {
        JSONObject jsonResponse;
        JSONArray jsonArray;
        JSONObject jsonCategory;
        try {
            jsonArray = new JSONArray(response);
            for(int i=0; i < jsonArray.length(); i++) {
                jsonCategory = jsonArray.getJSONObject(i);
                categoriesNames.add(jsonCategory.getString("name"));
                categoriesId.add(jsonCategory.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Number of entries " + categoriesNames.size());
        createDialog();
    }


    public void createDialog() {
        String[] stringArray = categoriesNames.toArray(new String[categoriesNames.size()]);
        WidgetsUtils.createSpinnerDialog(this, "Categories", stringArray, new SpinnerListener() {
            @Override
            public void update(int index) {
                onCategorySelected(index);
            }
        });
    }

    public void onCategorySelected(int index) {
        Intent _intent = getIntent();
        Bundle extras = _intent.getExtras();

        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        Intent intent = new Intent(this,FeaturesItemsWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.setAction(FeaturesItemsWidgetProvider.ACTION_CATEGORIES);
        intent.putExtra("value", index);
        selectedCategory = index;
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[mAppWidgetId]);
        sendBroadcast(intent);
        finish();
    }

    public static List<String> getCategoriesNames() {
        return categoriesNames;
    }

    public static void setCategoriesNames(List<String> categoriesNames) {
        CategoryActivity.categoriesNames = categoriesNames;
    }

    public static int getSelectedCategoryId() {
        return selectedCategory;
    }

    public static String getSelectedCategory() {
        return categoriesId.get(selectedCategory);
    }

    public static void setSelectedCategory(int selectedCategory) {
        CategoryActivity.selectedCategory = selectedCategory;
    }
}
