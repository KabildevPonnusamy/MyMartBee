package com.mart.mymartbee.viewmodel.interfaces;

import androidx.lifecycle.LiveData;

import com.mart.mymartbee.model.BusinessCategory_Model;

import java.util.Map;

public interface BusinessCategoryViewModel {

    LiveData<BusinessCategory_Model> getCategoryListLV();
    LiveData<BusinessCategory_Model> getAddedCategoryListLV();
    LiveData<String> getCategoryError();
    LiveData<Boolean> progressCateUpdation();

    void getCateGories();
    void getAddedCategories(Map<String, String> params);
}
