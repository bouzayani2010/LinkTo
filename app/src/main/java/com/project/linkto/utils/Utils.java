package com.project.linkto.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public static String getdiffDate(String currenttimestamp, String timestamp) {
        try {
            java.sql.Timestamp ts1 = java.sql.Timestamp.valueOf(currenttimestamp);
            java.sql.Timestamp ts2 = java.sql.Timestamp.valueOf(timestamp);
            int diffyear = ts2.getYear() - ts1.getYear();
            Log.i("yeardaymonth",ts2.getYear()+" "+ts1.getYear());
            Log.i("yeardaymonth",ts2.getMonth()+" "+ts1.getMonth());
            Log.i("yeardaymonth",ts2.getDay()+" "+ts1.getDay());
            if (diffyear > 1)
                return diffyear + " years ago";
            else if (diffyear == 1) return "A year ago";
            else {
                int diffmonth = ts2.getMonth() - ts1.getMonth();
                if (diffmonth > 1)
                    return diffmonth + " months ago";
                else if (diffmonth == 1) return "A month ago";
                else {
                    int diffday = ts2.getDay() - ts1.getDay();
                    if (diffday > 1)
                        return diffday + " days ago";
                    else if (diffday == 1) return "Yesterday";
                    else {
                        int diffhours = ts2.getHours() - ts1.getHours();
                        if (diffhours > 1)
                            return diffhours + " hours ago";
                        else if (diffhours == 1) return "An hour ago";
                        else {
                            int diffminutes = ts2.getMinutes() - ts1.getMinutes();
                            if (diffminutes > 1)
                                return diffhours + " minutes ago";
                            else return "Just now!";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
