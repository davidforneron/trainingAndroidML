package com.meli.trainingml.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Utils {

    /**
     * Takes a date string in the format :dd/mm/yyyy
     * @param date 
     * @return a date object that represents that date string
     */
    public static Date getDate(String date) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String convertStreamToString(InputStream is) {
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
    
    public static void openFragment(Fragment srcFrg, Fragment dstFragment,
            Bundle options, int idContainer, boolean addToBack, String tag) {
        FragmentManager fragmentManager = srcFrg.getFragmentManager();
        if (options != null) {
            dstFragment.setArguments(options);
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (tag == null) {
            ft.replace(idContainer, dstFragment);
        } else {
            ft.replace(idContainer, dstFragment, tag);
        }
        if (addToBack) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }
    
    public static void openFragment(FragmentManager fragmentManager, Fragment dstFragment,
            Bundle options, int idContainer, boolean addToBack, String tag) {
        if (options != null) {
            dstFragment.setArguments(options);
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (tag == null) {
            ft.replace(idContainer, dstFragment);
        } else {
            ft.replace(idContainer, dstFragment, tag);
        }
        if (addToBack) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }

}
