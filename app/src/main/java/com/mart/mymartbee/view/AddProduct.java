package com.mart.mymartbee.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textview.MaterialTextView;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.CameraUtils;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.HintAdapter;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.model.UOMModel;
import com.mart.mymartbee.repository.implementor.ProductListRepoImpl;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.viewmodel.implementor.ProductsViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.ProductsViewModel;
import com.zjun.widget.tagflowlayout.TagFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AddProduct extends AppCompatActivity implements View.OnClickListener, Constants {

    ProgressDialog progressDialog;
    File finalPath;
    String strPImage = "", strPName, strPPrice, strPQty, strPDiscount, strPPayment, strPDelivery,
                    strPSubProduct, strPType = "", strPDesc = "" ;

    EditText product_payment, product_delivery, product_name, product_qty, product_sellprice,
            product_discount_price, product_description, product_uom, product_subcate;
    MaterialTextView profile_change_text;
    TextView addpage_title;
    LinearLayout upload_view, upload_layout;
    ImageView product_image, product_back;
    CardView cardImage;
    Button add_product_btn;
    int selSubCateId = -1;
    String cate_id = "";
    String sellerId = "";
    String strProductId = "";
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    String sel_subcategory = "";
    ProductsViewModel productsViewModel;
    BottomSheetDialog bottomSheetDialog;
    TagFlowLayout uom_tags;
    ArrayList<UOMModel.UOMList> uomDatasList;
    Products_Model.ProductCategories.ProductsList productsObj;
    String fromActivity = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModelImpl.class);

        init();
        bottomSheetInitView();
        getMyPreferences();
        observeProgress();
        showUOMSheet();

    }

    public void observeProgress() {
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
    }

    private void bottomSheetInitView() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.uom_bottomsheet);
        bottomSheetDialog.setCancelable(true);

        uom_tags = (TagFlowLayout) bottomSheetDialog.findViewById(R.id.uom_tags);
    }

    private void getMyPreferences() {
        myKeyValue = getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(AddProduct.this);
        cate_id = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY), myKeyValue);
        sellerId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ID), myKeyValue);
    }

    private void init() {
        uomDatasList = new ArrayList<UOMModel.UOMList>();
        product_payment = findViewById(R.id.product_payment);
        addpage_title = findViewById(R.id.addpage_title);
        product_uom = findViewById(R.id.product_uom);
        product_subcate = findViewById(R.id.product_subcate);
        product_delivery = findViewById(R.id.product_delivery);
        product_name = findViewById(R.id.product_name);
        product_qty = findViewById(R.id.product_qty);
        product_sellprice = findViewById(R.id.product_sellprice);
        product_discount_price = findViewById(R.id.product_discount_price);
        product_description = findViewById(R.id.product_description);
        upload_view = findViewById(R.id.upload_view);
        profile_change_text = findViewById(R.id.profile_change_text);
        upload_layout = findViewById(R.id.upload_layout);
        product_image = findViewById(R.id.product_image);
        cardImage = findViewById(R.id.cardImage);
        product_back = findViewById(R.id.product_back);
        add_product_btn = findViewById(R.id.add_product_btn);

        product_payment.setEnabled(false);
        product_delivery.setEnabled(false);

        product_uom.setFocusable(false);
        product_uom.setClickable(true);
        product_subcate.setFocusable(false);
        product_subcate.setClickable(true);
        upload_view.setOnClickListener(this);
        profile_change_text.setOnClickListener(this);
        product_back.setOnClickListener(this);
        add_product_btn.setOnClickListener(this);
        product_uom.setOnClickListener(this);
        product_subcate.setOnClickListener(this);

        getBundles();

    }

    private void getBundles() {
        Bundle bundle = getIntent().getExtras();
        fromActivity = bundle.getString("fromActivity");

        if(fromActivity.equalsIgnoreCase("HomeFragment")) {
            addpage_title.setText("ADD PRODUCT");
            add_product_btn.setText("ADD PRODUCT");
            showUploadView();
        } else {
            upload_layout.setVisibility(View.GONE);
            cardImage.setVisibility(View.VISIBLE);
            addpage_title.setText("EDIT PRODUCT");
            add_product_btn.setText("UPDATE PRODUCT");

            productsObj = StorageDatas.getsObj().getProductsObj();
            strProductId = productsObj.getStrProdut_id();

            selSubCateId = bundle.getInt("fromActivitySubcategoryID");
            product_name.setText(productsObj.getStrProduct_title());
            product_description.setText(productsObj.getStrProduct_description());
            product_qty.setText(productsObj.getStrProduct_quantity());
            product_uom.setText(productsObj.getStrProduct_uom());
            product_subcate.setText(bundle.getString("fromActivitySubcategory"));

            Glide.with(getApplicationContext()).load(productsObj.getStrProduct_image()).into(product_image);

            String oldPrice = productsObj.getStrProduct_oldprice().replace(".00", "");
            product_sellprice.setText(oldPrice);

            String newPrice = productsObj.getStrProduct_price().replace(".00", "");
            product_discount_price.setText(newPrice);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_view:
                if (!CameraUtils.isDeviceSupportCamera(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.device_dont_have_camera),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Intent cropintent = new Intent(AddProduct.this, CrapImageSample.class);
                startActivityForResult(cropintent, ADD_PRODUCT_to_CROP_SAMPLE_ACTIVITY);
                break;

            case R.id.profile_change_text:
                if (!CameraUtils.isDeviceSupportCamera(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.device_dont_have_camera),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Intent profintent = new Intent(AddProduct.this, CrapImageSample.class);
                startActivityForResult(profintent, ADD_PRODUCT_to_CROP_SAMPLE_ACTIVITY);
                break;

            case R.id.product_back:
                finish();
                break;

            case R.id.product_subcate:
                hideKeyboard(product_subcate);
                Bundle bundle = new Bundle();
                bundle.putInt("SelectedSubId", selSubCateId);
                bundle.putString("SellerId", sellerId);
                bundle.putString("CategoryId", cate_id);
                Intent intent = new Intent(AddProduct.this, SubCategorySelection.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, ADD_PRODUCT_to_SUBCATE_SELECTION);
                break;

            case R.id.product_uom:
                bottomSheetDialog.show();
                break;

            case R.id.add_product_btn:
                strPName = product_name.getText().toString().trim();
                strPPrice = product_sellprice.getText().toString().trim();
                strPQty = product_qty.getText().toString().trim();
                strPDiscount = product_discount_price.getText().toString().trim();
                strPDesc = product_description.getText().toString().trim();
                strPPayment = product_payment.getText().toString().trim();
                strPDelivery = product_delivery.getText().toString().trim();
                strPSubProduct = product_subcate.getText().toString().trim();
                strPType = product_uom.getText().toString().trim();

                if(strPName.equalsIgnoreCase("")) {
                    showErrorMsg("Please enter product name.");
                    return;
                }

                if(strPDesc.equalsIgnoreCase("")) {
                    showErrorMsg("Please enter product description.");
                    return;
                }

                if(strPPrice.equalsIgnoreCase("")) {
                    showErrorMsg("Please enter product actual price.");
                    return;
                }

                if(strPQty.equalsIgnoreCase("")) {
                    showErrorMsg("Please enter product quantity.");
                    return;
                }

                if(strPType.equalsIgnoreCase("")) {
                    showErrorMsg("Please select product UOM.");
                    return;
                }

                if(strPDiscount.equalsIgnoreCase("")) {
                    showErrorMsg("Please enter product discount price.");
                    return;
                }

                if(strPPayment.equalsIgnoreCase("")) {
                    showErrorMsg("Please select product payment type.");
                    return;
                }

                if(strPDelivery.equalsIgnoreCase("")) {
                    showErrorMsg("Please select product delivery type.");
                    return;
                }

                if(strPSubProduct.equalsIgnoreCase("")) {
                    showErrorMsg("Please select sub product.");
                    return;
                }

                if(Integer.parseInt(strPDiscount) > Integer.parseInt(strPPrice)) {
                    showErrorMsg("Selling price was low comparing to Discount price.");
                    return;
                }

                Map<String, String> params = new HashMap<>();
                params.put("title", strPName);
                params.put("description", strPDesc);
                params.put("meta_title", strPName);
                params.put("meta_description", strPDesc);
                params.put("meta_keyword", "MartBee");
                params.put("price", strPDiscount);
                params.put("old_price", strPPrice);
                params.put("cat_id", cate_id);
                params.put("sub_cat_id", "" + selSubCateId);
                params.put("quantity", strPQty);
                params.put("seller_id", sellerId);
                params.put("uom", strPType);

                if(fromActivity.equalsIgnoreCase("HomeFragment")) {

                    if(finalPath == null) {
                        showErrorMsg("Please select product image.");
                        return;
                    }

                    productsViewModel.addProducts(finalPath, params);
                    productsViewModel.addProductLV().observe(this, new Observer<Products_Model>() {
                        @Override
                        public void onChanged(Products_Model products_model) {
                            if(products_model.isStrStatus() == true) {
                                showSuccessDialog(products_model, "Product successfully added.", PRODUCT_ADDED_success);
                            }
                        }
                    });
                } else {
                    params.put("product_id", strProductId);
                    Log.e("appSample", "Params: " + params.toString());

                    if(finalPath == null) {
                        productsViewModel.editProducts(params);
                        productsViewModel.editProductLV().observe(this, new Observer<Products_Model>() {
                            @Override
                            public void onChanged(Products_Model products_model) {
                                if(products_model.isStrStatus() == true) {
                                    showSuccessDialog(products_model, "Product updated successfully.", PRODUCT_UPDATED_success);
                                }
                            }
                        });
                    } else {
                        productsViewModel.editProductwithImage(finalPath, params);
                        productsViewModel.editProductwithImageLV().observe(this, new Observer<Products_Model>() {
                            @Override
                            public void onChanged(Products_Model products_model) {
                                if(products_model.isStrStatus() == true) {
                                    showSuccessDialog(products_model, "Product updated successfully.", PRODUCT_UPDATED_success);
                                }
                            }
                        });
                    }


                }


                break;
        }
    }

    private void showUOMSheet() {

        productsViewModel.getUOM();
        productsViewModel.getUOMLV().observe(this, new Observer<UOMModel>() {
            @Override
            public void onChanged(UOMModel uomModel) {
                uomDatasList = uomModel.getUomLists();
                showUOMAdapter();
            }
        });


    }

    private void showSuccessDialog(Products_Model products_model, String strMessage, int resultCode) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AddProduct.this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Success");
        sweetAlertDialog.setContentText(strMessage);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sweetAlertDialog.dismiss();
                StorageDatas.getInstance().setProducts_model(products_model);
                setResult(resultCode);
                finish();
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showErrorMsg(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_PRODUCT_to_SUBCATE_SELECTION) {
            if(resultCode == SUB_CATEGORY_SELECTED) {
                hideKeyboard(product_subcate);
                sel_subcategory = data.getStringExtra("SelectedSubCategory");
                selSubCateId = data.getIntExtra("SelectedSubId", 0);
                product_subcate.setText(sel_subcategory);
            }
        }

        if (requestCode == ADD_PRODUCT_to_CROP_SAMPLE_ACTIVITY) {
            if (resultCode == CROP_success) {
                strPImage = "" + data.getStringExtra("FilePath");
                finalPath = new File(strPImage);
                Uri compressUri = Uri.fromFile(finalPath);
                showUploadedImage();

                Glide.with(getApplicationContext())
                        .load(compressUri)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(product_image);
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

    private void showUploadView() {
        upload_layout.setVisibility(View.VISIBLE);
        cardImage.setVisibility(View.GONE);
    }

    private void showUploadedImage() {
        upload_layout.setVisibility(View.GONE);
        cardImage.setVisibility(View.VISIBLE);
    }

    private void showUOMAdapter() {
        com.zjun.widget.tagflowlayout.TagFlowLayout.Adapter adapter =
                new com.zjun.widget.tagflowlayout.TagFlowLayout.Adapter(getApplicationContext()) {
                    private View.OnClickListener onClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int position = (int) v.getTag();
                            final UOMModel.UOMList sg_group = uomDatasList.get(position);
                            if (sg_group.isChosen()) {
                                return;
                            }
                            for (UOMModel.UOMList c : uomDatasList) {
                                c.setChosen(false);
                            }
                            sg_group.setChosen(true);
                            notifyDataSetChanged();
                            product_uom.setText(uomDatasList.get(position).getStrUom());
                            bottomSheetDialog.dismiss();

                        }
                    };

                    @Override
                    public int getViewCount() {
                        return uomDatasList.size();
                    }

                    @Override
                    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
                        return inflater.inflate(R.layout.uom_single_choose, parent, false);
                    }

                    @Override
                    public void onBindView(View view, int position) {
                        TextView textView = (TextView) view;
                        UOMModel.UOMList sg_group = uomDatasList.get(position);
                        textView.setText(sg_group.getStrUom());
                        textView.setTag(position);
                        textView.setSelected(sg_group.isChosen());
                        textView.setOnClickListener(onClickListener);
                    }
                };

        uom_tags.setAdapter(adapter);
    }

}
