package com.mart.mymartbee.viewmodel.implementor;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mart.mymartbee.model.Category_Model;
import com.mart.mymartbee.repository.implementor.CategoryListRepoImpl;
import com.mart.mymartbee.viewmodel.interfaces.CategoryViewModel;

import java.util.Map;

public class CateGoryViewModelImpl extends ViewModel implements CategoryViewModel {

    private MutableLiveData<Category_Model> mutableCategoryList;
    private MutableLiveData<String> mutableCateError;
    private MutableLiveData<Boolean> progressCateObserver;
    private CategoryListRepoImpl categoryListRepo;

    public CateGoryViewModelImpl() {
        categoryListRepo = new CategoryListRepoImpl();
        mutableCategoryList = new MutableLiveData<Category_Model>();
        mutableCateError = new MutableLiveData<String>();
        progressCateObserver = new MutableLiveData<Boolean>();
    }

    @Override
    public LiveData<Boolean> progressCateUpdation() {
        try {
            progressCateObserver = categoryListRepo.progressCateUpdation();
        } catch(Exception e) {
            progressCateObserver.setValue(false);
        }
        return progressCateObserver;
    }

    @Override
    public LiveData<Category_Model> getCategoryListLV() {
        return mutableCategoryList;
    }

    @Override
    public LiveData<String> getCategoryError() {
        return mutableCateError;
    }

    @Override
    public void getCateGories() {
        Log.e("appSample", "GetCategoryVMCalled");
        try {
            mutableCategoryList = categoryListRepo.getCategoryRepo();
        } catch(Exception e) {
            mutableCateError.setValue(e.getMessage());
        }
    }
}
