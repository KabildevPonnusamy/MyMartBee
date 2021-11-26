package com.mart.mymartbee.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mart.mymartbee.R;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.model.BusinessCategory_Model;
import com.mart.mymartbee.viewmodel.implementor.BusinessCateGoryViewModelImplBusiness;
import com.mart.mymartbee.viewmodel.interfaces.BusinessCategoryViewModel;
import com.mart.mymartbee.view.adapters.BusinessCategoryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BusinessCategorySelection extends AppCompatActivity implements View.OnClickListener, Constants {

    ImageView cate_back;
    EditText cate_search_edit;
    RecyclerView cate_recycle;
    Button add_category_btn;

    String strMobileNumber = "";
    int selectedId = -1;

    ProgressDialog progressDialog;
    BusinessCategoryViewModel businessCateGoryViewModel;
    BusinessCategoryAdapter businessCategoryAdapter;
    ArrayList<BusinessCategory_Model.Categorys> businessCategoryList;
    ArrayList<BusinessCategory_Model.Categorys> businessCategoryListTemp;
    BottomSheetDialog bottomSheetDialog;
    EditText category_name;
    Button add_cate_sheet_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_category_selection);

        businessCateGoryViewModel = ViewModelProviders.of(this).get(BusinessCateGoryViewModelImplBusiness.class);
        initView();
        getBundleData();
        updateView();
        observeCateProgress();
    }

    public void observeCateProgress() {
        businessCateGoryViewModel.progressCateUpdation().observe(this, new Observer<Boolean>() {
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

    public void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        strMobileNumber = bundle.getString("mobileNumber");
        selectedId = bundle.getInt("SelectedId");
        Log.e("appSample", "SelectedId: " + selectedId);
    }

    public void initView() {
        businessCategoryList = new ArrayList<BusinessCategory_Model.Categorys>();
//        prefcategoryList = new ArrayList<Category_Model.Categorys>();
        businessCategoryListTemp = new ArrayList<BusinessCategory_Model.Categorys>();
        cate_back = findViewById(R.id.cate_back);
        add_category_btn = findViewById(R.id.add_category_btn);
        cate_search_edit = findViewById(R.id.cate_search_edit);
        cate_recycle = findViewById(R.id.cate_recycle);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_add_category);
        bottomSheetDialog.setCancelable(true);
        category_name = (EditText) bottomSheetDialog.findViewById(R.id.category_name);
        add_cate_sheet_btn = (Button) bottomSheetDialog.findViewById(R.id.add_cate_sheet_btn);

        cate_back.setOnClickListener(this);
        add_category_btn.setOnClickListener(this);
        add_cate_sheet_btn.setOnClickListener(this);
        listeners();
    }

    public void updateView() {
        if (NetworkAvailability.isNetworkAvailable(BusinessCategorySelection.this)) {
            businessCateGoryViewModel.getCateGories();
        } else {
            NetworkAvailability networkAvailability = new NetworkAvailability(this);
            networkAvailability.noInternetConnection(BusinessCategorySelection.this, Constants.NETWORK_ENABLE_SETTINGS);
        }

        businessCateGoryViewModel.getCategoryListLV().observe(this, new Observer<BusinessCategory_Model>() {
            @Override
            public void onChanged(BusinessCategory_Model businessCategory_model) {
                businessCategoryList = businessCategory_model.getCategorys();
                if(businessCategoryList != null) {
                    if(businessCategoryList.size() > 0) {
                        businessCategoryListTemp.addAll(businessCategoryList);
                        cate_recycle.setHasFixedSize(true);
                        cate_recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        businessCategoryAdapter = new BusinessCategoryAdapter(businessCategoryList, getApplicationContext(), selectedId);
                        cate_recycle.setAdapter(businessCategoryAdapter);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cate_back:
                finish();
                break;

            case R.id.add_category_btn:
                bottomSheetDialog.show();
                break;

            case R.id.add_cate_sheet_btn:
                String strCate = category_name.getText().toString().trim();
                if(strCate.equalsIgnoreCase("")) {
                    CommonMethods.Toast(BusinessCategorySelection.this,  "Please enter category");
                    return;
                }

                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile_no", strMobileNumber);
                params.put("categorie_name", strCate);

                Log.e("appSample", "Mobile: " + strMobileNumber);
                Log.e("appSample", "Category: " + strCate);

                if (NetworkAvailability.isNetworkAvailable(BusinessCategorySelection.this)) {
                    businessCateGoryViewModel.getAddedCategories(params);
                } else {
                    NetworkAvailability networkAvailability = new NetworkAvailability(this);
                    networkAvailability.noInternetConnection(BusinessCategorySelection.this, CATEGORY_CREATION);
                }
                businessCateGoryViewModel.getAddedCategoryListLV().observe(this, new Observer<BusinessCategory_Model>() {
                    @Override
                    public void onChanged(BusinessCategory_Model businessCategory_model) {
                        if(businessCategory_model.isStrStatus() == true) {
                            businessCategoryList.clear();
                            businessCategoryList.addAll(businessCategory_model.getCategorys());
                            businessCategoryAdapter.notifyDataSetChanged();
                            category_name.setText("");
                            bottomSheetDialog.dismiss();
                        }
                    }
                });

                break;
        }
    }

    public void listeners() {
        cate_recycle.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    moveBack(position);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        cate_search_edit.addTextChangedListener(new TextWatcher() {
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

    private void moveBack(int position) {
        Intent intent = new Intent();
        intent.putExtra("SelectedCategory", "" + businessCategoryList.get(position).getStrCateGoryName());
        intent.putExtra("SelectedId", Integer.parseInt(businessCategoryList.get(position).getStrCategoryId()));
        setResult(CATEGORY_SELECTED, intent);
        finish();
    }

    private void shortList(Editable s) {

        if (businessCategoryListTemp != null) {
            if (businessCategoryListTemp.size() > 0) {
                String value = "" + s;
                value = value.trim();
                businessCategoryList.clear();

                if (value != null) {
                    if (value.equals("")) {
                        businessCategoryList.addAll(businessCategoryListTemp);
                    } else {
                        for (int i = 0; i < businessCategoryListTemp.size(); i++) {
                            if (businessCategoryListTemp.get(i).getStrCateGoryName().toLowerCase().
                                    contains(value.toLowerCase())) {
                                BusinessCategory_Model.Categorys item = new BusinessCategory_Model.Categorys();
                                item.setStrCategoryId(businessCategoryListTemp.get(i).getStrCategoryId());
                                item.setStrCategoryImage(businessCategoryListTemp.get(i).getStrCategoryImage());
                                item.setStrCateGoryName(businessCategoryListTemp.get(i).getStrCateGoryName());
                                item.setStrCategoryStatus(businessCategoryListTemp.get(i).getStrCategoryStatus());
                                businessCategoryList.add(item);
                            }
                        }
                    }
                }
                businessCategoryAdapter.notifyDataSetChanged();
            }
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NETWORK_ENABLE_SETTINGS) {
            updateView();
        }
    }
}
