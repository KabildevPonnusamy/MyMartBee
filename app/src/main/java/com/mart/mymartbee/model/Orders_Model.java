package com.mart.mymartbee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Orders_Model {
    @SerializedName("status")
    @Expose
    private boolean strStatus;
    @SerializedName("orders")
    @Expose
    ArrayList<OrdersList> ordersList;

    public boolean isStrStatus() {
        return strStatus;
    }

    public void setStrStatus(boolean strStatus) {
        this.strStatus = strStatus;
    }

    public ArrayList<OrdersList> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(ArrayList<OrdersList> ordersList) {
        this.ordersList = ordersList;
    }

    public static class OrdersList {
        @SerializedName("order_id")
        @Expose
        private String strOrderId;
        @SerializedName("status")
        @Expose
        private String strStatus;
        @SerializedName("order_date")
        @Expose
        private String strOrderDate;
        @SerializedName("total_amount")
        @Expose
        private String strTotalAmount;
        @SerializedName("country_code")
        @Expose
        private String strCountryCode;
        @SerializedName("phone")
        @Expose
        private String strPhone;
        @SerializedName("address")
        @Expose
        private String strAddress;
        @SerializedName("products")
        @Expose
        ArrayList<OrderedProducts> orderedProductsList;
        @SerializedName("order_history")
        @Expose
        ArrayList<OrderHistory> orderHistoryList;

        public ArrayList<OrderHistory> getOrderHistoryList() {
            return orderHistoryList;
        }

        public void setOrderHistoryList(ArrayList<OrderHistory> orderHistoryList) {
            this.orderHistoryList = orderHistoryList;
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

        public String getStrOrderId() {
            return strOrderId;
        }

        public void setStrOrderId(String strOrderId) {
            this.strOrderId = strOrderId;
        }

        public String getStrStatus() {
            return strStatus;
        }

        public void setStrStatus(String strStatus) {
            this.strStatus = strStatus;
        }

        public String getStrOrderDate() {
            return strOrderDate;
        }

        public void setStrOrderDate(String strOrderDate) {
            this.strOrderDate = strOrderDate;
        }

        public String getStrTotalAmount() {
            return strTotalAmount;
        }

        public void setStrTotalAmount(String strTotalAmount) {
            this.strTotalAmount = strTotalAmount;
        }

        public ArrayList<OrderedProducts> getOrderedProductsList() {
            return orderedProductsList;
        }

        public void setOrderedProductsList(ArrayList<OrderedProducts> orderedProductsList) {
            this.orderedProductsList = orderedProductsList;
        }

        public static class OrderHistory {
            @SerializedName("date_added")
            @Expose
            private String strAddedDate;
            @SerializedName("name")
            @Expose
            private String strStatusName;

            public String getStrAddedDate() {
                return strAddedDate;
            }

            public void setStrAddedDate(String strAddedDate) {
                this.strAddedDate = strAddedDate;
            }

            public String getStrStatusName() {
                return strStatusName;
            }

            public void setStrStatusName(String strStatusName) {
                this.strStatusName = strStatusName;
            }
        }

        public static class OrderedProducts {
            @SerializedName("product_id")
            @Expose
            private String strProductId;
            @SerializedName("image")
            @Expose
            private String strProductImage;
            @SerializedName("quantity")
            @Expose
            private String strProductQuantity;
            @SerializedName("title")
            @Expose
            private String strProductTitle;
            @SerializedName("price")
            @Expose
            private String strProductPrice;
            @SerializedName("total")
            @Expose
            private String strProductTotal;
            @SerializedName("categorie_name")
            @Expose
            private String strCategoryName;

            public String getStrCategoryName() {
                return strCategoryName;
            }

            public void setStrCategoryName(String strCategoryName) {
                this.strCategoryName = strCategoryName;
            }

            public String getStrProductId() {
                return strProductId;
            }

            public void setStrProductId(String strProductId) {
                this.strProductId = strProductId;
            }

            public String getStrProductImage() {
                return strProductImage;
            }

            public void setStrProductImage(String strProductImage) {
                this.strProductImage = strProductImage;
            }

            public String getStrProductQuantity() {
                return strProductQuantity;
            }

            public void setStrProductQuantity(String strProductQuantity) {
                this.strProductQuantity = strProductQuantity;
            }

            public String getStrProductTitle() {
                return strProductTitle;
            }

            public void setStrProductTitle(String strProductTitle) {
                this.strProductTitle = strProductTitle;
            }

            public String getStrProductPrice() {
                return strProductPrice;
            }

            public void setStrProductPrice(String strProductPrice) {
                this.strProductPrice = strProductPrice;
            }

            public String getStrProductTotal() {
                return strProductTotal;
            }

            public void setStrProductTotal(String strProductTotal) {
                this.strProductTotal = strProductTotal;
            }
        }
    }
}
