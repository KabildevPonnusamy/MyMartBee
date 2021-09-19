package com.mart.mymartbee.viewmodel.interfaces;

import androidx.lifecycle.LiveData;

import com.mart.mymartbee.model.RegisterModel;

import java.io.File;
import java.util.Map;

public interface RegisterViewModel {

    LiveData<RegisterModel> checkRegisterLiveData();
    LiveData<String> getRegErrorLiveData();
    LiveData<Boolean> progressbarObservable();

    void checkRegister(File file, Map<String, String> params);
}
