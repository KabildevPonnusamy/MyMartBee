package com.mart.mymartbee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import retrofit2.http.FormUrlEncoded;

public class Order_Status_Model {

    @SerializedName("status")
    @Expose
    private boolean strStatus;
    @SerializedName("order_status")
    @Expose
    ArrayList<OrdersStatusList> ordersStatusList;

    public boolean isStrStatus() {
        return strStatus;
    }

    public void setStrStatus(boolean strStatus) {
        this.strStatus = strStatus;
    }

    public ArrayList<OrdersStatusList> getOrdersStatusList() {
        return ordersStatusList;
    }

    public void setOrdersStatusList(ArrayList<OrdersStatusList> ordersStatusList) {
        this.ordersStatusList = ordersStatusList;
    }

    public static class OrdersStatusList {
        @SerializedName("order_status_id")
        @Expose
        private String strOrderStatusId;
        @SerializedName("name")
        @Expose
        private String strOrderStatusName;

        public String getStrOrderStatusId() {
            return strOrderStatusId;
        }

        public void setStrOrderStatusId(String strOrderStatusId) {
            this.strOrderStatusId = strOrderStatusId;
        }

        public String getStrOrderStatusName() {
            return strOrderStatusName;
        }

        public void setStrOrderStatusName(String strOrderStatusName) {
            this.strOrderStatusName = strOrderStatusName;
        }

    }

}
