package com.mart.mymartbee.repository.implementor;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.Dashboard_Model;
import com.mart.mymartbee.model.Order_Status_Model;
import com.mart.mymartbee.networking.retrofit.ApiCallBack;
import com.mart.mymartbee.networking.retrofit.ApiClient;
import com.mart.mymartbee.repository.interfaces.DashboardRepo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepoImpl implements DashboardRepo {

    private MutableLiveData<String> errorStr;
    private MutableLiveData<Boolean> progressDashboardUpdate;

    public static DashboardRepoImpl instance = null;

    public static DashboardRepoImpl getInstance() {
        if (instance == null) {
            instance = new DashboardRepoImpl();
        }
        return instance;
    }

    public DashboardRepoImpl() {
        errorStr = new MutableLiveData<String>();
        progressDashboardUpdate = new MutableLiveData<Boolean>();
    }

    @Override
    public MutableLiveData<Dashboard_Model> getDashboardModel(String sellerId) throws Exception {
        Log.e("appSample", "SELLERID: " + sellerId);
        progressDashboardUpdate.setValue(true);
        MutableLiveData<Dashboard_Model> dashboard_MLD = new MutableLiveData<Dashboard_Model>();
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<Dashboard_Model> call = callBack.getDashboardDatas(sellerId);
        call.enqueue(new Callback<Dashboard_Model>() {
            @Override
            public void onResponse(Call<Dashboard_Model> call, Response<Dashboard_Model> response) {
                progressDashboardUpdate.setValue(false);
                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Log.e("appSample", "Success");
                            Log.e("appSample", "Count: " + response.body().getStrOrderCount());
                            Log.e("appSample", "Count: " + response.body().getViewedProductList().size());
                            dashboard_MLD.setValue(response.body());
                        }
                    } else {
                        dashboard_MLD.setValue(null);
                        errorStr.setValue("No Response");
                    }
                } catch (Exception e) {
                    Log.e("appSample", "Exception: " + e.getMessage());
                    dashboard_MLD.setValue(null);
                    errorStr.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Dashboard_Model> call, Throwable t) {
                Log.e("appSample", "Failure");
                progressDashboardUpdate.setValue(false);
                dashboard_MLD.setValue(null);
                errorStr.setValue("Connection Error");
            }
        });
        return dashboard_MLD;
    }

    @Override
    public MutableLiveData<String> getError() throws Exception {
        return errorStr;
    }

    @Override
    public MutableLiveData<Boolean> progressDashboardUpdation() {
        return progressDashboardUpdate;
    }
}
