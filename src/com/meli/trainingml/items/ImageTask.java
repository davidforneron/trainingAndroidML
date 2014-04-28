package com.meli.trainingml.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.meli.trainingml.util.HttpClientCustom;
import com.meli.trainingml.util.ImageDownloader;

public class ImageTask  implements Runnable{

	
	private String imageUrl;
	private String id;
	private Bitmap bitmap;
	
	public Bitmap getBitmap() {
		return bitmap;
	}


	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
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
		 BitmapFactory.Options options = new BitmapFactory.Options();
		 options.inJustDecodeBounds = true;
		 bitmap = BitmapFactory.decodeByteArray(imageBuffer, 0, imageBuffer.length, null);
		 handleState(ImageDownloader.DOWNLOAD_COMPLETE);
	}

    // Delegates handling the current state of the task to the PhotoManager object
    void handleState(int state) {
    	 ImageDownloader.getInstance().handleState(this, state);
    }

	public void recycle() {
	}

}
