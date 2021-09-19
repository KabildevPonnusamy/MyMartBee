package com.mart.mymartbee.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mart.mymartbee.model.Category_Model;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StorageDatas {

    public static StorageDatas sObj;

    public static StorageDatas getsObj() {
        return sObj;
    }

    public static void setsObj(StorageDatas sObj) {
        StorageDatas.sObj = sObj;
    }

    public static StorageDatas getInstance() {
        if(sObj == null) {
            sObj = new StorageDatas();
        }
        return sObj;
    }

    String firebaseToken;

    public String getFirebaseToken() {
        return firebaseToken;
    }
    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public void saveMyOwnCategory(ArrayList<Category_Model.Categorys> list, String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<Category_Model.Categorys> getMyOwnCategory(String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Category_Model.Categorys>>() {}.getType();
        return gson.fromJson(json, type);
    }


}
