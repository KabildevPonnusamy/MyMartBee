package com.mart.mymartbee.model;

public class UploadingImageList {


    public String img_id;
    public String image;
    public String created_date;

    public UploadingImageList(){}

    public UploadingImageList(String img_id, String image, String created_date) {
        this.img_id = img_id;
        this.image = image;
        this.created_date = created_date;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }




}
