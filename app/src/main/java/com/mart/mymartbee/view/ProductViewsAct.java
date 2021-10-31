package com.mart.mymartbee.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.model.Dashboard_Model;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.adapters.ViewAllProductsAdapter;
import com.mart.mymartbee.view.adapters.ViewedProductsAdapter;

import java.util.ArrayList;

public class ProductViewsAct extends AppCompatActivity implements View.OnClickListener, Constants {

    ImageView productview_back;
    EditText product_views_search;
    RecyclerView products_recycler;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    ArrayList<Dashboard_Model.ViewedProductList> viewedProductList;
    ArrayList<Dashboard_Model.ViewedProductList> viewedProductListTemp;
    ViewedProductsAdapter viewedProductsAdapter;
    RelativeLayout productviews_title_layout;
    ImageView icon_search, search_back;
    LinearLayout search_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productviewsact);

        initView();
        getMyPreferences();
    }

    public void initView() {
        productviews_title_layout = findViewById(R.id.productviews_title_layout);
        icon_search = findViewById(R.id.icon_search);
        search_back = findViewById(R.id.search_back);
        search_layout = findViewById(R.id.search_layout);

        viewedProductList = new ArrayList<>();
        viewedProductListTemp = new ArrayList<>();
        viewedProductList = StorageDatas.getInstance().getViewedProductLists();
        viewedProductListTemp.addAll(viewedProductList);

        productview_back = findViewById(R.id.productview_back);
        product_views_search = findViewById(R.id.product_views_search);
        products_recycler = findViewById(R.id.products_recycler);
        productview_back.setOnClickListener(this);
        setListeners();
        setAdapter();
    }

    private void getMyPreferences() {
        myKeyValue = getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(ProductViewsAct.this);
    }

    public void setListeners() {
        icon_search.setOnClickListener(this);
        search_back.setOnClickListener(this);
        product_views_search.addTextChangedListener(new TextWatcher() {
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
        if (viewedProductListTemp != null) {
            if (viewedProductListTemp.size() > 0) {
                String value = "" + s;
                value = value.trim();
                viewedProductList.clear();

                if (value != null) {
                    if (value.equals("")) {
                        viewedProductList.addAll(viewedProductListTemp);
                    } else {
                        for (int i = 0; i < viewedProductListTemp.size(); i++) {
                            if (viewedProductListTemp.get(i).getStrProductName().toLowerCase().
                                    contains(value.toLowerCase())) {
                                Dashboard_Model.ViewedProductList item = new Dashboard_Model.ViewedProductList();
                                item.setStrViewed(viewedProductListTemp.get(i).getStrViewed());
                                item.setStrProductName(viewedProductListTemp.get(i).getStrProductName());
                                item.setStrProductId(viewedProductListTemp.get(i).getStrProductId());
                                item.setStrProductImage(viewedProductListTemp.get(i).getStrProductImage());
                                item.setStrProductQuantity(viewedProductListTemp.get(i).getStrProductQuantity());
                                item.setStrProductDescription(viewedProductListTemp.get(i).getStrProductDescription());
                                item.setStrProductPrice(viewedProductListTemp.get(i).getStrProductPrice());
                                item.setStrProductOldPrice(viewedProductListTemp.get(i).getStrProductOldPrice());
                                item.setStrProductUOM(viewedProductListTemp.get(i).getStrProductUOM());
                                viewedProductList.add(item);
                            }
                        }
                    }
                }
                viewedProductsAdapter.notifyDataSetChanged();
            }
        }
    }

    private void moveProductDetails(int position) {
//        StorageDatas.getInstance().setProductsObj(viewedProductList.get(position));
//
//        Bundle bundle = new Bundle();
////        bundle.putString("subcateName", strSubCateName);
////        bundle.putString("subcateId", strSubCateId);
//        Intent intent = new Intent(ProductViewsAct.this, ProductDetails.class);
//        intent.putExtras(bundle);
//        startActivityForResult(intent, VIEW_ALL_to_PRODUCT_DETAILS);
    }

    private void setAdapter() {
        if(viewedProductList != null) {
            if(viewedProductList.size() > 0) {
                products_recycler.setHasFixedSize(true);
                products_recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                viewedProductsAdapter = new ViewedProductsAdapter(viewedProductList, getApplicationContext());
                products_recycler.setAdapter(viewedProductsAdapter);
            }
        }
    }

    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_back.getWindowToken(), 0);
    }

    public void openKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow( product_views_search.getApplicationWindowToken(),  InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_search:
                openKeyboard();
                product_views_search.requestFocus();
                productviews_title_layout.setVisibility(View.GONE);
                search_layout.setVisibility(View.VISIBLE);
                break;

            case R.id.search_back:
                closeKeyboard();
                product_views_search.setText("");
                productviews_title_layout.setVisibility(View.VISIBLE);
                search_layout.setVisibility(View.GONE);
                break;
            case R.id.productview_back:
                finish();
                break;
        }
    }
}
