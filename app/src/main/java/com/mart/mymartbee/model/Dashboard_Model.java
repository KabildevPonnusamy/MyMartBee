package com.mart.mymartbee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Dashboard_Model {

    @SerializedName("status")
    @Expose
    private boolean strStatus;
    @SerializedName("page_view_count")
    @Expose
    private String strPageViewCount;
    @SerializedName("product_view_count")
    @Expose
    private String strProductViewCount;
    @SerializedName("order_count")
    @Expose
    private String strOrderCount;
    @SerializedName("total_revenue")
    @Expose
    private String strTotalRevenue;
    @SerializedName("link")
    @Expose
    private String strStoreLink;
    @SerializedName("product_list")
    @Expose
    public ArrayList<ViewedProductList> viewedProductList;

    public boolean isStrStatus() {
        return strStatus;
    }

    public void setStrStatus(boolean strStatus) {
        this.strStatus = strStatus;
    }

    public String getStrPageViewCount() {
        return strPageViewCount;
    }

    public void setStrPageViewCount(String strPageViewCount) {
        this.strPageViewCount = strPageViewCount;
    }

    public String getStrProductViewCount() {
        return strProductViewCount;
    }

    public void setStrProductViewCount(String strProductViewCount) {
        this.strProductViewCount = strProductViewCount;
    }

    public String getStrOrderCount() {
        return strOrderCount;
    }

    public void setStrOrderCount(String strOrderCount) {
        this.strOrderCount = strOrderCount;
    }

    public String getStrTotalRevenue() {
        return strTotalRevenue;
    }

    public void setStrTotalRevenue(String strTotalRevenue) {
        this.strTotalRevenue = strTotalRevenue;
    }

    public String getStrStoreLink() {
        return strStoreLink;
    }

    public void setStrStoreLink(String strStoreLink) {
        this.strStoreLink = strStoreLink;
    }

    public ArrayList<ViewedProductList> getViewedProductList() {
        return viewedProductList;
    }

    public void setViewedProductList(ArrayList<ViewedProductList> viewedProductList) {
        this.viewedProductList = viewedProductList;
    }

    public ArrayList<PendingOrdersList> getPendingOrdersLists() {
        return pendingOrdersLists;
    }

    public void setPendingOrdersLists(ArrayList<PendingOrdersList> pendingOrdersLists) {
        this.pendingOrdersLists = pendingOrdersLists;
    }

    @SerializedName("pending_order_list")
    @Expose
    public ArrayList<PendingOrdersList> pendingOrdersLists;

    public static class PendingOrdersList {
        @SerializedName("order_id")
        @Expose
        String strOrderId;
        @SerializedName("country_code")
        @Expose
        String strCountryCode;
        @SerializedName("phone")
        @Expose
        String strPhone;
        @SerializedName("address")
        @Expose
        String strAddress;
        @SerializedName("status")
        @Expose
        String strStatus;
        @SerializedName("order_date")
        @Expose
        String strOrderDate;
        @SerializedName("total_amount")
        @Expose
        String strTotalAmount;
        @SerializedName("products")
        @Expose
        public ArrayList<PendingProductsList> pendingProductsList;

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

        public ArrayList<PendingProductsList> getPendingProductsList() {
            return pendingProductsList;
        }

        public void setPendingProductsList(ArrayList<PendingProductsList> pendingProductsList) {
            this.pendingProductsList = pendingProductsList;
        }

        public ArrayList<PendingOrderHistoryList> getPendingOrderHistoryList() {
            return pendingOrderHistoryList;
        }

        public void setPendingOrderHistoryList(ArrayList<PendingOrderHistoryList> pendingOrderHistoryList) {
            this.pendingOrderHistoryList = pendingOrderHistoryList;
        }

        @SerializedName("order_history")
        @Expose
        public ArrayList<PendingOrderHistoryList> pendingOrderHistoryList;

        public static class PendingProductsList {
            @SerializedName("product_id")
            @Expose
            String strProductId;
            @SerializedName("image")
            @Expose
            String strProductImage;
            @SerializedName("quantity")
            @Expose
            String strProductQuantity;
            @SerializedName("title")
            @Expose
            String strproductTitle;
            @SerializedName("price")
            @Expose
            String strProductPrice;
            @SerializedName("total")
            @Expose
            String strProductTotal;

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

            public String getStrproductTitle() {
                return strproductTitle;
            }

            public void setStrproductTitle(String strproductTitle) {
                this.strproductTitle = strproductTitle;
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

        public static class PendingOrderHistoryList {
            @SerializedName("date_added")
            @Expose
            String strDateodAdded;
            @SerializedName("name")
            @Expose
            String strName;

            public String getStrDateodAdded() {
                return strDateodAdded;
            }

            public void setStrDateodAdded(String strDateodAdded) {
                this.strDateodAdded = strDateodAdded;
            }

            public String getStrName() {
                return strName;
            }

            public void setStrName(String strName) {
                this.strName = strName;
            }
        }
    }

    public static class ViewedProductList {
        @SerializedName("viewed")
        @Expose
        String strViewed;
        @SerializedName("title")
        @Expose
        String strProductName;
        @SerializedName("produt_id")
        @Expose
        String strProductId;
        @SerializedName("image")
        @Expose
        String strProductImage;
        @SerializedName("quantity")
        @Expose
        String strProductQuantity;
        @SerializedName("description")
        @Expose
        String strProductDescription;
        @SerializedName("price")
        @Expose
        String strProductPrice;
        @SerializedName("old_price")
        @Expose
        String strProductOldPrice;
        @SerializedName("uom")
        @Expose
        String strProductUOM;
        @SerializedName("cat_id")
        @Expose
        String strCateId;
        @SerializedName("cat_name")
        @Expose
        String strCateName;
        @SerializedName("sub_cat_id")
        @Expose
        String strSubCateId;
        @SerializedName("sub_cat_name")
        @Expose
        String strSubCateName;

        public String getStrCateId() {
            return strCateId;
        }

        public void setStrCateId(String strCateId) {
            this.strCateId = strCateId;
        }

        public String getStrCateName() {
            return strCateName;
        }

        public void setStrCateName(String strCateName) {
            this.strCateName = strCateName;
        }

        public String getStrSubCateId() {
            return strSubCateId;
        }

        public void setStrSubCateId(String strSubCateId) {
            this.strSubCateId = strSubCateId;
        }

        public String getStrSubCateName() {
            return strSubCateName;
        }

        public void setStrSubCateName(String strSubCateName) {
            this.strSubCateName = strSubCateName;
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

        public String getStrProductDescription() {
            return strProductDescription;
        }

        public void setStrProductDescription(String strProductDescription) {
            this.strProductDescription = strProductDescription;
        }

        public String getStrProductPrice() {
            return strProductPrice;
        }

        public void setStrProductPrice(String strProductPrice) {
            this.strProductPrice = strProductPrice;
        }

        public String getStrProductOldPrice() {
            return strProductOldPrice;
        }

        public void setStrProductOldPrice(String strProductOldPrice) {
            this.strProductOldPrice = strProductOldPrice;
        }

        public String getStrProductUOM() {
            return strProductUOM;
        }

        public void setStrProductUOM(String strProductUOM) {
            this.strProductUOM = strProductUOM;
        }

        public String getStrViewed() {
            return strViewed;
        }

        public void setStrViewed(String strViewed) {
            this.strViewed = strViewed;
        }

        public String getStrProductName() {
            return strProductName;
        }

        public void setStrProductName(String strProductName) {
            this.strProductName = strProductName;
        }

    }

}
