package com.mart.mymartbee.view.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.model.ShopStatusModel;
import com.mart.mymartbee.model.SubCategory_Model;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.AddProduct;
import com.mart.mymartbee.view.HomeActivity;
import com.mart.mymartbee.view.ProductDetails;
import com.mart.mymartbee.view.ProductViewAll;
import com.mart.mymartbee.view.adapters.ShopStatusAdapter;
import com.mart.mymartbee.view.adapters.SubCateTitleAdapter;
import com.mart.mymartbee.viewmodel.implementor.ProductsViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.ProductsViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener, ShopStatusAdapter.ShopStatusClickListener, Constants {

    ArrayList<ShopStatusModel> shopStatusModel;
    RecyclerView recyclerView, products_recycler;
    EditText search_product_edit;
    ShopStatusAdapter shopStatusAdapter;
    RelativeLayout product_creation_layout, product_layout;

    ProductsViewModel productsViewModel;
    ProgressDialog progressDialog;

    ArrayList<Products_Model.ProductCategories> productsList;
    ArrayList<Products_Model.ProductCategories> productsListTemp;
    SubCateTitleAdapter subCateTitleAdapter;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    TextView shop_tv, new_product_txt;
    String strCateId, strSellerId;
    LinearLayoutManager lManager = null;
    Products_Model.ProductCategories.ProductsList productsObj;
    ArrayList<Products_Model.ProductCategories.ProductsList> cateProductsLists;
    ImageView whatsapp_share;
    String whatsAppLink = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        productsViewModel = ViewModelProviders.of(getActivity()).get(ProductsViewModelImpl.class);
        initView(view);
        getMyPreferences(view);

        return view;
    }

    private void initView(View view) {
        productsList = new ArrayList<Products_Model.ProductCategories>();
        productsListTemp = new ArrayList<Products_Model.ProductCategories>();
        shopStatusModel = new ArrayList<ShopStatusModel>();
        whatsapp_share = (ImageView) view.findViewById(R.id.whatsapp_share);
        shop_tv = (TextView) view.findViewById(R.id.shop_tv);
        new_product_txt = (TextView) view.findViewById(R.id.new_product_txt);
        product_creation_layout = (RelativeLayout) view.findViewById(R.id.product_creation_layout);
        product_layout = (RelativeLayout) view.findViewById(R.id.product_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        products_recycler = (RecyclerView) view.findViewById(R.id.products_recycler);
        search_product_edit = (EditText) view.findViewById(R.id.search_product_edit);
        setListeners();
    }

    private void getMyPreferences(View view) {
        myKeyValue = getActivity().getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(getActivity());
        shop_tv.setText(TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_SHOP), myKeyValue));
        strCateId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY), myKeyValue);
        strSellerId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ID), myKeyValue);
        productsViewModel.progressProductUpdation().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean progressObserve) {
                if(progressObserve) {
                    showProgress();
                } else {
                    hideProgress();
                }
            }
        });

        productsViewModel.getProducts(strCateId, strSellerId);
        productsViewModel.getProductLV().observe(this, new Observer<Products_Model>() {
            @Override
            public void onChanged(Products_Model products_model) {
                productsList = products_model.getProductCategories();
                productsListTemp.addAll(productsList);
                whatsAppLink = products_model.getStrStoreLink();
                StorageDatas.getInstance().setStoreWhatsappLink(whatsAppLink);
                Log.e("appSample", "StoreLink: " + whatsAppLink);

                if(productsList != null) {
                    if(productsList.size() > 0) {
                        product_creation_layout.setVisibility(View.GONE);
                        product_layout.setVisibility(View.VISIBLE);
                        setProductAdapter();
                    } else {
                    noProductFound();
                    }
                } else {
                    noProductFound();
                }
            }
        });
    }

    private void noProductFound() {
        product_creation_layout.setVisibility(View.VISIBLE);
        product_layout.setVisibility(View.GONE);
        ShopStatusModel item1 = new ShopStatusModel("1", getActivity().getResources().getString(R.string.maintain_store),
                getActivity().getResources().getString(R.string.vist_edit_store), "1");
        ShopStatusModel item2 = new ShopStatusModel("2", getActivity().getResources().getString(R.string.advertise_store),
                getActivity().getResources().getString(R.string.share_store_link),
                "0");
        shopStatusModel.add(item1);
        shopStatusModel.add(item2);
        setAdapter();
    }

    private void setAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        shopStatusAdapter = new ShopStatusAdapter(shopStatusModel, getActivity(), HomeFragment.this);
        recyclerView.setAdapter(shopStatusAdapter);
    }

    private void setProductAdapter() {
        products_recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getActivity());
        products_recycler.setLayoutManager(lManager);
        subCateTitleAdapter = new SubCateTitleAdapter(productsList, getActivity(), HomeFragment.this,
                lManager);
        products_recycler.setAdapter(subCateTitleAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.whatsapp_share:
                shareOnWhatsapp();
                break;

            case R.id.new_product_txt:
                Bundle bundle = new Bundle();
                bundle.putString("fromActivity", "HomeFragment");
                Intent addProdIntent = new Intent(getActivity(), AddProduct.class);
                addProdIntent.putExtras(bundle);
                startActivityForResult(addProdIntent, HOME_FRAG_to_ADD_PRODUCT);
                break;
        }
    }

    private void shareOnWhatsapp() {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, whatsAppLink + " \n\n   Please visit my store to buy electronics products on very low cost.");
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "WhatsApp have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateProduct() {
        if(productsList == null) {
            productsList = new ArrayList<Products_Model.ProductCategories>();
        } else {
            productsList.clear();
        }

        productsList.addAll(StorageDatas.getInstance().getProducts_model().getProductCategories());
        if(productsList.size() == 1) {
            product_creation_layout.setVisibility(View.GONE);
            product_layout.setVisibility(View.VISIBLE);
            setProductAdapter();
        } else {
            subCateTitleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == HOME_FRAG_to_VIEW_ALL) {
            if(resultCode == PRODUCT_DELETION_success) {
                updateProduct();
            } else if (resultCode == PRODUCT_UPDATED_success) {
                updateProduct();

                /*if(productsList == null) {
                    productsList = new ArrayList<Products_Model.ProductCategories>();
                }
                productsList.clear();
                productsList.addAll(StorageDatas.getInstance().getProducts_model().getProductCategories());
                if(productsList.size() == 1) {
                    product_creation_layout.setVisibility(View.GONE);
                    product_layout.setVisibility(View.VISIBLE);
                    setProductAdapter();
                } else {
                    subCateTitleAdapter.notifyDataSetChanged();
                }*/
            }
        }

        if(requestCode == HOME_FRAG_to_PRODUCT_DETAILS) {
            if(resultCode == PRODUCT_UPDATED_success) {
                updateProduct();
                /*if(productsList == null) {
                    productsList = new ArrayList<Products_Model.ProductCategories>();
                }
                productsList.clear();
                productsList.addAll(StorageDatas.getInstance().getProducts_model().getProductCategories());
                if(productsList.size() == 1) {
                    product_creation_layout.setVisibility(View.GONE);
                    product_layout.setVisibility(View.VISIBLE);
                    setProductAdapter();
                } else {
                    subCateTitleAdapter.notifyDataSetChanged();
                }*/
            }
        }

        if(requestCode == HOME_FRAG_to_ADD_PRODUCT) {
            if(resultCode == PRODUCT_ADDED_success) {
                updateProduct();
                /*if(productsList == null) {
                    productsList = new ArrayList<Products_Model.ProductCategories>();
                }
                productsList.clear();
                productsList.addAll(StorageDatas.getInstance().getProducts_model().getProductCategories());
                if(productsList.size() == 1) {
                    product_creation_layout.setVisibility(View.GONE);
                    product_layout.setVisibility(View.VISIBLE);
                    setProductAdapter();
                } else {
                    subCateTitleAdapter.notifyDataSetChanged();
                }*/
            }
        }

        if(requestCode == HOME_FRAG_to_PRODUCT_DETAILS) {
            if(resultCode == PRODUCT_DELETION_success) {
                updateProduct();
                /*if(productsList == null) {
                    productsList = new ArrayList<Products_Model.ProductCategories>();
                } else {
                    productsList.clear();
                }

                productsList.addAll(StorageDatas.getInstance().getProducts_model().getProductCategories());
                if(productsList.size() == 1) {
                    product_creation_layout.setVisibility(View.GONE);
                    product_layout.setVisibility(View.VISIBLE);
                    setProductAdapter();
                } else {
                    subCateTitleAdapter.notifyDataSetChanged();
                }*/
            }
        }
    }

    private void setListeners() {
        new_product_txt.setOnClickListener(this);
        whatsapp_share.setOnClickListener(this);
        search_product_edit.addTextChangedListener(new TextWatcher() {
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

    private void shortList(Editable s) {
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
                            if (productsListTemp.get(i).getStrCateName().toLowerCase().
                                    contains(value.toLowerCase())) {
                                Products_Model.ProductCategories item = new Products_Model.ProductCategories();
                                item.setStrCateId(productsListTemp.get(i).getStrCateId());
                                item.setStrCateName(productsListTemp.get(i).getStrCateName());
                                item.setProductsLists(productsListTemp.get(i).getProductsLists());
                                productsList.add(item);
                            }
                        }
                    }
                }
                subCateTitleAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        if(position == 0) {
            Bundle bundle = new Bundle();
            bundle.putString("fromActivity", "HomeFragment");
            Intent intent = new Intent(getActivity(), AddProduct.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, HOME_FRAG_to_ADD_PRODUCT);
        } else {

        }
    }

    public void movetoProductEdit(Products_Model.ProductCategories.ProductsList productsObj,
                                  String subcateName, String strsubCateId) {
        this.productsObj = productsObj;
        StorageDatas.getInstance().setProductsObj(productsObj);

        Bundle bundle = new Bundle();
        bundle.putString("subcateName", subcateName);
        bundle.putString("subcateId", strsubCateId);
        Intent intent = new Intent(getActivity(), ProductDetails.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, HOME_FRAG_to_PRODUCT_DETAILS);
    }

    public void movetoProductViewAll(ArrayList<Products_Model.ProductCategories.ProductsList> cateProductsLists,
                                  String subcateName, String strsubCateId) {
        this.cateProductsLists = cateProductsLists;
        StorageDatas.getInstance().setCateProductsList(cateProductsLists);

        Bundle pvbundle = new Bundle();
        pvbundle.putString("subcateName", subcateName);
        pvbundle.putString("subcateId", strsubCateId);
        Intent pvintent = new Intent(getActivity(), ProductViewAll.class);
        pvintent.putExtras(pvbundle);
        startActivityForResult(pvintent, HOME_FRAG_to_VIEW_ALL);
    }

    private void showProgress() {
        progressDialog = new ProgressDialog(getActivity());
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
