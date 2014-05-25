package com.meli.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.util.HashMap;

public class FindTask extends AsyncTask<HashMap<String, String>, Void, String> {
    private final static String TAG = FindTask.class.getSimpleName();

    private ProgressDialog dialog;
    private String url;
    private String response;
    private IObserver observer;
    
    
    public FindTask(Activity activity, String url, IObserver observer) {
    	dialog = new ProgressDialog(activity);
        this.observer = observer;
    	this.url = url;
    }
    
    @Override
    protected void onPreExecute() {
    	dialog.setMessage("Searching...");
    	dialog.show();
    }

    @Override
    protected String doInBackground(HashMap<String, String>... params) {
		return  MeliService.find(url, params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
        response = result;
        observer.update(response);
    }
}
