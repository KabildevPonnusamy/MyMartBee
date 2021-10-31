package com.mart.mymartbee.viewmodel.implementor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mart.mymartbee.model.SubCategoryUpdate_Model;
import com.mart.mymartbee.repository.implementor.SubCategoryOptionsRepoImpl;
import com.mart.mymartbee.viewmodel.interfaces.SubCateOptionsViewModel;

import java.util.ArrayList;
import java.util.Map;

public class SubCateOptionsViewModelImpl extends ViewModel implements SubCateOptionsViewModel {

    private SubCategoryOptionsRepoImpl subCategoryOptionsRepo;
    private MutableLiveData<SubCategoryUpdate_Model> updatedSubCateOptions;
    private MutableLiveData<SubCategoryUpdate_Model> deletedSubCateOptions;
    private MutableLiveData<String> subcateOptionsErr;
    private MutableLiveData<Boolean> subcateOptionsProgress;


    public SubCateOptionsViewModelImpl() {
        updatedSubCateOptions = new MutableLiveData<SubCategoryUpdate_Model>();
        deletedSubCateOptions = new MutableLiveData<SubCategoryUpdate_Model>();
        subcateOptionsErr = new MutableLiveData<String>();
        subcateOptionsProgress = new MutableLiveData<Boolean>();
        subCategoryOptionsRepo = new SubCategoryOptionsRepoImpl();
    }

    @Override
    public LiveData<SubCategoryUpdate_Model> getUpdatedSubCategory() {
        return updatedSubCateOptions;
    }

    @Override
    public LiveData<SubCategoryUpdate_Model> getDeletedSubCategory() {
        return deletedSubCateOptions;
    }

    @Override
    public LiveData<String> subCateOptionsError() {
        return subcateOptionsErr;
    }

    @Override
    public LiveData<Boolean> subCateOptionsProgress() {
        try {
            subcateOptionsProgress = subCategoryOptionsRepo.subCateUpdProgress();
        } catch(Exception e) {
            subcateOptionsProgress.setValue(false);
        }
        return subcateOptionsProgress;
    }

    @Override
    public void getUpdatedSubCategories(Map<String, String> params) {
        try {
            updatedSubCateOptions = subCategoryOptionsRepo.updateSubCategory(params);
        } catch(Exception e) {
            subcateOptionsErr.setValue(e.getMessage());
        }

    }

    @Override
    public void getDeletedSubCategories(Map<String, String> params) {
        try {
            deletedSubCateOptions = subCategoryOptionsRepo.deleteSubCategory(params);
        } catch(Exception e) {
            subcateOptionsErr.setValue(e.getMessage());
        }
    }
}
