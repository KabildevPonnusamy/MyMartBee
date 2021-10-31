package com.mart.mymartbee.repository.implementor;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.SubCategory_Model;
import com.mart.mymartbee.networking.retrofit.ApiCallBack;
import com.mart.mymartbee.networking.retrofit.ApiClient;
import com.mart.mymartbee.repository.interfaces.SubCategoryListRepo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryListRepoImpl implements SubCategoryListRepo {

    private MutableLiveData<SubCategory_Model> subCateModel_MLD;
    private MutableLiveData<String> subCateErr_MLD;
    private MutableLiveData<Boolean> progressSubCateObservable;

    private static SubCategoryListRepoImpl instance = null;

    private static SubCategoryListRepoImpl getInstance() {
        if(instance == null) {
            instance = new SubCategoryListRepoImpl();
        }
        return instance;
    }

    public SubCategoryListRepoImpl() {
        subCateModel_MLD = new MutableLiveData<SubCategory_Model>();
        subCateErr_MLD = new MutableLiveData<String>();
        progressSubCateObservable = new MutableLiveData<Boolean>();
    }

    @Override
    public MutableLiveData<SubCategory_Model> subCategoryRepo(String seller_id, String cate_id) throws Exception {
        Log.e("appSample", "CATEID: " + cate_id);
        Log.e("appSample", "SELLERID: " + seller_id);
        progressSubCateObservable.setValue(true);

        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<SubCategory_Model> call = callBack.getSubCategories(seller_id, cate_id);
        call.enqueue(new Callback<SubCategory_Model>() {
            @Override
            public void onResponse(Call<SubCategory_Model> call, Response<SubCategory_Model> response) {
                progressSubCateObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            subCateModel_MLD.setValue(response.body());
                        }
                    } else {
                        subCateModel_MLD.setValue(null);
                        subCateErr_MLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "ResponseExc: " + e.getMessage());
                    subCateModel_MLD.setValue(null);
                    subCateErr_MLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SubCategory_Model> call, Throwable t) {
                Log.e("appSample", "Failure: " + t.getMessage());
                progressSubCateObservable.setValue(false);
                subCateModel_MLD.setValue(null);
                subCateErr_MLD.setValue("Connection Error");
            }
        });
        return subCateModel_MLD;
    }

    @Override
    public MutableLiveData<SubCategory_Model> getAddedSubCategoryRepo(Map<String, String> params) throws Exception {
        progressSubCateObservable.setValue(true);
        MutableLiveData<SubCategory_Model> addedSubCate_MLD = new MutableLiveData<SubCategory_Model>();
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<SubCategory_Model> call = callBack.addMySubCateGory(params);
        call.enqueue(new Callback<SubCategory_Model>() {
            @Override
            public void onResponse(Call<SubCategory_Model> call, Response<SubCategory_Model> response) {
                progressSubCateObservable.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            addedSubCate_MLD.setValue(response.body());
                        }
                    } else {
                        addedSubCate_MLD.setValue(null);
                        subCateErr_MLD.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "ResponseExc: " + e.getMessage());
                    addedSubCate_MLD.setValue(null);
                    subCateErr_MLD.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SubCategory_Model> call, Throwable t) {
                Log.e("appSample", "Failure: " + t.getMessage());
                progressSubCateObservable.setValue(false);
                addedSubCate_MLD.setValue(null);
                subCateErr_MLD.setValue("Connection Error");
            }
        });

        return addedSubCate_MLD;
    }

    @Override
    public MutableLiveData<String> getSubCateErrorRepo() throws Exception {
        return subCateErr_MLD;
    }

    @Override
    public MutableLiveData<Boolean> progressSubCateUpdation() {
        return progressSubCateObservable;
    }
}
