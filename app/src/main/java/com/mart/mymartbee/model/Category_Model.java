package com.mart.mymartbee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Category_Model {

    @SerializedName("status")
    @Expose
    String strStatus;
    @SerializedName("categorys")
    @Expose
    ArrayList<Categorys> categorys;

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public ArrayList<Categorys> getCategorys() {
        return categorys;
    }

    public void setCategorys(ArrayList<Categorys> categorys) {
        this.categorys = categorys;
    }

    public static class Categorys {
        @SerializedName("id")
        @Expose
        String strCategoryId;
        @SerializedName("cat_name")
        @Expose
        String strCateGoryName;
        @SerializedName("image")
        @Expose
        String strCategoryImage;
        @SerializedName("status")
        @Expose
        String strCategoryStatus;

        public String getStrCategoryId() {
            return strCategoryId;
        }

        public void setStrCategoryId(String strCategoryId) {
            this.strCategoryId = strCategoryId;
        }

        public String getStrCateGoryName() {
            return strCateGoryName;
        }

        public void setStrCateGoryName(String strCateGoryName) {
            this.strCateGoryName = strCateGoryName;
        }

        public String getStrCategoryImage() {
            return strCategoryImage;
        }

        public void setStrCategoryImage(String strCategoryImage) {
            this.strCategoryImage = strCategoryImage;
        }

        public String getStrCategoryStatus() {
            return strCategoryStatus;
        }

        public void setStrCategoryStatus(String strCategoryStatus) {
            this.strCategoryStatus = strCategoryStatus;
        }

    }
}
