package com.mart.mymartbee.repository.implementor;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.model.UOMModel;
import com.mart.mymartbee.networking.retrofit.ApiCallBack;
import com.mart.mymartbee.networking.retrofit.ApiClient;
import com.mart.mymartbee.repository.interfaces.ProductListRepo;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductListRepoImpl implements ProductListRepo {

    private MutableLiveData<String> productErrorMLD;
    private MutableLiveData<Boolean> progressProductObservable;

    private static ProductListRepoImpl instance = null;

    private static ProductListRepoImpl getInstance() {
        if(instance == null) {
            instance = new ProductListRepoImpl();
        }
        return instance;
    }

    public ProductListRepoImpl() {
        productErrorMLD = new MutableLiveData<String>();
        progressProductObservable = new MutableLiveData<Boolean>();
    }

    @Override
    public MutableLiveData<Products_Model> addProductRepo(File file, Map<String, String> params) throws Exception {
        progressProductObservable.setValue(true);
        RequestBody p_title = RequestBody.create(MediaType.parse("text/plain"), params.get("title"));
        RequestBody p_description = RequestBody.create(MediaType.parse("text/plain"), params.get("description"));
        RequestBody p_meta_title = RequestBody.create(MediaType.parse("text/plain"), params.get("meta_title"));
        RequestBody p_meta_description = RequestBody.create(MediaType.parse("text/plain"), params.get("meta_description"));
        RequestBody p_meta_keyword = RequestBody.create(MediaType.parse("text/plain"), params.get("meta_keyword"));
        RequestBody p_price = RequestBody.create(MediaType.parse("text/plain"), params.get("price"));
        RequestBody p_old_price = RequestBody.create(MediaType.parse("text/plain"), params.get("old_price"));
        RequestBody p_cat_id = RequestBody.create(MediaType.parse("text/plain"), params.get("cat_id"));
        RequestBody p_sub_cat_id = RequestBody.create(MediaType.parse("text/plain"), params.get("sub_cat_id"));
        RequestBody p_quantity = RequestBody.create(MediaType.parse("text/plain"), params.get("quantity"));
        RequestBody p_seller_id = RequestBody.create(MediaType.parse("text/plain"), params.get("seller_id"));
        RequestBody p_uom = RequestBody.create(MediaType.parse("text/plain"), params.get("uom"));

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/*"), file);
        Retrofit retrofit = ApiClient.getRetrofit();

        MutableLiveData<Products_Model> addProductModelMLD = new MutableLiveData<Products_Model>();

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("product_image", file.getName(), requestFile);

        ApiCallBack apiService = retrofit.create(ApiCallBack.class);

        apiService.addProduct(body, p_title, p_description, p_meta_title, p_meta_description, p_meta_keyword, p_price,
                p_old_price, p_cat_id, p_sub_cat_id, p_quantity, p_seller_id, p_uom).enqueue(new Callback<Products_Model>() {
            @Override
            public void onResponse(Call<Products_Model> call, Response<Products_Model> response) {
                progressProductObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            addProductModelMLD.setValue(response.body());
                        }
                    } else {
                        addProductModelMLD.setValue(null);
                        productErrorMLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "ResponseExc: " + e.getMessage());
                    addProductModelMLD.setValue(null);
                    productErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Products_Model> call, Throwable t) {
                Log.e("appSample", "Failure: " + t.getMessage());
                progressProductObservable.setValue(false);
                addProductModelMLD.setValue(null);
                productErrorMLD.setValue("Connection Error");
            }
        });
        return addProductModelMLD;
    }

    @Override
    public MutableLiveData<Products_Model> editProductRepo(Map<String, String> params) throws Exception {
        progressProductObservable.setValue(true);
        MutableLiveData<Products_Model> editProductModelMLD = new MutableLiveData<Products_Model>();
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<Products_Model> call = callBack.editProduct(params);
        call.enqueue(new Callback<Products_Model>() {
            @Override
            public void onResponse(Call<Products_Model> call, Response<Products_Model> response) {
                progressProductObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            editProductModelMLD.setValue(response.body());
                        }
                    } else {
                        editProductModelMLD.setValue(null);
                        productErrorMLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "ResponseExc: " + e.getMessage());
                    editProductModelMLD.setValue(null);
                    productErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Products_Model> call, Throwable t) {
                progressProductObservable.setValue(false);
                editProductModelMLD.setValue(null);
                productErrorMLD.setValue("Connection Error");
            }
        });

        return editProductModelMLD;
    }

    @Override
    public MutableLiveData<Products_Model> uploadProductImage(File file, Map<String, String> params) throws Exception {
        Log.e("appSample", "uploadProductImage");
        progressProductObservable.setValue(true);
        RequestBody p_cat_id = RequestBody.create(MediaType.parse("text/plain"), params.get("cat_id"));
        RequestBody p_seller_id = RequestBody.create(MediaType.parse("text/plain"), params.get("seller_id"));
//        RequestBody p_product_id = RequestBody.create(MediaType.parse("text/plain"), params.get("product_id"));

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/*"), file);
        Retrofit retrofit = ApiClient.getRetrofit();

        MutableLiveData<Products_Model> uploadProductImageMLD = new MutableLiveData<Products_Model>();

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("product_image", file.getName(), requestFile);

        ApiCallBack apiService = retrofit.create(ApiCallBack.class);
        RequestBody p_product_id = null;

        apiService.uploadProductImages(body, p_seller_id, p_product_id, p_cat_id).enqueue(new Callback<Products_Model>() {
            @Override
            public void onResponse(Call<Products_Model> call, Response<Products_Model> response) {
                progressProductObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            uploadProductImageMLD.setValue(response.body());
                        }
                    } else {
                        uploadProductImageMLD.setValue(null);
                        productErrorMLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "ResponseExc: " + e.getMessage());
                    uploadProductImageMLD.setValue(null);
                    productErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Products_Model> call, Throwable t) {
                Log.e("appSample", "Failure: " + t.getMessage());
                progressProductObservable.setValue(false);
                uploadProductImageMLD.setValue(null);
                productErrorMLD.setValue("Connection Error");
            }
        });
        return uploadProductImageMLD;
    }

    @Override
    public MutableLiveData<Products_Model> editProductRepoWithImage(File file, Map<String, String> params) throws Exception {
        progressProductObservable.setValue(true);
        RequestBody p_title = RequestBody.create(MediaType.parse("text/plain"), params.get("title"));
        RequestBody p_description = RequestBody.create(MediaType.parse("text/plain"), params.get("description"));
        RequestBody p_meta_title = RequestBody.create(MediaType.parse("text/plain"), params.get("meta_title"));
        RequestBody p_meta_description = RequestBody.create(MediaType.parse("text/plain"), params.get("meta_description"));
        RequestBody p_meta_keyword = RequestBody.create(MediaType.parse("text/plain"), params.get("meta_keyword"));
        RequestBody p_price = RequestBody.create(MediaType.parse("text/plain"), params.get("price"));
        RequestBody p_old_price = RequestBody.create(MediaType.parse("text/plain"), params.get("old_price"));
        RequestBody p_cat_id = RequestBody.create(MediaType.parse("text/plain"), params.get("cat_id"));
        RequestBody p_sub_cat_id = RequestBody.create(MediaType.parse("text/plain"), params.get("sub_cat_id"));
        RequestBody p_quantity = RequestBody.create(MediaType.parse("text/plain"), params.get("quantity"));
        RequestBody p_seller_id = RequestBody.create(MediaType.parse("text/plain"), params.get("seller_id"));
        RequestBody p_uom = RequestBody.create(MediaType.parse("text/plain"), params.get("uom"));
        RequestBody p_product_id = RequestBody.create(MediaType.parse("text/plain"), params.get("product_id"));

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/*"), file);
        Retrofit retrofit = ApiClient.getRetrofit();

        MutableLiveData<Products_Model> editProductModelMLDwithImage = new MutableLiveData<Products_Model>();

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("product_image", file.getName(), requestFile);

        ApiCallBack apiService = retrofit.create(ApiCallBack.class);

        apiService.updateProductwithImage(body, p_title, p_description, p_meta_title, p_meta_description, p_meta_keyword, p_price,
                p_old_price, p_cat_id, p_sub_cat_id, p_quantity, p_seller_id, p_product_id, p_uom).enqueue(new Callback<Products_Model>() {
            @Override
            public void onResponse(Call<Products_Model> call, Response<Products_Model> response) {
                progressProductObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            editProductModelMLDwithImage.setValue(response.body());
                        }
                    } else {
                        editProductModelMLDwithImage.setValue(null);
                        productErrorMLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "ResponseExc: " + e.getMessage());
                    editProductModelMLDwithImage.setValue(null);
                    productErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Products_Model> call, Throwable t) {
                progressProductObservable.setValue(false);
                editProductModelMLDwithImage.setValue(null);
                productErrorMLD.setValue("Connection Error");
            }
        });
        return editProductModelMLDwithImage;
    }

    @Override
    public MutableLiveData<Products_Model> deleteProductRepo(Map<String, String> params) throws Exception {
        progressProductObservable.setValue(true);
        MutableLiveData<Products_Model> deleteProductModelMLD = new MutableLiveData<Products_Model>();
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<Products_Model> call = callBack.deleteProduct(params);
        call.enqueue(new Callback<Products_Model>() {
            @Override
            public void onResponse(Call<Products_Model> call, Response<Products_Model> response) {
                progressProductObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            deleteProductModelMLD.setValue(response.body());
                        }
                    } else {
                        deleteProductModelMLD.setValue(null);
                        productErrorMLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    deleteProductModelMLD.setValue(null);
                    productErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Products_Model> call, Throwable t) {
                progressProductObservable.setValue(false);
                deleteProductModelMLD.setValue(null);
                productErrorMLD.setValue("Connection Error");
            }
        });

        return deleteProductModelMLD;
    }

    @Override
    public MutableLiveData<Products_Model> getProductRepo(String cate_id, String seller_id) throws Exception {
        progressProductObservable.setValue(true);
        MutableLiveData<Products_Model> getProductModelMLD = new MutableLiveData<Products_Model>();
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<Products_Model> call = callBack.getProducts(cate_id, seller_id);
        call.enqueue(new Callback<Products_Model>() {
            @Override
            public void onResponse(Call<Products_Model> call, Response<Products_Model> response) {
                progressProductObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            getProductModelMLD.setValue(response.body());
                        }
                    } else {
                        getProductModelMLD.setValue(null);
                        productErrorMLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    getProductModelMLD.setValue(null);
                    productErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Products_Model> call, Throwable t) {
                progressProductObservable.setValue(false);
                getProductModelMLD.setValue(null);
                productErrorMLD.setValue("Connection Error");
            }
        });

        return getProductModelMLD;
    }

    @Override
    public MutableLiveData<UOMModel> getUOMDatas() throws Exception {
        progressProductObservable.setValue(true);
        MutableLiveData<UOMModel> uomMLDList = new MutableLiveData<UOMModel>();
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<UOMModel> call = callBack.getUOMList();
        call.enqueue(new Callback<UOMModel>() {
            @Override
            public void onResponse(Call<UOMModel> call, Response<UOMModel> response) {
                progressProductObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            uomMLDList.setValue(response.body());
                        }
                    } else {
                        uomMLDList.setValue(null);
                        productErrorMLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    uomMLDList.setValue(null);
                    productErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<UOMModel> call, Throwable t) {
                progressProductObservable.setValue(false);
                uomMLDList.setValue(null);
                productErrorMLD.setValue("Connection Error");
            }
        });

        return uomMLDList;
    }

    @Override
    public MutableLiveData<String> getCateRepoError() throws Exception {
        return productErrorMLD;
    }

    @Override
    public MutableLiveData<Boolean> progressProductUpdation() {
        return progressProductObservable;
    }
}
