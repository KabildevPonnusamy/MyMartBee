package com.mart.mymartbee.viewmodel.implementor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mart.mymartbee.model.BusinessCategory_Model;
import com.mart.mymartbee.repository.implementor.BusinessCategoryListRepoImpl;
import com.mart.mymartbee.viewmodel.interfaces.BusinessCategoryViewModel;

import java.util.Map;

public class BusinessCateGoryViewModelImplBusiness extends ViewModel implements BusinessCategoryViewModel {

    private MutableLiveData<BusinessCategory_Model> mutableCategoryList;
    private MutableLiveData<BusinessCategory_Model> mutableAddedCategoryList;
    private MutableLiveData<String> mutableCateError;
    private MutableLiveData<Boolean> progressCateObserver;
    private BusinessCategoryListRepoImpl categoryListRepo;

    public BusinessCateGoryViewModelImplBusiness() {
        categoryListRepo = new BusinessCategoryListRepoImpl();
        mutableCategoryList = new MutableLiveData<BusinessCategory_Model>();
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
    public LiveData<BusinessCategory_Model> getAddedCategoryListLV() {
        return mutableAddedCategoryList;
    }

    @Override
    public void getAddedCategories(Map<String, String> params) {
        try {
            mutableAddedCategoryList = categoryListRepo.getAddedCategoryRepo(params);
        } catch (Exception e) {
            mutableCateError.setValue(e.getMessage());
        }
    }

    @Override
    public LiveData<BusinessCategory_Model> getCategoryListLV() {
        return mutableCategoryList;
    }

    @Override
    public LiveData<String> getCategoryError() {
        return mutableCateError;
    }

    @Override
    public void getCateGories() {
        try {
            mutableCategoryList = categoryListRepo.getCategoryRepo();
        } catch(Exception e) {
            mutableCateError.setValue(e.getMessage());
        }
    }
}
