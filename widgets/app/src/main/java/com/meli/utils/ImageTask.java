package com.meli.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.meli.utils.CacheManager;
import com.meli.utils.HttpClientCustom;
import com.meli.utils.ImageDownloader;

public class ImageTask implements Runnable{


    private String imageUrl;
    private String id;
    private Bitmap imageBitmap;
   
    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }


    public ImageTask(String id, String imageUrl) {
        this.imageUrl = imageUrl;
        this.id = id;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getImageUrl() {
        return imageUrl;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Override
    public void run() {
        byte[] imageBuffer = HttpClientCustom.requestStream(imageUrl);
        //TODO: be able to set options
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        imageBitmap = BitmapFactory.decodeByteArray(imageBuffer, 0, imageBuffer.length, null);
        CacheManager.getInstance().set(id, imageBuffer);
        handleState(ImageDownloader.DOWNLOAD_COMPLETE);
    }

    // Delegates handling the current state of the task to the PhotoManager object
    void handleState(int state) {
        ImageDownloader.getInstance().handleState(this, state);
    }

    public void recycle() {
        // Releases references 
        imageBitmap = null;
        imageUrl = null;
        id = null;
    }

}
