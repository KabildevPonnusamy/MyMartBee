package com.mart.mymartbee.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mart.mymartbee.model.BusinessCategory_Model;
import com.mart.mymartbee.model.Dashboard_Model;
import com.mart.mymartbee.model.Orders_Model;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.model.Reports_Model;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StorageDatas  {

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
    Orders_Model.OrdersList ordersListObj;
    Orders_Model orders_model;
    Reports_Model.ReportsList reportListObj;
    String currHour;
    ArrayList<Dashboard_Model.ViewedProductList> viewedProductLists;
    Dashboard_Model.PendingOrdersList pendingOrdersListObj;
    ArrayList<Products_Model.ProductCategories> subCategoryList;
    boolean isSubCateAdded;

    public boolean isSubCateAdded() {
        return isSubCateAdded;
    }

    public void setSubCateAdded(boolean subCateAdded) {
        isSubCateAdded = subCateAdded;
    }

    public ArrayList<Products_Model.ProductCategories> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(ArrayList<Products_Model.ProductCategories> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public Dashboard_Model.PendingOrdersList getPendingOrdersListObj() {
        return pendingOrdersListObj;
    }

    public void setPendingOrdersListObj(Dashboard_Model.PendingOrdersList pendingOrdersListObj) {
        this.pendingOrdersListObj = pendingOrdersListObj;
    }



    public ArrayList<Dashboard_Model.ViewedProductList> getViewedProductLists() {
        return viewedProductLists;
    }

    public void setViewedProductLists(ArrayList<Dashboard_Model.ViewedProductList> viewedProductLists) {
        this.viewedProductLists = viewedProductLists;
    }

    public String getCurrHour() {
        return currHour;
    }

    public void setCurrHour(String currHour) {
        this.currHour = currHour;
    }

    public String getCurrMint() {
        return currMint;
    }

    public void setCurrMint(String currMint) {
        this.currMint = currMint;
    }

    String currMint;

    public Reports_Model.ReportsList getReportListObj() {
        return reportListObj;
    }

    public void setReportListObj(Reports_Model.ReportsList reportListObj) {
        this.reportListObj = reportListObj;
    }

    public Orders_Model getOrders_model() {
        return orders_model;
    }

    public void setOrders_model(Orders_Model orders_model) {
        this.orders_model = orders_model;
    }

    public Orders_Model.OrdersList getOrdersListObj() {
        return ordersListObj;
    }

    public void setOrdersListObj(Orders_Model.OrdersList ordersListObj) {
        this.ordersListObj = ordersListObj;
    }

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

    public void saveMyOwnCategory(ArrayList<BusinessCategory_Model.Categorys> list, String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<BusinessCategory_Model.Categorys> getMyOwnCategory(String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<BusinessCategory_Model.Categorys>>() {}.getType();
        return gson.fromJson(json, type);
    }


}
