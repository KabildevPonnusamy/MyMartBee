package com.mart.mymartbee.viewmodel.interfaces;

import androidx.lifecycle.LiveData;

import com.mart.mymartbee.model.NewReportProducts_Model;
import com.mart.mymartbee.model.NewReportSales_Model;
import com.mart.mymartbee.model.Products_Model;

import java.util.Map;

public interface NewReportViewModel {

    LiveData<NewReportProducts_Model> getProductReportsLV();
    LiveData<NewReportSales_Model> getSalesReportsLV();
    LiveData<NewReportSales_Model> getSalesReportsByYearLV();
    LiveData<String> getReportsNewError();
    LiveData<Boolean> getReportsNewUpdation();

    void getProductReports(Map<String, String> params);
    void getSalesReports(Map<String, String> params);
    void getSalesReportsByYear(Map<String, String> params);

}
