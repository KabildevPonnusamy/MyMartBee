package com.mart.mymartbee.repository.implementor;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.NewReportProducts_Model;
import com.mart.mymartbee.model.NewReportSales_Model;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.networking.retrofit.ApiCallBack;
import com.mart.mymartbee.networking.retrofit.ApiClient;
import com.mart.mymartbee.repository.interfaces.NewReportListRepo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewReportListRepoImpl implements NewReportListRepo {

    private MutableLiveData<String> newReportErrorMLD;
    private MutableLiveData<Boolean> progressReportNewObservable;
    private static NewReportListRepoImpl instance = null;

    private static NewReportListRepoImpl getInstance() {
        if(instance == null) {
            instance = new NewReportListRepoImpl();
        }
        return instance;
    }

    public NewReportListRepoImpl() {
        newReportErrorMLD = new MutableLiveData<String>();
        progressReportNewObservable = new MutableLiveData<Boolean>();
    }

    @Override
    public MutableLiveData<NewReportProducts_Model> getProductReports(Map<String, String> params) throws Exception {
        progressReportNewObservable.setValue(true);
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        MutableLiveData<NewReportProducts_Model> productReportsModelMLD = new MutableLiveData<NewReportProducts_Model>();
        Call<NewReportProducts_Model> call = callBack.getProductReport(params);
        call.enqueue(new Callback<NewReportProducts_Model>() {
            @Override
            public void onResponse(Call<NewReportProducts_Model> call, Response<NewReportProducts_Model> response) {
                progressReportNewObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            productReportsModelMLD.setValue(response.body());
                        }
                    } else {
                        productReportsModelMLD.setValue(null);
                        newReportErrorMLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "Exception: " + e.getMessage());
                    productReportsModelMLD.setValue(null);
                    newReportErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<NewReportProducts_Model> call, Throwable t) {
                Log.e("appSample", "onFailure: " + t.getMessage());
                productReportsModelMLD.setValue(null);
                progressReportNewObservable.setValue(false);
                newReportErrorMLD.setValue("Connection Error");
            }
        });
        return productReportsModelMLD;
    }

    //
    @Override
    public MutableLiveData<NewReportSales_Model> getSalesReportsByYear(Map<String, String> params) throws Exception {
        progressReportNewObservable.setValue(true);
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        MutableLiveData<NewReportSales_Model> salesReportByYearModelMLD = new MutableLiveData<NewReportSales_Model>();
        Call<NewReportSales_Model> call = callBack.getSalesReportByYear(params);
        call.enqueue(new Callback<NewReportSales_Model>() {
            @Override
            public void onResponse(Call<NewReportSales_Model> call, Response<NewReportSales_Model> response) {
                progressReportNewObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            salesReportByYearModelMLD.setValue(response.body());
                        }
                    } else {
                        salesReportByYearModelMLD.setValue(null);
                        newReportErrorMLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    salesReportByYearModelMLD.setValue(null);
                    newReportErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<NewReportSales_Model> call, Throwable t) {
                Log.e("appSample", "Failure: " + t.getMessage());
                salesReportByYearModelMLD.setValue(null);
                progressReportNewObservable.setValue(false);
                newReportErrorMLD.setValue("Connection Error");
            }
        });
        return salesReportByYearModelMLD;
    }

    @Override
    public MutableLiveData<NewReportSales_Model> getSalesReports(Map<String, String> params) throws Exception {
        progressReportNewObservable.setValue(true);
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        MutableLiveData<NewReportSales_Model> salesReportModelMLD = new MutableLiveData<NewReportSales_Model>();
        Call<NewReportSales_Model> call = callBack.getSalesReport(params);
        call.enqueue(new Callback<NewReportSales_Model>() {
            @Override
            public void onResponse(Call<NewReportSales_Model> call, Response<NewReportSales_Model> response) {
                progressReportNewObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            salesReportModelMLD.setValue(response.body());
                        }
                    } else {
                        salesReportModelMLD.setValue(null);
                        newReportErrorMLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    salesReportModelMLD.setValue(null);
                    newReportErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<NewReportSales_Model> call, Throwable t) {
                Log.e("appSample", "Failur: " + t.getMessage());
                salesReportModelMLD.setValue(null);
                progressReportNewObservable.setValue(false);
                newReportErrorMLD.setValue("Connection Error");
            }
        });
        return salesReportModelMLD;
    }

    @Override
    public MutableLiveData<String> getReportRepoError() throws Exception {
        return newReportErrorMLD;
    }

    @Override
    public MutableLiveData<Boolean> progressReportUpdation() {
        return progressReportNewObservable;
    }
}
