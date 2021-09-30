package com.mart.mymartbee.viewmodel.implementor;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mart.mymartbee.model.RegisterModel;
import com.mart.mymartbee.repository.implementor.RegisterRepoImpl;
import com.mart.mymartbee.viewmodel.interfaces.RegisterViewModel;

import java.io.File;
import java.util.Map;

public class RegisterViewModelImpl extends ViewModel implements RegisterViewModel {

    private MutableLiveData<RegisterModel> registerModelMutableLiveData;
    private MutableLiveData<RegisterModel> profileUpdateModelMLD;
    private MutableLiveData<String> regErrorMutableLiveData;
    private MutableLiveData<Boolean> progressObservable;
    private RegisterRepoImpl registerRepo;

    public RegisterViewModelImpl() {
        registerRepo = new RegisterRepoImpl();
        profileUpdateModelMLD = new MutableLiveData<RegisterModel>();
        registerModelMutableLiveData = new MutableLiveData<RegisterModel>();
        regErrorMutableLiveData = new MutableLiveData<String>();
        progressObservable = new MutableLiveData<Boolean>();
    }

    @Override
    public LiveData<Boolean> progressbarObservable() {
        try {
            progressObservable = registerRepo.progressUpdation();
        } catch (Exception e) {
            progressObservable.setValue(false);
        }
        return progressObservable;
    }

    @Override
    public void checkRegister(File file, Map<String, String> params) {
        Log.e("appSample", "checkRegister");
        try {
            registerModelMutableLiveData = registerRepo.checkRegisterRepo(file, params);
        } catch (Exception e) {
            regErrorMutableLiveData.setValue(e.getMessage());
        }
    }

    @Override
    public LiveData<RegisterModel> checkProfileUpdateLiveData() {
        return profileUpdateModelMLD;
    }

    @Override
    public void checkProfileUpdate(Map<String, String> params) {
        try {
            profileUpdateModelMLD = registerRepo.updateProfileRepo(params);
        } catch(Exception e) {
            regErrorMutableLiveData.setValue(e.getMessage());
        }
    }

    @Override
    public LiveData<RegisterModel> checkRegisterLiveData() {
        return registerModelMutableLiveData;
    }

    @Override
    public LiveData<String> getRegErrorLiveData() {
        return regErrorMutableLiveData;
    }
}
