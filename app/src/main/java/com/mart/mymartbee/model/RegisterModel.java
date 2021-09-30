package com.mart.mymartbee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RegisterModel {

    @SerializedName("status")
    @Expose
    String strStatus;
    @SerializedName("module")
    @Expose
    String strModule;
    @SerializedName("message")
    @Expose
    String strMessage;
    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public String getStrModule() {
        return strModule;
    }

    public void setStrModule(String strModule) {
        this.strModule = strModule;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }

    public SellerDetails getSellerDetails() {
        return sellerDetails;
    }

    public void setSellerDetails(SellerDetails sellerDetails) {
        this.sellerDetails = sellerDetails;
    }

    @SerializedName("seller_details")
    @Expose
    SellerDetails sellerDetails;

    public static class SellerDetails {
        @SerializedName("id")
        @Expose
        String strRegId;
        @SerializedName("shop")
        @Expose
        String strRegshop;
        @SerializedName("category_name")
        @Expose
        String strRegCategory;
        @SerializedName("cat_id")
        @Expose
        String strRegCategoryID;
        @SerializedName("image")
        @Expose
        String strRegImage;
        @SerializedName("address")
        @Expose
        String strRegAddress;
        @SerializedName("country_code")
        @Expose
        String strRegCountryCode;
        @SerializedName("mobile_number")
        @Expose
        String strRegMobileNumber;
        @SerializedName("gcm_id")
        @Expose
        String strRegGCMId;
        @SerializedName("imie_no")
        @Expose
        String strRegIMIENo;
        @SerializedName("latitude")
        @Expose
        String strRegLatitude;
        @SerializedName("longitude")
        @Expose
        String strRegLongitude;
        @SerializedName("created_at")
        @Expose
        String strRegCreatedAt;

        public String getStrRegCategoryID() {
            return strRegCategoryID;
        }

        public void setStrRegCategoryID(String strRegCategoryID) {
            this.strRegCategoryID = strRegCategoryID;
        }

        public String getStrRegId() {
            return strRegId;
        }

        public void setStrRegId(String strRegId) {
            this.strRegId = strRegId;
        }

        public String getStrRegshop() {
            return strRegshop;
        }

        public void setStrRegshop(String strRegshop) {
            this.strRegshop = strRegshop;
        }

        public String getStrRegCategory() {
            return strRegCategory;
        }

        public void setStrRegCategory(String strRegCategory) {
            this.strRegCategory = strRegCategory;
        }

        public String getStrRegImage() {
            return strRegImage;
        }

        public void setStrRegImage(String strRegImage) {
            this.strRegImage = strRegImage;
        }

        public String getStrRegAddress() {
            return strRegAddress;
        }

        public void setStrRegAddress(String strRegAddress) {
            this.strRegAddress = strRegAddress;
        }

        public String getStrRegCountryCode() {
            return strRegCountryCode;
        }

        public void setStrRegCountryCode(String strRegCountryCode) {
            this.strRegCountryCode = strRegCountryCode;
        }

        public String getStrRegMobileNumber() {
            return strRegMobileNumber;
        }

        public void setStrRegMobileNumber(String strRegMobileNumber) {
            this.strRegMobileNumber = strRegMobileNumber;
        }

        public String getStrRegGCMId() {
            return strRegGCMId;
        }

        public void setStrRegGCMId(String strRegGCMId) {
            this.strRegGCMId = strRegGCMId;
        }

        public String getStrRegIMIENo() {
            return strRegIMIENo;
        }

        public void setStrRegIMIENo(String strRegIMIENo) {
            this.strRegIMIENo = strRegIMIENo;
        }

        public String getStrRegLatitude() {
            return strRegLatitude;
        }

        public void setStrRegLatitude(String strRegLatitude) {
            this.strRegLatitude = strRegLatitude;
        }

        public String getStrRegLongitude() {
            return strRegLongitude;
        }

        public void setStrRegLongitude(String strRegLongitude) {
            this.strRegLongitude = strRegLongitude;
        }

        public String getStrRegCreatedAt() {
            return strRegCreatedAt;
        }

        public void setStrRegCreatedAt(String strRegCreatedAt) {
            this.strRegCreatedAt = strRegCreatedAt;
        }

    }
}
