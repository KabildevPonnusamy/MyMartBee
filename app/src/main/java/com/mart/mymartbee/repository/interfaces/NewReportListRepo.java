package com.mart.mymartbee.repository.interfaces;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.NewReportProducts_Model;
import com.mart.mymartbee.model.NewReportSales_Model;
import com.mart.mymartbee.model.Products_Model;

import java.util.Map;

public interface NewReportListRepo {

    MutableLiveData<NewReportProducts_Model> getProductReports(Map<String, String> params) throws Exception;
    MutableLiveData<NewReportSales_Model> getSalesReports(Map<String, String> params) throws Exception;
    MutableLiveData<NewReportSales_Model> getSalesReportsByYear(Map<String, String> params) throws Exception;
    MutableLiveData<String> getReportRepoError() throws Exception;
    MutableLiveData<Boolean> progressReportUpdation();

}
