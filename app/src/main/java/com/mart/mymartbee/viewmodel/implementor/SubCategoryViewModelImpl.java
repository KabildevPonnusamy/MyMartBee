package com.mart.mymartbee.viewmodel.implementor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mart.mymartbee.model.SubCategory_Model;
import com.mart.mymartbee.repository.implementor.SubCategoryListRepoImpl;
import com.mart.mymartbee.viewmodel.interfaces.SubCategoryViewModel;

import java.util.Map;

public class SubCategoryViewModelImpl extends ViewModel implements SubCategoryViewModel {

    private MutableLiveData<SubCategory_Model> mutableSubCategoryList;
    private MutableLiveData<SubCategory_Model> mutableAddedSubCategoryList;
    private MutableLiveData<String> mutableSubCateError;
    private MutableLiveData<Boolean> mutableProgressSubCateObserver;
    private SubCategoryListRepoImpl subCategoryListRepo;

    public SubCategoryViewModelImpl() {
        subCategoryListRepo = new SubCategoryListRepoImpl();
        mutableSubCategoryList = new MutableLiveData<SubCategory_Model>();
        mutableAddedSubCategoryList = new MutableLiveData<SubCategory_Model>();
        mutableSubCateError = new MutableLiveData<String>();
        mutableProgressSubCateObserver = new MutableLiveData<Boolean>();
    }

    @Override
    public LiveData<SubCategory_Model> getSubCategoryList() {
        return mutableSubCategoryList;
    }

    @Override
    public LiveData<SubCategory_Model> getAddedSubCategoryList() {
        return mutableAddedSubCategoryList;
    }

    @Override
    public LiveData<String> getSubCategoryError() {
        return mutableSubCateError;
    }

    @Override
    public LiveData<Boolean> progressSubCateUpdation() {
        try {
            mutableProgressSubCateObserver = subCategoryListRepo.progressSubCateUpdation();
        } catch(Exception e) {
            mutableProgressSubCateObserver.setValue(false);
        }
        return mutableProgressSubCateObserver;
    }

    @Override
    public void getSubCategories(String cate_id) {
        try {
            mutableSubCategoryList = subCategoryListRepo.subCategoryRepo(cate_id);
        } catch(Exception e) {
            mutableSubCateError.setValue(e.getMessage());
        }
    }

    @Override
    public void getAddedSubCategories(Map<String, String> params) {
        try {
            mutableAddedSubCategoryList = subCategoryListRepo.getAddedSubCategoryRepo(params);
        } catch (Exception e) {
            mutableSubCateError.setValue(e.getMessage());
        }
    }
}
