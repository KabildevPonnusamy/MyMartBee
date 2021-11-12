package com.mart.mymartbee.viewmodel.interfaces;

import androidx.lifecycle.LiveData;

import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.model.UOMModel;

import java.io.File;
import java.util.Map;

public interface ProductsViewModel {

    LiveData<Products_Model> addProductLV();
    LiveData<Products_Model> deleteProductLV();
    LiveData<Products_Model> editProductLV();
    LiveData<Products_Model> editProductwithImageLV();
    LiveData<Products_Model> getProductLV();
    LiveData<Products_Model> uploadingProductImagesLV();
    LiveData<UOMModel> getUOMLV();
    LiveData<String> getProductError();
    LiveData<Boolean> progressProductUpdation();

    void addProducts(File file, Map<String, String> params);
    void deleteProducts(Map<String, String> params);
    void editProductwithImage(File file, Map<String, String> params);
    void uploadingProductImage(File file, Map<String, String> params);
    void editProducts(Map<String, String> params);
    void getProducts(String cate_id, String seller_id);
    void getUOM();

}
