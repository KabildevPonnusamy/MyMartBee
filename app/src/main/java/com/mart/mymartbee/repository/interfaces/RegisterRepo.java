package com.mart.mymartbee.repository.interfaces;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.RegisterModel;

import java.io.File;
import java.util.Map;

public interface RegisterRepo {

    MutableLiveData<RegisterModel> checkRegisterRepo(File file, Map<String, String> params) throws Exception;
    MutableLiveData<String> CheckREgRepoERror() throws Exception;
    MutableLiveData<Boolean> progressUpdation();
}
