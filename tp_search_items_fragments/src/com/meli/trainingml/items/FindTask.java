package com.meli.trainingml.items;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.meli.trainingml.util.IObservable;
import com.meli.trainingml.util.IObserver;
import com.meli.trainingml.util.MeliService;

public class FindTask extends AsyncTask<HashMap<String, String>, Void, String> implements IObservable {
    private final static String LOGTAG = FindTask.class.getSimpleName();

    private ProgressDialog dialog;
    private ArrayList<IObserver> observers = new ArrayList<IObserver>();
    private String url;
    private String response;
    
    
    public FindTask(Activity activity, String url) {
    	dialog = new ProgressDialog(activity);
    	this.url = url;
    }
    
    @Override
    protected void onPreExecute() {
    	dialog.setMessage("Searching products...");
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
        notifyObservers();
    }

    @Override
    public void registerObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : observers) {
            System.out.println("Notifying Observers");
            observer.update(response);
        } 
    }

}
