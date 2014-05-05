package com.meli.trainingml;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.meli.trainingml.R;
import com.meli.trainingml.items.FindTask;
import com.meli.trainingml.items.ImageTask;
import com.meli.trainingml.items.Item;
import com.meli.trainingml.items.Picture;
import com.meli.trainingml.util.CacheManager;
import com.meli.trainingml.util.IObserver;
import com.meli.trainingml.util.ImageDownloader;
import com.meli.trainingml.util.MeliService;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDetailFragment extends Fragment  implements IObserver{

    private final static String LOGTAG = ItemDetailFragment.class.getSimpleName();
    
    TextView textTitle;
    TextView textPrice;
    TextView textStopTime;
    TextView textAddress;
    TextView textCondition;
    ImageView imgItem;
    Item item;
    Picture bigPicture;
    String response; 
    
    private View view;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        Log.d(LOGTAG, "onCreateView");
        if(this.view != null) {
            // Remove the view from the parent
            ((ViewGroup)this.view.getParent()).removeView(this.view);
        } else {
            this.view = inflater.inflate(R.layout.fragment_item_detail, container, false);    
        }
        return this.view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(item == null) {
            if(savedInstanceState != null) {
                item = (Item) savedInstanceState.getSerializable("item");
                response = savedInstanceState.getString("detail");
                initComponents();
                processPictures();
            } else if(getArguments() != null){
                item = (Item) getArguments().getSerializable("item");
                initComponents();
                findDetail();
            }
        }
    }
    
    @Override
    public void onSaveInstanceState (Bundle outState) {
        outState.putSerializable("item", item);
        outState.putString("detail", response);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        ImageDownloader.getInstance().registerObserver(this);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        ImageDownloader.getInstance().removeObserver(this);
    }
    
    public void updateItemView(Item item) {
        this.item = item;
        initComponents();
        findDetail();
    }
    
    private void initComponents() {
        Log.i(LOGTAG, "initComponents");
        
        textTitle = (TextView) this.view.findViewById(R.id.textTitle);
        textPrice = (TextView) this.view.findViewById(R.id.textPrice);
        textStopTime = (TextView) this.view.findViewById(R.id.textStopTime);
        textAddress = (TextView) this.view.findViewById(R.id.textAddress);
        textCondition = (TextView) this.view.findViewById(R.id.textCondition);
        imgItem = (ImageView) this.view.findViewById(R.id.imgItem);
        
        textTitle.setText(item.getTitle());
        textPrice.setText(item.getPrice());
        textStopTime.setText(item.getStopTime().toString());
        textAddress.setText(item.getAddress());
        textCondition.setText(item.getCondition());
        
        
    }
    
    private void findDetail() {
        FindTask findTask = new FindTask(getActivity(), MeliService.ITEMS_END_POINT + "/" + item.getId());
        findTask.registerObserver(this);
        HashMap<String, String> params = new HashMap<String, String>(); 
        findTask.execute(params);
    }

    @Override
    public void update(Object data) {
        if(data instanceof String) {
            response = (String) data;
            processPictures();
        } else if(data instanceof ImageTask) {
            ImageTask imageTask = (ImageTask)data;
            if(imageTask.getId() == bigPicture.getId()) {
                imgItem.setImageBitmap(imageTask.getImageBitmap());
            }
        }
    }
    
    public void processPictures() {
        JSONObject jsonResponse;
        JSONArray jsonArray;
        JSONObject jsonItem;
        Picture picture;
        try {
            jsonResponse = new JSONObject(response);
            jsonArray = jsonResponse.getJSONArray("pictures");
            for(int i=0; i < jsonArray.length(); i++) {
                jsonItem = jsonArray.getJSONObject(i);
                picture = new Picture(jsonItem.getString("id"), 
                        jsonItem.getString("url"),
                        jsonItem.getString("secure_url"),
                        jsonItem.getString("size"),
                        jsonItem.getString("max_size"));
                if(bigPicture == null || picture.compareTo(bigPicture) > 0) {
                    bigPicture = picture;
                }
            }
            
            Bitmap bufferedImage = CacheManager.getInstance().getImage(bigPicture.getId());
            if(bufferedImage == null) {
                ImageDownloader.getInstance().startDownload(bigPicture.getId(), bigPicture.getUrl());    
            } else {
                imgItem.setImageBitmap(bufferedImage);
                Log.d(LOGTAG, "image retrieved from cache");
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }


}
