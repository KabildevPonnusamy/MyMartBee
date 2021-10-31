package com.mart.mymartbee.viewmodel.interfaces;

import androidx.lifecycle.LiveData;

import com.mart.mymartbee.model.SubCategoryUpdate_Model;

import java.util.Map;

public interface SubCateOptionsViewModel {

    LiveData<SubCategoryUpdate_Model> getUpdatedSubCategory();
    LiveData<SubCategoryUpdate_Model> getDeletedSubCategory();
    LiveData<String> subCateOptionsError();
    LiveData<Boolean> subCateOptionsProgress();

    void getUpdatedSubCategories(Map<String, String> params);
    void getDeletedSubCategories(Map<String, String> params);
}
