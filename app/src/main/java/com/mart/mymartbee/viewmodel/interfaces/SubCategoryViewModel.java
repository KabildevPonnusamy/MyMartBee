package com.mart.mymartbee.viewmodel.interfaces;

import androidx.lifecycle.LiveData;

import com.mart.mymartbee.model.SubCategory_Model;

import java.util.Map;

public interface SubCategoryViewModel {
    LiveData<SubCategory_Model> getSubCategoryList();
    LiveData<SubCategory_Model> getAddedSubCategoryList();
    LiveData<String> getSubCategoryError();
    LiveData<Boolean> progressSubCateUpdation();

    void getSubCategories(String seller_id, String cate_id);
    void getAddedSubCategories(Map<String, String> params);

}
