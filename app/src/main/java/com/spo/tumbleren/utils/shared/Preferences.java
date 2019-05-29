package com.spo.tumbleren.utils.shared;

import android.content.Context;
import android.preference.PreferenceManager;

public class Preferences {

    public static boolean OnPrefChanged = false;

    public static void setDefaults(String key, String value, Context context) {
        android.content.SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
        OnPrefChanged = true;
    }

    public static String getDefaults(String key, Context context) {
        android.content.SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        OnPrefChanged = true;
        return preferences.getString(key, null);
    }
}
