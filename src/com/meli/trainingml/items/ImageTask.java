package com.meli.trainingml.items;

import com.meli.trainingml.util.HttpClientCustom;
import com.meli.trainingml.util.ImageDownloader;

public class ImageTask implements Runnable{


    private String imageUrl;
    private String id;
    private byte[] imageBuffer ;

    public byte[] getImageBuffer() {
        return imageBuffer;
    }


    public void setImageBuffer(byte[] imageBuffer) {
        this.imageBuffer = imageBuffer;
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
        imageBuffer = HttpClientCustom.requestStream(imageUrl);
        handleState(ImageDownloader.DOWNLOAD_COMPLETE);
    }

    // Delegates handling the current state of the task to the PhotoManager object
    void handleState(int state) {
        ImageDownloader.getInstance().handleState(this, state);
    }

    public void recycle() {
        // Releases references 
        imageBuffer = null;
        imageUrl = null;
        id = null;
    }

}
