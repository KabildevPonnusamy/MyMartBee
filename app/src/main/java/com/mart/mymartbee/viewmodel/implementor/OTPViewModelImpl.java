package com.mart.mymartbee.viewmodel.implementor;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mart.mymartbee.model.OTPModel;
import com.mart.mymartbee.model.OTPVerifyModel;
import com.mart.mymartbee.repository.implementor.OTPRepoImpl;
import com.mart.mymartbee.viewmodel.interfaces.OTPViewModel;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OTPViewModelImpl extends ViewModel implements OTPViewModel {

    private MutableLiveData<OTPModel> mutableOTPModel;
    private MutableLiveData<String> mutableErrorData;
    private MutableLiveData<OTPVerifyModel> mutableOTPVerifyModel;
    private MutableLiveData<Boolean> progressOTPObservable;
    private OTPRepoImpl otpRepo;

    private MutableLiveData<String> pendingTimeData;
    CountDownTimer countDownTimer = null;
    private int milliSeconds = 10000;

    public OTPViewModelImpl() {
        otpRepo = new OTPRepoImpl();
        mutableOTPModel = new MutableLiveData<OTPModel>();
        mutableErrorData = new MutableLiveData<String>();
        mutableOTPVerifyModel = new MutableLiveData<OTPVerifyModel>();
        pendingTimeData = new MutableLiveData<String>();
        progressOTPObservable = new MutableLiveData<Boolean>();

        countDownTimer = new CountDownTimer(milliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("appSample", "Counter Started");
                long min = millisUntilFinished / 1000;
                String strTime = String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                pendingTimeData.setValue(strTime);
                Log.e("appSample", "Seconds: " + strTime);
            }

            @Override
            public void onFinish() {
                Log.e("appSample", "Seconds: Finished");
                countDownTimer.cancel();
                pendingTimeData.setValue("DONE");
            }
        };
    }

    @Override
    public void startCounter() {
        countDownTimer.start();
    }

    @Override
    public void stopCounter() {
        if(countDownTimer != null) {
            countDownTimer.cancel();
            pendingTimeData.setValue("DONE");
            Log.e("appSample", "Counter: Stopped");
        }
    }

    @Override
    public LiveData<Boolean> progressbarOTPObservable() {
        try {
            progressOTPObservable = otpRepo.progressOTPUpdation();
        } catch(Exception e) {
            progressOTPObservable.setValue(false);
        }
        return progressOTPObservable;
    }

    @Override
    public void getOTP(String pincode, String mobile) {
        try {
            mutableOTPModel = otpRepo.getOTPModel(pincode, mobile);
        } catch (Exception e) {
            mutableErrorData.setValue(e.getMessage());
        }
    }

    @Override
    public void verifyOTP(Map<String, String> params) {
        try {
            mutableOTPVerifyModel = otpRepo.verifyOTPRepo(params);
        } catch (Exception e) {
            mutableErrorData.setValue(e.getMessage());
        }
    }

    @Override
    public LiveData<OTPVerifyModel> verifyOTPLiveData() {
        return mutableOTPVerifyModel;
    }

    @Override
    public LiveData<OTPModel> getOTPLiveData() {
        return mutableOTPModel;
    }

    @Override
    public LiveData<String> getErrorData() {
        return mutableErrorData;
    }

    @Override
    public LiveData<String> getPendingTime() {
        return pendingTimeData;
    }

    @Override
    public void onRefresh() {

    }
}
