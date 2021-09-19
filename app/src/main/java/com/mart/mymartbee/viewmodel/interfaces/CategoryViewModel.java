package com.mart.mymartbee.viewmodel.interfaces;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.OnLifecycleEvent;

import com.mart.mymartbee.model.Category_Model;

import java.util.Map;

public interface CategoryViewModel {

    LiveData<Category_Model> getCategoryListLV();
    LiveData<String> getCategoryError();
    LiveData<Boolean> progressCateUpdation();
    void getCateGories();

}
