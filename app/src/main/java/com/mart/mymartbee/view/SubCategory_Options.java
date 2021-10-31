package com.mart.mymartbee.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.model.SubCategoryUpdate_Model;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.adapters.SubcateOptionsAdapter;
import com.mart.mymartbee.viewmodel.implementor.SubCateOptionsViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.SubCateOptionsViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubCategory_Options extends AppCompatActivity implements View.OnClickListener,
        SubcateOptionsAdapter.OptionsClickListener, Constants {

    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    ArrayList<Products_Model.ProductCategories> subCategoryList;
    ArrayList<Products_Model.ProductCategories> subCategoryListTemp;
    ImageView subcategory_back;
    EditText search_category_edit;
    RecyclerView category_recycler;
    String options_str = "", strSellerId, strCateId, strSubCateId, strSubCateName;
    SubcateOptionsAdapter subcateOptionsAdapter;
    BottomSheetDialog bottomSheetDialog;
    EditText subcategory_name;
    Button sub_cate_sheet_btn;
    SubCateOptionsViewModel subCateOptionsViewModel;
    ProgressDialog progressDialog;
    boolean isSubCateUpdated = false;
    RelativeLayout subcateoptions_title_layout;
    ImageView icon_search, search_back;
    LinearLayout search_layout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategory_options);

        subCateOptionsViewModel = ViewModelProviders.of(this).get(SubCateOptionsViewModelImpl.class);
        getMyPreferences();
        getMybundle();
        initView();
        observeProgress();
    }

    public void observeProgress() {
        subCateOptionsViewModel.subCateOptionsProgress().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean progressObserve) {
                if(progressObserve) {
                    showProgress();
                } else {
                    hideProgress();
                }
            }
        });
    }

    private void getMyPreferences() {
        myKeyValue = getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(SubCategory_Options.this);
        strSellerId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ID), myKeyValue);
        strCateId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY), myKeyValue);
    }

    private void getMybundle() {
        Bundle bundle = getIntent().getExtras();
        options_str = bundle.getString("subcateOptions");
    }

    public void initView() {
        subcateoptions_title_layout = (RelativeLayout) findViewById(R.id.subcateoptions_title_layout);
        icon_search = (ImageView) findViewById(R.id.icon_search);
        search_back = (ImageView) findViewById(R.id.search_back);
        search_layout = (LinearLayout) findViewById(R.id.search_layout);

        subCategoryList = new ArrayList<Products_Model.ProductCategories>();
        subCategoryListTemp = new ArrayList<Products_Model.ProductCategories>();
        subcategory_back = (ImageView) findViewById(R.id.subcategory_back);
        search_category_edit = (EditText) findViewById(R.id.search_category_edit);
        category_recycler = (RecyclerView) findViewById(R.id.category_recycler);

        subcategory_back.setOnClickListener(this);
        icon_search.setOnClickListener(this);
        search_back.setOnClickListener(this);

        subCategoryList = StorageDatas.getInstance().getSubCategoryList();
        subCategoryListTemp.addAll(subCategoryList);
        setListeners();
        setAdapter();
    }

    public void setListeners() {

        search_category_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                shortList(s);
            }
        });
    }

    public void shortList(Editable s) {
        if (subCategoryListTemp != null) {
            if (subCategoryListTemp.size() > 0) {
                String value = "" + s;
                value = value.trim();
                subCategoryList.clear();

                if (value != null) {
                    if (value.equals("")) {
                        subCategoryList.addAll(subCategoryListTemp);
                    } else {
                        for (int i = 0; i < subCategoryListTemp.size(); i++) {
                            if (subCategoryListTemp.get(i).getStrCateName().toLowerCase().
                                    contains(value.toLowerCase())) {
                                Products_Model.ProductCategories item = new Products_Model.ProductCategories();
                                item.setStrCateId(subCategoryListTemp.get(i).getStrCateId());
                                item.setStrCateName(subCategoryListTemp.get(i).getStrCateName());
                                item.setProductsLists(subCategoryListTemp.get(i).getProductsLists());
                                subCategoryList.add(item);
                            }
                        }
                    }
                }
                subcateOptionsAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setAdapter() {
        int resId = 0;
        if(options_str.equalsIgnoreCase("EDIT") ) {
            resId = R.drawable.icon_subcate_edit;
        } else if (options_str.equalsIgnoreCase("DELETE") ) {
            resId = R.drawable.icon_delete_subcate;
        }

        category_recycler.setHasFixedSize(true);
        category_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        subcateOptionsAdapter = new SubcateOptionsAdapter(subCategoryList, getApplicationContext(), SubCategory_Options.this, resId);
        category_recycler.setAdapter(subcateOptionsAdapter);
    }

    public void openKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow( search_back.getApplicationWindowToken(),  InputMethodManager.SHOW_FORCED, 0);
    }

    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_back.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_search:
                openKeyboard();
                search_category_edit.requestFocus();
                subcateoptions_title_layout.setVisibility(View.GONE);
                search_layout.setVisibility(View.VISIBLE);
                break;

            case R.id.search_back:
                closeKeyboard();
                search_category_edit.setText("");
                subcateoptions_title_layout.setVisibility(View.VISIBLE);
                search_layout.setVisibility(View.GONE);
                break;

            case R.id.subcategory_back:
                onBackPressed();
                break;

            case R.id.sub_cate_sheet_btn:
                bottomSheetDialog.dismiss();
                strSubCateName = subcategory_name.getText().toString().trim();
                if(strSubCateName.equalsIgnoreCase("")) {
                    CommonMethods.Toast(SubCategory_Options.this, "Please enter sub-category name.");
                    return;
                }

                Map<String, String> params = new HashMap<>();
                params.put("seller_id", strSellerId);
                params.put("cat_id", strCateId);
                params.put("sub_cat_id", strSubCateId);
                params.put("sub_categorie_name", strSubCateName);

                if (NetworkAvailability.isNetworkAvailable(SubCategory_Options.this)) {
                    subCateOptionsViewModel.getUpdatedSubCategories( params);
                } else {
                    NetworkAvailability networkAvailability = new NetworkAvailability(this);
                    networkAvailability.noInternetConnection(SubCategory_Options.this, Constants.NETWORK_ENABLE_SETTINGS);
                }
                subCateOptionsViewModel.getUpdatedSubCategory().observe(this, new Observer<SubCategoryUpdate_Model>() {
                    @Override
                    public void onChanged(SubCategoryUpdate_Model subCategoryUpdate_model) {
                        if(subCategoryUpdate_model.isStrStatus() == true) {
                            isSubCateUpdated = true;
                            for(int i=0; i < subCategoryList.size(); i++) {
                                if(strSubCateId.equalsIgnoreCase(subCategoryList.get(i).getStrCateId())) {
                                    subCategoryList.get(i).setStrCateName(strSubCateName);
                                    subCategoryListTemp.get(i).setStrCateName(strSubCateName);
                                }
                            }
                            subcateOptionsAdapter.notifyDataSetChanged();
                            showSweetAlertDialog("Success" , strSubCateName + " updated successfully.", 2);
                        } else {
                            showSweetAlertDialog("Error", subCategoryUpdate_model.getStrMessage(),  1);
                        }
                    }
                });
                break;
        }
    }

    public void showSweetAlertDialog(String title, String strMsg, int dialog_type) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SubCategory_Options.this, dialog_type);
        sweetAlertDialog.setTitleText(title);
        sweetAlertDialog.setContentText(strMsg);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sweetAlertDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(int position, String subcate_id, String subcate_name) {
        if(options_str.equalsIgnoreCase("EDIT") ) {
            showEditBottomSheet(position, subcate_id, subcate_name);
        } else if (options_str.equalsIgnoreCase("DELETE") ) {
            strSubCateName = subcate_name;
            showDeleteOption(position, subcate_id, subcate_name);
        }
    }

    public void showEditBottomSheet(int position, String subcate_id, String subcate_name) {
        strSubCateId = subcate_id;
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_subcate_options);
        bottomSheetDialog.setCancelable(true);
        subcategory_name = (EditText) bottomSheetDialog.findViewById(R.id.subcategory_name);
        sub_cate_sheet_btn = (Button) bottomSheetDialog.findViewById(R.id.sub_cate_sheet_btn);
        sub_cate_sheet_btn.setOnClickListener(this);
        subcategory_name.setText(subcate_name);
        bottomSheetDialog.show();
    }

    public void showDeleteOption(int position, String subcate_id, String subcate_name) {
        strSubCateId = subcate_id;
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SubCategory_Options.this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("Warning");
        sweetAlertDialog.setContentText("If you delete " + subcate_name + ", your all products under " + subcate_name + " will be deleted. Can you proceed?");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        sweetAlertDialog.setCancelText(getResources().getString(R.string.option_no));
        sweetAlertDialog.setConfirmText(getResources().getString(R.string.option_yes));
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                deleteSubCategory();
            }
        });
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
    }

    public void deleteSubCategory() {
        Map<String, String> params = new HashMap<>();
        params.put("seller_id", strSellerId);
        params.put("cat_id", strCateId);
        params.put("sub_cat_id", strSubCateId);

        Log.e("appSample", "SellerId: " + strSellerId);
        Log.e("appSample", "CateId: " + strCateId);
        Log.e("appSample", "SubCateId: " + strSubCateId);

        if (NetworkAvailability.isNetworkAvailable(SubCategory_Options.this)) {
            subCateOptionsViewModel.getDeletedSubCategories( params);
        } else {
            NetworkAvailability networkAvailability = new NetworkAvailability(this);
            networkAvailability.noInternetConnection(SubCategory_Options.this, Constants.NETWORK_ENABLE_SETTINGS);
        }

        subCateOptionsViewModel.getDeletedSubCategory().observe(this, new Observer<SubCategoryUpdate_Model>() {
            @Override
            public void onChanged(SubCategoryUpdate_Model subCategoryUpdate_model) {
                    if(subCategoryUpdate_model.isStrStatus() == true) {
                        showSweetAlertDialog("Success" , strSubCateName + " deleted successfully.", 2);
                        isSubCateUpdated = true;
                        for(int i=0; i < subCategoryList.size(); i++) {
                            if(strSubCateId.equalsIgnoreCase(subCategoryList.get(i).getStrCateId())) {
                                subCategoryList.remove(i);
                                subCategoryListTemp.remove(i);
                            }
                        }
                        subcateOptionsAdapter.notifyDataSetChanged();
                    } else {
                        showSweetAlertDialog("Error", subCategoryUpdate_model.getStrMessage(),  1);
                    }
                }
        });
    }

    @Override
    public void onBackPressed() {
        subCategoryList.clear();
        subCategoryList.addAll(subCategoryListTemp);

        if(isSubCateUpdated == true) {
            setResult(SUBCATE_update_success);
            finish();
        } else {
            finish();
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgress() {
        if(progressDialog != null) {
            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }
}
