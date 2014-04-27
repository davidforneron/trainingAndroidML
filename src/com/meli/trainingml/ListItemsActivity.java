package com.meli.trainingml;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.meli.trainingml.items.Item;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class ListItemsActivity extends Activity {
	
	private final static String LOGTAG = ListItemsActivity.class.getSimpleName();
	private String response;
    private ListView list;
    private ItemAdapter adapter = null;
    private List<Item> items;
    private final static String KEY_LIST =  "response";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_items);

		if(savedInstanceState != null) {
			response = savedInstanceState.getString(KEY_LIST);
		} else {
			response = getIntent().getExtras().getString(KEY_LIST);
		}
		initComponents();
		
	}
	
	@Override
	protected void onSaveInstanceState (Bundle outState) {
		outState.putString(KEY_LIST, response);
	}
	
	private void initComponents() {
		Log.i(LOGTAG, "initComponents");
		listResults();
	}
	
	private void creteListItem() {
		JSONObject jsonResponse;
		JSONArray jsonArray;
		JSONObject jsonItem;
		Item item;
		items = new ArrayList<Item>();
		try {
			jsonResponse = new JSONObject(response);
			jsonArray = jsonResponse.getJSONArray("results");
			for(int i=0; i < jsonArray.length(); i++) {
				jsonItem = jsonArray.getJSONObject(i);
				item = new Item(jsonItem.getString("title"), jsonItem.getString("price"));
				items.add(item);
				//jsonItem.getString("thumbnail")
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void listResults() {
    	try {
    		creteListItem();
            list = (ListView) findViewById(R.id.listItems);
            adapter = new ItemAdapter(this, items);
            list.setAdapter(adapter);
            Log.i(LOGTAG,"Number of entries " + items.size());
            
            // Click event for single list row
            list.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    openDetail(items.get(position));
                }
            });
          } catch (Exception e) {
            e.printStackTrace();
          }
    	
	}
	
    private void openDetail(Item item) {


    }

}
