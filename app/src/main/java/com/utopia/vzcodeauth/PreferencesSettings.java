package com.utopia.vzcodeauth;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesSettings {

    private static final String REFERENCE = "settings_pref";

    static void saveToPref(Context context, String str) {
        SharedPreferences sharedPref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("code", str);
        editor.apply();
    }

    static String getCode(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(REFERENCE, Context.MODE_PRIVATE);
        return sharedPref.getString("code", "");
    }

}
