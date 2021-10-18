package com.mart.mymartbee.viewmodel.implementor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mart.mymartbee.model.Reports_Model;
import com.mart.mymartbee.repository.implementor.ReportsListRepoImpl;
import com.mart.mymartbee.viewmodel.interfaces.ReportsViewModel;

public class ReportsViewModelImpl extends ViewModel implements ReportsViewModel {

    MutableLiveData<Reports_Model> mutableReportsList;
    MutableLiveData<String> mutableReportError;
    MutableLiveData<Boolean> mutableReportProgressUpdate;
    ReportsListRepoImpl reportsListRepo;

    public ReportsViewModelImpl() {
        reportsListRepo = new ReportsListRepoImpl();
        mutableReportsList = new MutableLiveData<Reports_Model>();
        mutableReportError = new MutableLiveData<String>();
        mutableReportProgressUpdate = new MutableLiveData<Boolean>();
    }

    @Override
    public LiveData<Reports_Model> getReportsLD() {
        return mutableReportsList;
    }

    @Override
    public LiveData<String> reportErrorLV() {
        return mutableReportError;
    }

    @Override
    public LiveData<Boolean> reportsProgressUpdateLV() {
        try {
            mutableReportProgressUpdate = reportsListRepo.progressReportUpdation();
        } catch (Exception e) {
            mutableReportProgressUpdate.setValue(false);
        }
        return mutableReportProgressUpdate;
    }

    @Override
    public void getReports(String seller_id) {
        try {
            mutableReportsList = reportsListRepo.getReportsRepo(seller_id);
        } catch (Exception e) {
            mutableReportError.setValue(e.getMessage());
        }
    }
}
