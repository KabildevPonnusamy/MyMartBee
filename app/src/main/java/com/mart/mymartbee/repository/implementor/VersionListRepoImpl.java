package com.mart.mymartbee.repository.implementor;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.CommonResponseModel;
import com.mart.mymartbee.networking.retrofit.ApiCallBack;
import com.mart.mymartbee.networking.retrofit.ApiClient;
import com.mart.mymartbee.repository.interfaces.VersionListRepo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VersionListRepoImpl implements VersionListRepo {

    private MutableLiveData<String> versionErrorMLD;
    private MutableLiveData<Boolean> versionVersionObservable;

    private static VersionListRepoImpl instance = null;

    private static VersionListRepoImpl getInstance() {
        if(instance == null) {
            instance = new VersionListRepoImpl();
        }
        return instance;
    }

    public VersionListRepoImpl() {

        versionErrorMLD = new MutableLiveData<String>();
        versionVersionObservable = new MutableLiveData<Boolean>();
    }

    @Override
    public MutableLiveData<CommonResponseModel> getVersionList(Map<String, String> params) throws Exception {
        versionVersionObservable.setValue(true);
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        MutableLiveData<CommonResponseModel>  version_modelMLD = new MutableLiveData<CommonResponseModel>();
        Call<CommonResponseModel> call = callBack.checkVersion(params);
        call.enqueue(new Callback<CommonResponseModel>() {
            @Override
            public void onResponse(Call<CommonResponseModel> call, Response<CommonResponseModel> response) {
                versionVersionObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            version_modelMLD.setValue(response.body());
                        }
                    } else {
                        version_modelMLD.setValue(null);
                        versionErrorMLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "ResponseExc: " + e.getMessage());
                    version_modelMLD.setValue(null);
                    versionErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CommonResponseModel> call, Throwable t) {
                versionVersionObservable.setValue(false);
                version_modelMLD.setValue(null);
                versionErrorMLD.setValue("Connection Error");
            }
        });
        return version_modelMLD;
    }

    @Override
    public MutableLiveData<String> getVersionRepoError() throws Exception {
        return versionErrorMLD;
    }

    @Override
    public MutableLiveData<Boolean> progressVersionUpdation() {
        return versionVersionObservable;
    }
}
