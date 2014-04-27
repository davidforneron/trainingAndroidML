package com.meli.trainingml.items;

import android.graphics.Bitmap;

public class Item {
	String title;
	String price;
	Bitmap thumbnail;
     
	public Item(String title, String price) {
		super();
		this.title = title;
		this.price = price;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public Bitmap getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(Bitmap thumbnail) {
		this.thumbnail = thumbnail;
	}
}
