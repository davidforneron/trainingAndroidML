package com.meli.trainingml.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class CacheManager {

    private final static String LOGTAG = CacheManager.class.getSimpleName();

    private final static int CACHE_MAX_SIZE = 20;

    private static CacheManager mInstance;
    
    private LinkedHashMap<String, byte[]> cache;
    
    private String destFolder;


    static  {
        // Creates a single static instance of PhotoManager
        mInstance = new CacheManager();
    }

    @SuppressWarnings("serial")
    private CacheManager() {
        cache = new LinkedHashMap<String, byte[]>(CACHE_MAX_SIZE, 0.75f, true) { 
            protected boolean removeEldestEntry(
                    Map.Entry<String, byte[]> eldest) {
                // Remove the eldest entry if the size of the cache exceeds the maximum size
                return size() > CACHE_MAX_SIZE;
            }
        };
    }

    /**
     * Returns the CacheManager object
     * @return The global CacheManager object
     */
    public static CacheManager getInstance() {
        return mInstance;
    }
    
    public void setDestinationFolder(String destinationFolder) {
        destFolder = destinationFolder;
    }
    
    public Bitmap getImage(String key) {
        Bitmap value = null;
        byte[] buffer = get(key);
        if(buffer != null) {
            value = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, null);
        }
        return value;
    }

    public byte[] get(String key) {
        byte[] value = cache.get(key);
        if(value != null) {
            Log.d(LOGTAG, "item found in memory cache");
            return value;
        }
        if(destFolder != null) {
            try {
                FileInputStream is = new FileInputStream(destFolder + "/" + key);
                value = Utils.toByteArray(is);
                cache.put(key, value);
            } catch (FileNotFoundException e) {
                //Normal
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }


    public void set(String key, byte[] buffer) {
        cache.put(key, buffer);
        if(destFolder != null) {
            try {
                FileOutputStream out = new FileOutputStream(destFolder + "/" + key);
                out.write(buffer);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
