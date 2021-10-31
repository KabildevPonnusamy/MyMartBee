package com.mart.mymartbee.repository.interfaces;

import androidx.lifecycle.MutableLiveData;
import com.mart.mymartbee.model.SubCategory_Model;
import java.util.Map;

public interface SubCategoryListRepo {

    MutableLiveData<SubCategory_Model> subCategoryRepo(String seller_id, String cate_id) throws Exception;
    MutableLiveData<SubCategory_Model> getAddedSubCategoryRepo(Map<String, String> params) throws Exception;
    MutableLiveData<String> getSubCateErrorRepo() throws Exception;
    MutableLiveData<Boolean> progressSubCateUpdation();

}
