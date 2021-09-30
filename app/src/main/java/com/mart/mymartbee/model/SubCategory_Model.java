package com.mart.mymartbee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubCategory_Model {

    @SerializedName("status")
    @Expose
    private boolean strStatus;
    @SerializedName("categorys")
    @Expose
    ArrayList<SubCategory> subCategories;

    public ArrayList<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public boolean isStrStatus() {
        return strStatus;
    }

    public void setStrStatus(boolean strStatus) {
        this.strStatus = strStatus;
    }

    public static class SubCategory {
        @SerializedName("cat_id")
        @Expose
        String strCateId;
        @SerializedName("sub_cat_id")
        @Expose
        String strSubCateId;
        @SerializedName("sub_cat_name")
        @Expose
        String strSubCateName;
        @SerializedName("status")
        @Expose
        String strSubCateStatus;

        public String getStrCateId() {
            return strCateId;
        }

        public void setStrCateId(String strCateId) {
            this.strCateId = strCateId;
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

        public String getStrSubCateStatus() {
            return strSubCateStatus;
        }

        public void setStrSubCateStatus(String strSubCateStatus) {
            this.strSubCateStatus = strSubCateStatus;
        }

    }

}
