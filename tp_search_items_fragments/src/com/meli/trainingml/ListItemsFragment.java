package com.meli.trainingml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.meli.trainingml.R;
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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;


public class ListItemsFragment extends Fragment implements IObserver{
    
    private final static String LOGTAG = ListItemsFragment.class.getSimpleName();
    private final static int SEARCH_LIMIT = 15;
    public  final static String SEARCH_KEY =  "query";
    private final static String RESPONSE =  "response";
    
    //start Search section
    EditText edit;
    ImageButton search;
    //end Search section

    //start list section
    private String query;
    private ListView list;
    private ItemAdapter adapter = null;
    private List<Item> items;
    private ArrayList<String> results;
    private boolean loading = false;
    private int offset = 0;
    private ListSelectedListener listSelectedListener;
    //end list section
    
    private View view;
    
    // Container Activity must implement this interface
    public interface ListSelectedListener {
        public void onSearchItemSelected(Item item);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOGTAG, "onActivityCreated");
        if(query == null) {
            initSearchComponents();
            initListComponents();
            //Its always null
            if(savedInstanceState != null) {
                query = savedInstanceState.getString(SEARCH_KEY);
                results = savedInstanceState.getStringArrayList(RESPONSE);
                restoreResults();
            }
        }
    }
    
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(LOGTAG, "onViewStateRestored");
        //Its always null
        if(savedInstanceState != null) {
            query = savedInstanceState.getString(SEARCH_KEY);
            results = savedInstanceState.getStringArrayList(RESPONSE);
            restoreResults();
        }
    }
    
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        Log.d(LOGTAG, "onCreateView");
        if(this.view != null) {
            // Remove the view from the parent
            ((ViewGroup)this.view.getParent()).removeView(this.view);
        } else {
            this.view = inflater.inflate(R.layout.fragment_list_items, container, false);    
        }
        return this.view;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOGTAG, "onAttach");
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listSelectedListener = (ListSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ListSelectedListener");
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOGTAG, "onResume");
        ImageDownloader.getInstance().registerObserver(this);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOGTAG, "onPause");
        ImageDownloader.getInstance().removeObserver(this);
    }
    
    private void restoreResults() {
        for(String response : results) {
            appendItemsToList(response);
        }
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOGTAG, "onSaveInstanceState");
        //The state is not saved
        outState.putString(SEARCH_KEY, query);
        outState.putStringArrayList(RESPONSE, results);
    }
    
    
    private void initSearchComponents() {
        Log.i(LOGTAG, "initSearchComponents");
        
        edit = (EditText) this.view.findViewById(R.id.editTextProduct);
        search = (ImageButton) this.view.findViewById(R.id.btnSearch);
        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query = edit.getText().toString();
                findProduct();
            }
        });
    }
    
    
    private void initListComponents() {
        Log.i(LOGTAG, "initListComponents");
        items = new ArrayList<Item>();
        results = new ArrayList<String>();
        list = (ListView) this.view.findViewById(R.id.listItems);
        adapter = new ItemAdapter(getActivity(), items);
        list.setAdapter(adapter);
        ImageDownloader.getInstance().registerObserver(this);
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                listSelectedListener.onSearchItemSelected(items.get(position));
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
                        moreItems();
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
                    //Log.d(LOGTAG, "image retrieved from cache");
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
        offset = 0;
        results.clear();
        items.clear();
        moreItems();
    }
    
    private void moreItems() {
        loading = true;
        FindTask findTask = new FindTask(getActivity(), MeliService.SEARCH_ITEMS_END_POINT);
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

}
