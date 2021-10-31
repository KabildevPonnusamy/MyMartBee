package com.mart.mymartbee.viewmodel.interfaces;

import androidx.lifecycle.LiveData;

import com.mart.mymartbee.model.Dashboard_Model;

public interface DashboardViewModel {

    LiveData<String> getErrorData();
    LiveData<Boolean> progressbarDASHObservable();
    LiveData<Dashboard_Model> getDashboardDatasLD();

    void getDashboardDatas(String sellerId, String short_value);
}
