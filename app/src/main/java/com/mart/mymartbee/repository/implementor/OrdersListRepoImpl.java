package com.mart.mymartbee.repository.implementor;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.Order_Status_Model;
import com.mart.mymartbee.model.Orders_Model;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.networking.retrofit.ApiCallBack;
import com.mart.mymartbee.networking.retrofit.ApiClient;
import com.mart.mymartbee.repository.interfaces.OrdersListRepo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersListRepoImpl implements OrdersListRepo {

    private MutableLiveData<String> ordersErrRepo;
    private MutableLiveData<Boolean> progressOrdersObservable;

    private static OrdersListRepoImpl instance = null;
    private static OrdersListRepoImpl getInstance() {
        if(instance == null) {
            instance = new OrdersListRepoImpl();
        }
        return instance;
    }

    public OrdersListRepoImpl() {
        ordersErrRepo = new MutableLiveData<String>();
        progressOrdersObservable = new MutableLiveData<Boolean>();
    }

    @Override
    public MutableLiveData<Order_Status_Model> getOrderStatus() throws Exception {
        progressOrdersObservable.setValue(true);
        MutableLiveData<Order_Status_Model> orderStatusListMLD = new MutableLiveData<Order_Status_Model>();
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<Order_Status_Model> call = callBack.getOrdersStatusList();
        call.enqueue(new Callback<Order_Status_Model>() {
            @Override
            public void onResponse(Call<Order_Status_Model> call, Response<Order_Status_Model> response) {
                progressOrdersObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            orderStatusListMLD.setValue(response.body());
                        }
                    } else {
                        orderStatusListMLD.setValue(null);
                        ordersErrRepo.setValue("No Response");
                    }
                } catch(Exception e) {
                    orderStatusListMLD.setValue(null);
                    ordersErrRepo.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Order_Status_Model> call, Throwable t) {
                progressOrdersObservable.setValue(false);
                orderStatusListMLD.setValue(null);
                ordersErrRepo.setValue("Connection Error");
            }
        });
        return orderStatusListMLD;
    }

    @Override
    public MutableLiveData<Orders_Model> updateOrderStatusRepo(Map<String, String> params) throws Exception {
        progressOrdersObservable.setValue(true);
        MutableLiveData<Orders_Model> updOrdersListMLD = new MutableLiveData<Orders_Model>();
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<Orders_Model> call = callBack.updateOrderStatus(params);
        call.enqueue(new Callback<Orders_Model>() {
            @Override
            public void onResponse(Call<Orders_Model> call, Response<Orders_Model> response) {
                progressOrdersObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            updOrdersListMLD.setValue(response.body());
                        }
                    } else {
                        updOrdersListMLD.setValue(null);
                        ordersErrRepo.setValue("No Response");
                    }
                } catch(Exception e) {
                    updOrdersListMLD.setValue(null);
                    ordersErrRepo.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Orders_Model> call, Throwable t) {
                progressOrdersObservable.setValue(false);
                updOrdersListMLD.setValue(null);
                ordersErrRepo.setValue("Connection Error");
            }
        });
        return updOrdersListMLD;
    }

    @Override
    public MutableLiveData<Orders_Model> getOrdersRepo(String seller_id) throws Exception {
        Log.e("appSample", "SellerId: " + seller_id);
//        seller_id = "1";
        progressOrdersObservable.setValue(true);
        MutableLiveData<Orders_Model> ordersListMLD = new MutableLiveData<Orders_Model>();
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<Orders_Model> call = callBack.getOrders(seller_id);
        call.enqueue(new Callback<Orders_Model>() {
            @Override
            public void onResponse(Call<Orders_Model> call, Response<Orders_Model> response) {
                progressOrdersObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            ordersListMLD.setValue(response.body());
                        }
                    } else {
                        ordersListMLD.setValue(null);
                        ordersErrRepo.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "Excep: " + e.getMessage());
                    ordersListMLD.setValue(null);
                    ordersErrRepo.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Orders_Model> call, Throwable t) {
                Log.e("appSample", "Failure: " + t.getMessage());
                progressOrdersObservable.setValue(false);
                ordersListMLD.setValue(null);
                ordersErrRepo.setValue("Connection Error");
            }
        });
        return ordersListMLD;
    }

    @Override
    public MutableLiveData<String> getOrdersRepoError() throws Exception {
        return ordersErrRepo;
    }

    @Override
    public MutableLiveData<Boolean> progressOrdersUpdation() {
        return progressOrdersObservable;
    }
}
