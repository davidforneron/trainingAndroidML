package com.meli.trainingml.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;



import android.util.Log;

public class HttpClientCustom {

    public static final String GET = "GET";
    public static final String POST = "POST";
    private static final String TAG = "HttpClientCustom";

    public static String request(String url, String operation, HashMap<String, String> postParameters) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpRequestBase request;
        String result = null;
        List<NameValuePair> nameValuePairs = null;
        
        //Add parameters
        if (postParameters != null && postParameters.isEmpty() == false) {
            nameValuePairs = new ArrayList<NameValuePair>(postParameters.size());
            String k, v;
            Iterator<String> itKeys = postParameters.keySet().iterator();
            while (itKeys.hasNext()) {
                k = itKeys.next();
                v = postParameters.get(k);
                nameValuePairs.add(new BasicNameValuePair(k, v));
            }
        }
        
        // Prepare a request object
        if(operation.equals(GET)) {
            if (nameValuePairs != null) {
                String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
                url += "?" + paramString;
            }
            request = new HttpGet(url);
        } else if(operation.equals(POST)) {
            request = new HttpPost(url);
            if (nameValuePairs != null) {
                UrlEncodedFormEntity urlEntity;
                try {
                    urlEntity = new UrlEncodedFormEntity(nameValuePairs);
                    ((HttpPost)request).setEntity(urlEntity);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }
        
        // Execute the request
        HttpResponse response;
        try {
            response = httpclient.execute(request);
            // Examine the response status
            Log.i(TAG, response.getStatusLine().toString());

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release

            if (entity != null) {

                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                result = convertStreamToString(instream);
                // now you have the string representation of the HTML request
                instream.close();
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        
        return result;
        
    }
    
    
    public static byte[] requestStream(String url) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpRequestBase request;
        byte[] result = null;

        request = new HttpGet(url);
        
        // Execute the request
        HttpResponse response;
        try {
            response = httpclient.execute(request);
            // Examine the response status
            Log.i(TAG, response.getStatusLine().toString());

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release

            if (entity != null) {
                InputStream instream = entity.getContent();
                result = toByteArray(instream);
                // now you have the string representation of the HTML request
                instream.close();
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        
        return result;
        
    }

    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the
         * BufferedReader.readLine() method. We iterate until the BufferedReader
         * return null which means there's no more data to read. Each line will
         * appended to a StringBuilder and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    
	public static byte[] toByteArray(InputStream is)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = is.read();
		while (reads != -1) {
			baos.write(reads);
			reads = is.read();
		}
		return baos.toByteArray();
	}

}
