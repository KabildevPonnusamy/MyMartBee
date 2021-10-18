package com.mart.mymartbee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Reports_Model {
    @SerializedName("status")
    @Expose
    private boolean strStatus;
    @SerializedName("orders")
    @Expose
    ArrayList<ReportsList> reportsList;

    public boolean isStrStatus() {
        return strStatus;
    }

    public void setStrStatus(boolean strStatus) {
        this.strStatus = strStatus;
    }

    public ArrayList<ReportsList> getReportsList() {
        return reportsList;
    }

    public void setReportsList(ArrayList<ReportsList> reportsList) {
        this.reportsList = reportsList;
    }

    public static class ReportsList {
        @SerializedName("order_id")
        @Expose
        private String strOrderId;
        @SerializedName("country_code")
        @Expose
        private String strCountryCode;
        @SerializedName("phone")
        @Expose
        private String strPhone;
        @SerializedName("address")
        @Expose
        private String strAddress;
        @SerializedName("seller_comment")
        @Expose
        private String strSellerComment;
        @SerializedName("status")
        @Expose
        private String strStatus;
        @SerializedName("order_date")
        @Expose
        private String strOrderedDate;
        @SerializedName("total_amount")
        @Expose
        private String strTotalAmount;
        @SerializedName("payment_type")
        @Expose
        private String strPaymentType;
        @SerializedName("order_history")
        @Expose
        ArrayList<OrderHistory> orderHistoryList;
        @SerializedName("products")
        @Expose
        ArrayList<ReportsProducts> productsList;

        public ArrayList<OrderHistory> getOrderHistoryList() {
            return orderHistoryList;
        }

        public void setOrderHistoryList(ArrayList<OrderHistory> orderHistoryList) {
            this.orderHistoryList = orderHistoryList;
        }

        public ArrayList<ReportsProducts> getProductsList() {
            return productsList;
        }

        public void setProductsList(ArrayList<ReportsProducts> productsList) {
            this.productsList = productsList;
        }

        public String getStrOrderId() {
            return strOrderId;
        }

        public void setStrOrderId(String strOrderId) {
            this.strOrderId = strOrderId;
        }

        public String getStrCountryCode() {
            return strCountryCode;
        }

        public void setStrCountryCode(String strCountryCode) {
            this.strCountryCode = strCountryCode;
        }

        public String getStrPhone() {
            return strPhone;
        }

        public void setStrPhone(String strPhone) {
            this.strPhone = strPhone;
        }

        public String getStrAddress() {
            return strAddress;
        }

        public void setStrAddress(String strAddress) {
            this.strAddress = strAddress;
        }

        public String getStrSellerComment() {
            return strSellerComment;
        }

        public void setStrSellerComment(String strSellerComment) {
            this.strSellerComment = strSellerComment;
        }

        public String getStrStatus() {
            return strStatus;
        }

        public void setStrStatus(String strStatus) {
            this.strStatus = strStatus;
        }

        public String getStrOrderedDate() {
            return strOrderedDate;
        }

        public void setStrOrderedDate(String strOrderedDate) {
            this.strOrderedDate = strOrderedDate;
        }

        public String getStrTotalAmount() {
            return strTotalAmount;
        }

        public void setStrTotalAmount(String strTotalAmount) {
            this.strTotalAmount = strTotalAmount;
        }

        public String getStrPaymentType() {
            return strPaymentType;
        }

        public void setStrPaymentType(String strPaymentType) {
            this.strPaymentType = strPaymentType;
        }


    }

    public static class OrderHistory {
        @SerializedName("date_added")
        @Expose
        private String strDateAdded;
        @SerializedName("name")
        @Expose
        private String strName;
        public String getStrDateAdded() {
            return strDateAdded;
        }

        public void setStrDateAdded(String strDateAdded) {
            this.strDateAdded = strDateAdded;
        }

        public String getStrName() {
            return strName;
        }

        public void setStrName(String strName) {
            this.strName = strName;
        }
    }

    public static class ReportsProducts {
        @SerializedName("product_id")
        @Expose
        private String strProductId;
        @SerializedName("image")
        @Expose
        private String strImage;
        @SerializedName("quantity")
        @Expose
        private String strQuantity;
        @SerializedName("title")
        @Expose
        private String strTitle;
        @SerializedName("price")
        @Expose
        private String strPrice;
        @SerializedName("total")
        @Expose
        private String strTotal;

        public String getStrProductId() {
            return strProductId;
        }

        public void setStrProductId(String strProductId) {
            this.strProductId = strProductId;
        }

        public String getStrImage() {
            return strImage;
        }

        public void setStrImage(String strImage) {
            this.strImage = strImage;
        }

        public String getStrQuantity() {
            return strQuantity;
        }

        public void setStrQuantity(String strQuantity) {
            this.strQuantity = strQuantity;
        }

        public String getStrTitle() {
            return strTitle;
        }

        public void setStrTitle(String strTitle) {
            this.strTitle = strTitle;
        }

        public String getStrPrice() {
            return strPrice;
        }

        public void setStrPrice(String strPrice) {
            this.strPrice = strPrice;
        }

        public String getStrTotal() {
            return strTotal;
        }

        public void setStrTotal(String strTotal) {
            this.strTotal = strTotal;
        }
    }
}
