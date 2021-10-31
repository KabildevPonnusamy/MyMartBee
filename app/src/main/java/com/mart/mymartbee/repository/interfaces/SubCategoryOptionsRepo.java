package com.mart.mymartbee.repository.interfaces;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.SubCategoryUpdate_Model;

import java.util.Map;

public interface SubCategoryOptionsRepo {

    MutableLiveData<SubCategoryUpdate_Model> updateSubCategory(Map<String, String> params) throws Exception;
    MutableLiveData<SubCategoryUpdate_Model> deleteSubCategory(Map<String, String> params) throws Exception;
    MutableLiveData<String> subCateUpdError() throws Exception;
    MutableLiveData<Boolean> subCateUpdProgress() throws Exception;
}
