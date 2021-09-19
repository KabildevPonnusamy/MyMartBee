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
import com.mart.mymartbee.model.RegisterModel;

public interface ApiCallBack {

    @POST("otp")
    @FormUrlEncoded
    Call<OTPModel> getOTP(@FieldMap Map<String, String> params);

    @POST("otp-check")
    @FormUrlEncoded
    Call<OTPVerifyModel> verifyOTP(@FieldMap Map<String, String> params);

    @GET("category")
    Call<Category_Model> getCategory();

    @Multipart
    @POST("register")
    Call<RegisterModel> sellerRegistration(@Part MultipartBody.Part image, @Part("country_code") RequestBody country_code, @Part("mobile_number") RequestBody mobile_number,
                                           @Part("imie_no") RequestBody imie_no, @Part("gcm_id") RequestBody gcm_id, @Part("latitude") RequestBody latitude,
                                           @Part("longitude") RequestBody longitude, @Part("shop") RequestBody shop, @Part("category") RequestBody category,
                                           @Part("address") RequestBody address);

    /*@GET("{value}/info.json")
    Call<StatesList> getStateInfos(@Path("value") String value);*/

}
