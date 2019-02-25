package com.project.linkto.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by bbouzaiene on 17/04/2018.
 */

public class Utils {
    public static boolean isEmptyString(String value) {
        if (value != null && value.length() > 0
                && !value.equalsIgnoreCase("null")) {
            return false;
        }
        return true;
    }

    public static String getParam(Context ctx, String key) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return appSharedPrefs.getString(key, "");
    }

    public static void saveParam(Context ctx, String name, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor e = sp.edit();
        e.putString(name, value);
        e.commit();
    }

    public static String getMD5EncryptedString(String encTarget) {
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        return md5;
    }


    public static String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }
    public static String getdiffDate(String currenttimestamp, String timestamp) {
        try {

            Calendar cal1 = Calendar.getInstance();

            Calendar cal2 = Calendar.getInstance();

            java.sql.Timestamp ts2 = java.sql.Timestamp.valueOf(currenttimestamp);
            java.sql.Timestamp ts1 = java.sql.Timestamp.valueOf(timestamp);

            java.sql.Time time1 = new java.sql.Time(ts1.getTime());
            java.sql.Time time2 = new java.sql.Time(ts2.getTime());

            java.sql.Date date1 = new java.sql.Date(ts1.getTime());
            cal1.setTime(date1);
            java.sql.Date date2 = new java.sql.Date(ts2.getTime());
            cal2.setTime(date2);


            int diffyear = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
            Log.i("yeardaymonth1", date2.getYear() + " " + date1.getYear());
            Log.i("yeardaymonth1", date2.getMonth() + " " + date1.getMonth());
            Log.i("yeardaymonth1", date2.getDay() + " " + date1.getDay());
            if (diffyear > 1)
                return diffyear + " years ago";
            else if (diffyear == 1) return "A year ago";
            else {
                int diffmonth = cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
                if (diffmonth > 1)
                    return diffmonth + " months ago";
                else if (diffmonth == 1) return "A month ago";
                else {
                    int diffday = cal2.get(Calendar.DAY_OF_MONTH) - cal1.get(Calendar.DAY_OF_MONTH);
                    if (diffday > 1)
                        return diffday + " days ago";
                    else if (diffday == 1) return "Yesterday";
                    else {
                        int diffhours = time2.getHours() - time1.getHours();
                        if (diffhours > 1)
                            return diffhours + " hours ago";
                        else if (diffhours == 1) return "An hour ago";
                        else {
                            int diffminutes = time2.getMinutes() - time1.getMinutes();
                            if (diffminutes > 1)
                                return diffminutes + " minutes ago";
                            else return "Just now!";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            Log.i("yeardaymonth", e.getMessage());
        }
        return "";
    }

    public static Bitmap getCroppedBitmap(Bitmap image) {
        int imageHeight = image.getHeight(); //get original image height
        int imageWidth = image.getWidth();  //get original image width
        int offset = 0;

        int shorterSide = imageWidth < imageHeight ? imageWidth : imageHeight;
        int longerSide = imageWidth < imageHeight ? imageHeight : imageWidth;
        boolean portrait = imageWidth < imageHeight ? true : false;  //find out the image orientation
//number array positions to allocate for one row of the pixels (+ some blanks - explained in the Bitmap.getPixels() documentation)
        int stride = shorterSide + 1;
        int lengthToCrop = (longerSide - shorterSide) / 2; //number of pixel to remove from each side
//size of the array to hold the pixels (amount of pixels) + (amount of strides after every line)
        int shorterImageDimension=500;
        int pixelArraySize = (shorterSide * shorterSide) + (shorterImageDimension * 1);
        int[] pixels = new int[pixelArraySize];

//now fill the pixels with the selected range
        image.getPixels(pixels, 0, stride, portrait ? 0 : lengthToCrop, portrait ? lengthToCrop : 0, shorterSide, shorterSide);

//save memory
        image.recycle();

//create new bitmap to contain the cropped pixels
        Bitmap croppedBitmap = Bitmap.createBitmap(shorterSide, shorterSide, Bitmap.Config.ARGB_4444);
        croppedBitmap.setPixels(pixels, offset, 0,0, 0, shorterSide, shorterSide);

//I'd recommend to perform these kind of operations on worker thread
      //  listener.imageCropped(croppedBitmap);

//Or if you like to live dangerously
        return croppedBitmap;
    }
}
