package com.meli.trainingml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.meli.trainingml.items.FindTask;
import com.meli.trainingml.items.Item;
import com.meli.trainingml.util.IObserver;
import com.meli.trainingml.util.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class ListItemsActivity extends Activity implements IObserver{
	
	private final static String LOGTAG = ListItemsActivity.class.getSimpleName();
	private final static int SEARCH_LIMIT = 15;
	private final static String SEARCH_KEY =  "query";
	private final static String RESPONSE =  "query";
	
	
	private String query;
    private ListView list;
    private ItemAdapter adapter = null;
    private List<Item> items;
    private ArrayList<String> results;
    private boolean loading = false;
    private int offset = 0;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_items);
		
		initComponents();

		if(savedInstanceState != null) {
			query = savedInstanceState.getString(SEARCH_KEY);
			results = savedInstanceState.getStringArrayList(RESPONSE);
			restoreResults();
		} else {
			query = getIntent().getExtras().getString(SEARCH_KEY);
			findProduct();
		}
		
		
	}
	
	private void restoreResults() {
		for(String response : results) {
			appendItemsToList(response);
		}
	}
	
	@Override
	protected void onSaveInstanceState (Bundle outState) {
		outState.putString(SEARCH_KEY, query);
		outState.putStringArrayList(RESPONSE, results);
	}
	
	private void initComponents() {
		Log.i(LOGTAG, "initComponents");
		items = new ArrayList<Item>();
		results = new ArrayList<String>();
		list = (ListView) findViewById(R.id.listItems);
        adapter = new ItemAdapter(this, items);
        list.setAdapter(adapter);
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                openDetail(items.get(position));
            }
        });
        
        list.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, 
                int visibleItemCount, int totalItemCount) {
                //Check if the last view is visible
            	int lastVisiblePosition = view.getLastVisiblePosition();
                if (lastVisiblePosition + 1 == totalItemCount) {
                    //load more content
                	if(!loading) {
                    	Log.i(LOGTAG, "load more content");
                    	findProduct();
                    	loading = true;
                	}
                	
                }
            }

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_IDLE:
                         // when list scrolling stops
                		Log.i(LOGTAG, "list scrolling stops");
                    break;
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    break;
                case OnScrollListener.SCROLL_STATE_FLING:
                    break;
                }
				
			}
        });
	}
	
	private void appendItemsToList(String response) {
		JSONObject jsonResponse;
		JSONArray jsonArray;
		JSONObject jsonItem;
		Item item;
		try {
			jsonResponse = new JSONObject(response);
			jsonArray = jsonResponse.getJSONArray("results");
			for(int i=0; i < jsonArray.length(); i++) {
				jsonItem = jsonArray.getJSONObject(i);
				
				String address = jsonItem.getJSONObject("address").getString("state_name");
				item = new Item(jsonItem.getString("title"), 
						jsonItem.getString("price"), 
						Utils.getDate(jsonItem.getString("stop_time")), 
						jsonItem.getString("condition"), 
						address);
				items.add(item);
				//jsonItem.getString("thumbnail")
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Log.i(LOGTAG,"Number of entries " + items.size());
        adapter.notifyDataSetChanged();
	}
	
	private void findProduct() {
		FindTask findTask = new FindTask(this);
		findTask.registerObserver(this);
		HashMap<String, String> params = new HashMap<String, String>(); 
        params.put("product",  query);
        params.put("limit",  String.valueOf(SEARCH_LIMIT));
        params.put("offset", String.valueOf(offset));
		findTask.execute(params);
	}
	
	@Override
	public void update(Object data) {
		String response = (String) data;
		results.add(response);
		offset+=SEARCH_LIMIT;
		appendItemsToList(response);
		loading = false;
	}
	
    private void openDetail(Item item) {


    }

}
