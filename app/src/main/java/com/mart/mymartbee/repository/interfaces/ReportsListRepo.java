package com.mart.mymartbee.repository.interfaces;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.Reports_Model;

public interface ReportsListRepo {
    MutableLiveData<Reports_Model> getReportsRepo(String seller_id) throws Exception;
    MutableLiveData<String> reportsReporError() throws Exception;
    MutableLiveData<Boolean> progressReportUpdation() throws Exception;
}
