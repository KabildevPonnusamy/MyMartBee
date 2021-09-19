package com.mart.mymartbee.viewmodel.interfaces;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.OnLifecycleEvent;

import com.mart.mymartbee.model.OTPModel;
import com.mart.mymartbee.model.OTPVerifyModel;

import java.util.Map;

public interface OTPViewModel extends LifecycleObserver {

    LiveData<OTPModel> getOTPLiveData();
    LiveData<String> getErrorData();
    LiveData<OTPVerifyModel> verifyOTPLiveData();
    LiveData<String> getPendingTime();
    LiveData<Boolean> progressbarOTPObservable();

    void getOTP(String pincode, String mobile);
    void verifyOTP(Map<String, String> params);
    void startCounter();
    void stopCounter();

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onRefresh();

}
