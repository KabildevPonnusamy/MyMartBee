package com.mart.mymartbee.model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPVerifyModel {

    @SerializedName("status")
    @Expose
    private boolean strStatus;
    @SerializedName("module")
    @Expose
    private String strModule;
    @SerializedName("message")
    @Expose
    private String strMessage;

    public boolean isStrStatus() {
        return strStatus;
    }

    public void setStrStatus(boolean strStatus) {
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

    @SerializedName("seller_details")
    @Expose
    SellerDetails sellerDetails;

    public SellerDetails getSellerDetails() {
        return sellerDetails;
    }

    public void setSellerDetails(SellerDetails sellerDetails) {
        this.sellerDetails = sellerDetails;
    }

    public static class SellerDetails {
        @SerializedName("id")
        @Expose
        String strRegId;
        @SerializedName("shop")
        @Expose
        String strRegshop;
        @SerializedName("cat_id")
        @Expose
        String strRegCategory;
        @SerializedName("category_name")
        @Expose
        String strRegCategoryName;
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
        @SerializedName("open_time")
        @Expose
        String strOpenTime;
        @SerializedName("close_time")
        @Expose
        String strCloseTime;

        @SerializedName("business")
        @Expose
        String strBusiness;
        @SerializedName("acc_holder_name")
        @Expose
        String strRegAccountHolderName;
        @SerializedName("acc_no")
        @Expose
        String strAccountNumber;
        @SerializedName("bank_name")
        @Expose
        String strBankName;

        public String getStrRegAccountHolderName() {
            return strRegAccountHolderName;
        }

        public void setStrRegAccountHolderName(String strRegAccountHolderName) {
            this.strRegAccountHolderName = strRegAccountHolderName;
        }

        public String getStrAccountNumber() {
            return strAccountNumber;
        }

        public void setStrAccountNumber(String strAccountNumber) {
            this.strAccountNumber = strAccountNumber;
        }

        public String getStrBankName() {
            return strBankName;
        }

        public void setStrBankName(String strBankName) {
            this.strBankName = strBankName;
        }

        public String getStrBusiness() {
            return strBusiness;
        }

        public void setStrBusiness(String strBusiness) {
            this.strBusiness = strBusiness;
        }

        public String getStrOpenTime() {
            return strOpenTime;
        }

        public void setStrOpenTime(String strOpenTime) {
            this.strOpenTime = strOpenTime;
        }

        public String getStrCloseTime() {
            return strCloseTime;
        }

        public void setStrCloseTime(String strCloseTime) {
            this.strCloseTime = strCloseTime;
        }

        public String getStrRegCategoryName() {
            return strRegCategoryName;
        }

        public void setStrRegCategoryName(String strRegCategoryName) {
            this.strRegCategoryName = strRegCategoryName;
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
