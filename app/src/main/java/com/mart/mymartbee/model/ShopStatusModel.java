package com.mart.mymartbee.model;

public class ShopStatusModel {

    public String id;
    public String status_title;
    public String status_link;
    public String status_status;

    public ShopStatusModel(){}

    public ShopStatusModel(String id, String status_title, String status_link, String status_status) {
        this.id = id;
        this.status_title = status_title;
        this.status_link = status_link;
        this.status_status = status_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus_title() {
        return status_title;
    }

    public void setStatus_title(String status_title) {
        this.status_title = status_title;
    }

    public String getStatus_link() {
        return status_link;
    }

    public void setStatus_link(String status_link) {
        this.status_link = status_link;
    }

    public String getStatus_status() {
        return status_status;
    }

    public void setStatus_status(String status_status) {
        this.status_status = status_status;
    }




}
