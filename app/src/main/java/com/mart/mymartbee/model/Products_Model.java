package com.mart.mymartbee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Products_Model {

    @SerializedName("status")
    @Expose
    private boolean strStatus;
    @SerializedName("message")
    @Expose
    private String strMessage;
    @SerializedName("categories")
    @Expose
    ArrayList<ProductCategories> productCategories;
    @SerializedName("link")
    @Expose
    private String strStoreLink;

    public String getStrStoreLink() {
        return strStoreLink;
    }

    public void setStrStoreLink(String strStoreLink) {
        this.strStoreLink = strStoreLink;
    }

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

    public ArrayList<ProductCategories> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(ArrayList<ProductCategories> productCategories) {
        this.productCategories = productCategories;
    }

    public static class ProductCategories {
        @SerializedName("id")
        @Expose
        private String strCateId;
        @SerializedName("cat_name")
        @Expose
        private String strCateName;
        @SerializedName("products")
        @Expose
        ArrayList<ProductsList> productsLists;
        private boolean expanded;
        public boolean isExpanded() {
            return expanded;
        }
        public void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }

        public String getStrCateId() {
            return strCateId;
        }

        public void setStrCateId(String strCateId) {
            this.strCateId = strCateId;
        }

        public String getStrCateName() {
            return strCateName;
        }

        public void setStrCateName(String strCateName) {
            this.strCateName = strCateName;
        }

        public ArrayList<ProductsList> getProductsLists() {
            return productsLists;
        }

        public void setProductsLists(ArrayList<ProductsList> productsLists) {
            this.productsLists = productsLists;
        }

        public static class ProductsList {
            @SerializedName("produt_id")
            @Expose
            private String strProdut_id;
            @SerializedName("image")
            @Expose
            private String strProduct_image;
            @SerializedName("quantity")
            @Expose
            private String strProduct_quantity;
            @SerializedName("title")
            @Expose
            private String strProduct_title;
            @SerializedName("description")
            @Expose
            private String strProduct_description;
            @SerializedName("price")
            @Expose
            private String strProduct_price;
            @SerializedName("old_price")
            @Expose
            private String strProduct_oldprice;
            @SerializedName("uom")
            @Expose
            private String strProduct_uom;
            @SerializedName("other_images")
            @Expose
            ArrayList<OtherImages> otherImages;

            public ArrayList<OtherImages> getOtherImages() {
                return otherImages;
            }

            public void setOtherImages(ArrayList<OtherImages> otherImages) {
                this.otherImages = otherImages;
            }

            public static class OtherImages {
                @SerializedName("image")
                @Expose
                public String strImage;
                @SerializedName("folder")
                @Expose
                public String strFolder;
                @SerializedName("image_name")
                @Expose
                public String strImageName;

                public String getStrImageName() {
                    return strImageName;
                }

                public void setStrImageName(String strImageName) {
                    this.strImageName = strImageName;
                }

                public String getStrImage() {
                    return strImage;
                }

                public void setStrImage(String strImage) {
                    this.strImage = strImage;
                }

                public String getStrFolder() {
                    return strFolder;
                }

                public void setStrFolder(String strFolder) {
                    this.strFolder = strFolder;
                }

            }

            public String getStrProdut_id() {
                return strProdut_id;
            }

            public void setStrProdut_id(String strProdut_id) {
                this.strProdut_id = strProdut_id;
            }

            public String getStrProduct_image() {
                return strProduct_image;
            }

            public void setStrProduct_image(String strProduct_image) {
                this.strProduct_image = strProduct_image;
            }

            public String getStrProduct_quantity() {
                return strProduct_quantity;
            }

            public void setStrProduct_quantity(String strProduct_quantity) {
                this.strProduct_quantity = strProduct_quantity;
            }

            public String getStrProduct_title() {
                return strProduct_title;
            }

            public void setStrProduct_title(String strProduct_title) {
                this.strProduct_title = strProduct_title;
            }

            public String getStrProduct_description() {
                return strProduct_description;
            }

            public void setStrProduct_description(String strProduct_description) {
                this.strProduct_description = strProduct_description;
            }

            public String getStrProduct_price() {
                return strProduct_price;
            }

            public void setStrProduct_price(String strProduct_price) {
                this.strProduct_price = strProduct_price;
            }

            public String getStrProduct_oldprice() {
                return strProduct_oldprice;
            }

            public void setStrProduct_oldprice(String strProduct_oldprice) {
                this.strProduct_oldprice = strProduct_oldprice;
            }

            public String getStrProduct_uom() {
                return strProduct_uom;
            }

            public void setStrProduct_uom(String strProduct_uom) {
                this.strProduct_uom = strProduct_uom;
            }
        }
    }
}
