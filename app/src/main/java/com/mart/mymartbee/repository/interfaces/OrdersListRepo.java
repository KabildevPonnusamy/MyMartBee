package com.mart.mymartbee.repository.interfaces;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.Order_Status_Model;
import com.mart.mymartbee.model.Orders_Model;

import java.util.Map;

public interface OrdersListRepo {
    MutableLiveData<Order_Status_Model> getOrderStatus() throws Exception;
    MutableLiveData<Orders_Model> getOrdersRepo(String seller_id) throws Exception;
    MutableLiveData<Orders_Model> updateOrderStatusRepo(Map<String, String> params) throws Exception;
    MutableLiveData<String> getOrdersRepoError() throws Exception;
    MutableLiveData<Boolean> progressOrdersUpdation();
}
