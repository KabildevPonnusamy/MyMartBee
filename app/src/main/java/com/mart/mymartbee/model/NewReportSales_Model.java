package com.mart.mymartbee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewReportSales_Model {

    @SerializedName("status")
    @Expose
    private boolean strStatus;
    @SerializedName("total_revenue")
    @Expose
    String strTotalRevenue;
    @SerializedName("category_revenue")
    @Expose
    ArrayList<CategoryRevenue> categoryRevenue;

    public boolean isStrStatus() {
        return strStatus;
    }

    public void setStrStatus(boolean strStatus) {
        this.strStatus = strStatus;
    }

    public String getStrTotalRevenue() {
        return strTotalRevenue;
    }

    public void setStrTotalRevenue(String strTotalRevenue) {
        this.strTotalRevenue = strTotalRevenue;
    }

    public ArrayList<CategoryRevenue> getCategoryRevenue() {
        return categoryRevenue;
    }

    public void setCategoryRevenue(ArrayList<CategoryRevenue> categoryRevenue) {
        this.categoryRevenue = categoryRevenue;
    }

    public static class CategoryRevenue {

        @SerializedName("category")
        @Expose
        String strCategory;
        @SerializedName("cat_id")
        @Expose
        String strCateId;
        @SerializedName("total")
        @Expose
        String strTotal;
        @SerializedName("month")
        @Expose
        String strMonth;
        @SerializedName("percent")
        @Expose
        String strPercent;
        @SerializedName("color")
        @Expose
        String strColor;

        public String getStrCateId() {
            return strCateId;
        }

        public void setStrCateId(String strCateId) {
            this.strCateId = strCateId;
        }

        public String getStrCategory() {
            return strCategory;
        }

        public void setStrCategory(String strCategory) {
            this.strCategory = strCategory;
        }

        public String getStrTotal() {
            return strTotal;
        }

        public void setStrTotal(String strTotal) {
            this.strTotal = strTotal;
        }

        public String getStrMonth() {
            return strMonth;
        }

        public void setStrMonth(String strMonth) {
            this.strMonth = strMonth;
        }

        public String getStrPercent() {
            return strPercent;
        }

        public void setStrPercent(String strPercent) {
            this.strPercent = strPercent;
        }

        public String getStrColor() {
            return strColor;
        }

        public void setStrColor(String strColor) {
            this.strColor = strColor;
        }

    }
}
