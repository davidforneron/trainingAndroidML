package com.meli.trainingml.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

}
