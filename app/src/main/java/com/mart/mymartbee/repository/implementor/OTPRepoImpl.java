package com.mart.mymartbee.repository.implementor;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.OTPModel;
import com.mart.mymartbee.model.OTPVerifyModel;
import com.mart.mymartbee.networking.retrofit.ApiCallBack;
import com.mart.mymartbee.networking.retrofit.ApiClient;
import com.mart.mymartbee.repository.interfaces.OTPRepo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class OTPRepoImpl implements OTPRepo {

    private MutableLiveData<String> mutableOTPError;
    private MutableLiveData<Boolean> progressOTPObservable;

    private static OTPRepoImpl instance = null;

    public static OTPRepoImpl getInstance() {
        if(instance == null) {
            instance = new OTPRepoImpl();
        }
        return instance;
    }

    @Override
    public MutableLiveData<Boolean> progressOTPUpdation() {
        return progressOTPObservable;
    }

    public OTPRepoImpl() {
        mutableOTPError = new MutableLiveData<String>();
        progressOTPObservable = new MutableLiveData<Boolean>();
    }

    @Override
    public MutableLiveData<OTPVerifyModel> verifyOTPRepo(Map<String, String> params) throws Exception {
        MutableLiveData<OTPVerifyModel> mutableOTPVerifyModel = new MutableLiveData<OTPVerifyModel>();
        progressOTPObservable.setValue(true);
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<OTPVerifyModel> call = callBack.verifyOTP(params);
        call.enqueue(new Callback<OTPVerifyModel>() {
            @Override
            public void onResponse(Call<OTPVerifyModel> call, Response<OTPVerifyModel> response) {
                progressOTPObservable.setValue(false);
                Log.e("appSample", "RespCode: " + response.code());
                Log.e("appSample", "RespCode: " + response.toString());
                Log.e("appSample", "BodyOTP: " + response.body().getStrModule());

                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            mutableOTPVerifyModel.setValue(response.body());
                        }
                    } else {
                        mutableOTPVerifyModel.setValue(null);
                        mutableOTPError.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "ResponseExc: " + e.getMessage());
                    mutableOTPVerifyModel.setValue(null);
                    mutableOTPError.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<OTPVerifyModel> call, Throwable t) {
                progressOTPObservable.setValue(false);
                Log.e("appSample", "ResponseFailure: " + t.getMessage());
                mutableOTPVerifyModel.setValue(null);
                mutableOTPError.setValue("Connection Error");
            }
        });

        return mutableOTPVerifyModel;
    }

    @Override
    public MutableLiveData<OTPModel> getOTPModel(String pincode, String mobile) throws Exception {
        progressOTPObservable.setValue(true);
        Map<String,String> params = new HashMap<String, String>();
        params.put("country_code", pincode);
        params.put("mobile_number", mobile);

        MutableLiveData<OTPModel> mutableOTPRepoModel = new MutableLiveData<OTPModel>();
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<OTPModel> call = callBack.getOTP(params);
        call.enqueue(new Callback<OTPModel>() {
            @Override
            public void onResponse(Call<OTPModel> call, Response<OTPModel> response) {
                progressOTPObservable.setValue(false);
                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            mutableOTPRepoModel.setValue(response.body());
                        }
                    } else {
                        mutableOTPRepoModel.setValue(null);
                        mutableOTPError.setValue("No Response");
                    }
                } catch (Exception e) {
                    mutableOTPRepoModel.setValue(null);
                    mutableOTPError.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<OTPModel> call, Throwable t) {
                progressOTPObservable.setValue(false);
                mutableOTPRepoModel.setValue(null);
                mutableOTPError.setValue("Connection Error");
            }
        });

        return mutableOTPRepoModel;
    }

    @Override
    public MutableLiveData<String> getError() throws Exception {
        return mutableOTPError;
    }
}
