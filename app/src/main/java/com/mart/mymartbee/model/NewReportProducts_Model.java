package com.mart.mymartbee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewReportProducts_Model {

    @SerializedName("status")
    @Expose
    private boolean strStatus;
    @SerializedName("total_views")
    @Expose
    String strTotalViews;
    @SerializedName("category_views")
    @Expose
    ArrayList<CategoryViews> categoryViews;
    @SerializedName("product_views")
    @Expose
    ArrayList<ProductViews> productViews;

    public boolean isStrStatus() {
        return strStatus;
    }

    public void setStrStatus(boolean strStatus) {
        this.strStatus = strStatus;
    }

    public String getStrTotalViews() {
        return strTotalViews;
    }

    public void setStrTotalViews(String strTotalViews) {
        this.strTotalViews = strTotalViews;
    }

    public ArrayList<CategoryViews> getCategoryViews() {
        return categoryViews;
    }

    public void setCategoryViews(ArrayList<CategoryViews> categoryViews) {
        this.categoryViews = categoryViews;
    }

    public ArrayList<ProductViews> getProductViews() {
        return productViews;
    }

    public void setProductViews(ArrayList<ProductViews> productViews) {
        this.productViews = productViews;
    }

    public static class CategoryViews {
        @SerializedName("category")
        @Expose
        String strCategory;
        @SerializedName("cat_id")
        @Expose
        String strCateId;
        @SerializedName("cat_id")
        @Expose
        String strCat_id;
        @SerializedName("views")
        @Expose
        String strViews;
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

        public String getStrCat_id() {
            return strCat_id;
        }

        public void setStrCat_id(String strCat_id) {
            this.strCat_id = strCat_id;
        }

        public String getStrViews() {
            return strViews;
        }

        public void setStrViews(String strViews) {
            this.strViews = strViews;
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

    public static class ProductViews {
        @SerializedName("title")
        @Expose
        String strTitle;
        @SerializedName("product_id")
        @Expose
        String strProductId;
        @SerializedName("cat_id")
        @Expose
        String strCateId;
        @SerializedName("views")
        @Expose
        String strViews;
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

        public String getStrTitle() {
            return strTitle;
        }

        public void setStrTitle(String strTitle) {
            this.strTitle = strTitle;
        }

        public String getStrProductId() {
            return strProductId;
        }

        public void setStrProductId(String strProductId) {
            this.strProductId = strProductId;
        }

        public String getStrViews() {
            return strViews;
        }

        public void setStrViews(String strViews) {
            this.strViews = strViews;
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
