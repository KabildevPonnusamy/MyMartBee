package com.mart.mymartbee.repository.interfaces;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.OTPModel;
import com.mart.mymartbee.model.OTPVerifyModel;

import java.util.Map;

public interface OTPRepo {

    MutableLiveData<OTPModel> getOTPModel(String pincode, String mobile) throws Exception;
    MutableLiveData<String> getError() throws Exception;
    MutableLiveData<OTPVerifyModel> verifyOTPRepo(Map<String, String> params) throws Exception;
    MutableLiveData<Boolean> progressOTPUpdation();

}

