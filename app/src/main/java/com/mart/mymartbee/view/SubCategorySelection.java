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
import android.widget.LinearLayout;

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
import com.mart.mymartbee.model.SubCategory_Model;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.adapters.SubCategoryAdapter;
import com.mart.mymartbee.viewmodel.implementor.SubCategoryViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.SubCategoryViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubCategorySelection extends AppCompatActivity implements View.OnClickListener, Constants {

    ImageView subcate_back;
    EditText subcate_search_edit;
    RecyclerView subcate_recycle;
    LinearLayout no_data_found_layout;
//    Button add_subcate_btn;

    ProgressDialog progressDialog;
    SubCategoryViewModel subCategoryViewModel;
    ArrayList<SubCategory_Model.SubCategory> subCategoryList;
    ArrayList<SubCategory_Model.SubCategory> subCategoryListTemp;
    String cate_id = "";
    String strSellerId = "";
    int selectedId = -1;
    SubCategoryAdapter subcategoryAdapter;
    BottomSheetDialog bottomSheetDialog;
    EditText subcategory_name;
    Button add_subcate_sheet_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategory_selection);

        subCategoryViewModel = ViewModelProviders.of(this).get(SubCategoryViewModelImpl.class);
        StorageDatas.getInstance().setSubCateAdded(false);
        initView();
        sheetDialoginitView();
        getBundleData();
        updateView();
        observeSubCateProgress();
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        cate_id = bundle.getString("CategoryId");
        strSellerId = bundle.getString("SellerId");
        selectedId = bundle.getInt("SelectedSubId");
    }

    private void sheetDialoginitView() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_add_subcate);
        bottomSheetDialog.setCancelable(true);
        subcategory_name = (EditText) bottomSheetDialog.findViewById(R.id.subcategory_name);
        add_subcate_sheet_btn = (Button) bottomSheetDialog.findViewById(R.id.add_subcate_sheet_btn);
        add_subcate_sheet_btn.setOnClickListener(this);
    }

    public void initView(){
        subCategoryList = new ArrayList<SubCategory_Model.SubCategory>();
        subCategoryListTemp = new ArrayList<SubCategory_Model.SubCategory>();
        no_data_found_layout = (LinearLayout) findViewById(R.id.no_data_found_layout);
        subcate_back = (ImageView) findViewById(R.id.subcate_back);
        subcate_search_edit = (EditText) findViewById(R.id.subcate_search_edit);
        subcate_recycle = (RecyclerView) findViewById(R.id.subcate_recycle);
//        add_subcate_btn = (Button) findViewById(R.id.add_subcate_btn);

        subcate_back.setOnClickListener(this);
//        add_subcate_btn.setOnClickListener(this);

        listeners();
    }

    private void updateView() {
        if (NetworkAvailability.isNetworkAvailable(SubCategorySelection.this)) {
            subCategoryViewModel.getSubCategories(strSellerId, cate_id);
        } else {
            NetworkAvailability networkAvailability = new NetworkAvailability(this);
            networkAvailability.noInternetConnection(SubCategorySelection.this, Constants.NETWORK_ENABLE_SETTINGS);
        }

        subCategoryViewModel.getSubCategoryList().observe(this, new Observer<SubCategory_Model>() {
            @Override
            public void onChanged(SubCategory_Model subCategory_model) {
                subCategoryList = subCategory_model.getSubCategories();
                if(subCategoryList != null) {
                    if(subCategoryList.size() > 0) {
                        showSubCategoryDatas();
                    } else {
                        showNoDataFound();
                    }
                } else {
                    showNoDataFound();
                }
            }
        });
    }

    private void showSubCategoryDatas() {
        no_data_found_layout.setVisibility(View.GONE);
        subcate_recycle.setVisibility(View.VISIBLE);
        subCategoryListTemp.addAll(subCategoryList);
        subcate_recycle.setHasFixedSize(true);
        subcate_recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        subcategoryAdapter = new SubCategoryAdapter(subCategoryList, getApplicationContext(), selectedId);
        subcate_recycle.setAdapter(subcategoryAdapter);
    }

    private void listeners() {
        subcate_recycle.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

        subcate_search_edit.addTextChangedListener(new TextWatcher() {
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
        intent.putExtra("SelectedSubCategory", "" + subCategoryList.get(position).getStrSubCateName());
        intent.putExtra("SelectedSubId", Integer.parseInt(subCategoryList.get(position).getStrSubCateId()));
        setResult(SUB_CATEGORY_SELECTED, intent);
        finish();
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
                            if (subCategoryListTemp.get(i).getStrSubCateName().toLowerCase().
                                    contains(value.toLowerCase())) {
                                SubCategory_Model.SubCategory item = new SubCategory_Model.SubCategory();
                                item.setStrCateId(subCategoryListTemp.get(i).getStrCateId());
                                item.setStrSubCateId(subCategoryListTemp.get(i).getStrSubCateId());
                                item.setStrSubCateName(subCategoryListTemp.get(i).getStrSubCateName());
                                item.setStrSubCateStatus(subCategoryListTemp.get(i).getStrSubCateStatus());
                                subCategoryList.add(item);
                            }
                        }
                    }
                }
                subcategoryAdapter.notifyDataSetChanged();
            }
        }
    }

    private void showNoDataFound() {
        no_data_found_layout.setVisibility(View.VISIBLE);
        subcate_recycle.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_subcate_sheet_btn:
                String strSubCate = subcategory_name.getText().toString().trim();
                if(strSubCate.equalsIgnoreCase("")) {
                    CommonMethods.Toast(SubCategorySelection.this, "Please enter sub-category.");
                    return;
                }

                Map<String, String> params = new HashMap<String, String>();
                params.put("seller_id", strSellerId);
                params.put("cat_id", cate_id);
                params.put("sub_categorie_name", strSubCate);

                if (NetworkAvailability.isNetworkAvailable(SubCategorySelection.this)) {
                    subCategoryViewModel.getAddedSubCategories(params);
                } else {
                    NetworkAvailability networkAvailability = new NetworkAvailability(this);
                    networkAvailability.noInternetConnection(SubCategorySelection.this, Constants.NETWORK_ENABLE_SETTINGS);
                }

                subCategoryViewModel.getAddedSubCategoryList().observe(this, new Observer<SubCategory_Model>() {
                    @Override
                    public void onChanged(SubCategory_Model subCategory_model) {
                        if(subCategory_model.isStrStatus() == true) {
                            StorageDatas.getInstance().setSubCateAdded(true);
                            subCategoryList.clear();
                            subCategoryList.addAll(subCategory_model.getSubCategories());

                            if(subCategoryList.size() == 1) {
                                showSubCategoryDatas();
                            } else {
                                subcategoryAdapter.notifyDataSetChanged();
                            }
                            subcategory_name.setText("");
                            bottomSheetDialog.dismiss();
                        }
                    }
                });
                break;

            case R.id.subcate_back:
                finish();
                break;

            /*case R.id.add_subcate_btn:
                bottomSheetDialog.show();
                break;*/
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

    private void observeSubCateProgress() {
        subCategoryViewModel.progressSubCateUpdation().observe(this, new Observer<Boolean>() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NETWORK_ENABLE_SETTINGS) {
            updateView();
        }
    }
}
