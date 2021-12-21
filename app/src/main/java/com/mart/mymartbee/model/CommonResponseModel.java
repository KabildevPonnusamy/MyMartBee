package com.mart.mymartbee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonResponseModel {

    @SerializedName("status")
    @Expose
    private boolean strStatus;
    @SerializedName("message")
    @Expose
    String strMessage;

    public boolean isStrStatus() {
        return strStatus;
    }

    public void setStrStatus(boolean strStatus) {
        this.strStatus = strStatus;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }

}
