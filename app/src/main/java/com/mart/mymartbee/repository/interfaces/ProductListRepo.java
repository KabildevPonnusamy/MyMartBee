package com.mart.mymartbee.repository.interfaces;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.model.UOMModel;

import java.io.File;
import java.util.Map;

public interface ProductListRepo {

    MutableLiveData<Products_Model> addProductRepo(File file, Map<String, String> params) throws Exception;
    MutableLiveData<Products_Model> editProductRepoWithImage(File file, Map<String, String> params) throws Exception;
    MutableLiveData<Products_Model> editProductRepo(Map<String, String> params) throws Exception;
    MutableLiveData<Products_Model> deleteProductRepo(Map<String, String> params) throws Exception;
    MutableLiveData<Products_Model> getProductRepo(String cate_id, String seller_id) throws Exception;
    MutableLiveData<UOMModel> getUOMDatas() throws Exception;
    MutableLiveData<String> getCateRepoError() throws Exception;
    MutableLiveData<Boolean> progressProductUpdation();
}
