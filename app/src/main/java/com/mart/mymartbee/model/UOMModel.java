package com.mart.mymartbee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UOMModel {

    @SerializedName("status")
    @Expose
    private boolean strStatus;

    @SerializedName("uom")
    @Expose
    ArrayList<UOMList> uomLists;

    public boolean isStrStatus() {
        return strStatus;
    }

    public void setStrStatus(boolean strStatus) {
        this.strStatus = strStatus;
    }

    public ArrayList<UOMList> getUomLists() {
        return uomLists;
    }

    public void setUomLists(ArrayList<UOMList> uomLists) {
        this.uomLists = uomLists;
    }

    public static class UOMList {
        @SerializedName("uom")
        @Expose
        String strUom;
        private boolean isSelected = false;
        private boolean isChosen;

        public String getStrUom() {
            return strUom;
        }

        public void setStrUom(String strUom) {
            this.strUom = strUom;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public boolean isChosen() {
            return isChosen;
        }

        public void setChosen(boolean chosen) {
            isChosen = chosen;
        }

    }



}
