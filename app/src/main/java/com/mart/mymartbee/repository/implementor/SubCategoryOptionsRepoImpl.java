package com.mart.mymartbee.repository.implementor;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mart.mymartbee.model.SubCategoryUpdate_Model;
import com.mart.mymartbee.networking.retrofit.ApiCallBack;
import com.mart.mymartbee.networking.retrofit.ApiClient;
import com.mart.mymartbee.repository.interfaces.SubCategoryOptionsRepo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryOptionsRepoImpl implements SubCategoryOptionsRepo {

    private MutableLiveData<String> subcateUpdErr;
    private MutableLiveData<Boolean> subcateUpdProgress;

    private static SubCategoryOptionsRepoImpl instance = null;
    private static SubCategoryOptionsRepoImpl getInstance() {
        if(instance == null) {
            instance = new SubCategoryOptionsRepoImpl();
        }
        return instance;
    }

    public SubCategoryOptionsRepoImpl() {
        subcateUpdErr = new MutableLiveData<String>();
        subcateUpdProgress = new MutableLiveData<Boolean>();
    }

    @Override
    public MutableLiveData<SubCategoryUpdate_Model> updateSubCategory(Map<String, String> params) throws Exception {
        subcateUpdProgress.setValue(true);
        MutableLiveData<SubCategoryUpdate_Model> updSubcateMLD = new MutableLiveData<SubCategoryUpdate_Model>();
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<SubCategoryUpdate_Model> call = callBack.updateSubCategory(params);
        call.enqueue(new Callback<SubCategoryUpdate_Model>() {
            @Override
            public void onResponse(Call<SubCategoryUpdate_Model> call, Response<SubCategoryUpdate_Model> response) {
                subcateUpdProgress.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            updSubcateMLD.setValue(response.body());
                        }
                    } else {
                        updSubcateMLD.setValue(null);
                        subcateUpdErr.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "ResponseExc: " + e.getMessage());
                    updSubcateMLD.setValue(null);
                    subcateUpdErr.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SubCategoryUpdate_Model> call, Throwable t) {
                Log.e("appSample", "Failure: " + t.getMessage());
                subcateUpdProgress.setValue(false);
                updSubcateMLD.setValue(null);
                subcateUpdErr.setValue("Connection Error");
            }
        });

        return updSubcateMLD;
    }

    @Override
    public MutableLiveData<SubCategoryUpdate_Model> deleteSubCategory(Map<String, String> params) throws Exception {
        subcateUpdProgress.setValue(true);
        MutableLiveData<SubCategoryUpdate_Model> deleteSubcateMLD = new MutableLiveData<SubCategoryUpdate_Model>();
        ApiCallBack callBack = ApiClient.getClient().create(ApiCallBack.class);
        Call<SubCategoryUpdate_Model> call = callBack.deleteSubCategory(params);
        call.enqueue(new Callback<SubCategoryUpdate_Model>() {
            @Override
            public void onResponse(Call<SubCategoryUpdate_Model> call, Response<SubCategoryUpdate_Model> response) {
                subcateUpdProgress.setValue(false);
                try {
                    if(response.isSuccessful()) {
                        if (response.body() != null) {
                            deleteSubcateMLD.setValue(response.body());
                        }
                    } else {
                        deleteSubcateMLD.setValue(null);
                        subcateUpdErr.setValue("No Response");
                    }
                } catch(Exception e) {
                    Log.e("appSample", "ResponseExc: " + e.getMessage());
                    deleteSubcateMLD.setValue(null);
                    subcateUpdErr.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SubCategoryUpdate_Model> call, Throwable t) {
                Log.e("appSample", "Failure: " + t.getMessage());
                subcateUpdProgress.setValue(false);
                deleteSubcateMLD.setValue(null);
                subcateUpdErr.setValue("Connection Error");
            }
        });

        return deleteSubcateMLD;
    }

    @Override
    public MutableLiveData<String> subCateUpdError() throws Exception {
        return subcateUpdErr;
    }

    @Override
    public MutableLiveData<Boolean> subCateUpdProgress() throws Exception {
        return subcateUpdProgress;
    }
}
