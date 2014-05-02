package com.meli.trainingml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.meli.trainingml.items.FindTask;
import com.meli.trainingml.items.ImageTask;
import com.meli.trainingml.items.Item;
import com.meli.trainingml.items.ItemAdapter;
import com.meli.trainingml.util.CacheManager;
import com.meli.trainingml.util.IObserver;
import com.meli.trainingml.util.ImageDownloader;
import com.meli.trainingml.util.MeliService;
import com.meli.trainingml.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
    public  final static String SEARCH_KEY =  "query";
    private final static String RESPONSE =  "response";


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
    
    @Override
    protected void onResume() {
        super.onResume();
        ImageDownloader.getInstance().registerObserver(this);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        ImageDownloader.getInstance().removeObserver(this);
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
        ImageDownloader.getInstance().registerObserver(this);
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
                if (lastVisiblePosition + 1 == totalItemCount && totalItemCount > 0) {
                    //load more content
                    if(!loading) {
                        Log.i(LOGTAG, "load more content");
                        findProduct();
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
                String id = jsonItem.getString("id");
                item = new Item(id, jsonItem.getString("title"), 
                        jsonItem.getString("price"), 
                        Utils.getDate(jsonItem.getString("stop_time")), 
                        jsonItem.getString("condition"), 
                        address);
                items.add(item);
                Bitmap bufferedThumbnail = CacheManager.getInstance().getImage(id);
                if(bufferedThumbnail == null) {
                    ImageDownloader.getInstance().startDownload(id, jsonItem.getString("thumbnail"));    
                } else {
                    item.setThumbnail(bufferedThumbnail);
                    Log.d(LOGTAG, "image retrieved from cache");
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i(LOGTAG,"Number of entries " + items.size());
        adapter.notifyDataSetChanged();
    }

    private void findProduct() {
        loading = true;
        FindTask findTask = new FindTask(this, MeliService.SEARCH_ITEMS_END_POINT);
        findTask.registerObserver(this);
        HashMap<String, String> params = new HashMap<String, String>(); 
        params.put("q",  query);
        params.put("limit",  String.valueOf(SEARCH_LIMIT));
        params.put("offset", String.valueOf(offset));
        findTask.execute(params);
    }

    @Override
    public void update(Object data) {
        if(data instanceof String) {
            String response = (String) data;
            results.add(response);
            offset+=SEARCH_LIMIT;
            appendItemsToList(response);
            loading = false;
        } else if(data instanceof ImageTask) {
            ImageTask imageTask = (ImageTask)data;
            Item item = new Item(imageTask.getId());
            int index = items.indexOf(item);
            if(index != -1) {
                item = items.get(index);
                item.setThumbnail(imageTask.getImageBitmap());
                adapter.notifyDataSetChanged();
            } else {
                Log.e(LOGTAG, "item not found");
            }
        }
    }

    private void openDetail(Item item) {
        Bundle options = new Bundle();
        options.putSerializable("item", item);
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(options);
        startActivity(intent);
    }

}
