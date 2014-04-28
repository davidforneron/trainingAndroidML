package com.meli.trainingml.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import android.content.Context;

public class CacheManager {

    private final static String LOGTAG = CacheManager.class.getSimpleName();

    private final static int SIZE = 20;

    private static CacheManager mInstance;

    private HashMap<String, byte[]> cache;

    static  {
        // Creates a single static instance of PhotoManager
        mInstance = new CacheManager();
    }

    private CacheManager() {
        cache = new HashMap<String, byte[]>();
    }

    /**
     * Returns the CacheManager object
     * @return The global CacheManager object
     */
    public static CacheManager getInstance() {
        return mInstance;
    }

    public byte[] get(String key, Context context) {
        byte[] value = cache.get(key);
        if(value != null) {
            return value;
        }
        String destFolder =  context.getCacheDir().getAbsolutePath();
        try {
            FileInputStream is = new FileInputStream(destFolder + "/" + key);
            value = Utils.toByteArray(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }


    public void set(String key, byte[] buffer, Context context) {

        //TODO: create cache with a capacity
        cache.put(key, buffer);


        String destFolder =  context.getCacheDir().getAbsolutePath();
        try {
            FileOutputStream out = new FileOutputStream(destFolder + "/" + key);
            out.write(buffer);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
