package com.mart.mymartbee.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.viewmodel.implementor.ProductsViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.ProductsViewModel;

import java.util.HashMap;
import java.util.Map;

public class ProductDetails extends AppCompatActivity implements View.OnClickListener, Constants {

    ImageView prod_details_back, prod_details_image;
    TextView prod_details_title, prod_details_subcate, prod_details_oldprice, prod_details_price, prod_details_desc,
            prod_details_stock_with_uom;
    TextView product_edit_btn, product_delete_btn;
    Products_Model.ProductCategories.ProductsList productsObj;

    ProductsViewModel productsViewModel;
    ProgressDialog progressDialog;

    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    String strCateId, strSellerId, strProductId;
    String strSubCateId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModelImpl.class);
        initView();
        observeProgress();
        getMyPreferences();

    }

    private void getMyPreferences() {
        myKeyValue = getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(ProductDetails.this);
        strCateId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY), myKeyValue);
        strSellerId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ID), myKeyValue);
    }

    public void observeProgress() {
        productsViewModel.progressProductUpdation().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean progressObserve) {
                if (progressObserve) {
                    showProgress();
                } else {
                    hideProgress();
                }
            }
        });
    }

    private void initView() {
        prod_details_back = findViewById(R.id.prod_details_back);
        prod_details_image = findViewById(R.id.prod_details_image);
        prod_details_title = findViewById(R.id.prod_details_title);
        prod_details_subcate = findViewById(R.id.prod_details_subcate);
        prod_details_oldprice = findViewById(R.id.prod_details_oldprice);
        prod_details_price = findViewById(R.id.prod_details_price);
        prod_details_desc = findViewById(R.id.prod_details_desc);
        prod_details_stock_with_uom = findViewById(R.id.prod_details_stock_with_uom);
        product_edit_btn = findViewById(R.id.product_edit_btn);
        product_delete_btn = findViewById(R.id.product_delete_btn);
        prod_details_back.setOnClickListener(this);
        product_edit_btn.setOnClickListener(this);
        product_delete_btn.setOnClickListener(this);

        setValues();
    }

    private void setValues() {
        productsObj = StorageDatas.getsObj().getProductsObj();
        strProductId = productsObj.getStrProdut_id();

        Bundle bundle = getIntent().getExtras();
        strSubCateId = bundle.getString("subcateId");
        prod_details_title.setText(productsObj.getStrProduct_title());
        prod_details_subcate.setText(bundle.getString("subcateName"));

        String oldPrice = productsObj.getStrProduct_oldprice().replace(".00", "");
        prod_details_oldprice.setText("RM. " + oldPrice);

        String newPrice = productsObj.getStrProduct_price().replace(".00", "");
        prod_details_price.setText("RM. " + newPrice);

        /*prod_details_oldprice.setText("RM. " + productsObj.getStrProduct_oldprice());
        prod_details_price.setText("RM. " + productsObj.getStrProduct_price());*/
        prod_details_desc.setText(productsObj.getStrProduct_description());
        prod_details_stock_with_uom.setText(productsObj.getStrProduct_quantity() + " " + productsObj.getStrProduct_uom());
        Glide.with(getApplicationContext()).load(productsObj.getStrProduct_image()).into(prod_details_image);

        prod_details_oldprice.setPaintFlags(prod_details_oldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prod_details_back:
                finish();
                break;

            case R.id.product_edit_btn:
                if (NetworkAvailability.isNetworkAvailable(ProductDetails.this)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("fromActivity", "ProductDetails");
                    bundle.putString("fromActivitySubcategory", prod_details_subcate.getText().toString().trim());
                    bundle.putInt("fromActivitySubcategoryID", Integer.parseInt(strSubCateId));
                    Intent intent = new Intent(ProductDetails.this, AddProduct.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, PRODUCT_DETAILS_to_PRODUCT_EDIT);
                } else {
                    NetworkAvailability networkAvailability = new NetworkAvailability(this);
                    networkAvailability.noInternetConnection(ProductDetails.this, Constants.NETWORK_ENABLE_SETTINGS);
                }
                break;

            case R.id.product_delete_btn:
                Map<String, String> params = new HashMap<>();
                params.put("seller_id", strSellerId);
                params.put("cat_id", strCateId);
                params.put("product_id", strProductId);

                if (NetworkAvailability.isNetworkAvailable(ProductDetails.this)) {
                    productsViewModel.deleteProducts(params);
                } else {
                    NetworkAvailability networkAvailability = new NetworkAvailability(this);
                    networkAvailability.noInternetConnection(ProductDetails.this, Constants.NETWORK_ENABLE_SETTINGS);
                }

                productsViewModel.deleteProductLV().observe(this, new Observer<Products_Model>() {
                    @Override
                    public void onChanged(Products_Model products_model) {
                        if (products_model.isStrStatus() == false) {
                            showErrorDialog(products_model.getStrMessage());
                        } else {
                            showSuccessDialog(products_model);
                        }
                    }
                });
                break;
        }
    }

    private void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    private void showErrorDialog(String errMessage) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ProductDetails.this, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText("Error!");
        sweetAlertDialog.setContentText(errMessage);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sweetAlertDialog.dismiss();
            }
        });
    }

    private void showSuccessDialog(Products_Model products_model) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ProductDetails.this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Success");
        sweetAlertDialog.setContentText("Product deleted successfully.");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sweetAlertDialog.dismiss();
                StorageDatas.getInstance().setProducts_model(products_model);
                setResult(PRODUCT_DELETION_success);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PRODUCT_DETAILS_to_PRODUCT_EDIT) {
            if (resultCode == PRODUCT_UPDATED_success) {
                setResult(PRODUCT_UPDATED_success);
                finish();
            }
        }
    }
}
