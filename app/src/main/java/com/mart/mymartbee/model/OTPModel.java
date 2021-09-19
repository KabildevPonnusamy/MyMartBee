package com.mart.mymartbee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPModel {

    @SerializedName("status")
    @Expose
    private String strStatus;
    @SerializedName("message")
    @Expose
    private String strMessage;

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }

    public String getStrOtp() {
        return strOtp;
    }

    public void setStrOtp(String strOtp) {
        this.strOtp = strOtp;
    }

    @SerializedName("otp")
    @Expose
    private String strOtp;

}
