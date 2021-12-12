package com.mart.mymartbee.viewmodel.implementor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mart.mymartbee.model.NewReportProducts_Model;
import com.mart.mymartbee.model.NewReportSales_Model;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.repository.implementor.NewReportListRepoImpl;
import com.mart.mymartbee.viewmodel.interfaces.NewReportViewModel;

import java.util.Map;

public class NewReportViewModelImpl extends ViewModel implements NewReportViewModel {

    private MutableLiveData<NewReportProducts_Model> mutableProductsReportsList;
    private MutableLiveData<NewReportSales_Model> mutableSalesReportsList;
    private MutableLiveData<NewReportSales_Model> mutableSalesReportsByYearList;
    private MutableLiveData<String> mutableReportError;
    private MutableLiveData<Boolean> progressReportObserver;
    private NewReportListRepoImpl newReportListRepo;

    public NewReportViewModelImpl() {
        mutableProductsReportsList = new MutableLiveData<NewReportProducts_Model>();
        mutableSalesReportsList = new MutableLiveData<NewReportSales_Model>();
        mutableSalesReportsByYearList = new MutableLiveData<NewReportSales_Model>();
        mutableReportError = new MutableLiveData<String>();
        progressReportObserver = new MutableLiveData<Boolean>();
        newReportListRepo = new NewReportListRepoImpl();
    }

    @Override
    public LiveData<NewReportProducts_Model> getProductReportsLV() {
        return mutableProductsReportsList;
    }

    @Override
    public LiveData<NewReportSales_Model> getSalesReportsLV() {
        return mutableSalesReportsList;
    }

    @Override
    public LiveData<String> getReportsNewError() {
        return mutableReportError;
    }

    @Override
    public LiveData<Boolean> getReportsNewUpdation() {
        try {
            progressReportObserver = newReportListRepo.progressReportUpdation();
        } catch(Exception e) {
            progressReportObserver.setValue(false);
        }
        return progressReportObserver;
    }

    @Override
    public void getProductReports(Map<String, String> params) {
        try {
            mutableProductsReportsList = newReportListRepo.getProductReports(params);
        } catch(Exception e) {
            mutableReportError.setValue(e.getMessage());
        }
    }

    @Override
    public void getSalesReports(Map<String, String> params) {
        try {
            mutableSalesReportsList = newReportListRepo.getSalesReports(params);
        } catch(Exception e) {
            mutableReportError.setValue(e.getMessage());
        }
    }

    @Override
    public LiveData<NewReportSales_Model> getSalesReportsByYearLV() {
        return mutableSalesReportsByYearList;
    }

    @Override
    public void getSalesReportsByYear(Map<String, String> params) {
        try {
            mutableSalesReportsByYearList = newReportListRepo.getSalesReportsByYear(params);
        } catch(Exception e) {
            mutableReportError.setValue(e.getMessage());
        }
    }

}
