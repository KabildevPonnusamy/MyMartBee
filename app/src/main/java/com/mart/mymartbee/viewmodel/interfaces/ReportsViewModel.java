package com.mart.mymartbee.viewmodel.interfaces;

import androidx.lifecycle.LiveData;

import com.mart.mymartbee.model.Reports_Model;

public interface ReportsViewModel {

    LiveData<Reports_Model> getReportsLD();
    LiveData<String> reportErrorLV();
    LiveData<Boolean> reportsProgressUpdateLV();

    void getReports(String seller_id);

}
