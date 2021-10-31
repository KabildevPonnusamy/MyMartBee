package com.mart.mymartbee.repository.interfaces;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.BusinessCategory_Model;

import java.util.Map;

public interface BusinessCategoryListRepo {

    MutableLiveData<BusinessCategory_Model> getCategoryRepo() throws Exception;
    MutableLiveData<BusinessCategory_Model> getAddedCategoryRepo(Map<String, String> params) throws Exception;
    MutableLiveData<String> getCateRepoError() throws Exception;
    MutableLiveData<Boolean> progressCateUpdation();

}
