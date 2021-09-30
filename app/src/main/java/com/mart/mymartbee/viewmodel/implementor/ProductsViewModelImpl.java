package com.mart.mymartbee.viewmodel.implementor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.model.UOMModel;
import com.mart.mymartbee.repository.implementor.ProductListRepoImpl;
import com.mart.mymartbee.viewmodel.interfaces.ProductsViewModel;

import java.io.File;
import java.util.Map;

public class ProductsViewModelImpl extends ViewModel implements ProductsViewModel {

    private MutableLiveData<Products_Model> mutableAddProducts;
    private MutableLiveData<Products_Model> mutableEditProducts;
    private MutableLiveData<Products_Model> mutableEditWithImageProducts;
    private MutableLiveData<Products_Model> mutableDeleteProducts;
    private MutableLiveData<Products_Model> mutableGetProducts;
    private MutableLiveData<UOMModel> mutableGetUOM;
    private MutableLiveData<String> mutableProductError;
    private MutableLiveData<Boolean> progressProductObserver;
    private ProductListRepoImpl productListRepo;

    public ProductsViewModelImpl() {
        productListRepo = new ProductListRepoImpl();
        mutableAddProducts = new MutableLiveData<Products_Model>();
        mutableEditProducts = new MutableLiveData<Products_Model>();
        mutableEditWithImageProducts = new MutableLiveData<Products_Model>();
        mutableDeleteProducts = new MutableLiveData<Products_Model>();
        mutableGetProducts = new MutableLiveData<Products_Model>();
        mutableGetUOM = new MutableLiveData<UOMModel>();
        mutableProductError = new MutableLiveData<String>();
        progressProductObserver = new MutableLiveData<Boolean>();
    }


    @Override
    public LiveData<Products_Model> addProductLV() {
        return mutableAddProducts;
    }

    @Override
    public LiveData<Products_Model> deleteProductLV() {
        return mutableDeleteProducts;
    }

    @Override
    public LiveData<Products_Model> editProductLV() {
        return mutableEditProducts;
    }

    @Override
    public LiveData<Products_Model> getProductLV() {
        return mutableGetProducts;
    }

    @Override
    public LiveData<String> getProductError() {
        return mutableProductError;
    }

    @Override
    public LiveData<UOMModel> getUOMLV() {
        return mutableGetUOM;
    }

    @Override
    public LiveData<Products_Model> editProductwithImageLV() {
        return mutableEditWithImageProducts;
    }

    @Override
    public void editProductwithImage(File file, Map<String, String> params) {
        try {
            mutableEditWithImageProducts = productListRepo.editProductRepoWithImage(file, params);
        } catch(Exception e) {
            mutableProductError.setValue(e.getMessage());
        }
    }

    @Override
    public void getUOM() {
        try {
            mutableGetUOM = productListRepo.getUOMDatas();
        } catch(Exception e) {
            mutableProductError.setValue(e.getMessage());
        }
    }

    @Override
    public LiveData<Boolean> progressProductUpdation() {
        try {
            progressProductObserver = productListRepo.progressProductUpdation();
        } catch(Exception e) {
            progressProductObserver.setValue(false);
        }
        return progressProductObserver;
    }

    @Override
    public void addProducts(File file, Map<String, String> params) {
        try {
            mutableAddProducts = productListRepo.addProductRepo(file, params);
        } catch (Exception e) {
            mutableProductError.setValue(e.getMessage());
        }
    }

    @Override
    public void deleteProducts(Map<String, String> params) {
        try {
            mutableDeleteProducts = productListRepo.deleteProductRepo(params);
        } catch (Exception e) {
            mutableProductError.setValue(e.getMessage());
        }
    }

    @Override
    public void editProducts(Map<String, String> params) {
        try {
            mutableEditProducts = productListRepo.editProductRepo(params);
        } catch (Exception e) {
            mutableProductError.setValue(e.getMessage());
        }
    }

    @Override
    public void getProducts(String cate_id, String seller_id) {
        try {
            mutableGetProducts = productListRepo.getProductRepo(cate_id, seller_id);
        } catch (Exception e) {
            mutableProductError.setValue(e.getMessage());
        }
    }
}
