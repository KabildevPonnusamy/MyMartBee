package com.mart.mymartbee.repository.interfaces;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.Dashboard_Model;
import com.mart.mymartbee.model.OTPVerifyModel;

import java.util.Map;

public interface DashboardRepo {

    MutableLiveData<Dashboard_Model> getDashboardModel(String sellerId) throws Exception;
    MutableLiveData<String> getError() throws Exception;
    MutableLiveData<Boolean> progressDashboardUpdation();
}
