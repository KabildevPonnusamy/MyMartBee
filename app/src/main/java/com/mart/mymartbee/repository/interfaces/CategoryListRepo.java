package com.mart.mymartbee.repository.interfaces;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.Category_Model;

import java.util.Map;

public interface CategoryListRepo {

    MutableLiveData<Category_Model> getCategoryRepo() throws Exception;
    MutableLiveData<String> getCateRepoError() throws Exception;
    MutableLiveData<Boolean> progressCateUpdation();

}
