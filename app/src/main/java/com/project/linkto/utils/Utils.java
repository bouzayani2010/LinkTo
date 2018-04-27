package com.project.linkto.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
}
