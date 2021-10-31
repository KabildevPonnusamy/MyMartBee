package com.mart.mymartbee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubCategoryUpdate_Model {

    @SerializedName("status")
    @Expose
    private boolean strStatus;
    @SerializedName("message")
    @Expose
    private String strMessage;
    @SerializedName("categorys")
    @Expose
    ArrayList<Categorys> categorys;

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }

    public boolean isStrStatus() {
        return strStatus;
    }

    public void setStrStatus(boolean strStatus) {
        this.strStatus = strStatus;
    }

    public ArrayList<Categorys> getCategorys() {
        return categorys;
    }

    public void setCategorys(ArrayList<Categorys> categorys) {
        this.categorys = categorys;
    }

    public static class Categorys {
        @SerializedName("cat_id")
        @Expose
        String strCateId;
        @SerializedName("sub_cat_id")
        @Expose
        String strSubCateId;
        @SerializedName("sub_cat_name")
        @Expose
        String strCateName;
        @SerializedName("status")
        @Expose
        String strCateStatus;

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

        public String getStrCateName() {
            return strCateName;
        }

        public void setStrCateName(String strCateName) {
            this.strCateName = strCateName;
        }

        public String getStrCateStatus() {
            return strCateStatus;
        }

        public void setStrCateStatus(String strCateStatus) {
            this.strCateStatus = strCateStatus;
        }

    }
}
