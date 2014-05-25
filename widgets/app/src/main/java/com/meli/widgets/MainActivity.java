package com.meli.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    int mAppWidgetId;
    List <String>categories;
    Map<String, String> categoriesMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
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
        if(action == "CATEGORIES") {
            loadCategories();
        } else {
            finish();
        }

    }

    private void loadCategories() {
        if(categories == null) {
            categories = new ArrayList<String>();
            categoriesMap = new HashMap<String, String>();
            FindTask findTask = new FindTask(this, MeliService.SEARCH_CATEGORIES_END_POINT, new IObserver() {
                @Override
                public void update(Object data) {
                    parseResponse((String) data);
                }
            });
            findTask.execute(new HashMap<String, String>());
        } else {
            createDialog();
        }
    }

    private void parseResponse(String response) {
        JSONObject jsonResponse;
        JSONArray jsonArray;
        JSONObject jsonCategory;
        try {
            jsonArray = new JSONArray(response);
            for(int i=0; i < jsonArray.length(); i++) {
                jsonCategory = jsonArray.getJSONObject(i);
                categories.add(jsonCategory.getString("name"));
                categoriesMap.put(jsonCategory.getString("id"), jsonCategory.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Number of entries " + categories.size());
        createDialog();
    }


    public void createDialog() {
        String[] stringArray = categories.toArray(new String[categories.size()]);
        WidgetsUtils.createSpinnerDialog(this, "Categories", stringArray, new SpinnerListener() {
            @Override
            public void update(int index) {
                onCategorySelected(index);
            }
        });
    }

//    public void _onCategorySelected(int index) {
//        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//
//        if (extras != null) {
//            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
//        }
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
//
//        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget_main);
//        views.setTextViewText(R.id.textCategories, types[index]);
//
//        appWidgetManager.updateAppWidget(mAppWidgetId, views);
//
//        Intent resultValue = new Intent();
//        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
//        setResult(RESULT_OK, resultValue);
//        finish();
//
//    }


    public void onCategorySelected(int index) {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        Intent _intent = new Intent(this,FeaturesItemsWidgetProvider.class);
        _intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        _intent.putExtra("INDEX", index);
        _intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[mAppWidgetId]);
        sendBroadcast(_intent);
        finish();
    }



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

	}

}
