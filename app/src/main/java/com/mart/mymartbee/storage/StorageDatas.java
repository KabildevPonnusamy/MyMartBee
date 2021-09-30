package com.mart.mymartbee.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mart.mymartbee.model.Category_Model;
import com.mart.mymartbee.model.Products_Model;

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
    String storeWhatsappLink;
    Products_Model products_model;
    Products_Model.ProductCategories.ProductsList productsObj;
    ArrayList<Products_Model.ProductCategories.ProductsList> cateProductsList;

    public ArrayList<Products_Model.ProductCategories.ProductsList> getCateProductsList() {
        return cateProductsList;
    }

    public void setCateProductsList(ArrayList<Products_Model.ProductCategories.ProductsList> cateProductsList) {
        this.cateProductsList = cateProductsList;
    }

    public String getStoreWhatsappLink() {
        return storeWhatsappLink;
    }

    public void setStoreWhatsappLink(String storeWhatsappLink) {
        this.storeWhatsappLink = storeWhatsappLink;
    }

    public Products_Model.ProductCategories.ProductsList getProductsObj() {
        return productsObj;
    }

    public void setProductsObj(Products_Model.ProductCategories.ProductsList productsObj) {
        this.productsObj = productsObj;
    }

    public Products_Model getProducts_model() {
        return products_model;
    }

    public void setProducts_model(Products_Model products_model) {
        this.products_model = products_model;
    }

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
