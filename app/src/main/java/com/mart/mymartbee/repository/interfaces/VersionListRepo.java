package com.mart.mymartbee.repository.interfaces;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.CommonResponseModel;

import java.util.Map;

public interface VersionListRepo {

    MutableLiveData<CommonResponseModel> getVersionList(Map<String, String> params) throws Exception;
    MutableLiveData<String> getVersionRepoError() throws Exception;
    MutableLiveData<Boolean> progressVersionUpdation();
}
