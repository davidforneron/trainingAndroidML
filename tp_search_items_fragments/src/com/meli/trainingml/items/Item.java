package com.meli.trainingml.items;

import java.io.Serializable;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Item implements Serializable{
    String id;
    String title;
    String price;
    Date stopTime;
    String condition;
    String address;
    transient Bitmap thumbnail;

    public Item(String id, String title, String price, Date stopTime, String condition, String address) {
        super();
        this.id = id;
        this.title = title;
        this.price = price;
        this.stopTime = stopTime;
        this.condition = condition;
        this.address = address;
    }

    public Item(String id) {
        super();
        this.id = id;
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
    
    public void setThumbnail(byte[] thumbnail) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        this.thumbnail = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length, null);
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object object) {
        if(object != null && object instanceof Item) {
            return this.id.equals(((Item)object).id);
        }
        return false;
    }

    public String getId() {
        return id;
    }

}
