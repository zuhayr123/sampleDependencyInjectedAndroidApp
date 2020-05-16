package com.laaltentech.abou.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    // Shared Preferences
    public static SharedPreferences pref;

    // Editor for Shared preferences
    private static SharedPreferences.Editor editor;

    public static String PREF_NAME = "AppIntro";


    public static void setLocation(String latitude, String longitude) {
        editor.putString("Lat", latitude);
        editor.putString("Lon", longitude);
        editor.commit();
    }

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }
}
