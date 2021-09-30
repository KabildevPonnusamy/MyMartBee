package com.mart.mymartbee.networking.retrofit;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

import com.mart.mymartbee.model.Category_Model;
import com.mart.mymartbee.model.OTPModel;
import com.mart.mymartbee.model.OTPVerifyModel;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.model.RegisterModel;
import com.mart.mymartbee.model.SubCategory_Model;
import com.mart.mymartbee.model.UOMModel;

public interface ApiCallBack {

    @POST("seller/otp")
    @FormUrlEncoded
    Call<OTPModel> getOTP(@FieldMap Map<String, String> params);

    @POST("seller/otp-check")
    @FormUrlEncoded
    Call<OTPVerifyModel> verifyOTP(@FieldMap Map<String, String> params);

    @GET("seller/category")
    Call<Category_Model> getCategory();

    @Multipart
    @POST("seller/register")
    Call<RegisterModel> sellerRegistration(@Part MultipartBody.Part image, @Part("country_code") RequestBody country_code, @Part("mobile_number") RequestBody mobile_number,
                                           @Part("imie_no") RequestBody imie_no, @Part("gcm_id") RequestBody gcm_id, @Part("latitude") RequestBody latitude,
                                           @Part("longitude") RequestBody longitude, @Part("shop") RequestBody shop, @Part("cat_id") RequestBody category,
                                           @Part("address") RequestBody address);

    @POST("seller/register")
    @FormUrlEncoded
    Call<RegisterModel> updateProfile(@FieldMap Map<String, String> params);

    @POST("seller/add-category")
    @FormUrlEncoded
    Call<Category_Model> addMyCateGory(@FieldMap Map<String, String> params);

    @GET("seller/sub-category/{value}")
    Call<SubCategory_Model> getSubCategories(@Path("value") String value);

    @POST("seller/add-sub-category")
    @FormUrlEncoded
    Call<SubCategory_Model> addMySubCateGory(@FieldMap Map<String, String> params);

    @Multipart
    @POST("product/add-product")
    Call<Products_Model> addProduct(@Part MultipartBody.Part product_image, @Part("title") RequestBody title, @Part("description") RequestBody description,
                                    @Part("meta_title") RequestBody meta_title, @Part("meta_description") RequestBody meta_description, @Part("meta_keyword") RequestBody meta_keyword,
                                    @Part("price") RequestBody price, @Part("old_price") RequestBody old_price, @Part("cat_id") RequestBody cat_id,
                                    @Part("sub_cat_id") RequestBody sub_cat_id, @Part("quantity") RequestBody quantity, @Part("seller_id") RequestBody seller_id,
                                    @Part("uom") RequestBody uom);

    @Multipart
    @POST("product/edit-product")
    Call<Products_Model> updateProductwithImage(@Part MultipartBody.Part product_image, @Part("title") RequestBody title, @Part("description") RequestBody description,
                                    @Part("meta_title") RequestBody meta_title, @Part("meta_description") RequestBody meta_description, @Part("meta_keyword") RequestBody meta_keyword,
                                    @Part("price") RequestBody price, @Part("old_price") RequestBody old_price, @Part("cat_id") RequestBody cat_id,
                                    @Part("sub_cat_id") RequestBody sub_cat_id, @Part("quantity") RequestBody quantity, @Part("seller_id") RequestBody seller_id,
                                    @Part("product_id") RequestBody product_id, @Part("uom") RequestBody uom);


    @POST("product/edit-product")
    @FormUrlEncoded
    Call<Products_Model> editProduct(@FieldMap Map<String, String> params);

    @POST("product/delete-product")
    @FormUrlEncoded
    Call<Products_Model> deleteProduct(@FieldMap Map<String, String> params);

    @GET("product/product-list/{cat_id}/{seller_id}")
    Call<Products_Model> getProducts(@Path("cat_id") String cat_id, @Path("seller_id") String seller_id);

    @GET("seller/uom")
    Call<UOMModel> getUOMList();

    /*@GET("{value}/info.json")
    Call<StatesList> getStateInfos(@Path("value") String value);*/

}
