package com.mart.mymartbee.viewmodel.interfaces;

import androidx.lifecycle.LiveData;

import com.mart.mymartbee.model.Order_Status_Model;
import com.mart.mymartbee.model.Orders_Model;

import java.util.Map;

public interface OrdersViewModel {

    LiveData<Orders_Model> getOrdersLV();
    LiveData<Order_Status_Model> getOrderStatusListLV();
    LiveData<Orders_Model> updateOrderStatusLV();
    LiveData<String> getOrdersErrorsLV();
    LiveData<Boolean> getProgressUpdateLV();

    void getOrders(String sellerId);
    void getOrderStatusList();
    void updateOrderStatusList(Map<String, String> params);
}
