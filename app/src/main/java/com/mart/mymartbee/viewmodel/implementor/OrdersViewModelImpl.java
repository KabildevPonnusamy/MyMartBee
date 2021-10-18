package com.mart.mymartbee.viewmodel.implementor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mart.mymartbee.model.Order_Status_Model;
import com.mart.mymartbee.model.Orders_Model;
import com.mart.mymartbee.repository.implementor.OrdersListRepoImpl;
import com.mart.mymartbee.viewmodel.interfaces.OrdersViewModel;

import java.util.Map;

public class OrdersViewModelImpl extends ViewModel implements OrdersViewModel {

    private MutableLiveData<Orders_Model> mutablegetOrders;
    private MutableLiveData<Orders_Model> mutableOStatusList;
    private MutableLiveData<Order_Status_Model> mutablegetOrdersList;
    private MutableLiveData<String> mutableOrderError;
    private MutableLiveData<Boolean> progressOrderObserve;
    OrdersListRepoImpl ordersListRepo;

    public OrdersViewModelImpl() {
        ordersListRepo = new OrdersListRepoImpl();
        mutablegetOrders = new MutableLiveData<Orders_Model>();
        mutableOStatusList = new MutableLiveData<Orders_Model>();
        mutablegetOrdersList = new MutableLiveData<Order_Status_Model>();
        mutableOrderError = new MutableLiveData<String>();
        progressOrderObserve = new MutableLiveData<Boolean>();
    }

    @Override
    public LiveData<Orders_Model> updateOrderStatusLV() {
        return mutableOStatusList;
    }

    @Override
    public void updateOrderStatusList(Map<String, String> params) {
        try {
            mutableOStatusList = ordersListRepo.updateOrderStatusRepo(params);
        } catch(Exception e) {
            mutableOrderError.setValue(e.getMessage());
        }
    }

    @Override
    public LiveData<Order_Status_Model> getOrderStatusListLV() {
        return mutablegetOrdersList;
    }

    @Override
    public void getOrderStatusList() {
        try {
            mutablegetOrdersList = ordersListRepo.getOrderStatus();
        } catch (Exception e) {
            mutableOrderError.setValue(e.getMessage());
        }
    }

    @Override
    public LiveData<Orders_Model> getOrdersLV() {
        return mutablegetOrders;
    }

    @Override
    public LiveData<String> getOrdersErrorsLV() {
        return mutableOrderError;
    }

    @Override
    public LiveData<Boolean> getProgressUpdateLV() {
        try {
            progressOrderObserve = ordersListRepo.progressOrdersUpdation();
        } catch(Exception e) {
            progressOrderObserve.setValue(false);
        }
        return progressOrderObserve;
    }

    @Override
    public void getOrders(String sellerId) {
        try {
            mutablegetOrders = ordersListRepo.getOrdersRepo(sellerId);
        } catch(Exception e) {
            mutableOrderError.setValue(e.getMessage());
        }
    }
}
