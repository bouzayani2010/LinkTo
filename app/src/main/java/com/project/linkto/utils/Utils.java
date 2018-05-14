package com.project.linkto.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

    public static String getMD5EncryptedString(String encTarget){
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while ( md5.length() < 32 ) {
            md5 = "0"+md5;
        }
        return md5;
    }
}
