package com.mart.mymartbee.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.mymartbee.R;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.model.SubCategory_Model;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.adapters.ShopStatusAdapter;
import com.mart.mymartbee.view.adapters.ViewAllProductsAdapter;
import com.mart.mymartbee.view.fragments.HomeFragment;

import java.util.ArrayList;

public class ProductViewAll extends AppCompatActivity implements View.OnClickListener, Constants {

    ImageView viewall_back;
    TextView subcate_title_tv;
    EditText all_product_search;
    RecyclerView allproducts_recycler;
    ArrayList<Products_Model.ProductCategories.ProductsList> productsList;
    ArrayList<Products_Model.ProductCategories.ProductsList> productsListTemp;
    String strSubCateId, strSubCateName;
    ViewAllProductsAdapter viewAllProductsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_viewall);


        initView();
        getBundles();
    }

    private void getBundles() {
        Bundle bundle = getIntent().getExtras();
        strSubCateId = bundle.getString("subcateId");
        strSubCateName = bundle.getString("subcateName");
        subcate_title_tv.setText(strSubCateName);
    }

    private void initView() {
        productsList = new ArrayList<Products_Model.ProductCategories.ProductsList>();
        productsListTemp = new ArrayList<Products_Model.ProductCategories.ProductsList>();
        productsList = StorageDatas.getInstance().getCateProductsList();
        productsListTemp.addAll(productsList);

        viewall_back = findViewById(R.id.viewall_back);
        subcate_title_tv = findViewById(R.id.subcate_title_tv);
        all_product_search = findViewById(R.id.all_product_search);
        allproducts_recycler = findViewById(R.id.allproducts_recycler);
        setListeners();
        setAdapter();
    }

    private void setListeners() {
        viewall_back.setOnClickListener(this);
        all_product_search.addTextChangedListener(new TextWatcher() {
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

        allproducts_recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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
                    moveProductDetails(position);
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
    }

    private void moveProductDetails(int position) {
        StorageDatas.getInstance().setProductsObj(productsList.get(position));

        Bundle bundle = new Bundle();
        bundle.putString("subcateName", strSubCateName);
        bundle.putString("subcateId", strSubCateId);
        Intent intent = new Intent(ProductViewAll.this, ProductDetails.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, VIEW_ALL_to_PRODUCT_DETAILS);
    }

    public void shortList(Editable s) {
        if (productsListTemp != null) {
            if (productsListTemp.size() > 0) {
                String value = "" + s;
                value = value.trim();
                productsList.clear();

                if (value != null) {
                    if (value.equals("")) {
                        productsList.addAll(productsListTemp);
                    } else {
                        for (int i = 0; i < productsListTemp.size(); i++) {
                            if (productsListTemp.get(i).getStrProduct_title().toLowerCase().
                                    contains(value.toLowerCase())) {
                                Products_Model.ProductCategories.ProductsList item = new Products_Model.ProductCategories.ProductsList();
                                item.setStrProdut_id(productsListTemp.get(i).getStrProdut_id());
                                item.setStrProduct_image(productsListTemp.get(i).getStrProduct_image());
                                item.setStrProduct_quantity(productsListTemp.get(i).getStrProduct_quantity());
                                item.setStrProduct_title(productsListTemp.get(i).getStrProduct_title());
                                item.setStrProduct_description(productsListTemp.get(i).getStrProduct_description());
                                item.setStrProduct_price(productsListTemp.get(i).getStrProduct_price());
                                item.setStrProduct_oldprice(productsListTemp.get(i).getStrProduct_oldprice());
                                item.setStrProduct_uom(productsListTemp.get(i).getStrProduct_uom());
                                productsList.add(item);
                            }
                        }
                    }
                }
                viewAllProductsAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setAdapter() {
        if(productsList != null) {
            if(productsList.size() > 0) {
                allproducts_recycler.setHasFixedSize(true);
                allproducts_recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                viewAllProductsAdapter = new ViewAllProductsAdapter(productsList, getApplicationContext(),
                        strSubCateName, strSubCateId);
                allproducts_recycler.setAdapter(viewAllProductsAdapter);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.viewall_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == VIEW_ALL_to_PRODUCT_DETAILS) {
            if(resultCode == PRODUCT_UPDATED_success) {
                setResult(PRODUCT_UPDATED_success);
                finish();
            }
            if(resultCode == PRODUCT_DELETION_success) {
                setResult(PRODUCT_DELETION_success);
                finish();
            }
        }
    }
}
