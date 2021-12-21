package com.mart.mymartbee.viewmodel.implementor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mart.mymartbee.model.CommonResponseModel;
import com.mart.mymartbee.repository.implementor.VersionListRepoImpl;
import com.mart.mymartbee.viewmodel.interfaces.VersionViewModel;

import java.util.Map;

public class VersionViewModelImpl extends ViewModel implements VersionViewModel {

    private MutableLiveData<CommonResponseModel> mutableVersionList;
    private MutableLiveData<String> versionError;
    private MutableLiveData<Boolean> progressVersionObserver;
    private VersionListRepoImpl versionListRepo;

    public VersionViewModelImpl() {
        versionListRepo = new VersionListRepoImpl();
        mutableVersionList = new MutableLiveData<CommonResponseModel>();
        versionError = new MutableLiveData<String>();
        progressVersionObserver = new MutableLiveData<Boolean>();
    }

    @Override
    public LiveData<CommonResponseModel> getVersionLD() {
        return mutableVersionList;
    }

    @Override
    public LiveData<String> getVersionError() {
        return versionError;
    }

    @Override
    public LiveData<Boolean> progressVersionUpdate() {
        try {
            progressVersionObserver = versionListRepo.progressVersionUpdation();
        } catch(Exception e) {
            progressVersionObserver.setValue(false);
        }
        return progressVersionObserver;
    }

    @Override
    public void getVersions(Map<String, String> params) {
        try {
            mutableVersionList = versionListRepo.getVersionList(params);
        } catch (Exception e) {
            versionError.setValue(e.getMessage());
        }
    }
}
