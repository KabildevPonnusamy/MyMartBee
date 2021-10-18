package com.mart.mymartbee.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mart.mymartbee.model.Category_Model;
import com.mart.mymartbee.model.RegisterModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyPreferenceDatas {

    private String myPrefName = "MyMartBee";
    private SharedPreferences myPrefs;

    public static final String SELLER_ID = "sellerId";
    public static final String SELLER_SHOP = "sellerShop";
    public static final String SELLER_CATEGORY = "sellerCategory";
    public static final String SELLER_CATEGORY_NAME= "sellerCategoryName";
    public static final String SELLER_IMAGE = "sellerImage";
    public static final String SELLER_START_TIME = "sellerStartTime";
    public static final String SELLER_CLOSE_TIME = "sellerCloseTime";
    public static final String SELLER_ADDRESS = "sellerAddress";
    public static final String SELLER_MOBILE = "sellerMobile";
    public static final String SELLER_LATITUDE = "sellerLatitude";
    public static final String SELLER_LONGITUDE = "sellerLongitude";
    public static final String SELLER_PRODUCTS_COUNT = "sellerProductCount";
    public static final String SELLER_COUNTRY_CODE = "sellerCountryCode";

    public MyPreferenceDatas(Context context) {
        myPrefs = context.getSharedPreferences(myPrefName, Context.MODE_PRIVATE);
    }

    public void clearPreference(Context context) {
        /*SharedPreferences sharedPreferences = context.getSharedPreferences(
                myPrefName, Context.MODE_PRIVATE);*/
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.clear().commit();
    }

    public String getPrefString(String data) {
        return myPrefs.getString(data, "");
    }

    public Boolean getPrefBoolean(String data) {
        return myPrefs.getBoolean(data, false);
    }

    public void putPrefString(String key, String value) {
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putPrefBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

}
