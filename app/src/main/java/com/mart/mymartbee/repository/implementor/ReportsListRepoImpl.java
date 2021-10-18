package com.mart.mymartbee.repository.implementor;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.Reports_Model;
import com.mart.mymartbee.networking.retrofit.ApiCallBack;
import com.mart.mymartbee.networking.retrofit.ApiClient;
import com.mart.mymartbee.repository.interfaces.ReportsListRepo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsListRepoImpl implements ReportsListRepo {

    private MutableLiveData<String> reportsErrorRepo;
    private MutableLiveData<Boolean> progressReportsObservable;

    private static ReportsListRepoImpl instance = null;
    private static ReportsListRepoImpl getInstance() {
        if(instance == null) {
            instance = new ReportsListRepoImpl();
        }
        return instance;
    }

    public ReportsListRepoImpl() {
        reportsErrorRepo = new MutableLiveData<String>();
        progressReportsObservable = new MutableLiveData<Boolean>();
    }

    @Override
    public MutableLiveData<Reports_Model> getReportsRepo(String seller_id) throws Exception {
//        seller_id = "1";
        progressReportsObservable.setValue(true);
        MutableLiveData<Reports_Model> reportsListMLD = new MutableLiveData<Reports_Model>();
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<Reports_Model> call = callBack.getReportsList(seller_id);
        call.enqueue(new Callback<Reports_Model>() {
            @Override
            public void onResponse(Call<Reports_Model> call, Response<Reports_Model> response) {
                progressReportsObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            reportsListMLD.setValue(response.body());
                        }
                    } else {
                        reportsListMLD.setValue(null);
                        reportsErrorRepo.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "Excep: " + e.getMessage());
                    reportsListMLD.setValue(null);
                    reportsErrorRepo.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Reports_Model> call, Throwable t) {
                Log.e("appSample", "Failure: " + t.getMessage());
                progressReportsObservable.setValue(false);
                reportsListMLD.setValue(null);
                reportsErrorRepo.setValue("Connection Error");
            }
        });
        return reportsListMLD;
    }

    @Override
    public MutableLiveData<String> reportsReporError() throws Exception {
        return reportsErrorRepo;
    }

    @Override
    public MutableLiveData<Boolean> progressReportUpdation() throws Exception {
        return progressReportsObservable;
    }
}
