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
import com.meli.trainingml.items.Picture;
import com.meli.trainingml.util.CacheManager;
import com.meli.trainingml.util.IObserver;
import com.meli.trainingml.util.ImageDownloader;
import com.meli.trainingml.util.MeliService;
import com.meli.trainingml.util.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDetailActivity  extends Activity implements IObserver{
	
	
	TextView textTitle;
	TextView textPrice;
	TextView textStopTime;
	TextView textAddress;
	TextView textCondition;
	ImageView imgItem;
	Item item;
	Picture bigPicture;
	String response; 
	private final static String LOGTAG = ItemDetailActivity.class.getSimpleName();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_detail);
		
		if(savedInstanceState != null) {
		    item = (Item) savedInstanceState.getSerializable("item");
		    response = savedInstanceState.getString("detail");
		    initComponents();
		    processPictures();
		} else {
			item = (Item) getIntent().getExtras().getSerializable("item");
			initComponents();
			findDetail();
		}
	}
	
	@Override
	protected void onSaveInstanceState (Bundle outState) {
	    outState.putSerializable("item", item);
	    outState.putString("detail", response);
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
    
	private void initComponents() {
		Log.i(LOGTAG, "initComponents");
		
		textTitle = (TextView) findViewById(R.id.textTitle);
		textPrice = (TextView) findViewById(R.id.textPrice);
		textStopTime = (TextView) findViewById(R.id.textStopTime);
		textAddress = (TextView) findViewById(R.id.textAddress);
		textCondition = (TextView) findViewById(R.id.textCondition);
		imgItem = (ImageView) findViewById(R.id.imgItem);
		
		textTitle.setText(item.getTitle());
		textPrice.setText(item.getPrice());
		textStopTime.setText(item.getStopTime().toString());
		textAddress.setText(item.getAddress());
		textCondition.setText(item.getCondition());
		
		
	}
	
	private void findDetail() {
		FindTask findTask = new FindTask(this, MeliService.ITEMS_END_POINT + "/" + item.getId());
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
