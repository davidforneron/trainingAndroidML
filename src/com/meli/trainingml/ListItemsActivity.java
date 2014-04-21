package com.meli.trainingml;

import org.json.JSONArray;
import org.json.JSONObject;
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
    private JSONArray jsonArray;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_items);

		initComponents();
		
	}
	
	private void initComponents() {
		Log.i(LOGTAG, "initComponents");
		response = getIntent().getExtras().getString("response");
		list();
	}
	
	private void list() {
    	try {
    		JSONObject jsonObject = new JSONObject(response);
			jsonArray = jsonObject.getJSONArray("results");
            list = (ListView) findViewById(R.id.listItems);
            adapter = new ItemAdapter(this, jsonArray);
            list.setAdapter(adapter);
            Log.i(LOGTAG,"Number of entries " + jsonArray.length());
            
            // Click event for single list row
            list.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    openDetail(jsonArray.optJSONObject(position).toString());
                }
            });
          } catch (Exception e) {
            e.printStackTrace();
          }
    	
	}
	
    private void openDetail(String data) {
        Bundle bundle = new Bundle();
        bundle.putString("detail", data);
        //TODO: show detail

    }

}
