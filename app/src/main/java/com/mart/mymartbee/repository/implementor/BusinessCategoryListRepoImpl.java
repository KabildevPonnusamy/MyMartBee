package com.mart.mymartbee.repository.implementor;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.BusinessCategory_Model;
import com.mart.mymartbee.networking.retrofit.ApiCallBack;
import com.mart.mymartbee.networking.retrofit.ApiClient;
import com.mart.mymartbee.repository.interfaces.BusinessCategoryListRepo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessCategoryListRepoImpl implements BusinessCategoryListRepo {

    private MutableLiveData<BusinessCategory_Model> category_modelMLD;
    private MutableLiveData<String> cateErrorMLD;
    private MutableLiveData<Boolean> progressCateObervable;

    private static BusinessCategoryListRepoImpl instance = null;

    private static BusinessCategoryListRepoImpl getInstance() {
        if(instance == null) {
            instance = new BusinessCategoryListRepoImpl();
        }
        return instance;
    }

    public BusinessCategoryListRepoImpl() {
        category_modelMLD = new MutableLiveData<BusinessCategory_Model>();
        cateErrorMLD = new MutableLiveData<String>();
        progressCateObervable = new MutableLiveData<Boolean>();
    }

    @Override
    public MutableLiveData<Boolean> progressCateUpdation() {
        return progressCateObervable;
    }

    @Override
    public MutableLiveData<BusinessCategory_Model> getCategoryRepo() throws Exception {
        progressCateObervable.setValue(true);
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);

        Call<BusinessCategory_Model> call = callBack.getCategory();
        call.enqueue(new Callback<BusinessCategory_Model>() {
            @Override
            public void onResponse(Call<BusinessCategory_Model> call, Response<BusinessCategory_Model> response) {
                progressCateObervable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            category_modelMLD.setValue(response.body());
                        }
                    } else {
                        category_modelMLD.setValue(null);
                        cateErrorMLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "ResponseExc: " + e.getMessage());
                    category_modelMLD.setValue(null);
                    cateErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<BusinessCategory_Model> call, Throwable t) {
                progressCateObervable.setValue(false);
                category_modelMLD.setValue(null);
                cateErrorMLD.setValue("Connection Error");
            }
        });
        return category_modelMLD;
    }

    @Override
    public MutableLiveData<BusinessCategory_Model> getAddedCategoryRepo(Map<String, String> params) throws Exception {
        progressCateObervable.setValue(true);
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        MutableLiveData<BusinessCategory_Model> category_modelAddedMLD = new MutableLiveData<BusinessCategory_Model>();
        Call<BusinessCategory_Model> call = callBack.addMyCateGory(params);
        call.enqueue(new Callback<BusinessCategory_Model>() {
            @Override
            public void onResponse(Call<BusinessCategory_Model> call, Response<BusinessCategory_Model> response) {
                progressCateObervable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            category_modelAddedMLD.setValue(response.body());
                        }
                    } else {
                        category_modelAddedMLD.setValue(null);
                        cateErrorMLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "ResponseExc: " + e.getMessage());
                    category_modelAddedMLD.setValue(null);
                    cateErrorMLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<BusinessCategory_Model> call, Throwable t) {
                progressCateObervable.setValue(false);
                category_modelAddedMLD.setValue(null);
                cateErrorMLD.setValue("Connection Error");
            }
        });
        return category_modelAddedMLD;
    }

    @Override
    public MutableLiveData<String> getCateRepoError() throws Exception {
        return cateErrorMLD;
    }
}
