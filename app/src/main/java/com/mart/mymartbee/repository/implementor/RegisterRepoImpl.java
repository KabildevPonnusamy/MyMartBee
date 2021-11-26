package com.mart.mymartbee.repository.implementor;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mart.mymartbee.model.OTPModel;
import com.mart.mymartbee.model.OTPVerifyModel;
import com.mart.mymartbee.model.RegisterModel;
import com.mart.mymartbee.networking.retrofit.ApiCallBack;
import com.mart.mymartbee.networking.retrofit.ApiClient;
import com.mart.mymartbee.repository.interfaces.RegisterRepo;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterRepoImpl implements RegisterRepo {

    private MutableLiveData<String> regErrorMLD;
    private MutableLiveData<Boolean> progressbarObservable;

    private static RegisterRepoImpl instance = null;

    private static RegisterRepoImpl getInstance() {
        if (instance == null) {
            instance = new RegisterRepoImpl();
        }
        return instance;
    }

    public RegisterRepoImpl() {
        regErrorMLD = new MutableLiveData<String>();
        progressbarObservable = new MutableLiveData<Boolean>();
    }

    @Override
    public MutableLiveData<RegisterModel> checkRegisterRepoWithoutImage(Map<String, String> params) throws Exception {
        progressbarObservable.setValue(true);
        Retrofit retrofit = ApiClient.getRetrofit();
        MutableLiveData<RegisterModel> registerModelWI_MLD = new MutableLiveData<RegisterModel>();
        ApiCallBack apiService = retrofit.create(ApiCallBack.class);
        apiService.sellerRegistrationWithoutImage(params).enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                progressbarObservable.setValue(false);
                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Log.e("appSample", "Success: " + response.body());
                            registerModelWI_MLD.setValue(response.body());
                        }
                    } else {
                        registerModelWI_MLD.setValue(null);
                        regErrorMLD.setValue("No Response");
                    }
                } catch (Exception e) {
                    Log.e("appSample", "regExc: " + e.getMessage());
                    registerModelWI_MLD.setValue(null);
                    regErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                Log.e("appSample", "RegFailure: " + t.getMessage());
                progressbarObservable.setValue(false);
                registerModelWI_MLD.setValue(null);
                regErrorMLD.setValue("Connection Error");
            }
        });

        return registerModelWI_MLD;
    }

    @Override
    public MutableLiveData<RegisterModel> updateProfileRepo(Map<String, String> params) throws Exception {
        progressbarObservable.setValue(true);
        MutableLiveData<RegisterModel> updateProfileModel_MLD = new MutableLiveData<RegisterModel>();
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<RegisterModel> call = callBack.updateProfile(params);
        call.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                progressbarObservable.setValue(false);
                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            updateProfileModel_MLD.setValue(response.body());
                        }
                    } else {
                        updateProfileModel_MLD.setValue(null);
                        regErrorMLD.setValue("No Response");
                    }
                } catch (Exception e) {
                    Log.e("appSample", "ResponseExc: " + e.getMessage());
                    updateProfileModel_MLD.setValue(null);
                    regErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                progressbarObservable.setValue(false);
                Log.e("appSample", "ResponseFailure: " + t.getMessage());
                updateProfileModel_MLD.setValue(null);
                regErrorMLD.setValue("Connection Error");
            }
        });
        return updateProfileModel_MLD;
    }

    @Override
    public MutableLiveData<RegisterModel> checkRegisterRepo(File file, Map<String, String> params) throws Exception {
        Log.e("appSample", "checkRegisterRepo");

        progressbarObservable.setValue(true);

        RequestBody r_CountryCode = RequestBody.create(MediaType.parse("text/plain"), params.get("country_code"));
        RequestBody r_MobileNumber = RequestBody.create(MediaType.parse("text/plain"), params.get("mobile_number"));
        RequestBody r_ImieNo = RequestBody.create(MediaType.parse("text/plain"), params.get("imie_no"));
        RequestBody r_GcmId = RequestBody.create(MediaType.parse("text/plain"), params.get("gcm_id"));
        RequestBody r_Latitude = RequestBody.create(MediaType.parse("text/plain"), params.get("latitude"));
        RequestBody r_Longitude = RequestBody.create(MediaType.parse("text/plain"), params.get("longitude"));
        RequestBody r_Shop = RequestBody.create(MediaType.parse("text/plain"), params.get("shop"));
        RequestBody r_Category = RequestBody.create(MediaType.parse("text/plain"), params.get("cat_id"));
        RequestBody r_Address = RequestBody.create(MediaType.parse("text/plain"), params.get("address"));
        RequestBody r_close_time = RequestBody.create(MediaType.parse("text/plain"), params.get("close_time"));
        RequestBody r_open_time = RequestBody.create(MediaType.parse("text/plain"), params.get("open_time"));

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/*"), file);
        Retrofit retrofit = ApiClient.getRetrofit();

        MutableLiveData<RegisterModel> registerModel_MLD = new MutableLiveData<RegisterModel>();

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        ApiCallBack apiService = retrofit.create(ApiCallBack.class);

        apiService.sellerRegistration(body, r_CountryCode, r_MobileNumber, r_ImieNo, r_GcmId, r_Latitude, r_Longitude,
                r_Shop, r_Category, r_Address, r_open_time, r_close_time
        ).enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                progressbarObservable.setValue(false);
                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Log.e("appSample", "Success: " + response.body());
                            registerModel_MLD.setValue(response.body());
                        }
                    } else {
                        registerModel_MLD.setValue(null);
                        regErrorMLD.setValue("No Response");
                    }
                } catch (Exception e) {
                    Log.e("appSample", "regExc: " + e.getMessage());
                    registerModel_MLD.setValue(null);
                    regErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                Log.e("appSample", "RegFailure: " + t.getMessage());
                progressbarObservable.setValue(false);
                registerModel_MLD.setValue(null);
                regErrorMLD.setValue("Connection Error");
            }
        });


        return registerModel_MLD;
    }

    @Override
    public MutableLiveData<String> CheckREgRepoERror() throws Exception {
        return regErrorMLD;
    }

    @Override
    public MutableLiveData<Boolean> progressUpdation() {
        return progressbarObservable;
    }
}
