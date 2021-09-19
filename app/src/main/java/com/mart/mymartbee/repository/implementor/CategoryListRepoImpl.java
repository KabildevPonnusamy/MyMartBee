package com.mart.mymartbee.repository.implementor;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.Category_Model;
import com.mart.mymartbee.networking.retrofit.ApiCallBack;
import com.mart.mymartbee.networking.retrofit.ApiClient;
import com.mart.mymartbee.repository.interfaces.CategoryListRepo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListRepoImpl implements CategoryListRepo {

    private MutableLiveData<Category_Model> category_modelMLD;
    private MutableLiveData<String> cateErrorMLD;
    private MutableLiveData<Boolean> progressCateObervable;

    private static CategoryListRepoImpl instance = null;

    private static CategoryListRepoImpl getInstance() {
        if(instance == null) {
            instance = new CategoryListRepoImpl();
        }
        return instance;
    }

    public CategoryListRepoImpl() {
        category_modelMLD = new MutableLiveData<Category_Model>();
        cateErrorMLD = new MutableLiveData<String>();
        progressCateObervable = new MutableLiveData<Boolean>();
    }

    @Override
    public MutableLiveData<Boolean> progressCateUpdation() {
        return progressCateObervable;
    }

    @Override
    public MutableLiveData<Category_Model> getCategoryRepo() throws Exception {

        progressCateObervable.setValue(true);
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);

        Call<Category_Model> call = callBack.getCategory();
        call.enqueue(new Callback<Category_Model>() {
            @Override
            public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                progressCateObervable.setValue(false);
                if(response == null) {
                    Log.e("appSample", "ResponseNull");
                }

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
            public void onFailure(Call<Category_Model> call, Throwable t) {
                progressCateObervable.setValue(false);
                category_modelMLD.setValue(null);
                cateErrorMLD.setValue("Connection Error");
            }
        });
        return category_modelMLD;
    }

    @Override
    public MutableLiveData<String> getCateRepoError() throws Exception {
        return cateErrorMLD;
    }
}
