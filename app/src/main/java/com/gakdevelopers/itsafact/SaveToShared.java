package com.gakdevelopers.itsafact;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SaveToShared {

    private static final String SHARED_KEY = "ITS_A_FACT_KEY";

    public static void writeListToPref (Context context, ArrayList<String> list) {
        Gson gson = new Gson();
        String jsonString =gson.toJson(list);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(SHARED_KEY, jsonString);
        editor.apply();
    }

    public static ArrayList<String> readListFromPref (Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString(SHARED_KEY, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> list = gson.fromJson(jsonString, type);
        return list;
    }
}
