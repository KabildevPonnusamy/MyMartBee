package com.mart.mymartbee.viewmodel.implementor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mart.mymartbee.model.Dashboard_Model;
import com.mart.mymartbee.repository.implementor.DashboardRepoImpl;
import com.mart.mymartbee.repository.interfaces.DashboardRepo;
import com.mart.mymartbee.viewmodel.interfaces.DashboardViewModel;

public class DashboardViewModelImpl extends ViewModel implements DashboardViewModel {

    private MutableLiveData<Dashboard_Model> mutablegetDashboardDatas;
    private MutableLiveData<String> mutableDashboardErrors;
    private MutableLiveData<Boolean> mutableDashboardProgress;
    DashboardRepoImpl dashboardRepo;

    public DashboardViewModelImpl() {
        dashboardRepo = new DashboardRepoImpl();
        mutableDashboardProgress = new MutableLiveData<Boolean>();
        mutableDashboardErrors = new MutableLiveData<String>();
        mutablegetDashboardDatas = new MutableLiveData<Dashboard_Model>();
    }

    @Override
    public LiveData<String> getErrorData() {
        return mutableDashboardErrors;
    }

    @Override
    public LiveData<Boolean> progressbarDASHObservable() {
        try {
            mutableDashboardProgress = dashboardRepo.progressDashboardUpdation();
        } catch(Exception e) {
            mutableDashboardProgress.setValue(false);
        }
        return mutableDashboardProgress;
    }

    @Override
    public LiveData<Dashboard_Model> getDashboardDatasLD() {
        return mutablegetDashboardDatas;
    }

    @Override
    public void getDashboardDatas(String sellerId, String short_value) {
        try {
            mutablegetDashboardDatas = dashboardRepo.getDashboardModel(sellerId, short_value);
        } catch(Exception e) {
            mutableDashboardErrors.setValue(e.getMessage());
        }
    }
}
