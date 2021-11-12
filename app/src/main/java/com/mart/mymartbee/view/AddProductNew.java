package com.mart.mymartbee.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mart.mymartbee.BuildConfig;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.CameraUtils;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.commons.PermissionManager;
import com.mart.mymartbee.commons.Util;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.HintAdapter;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.model.UOMModel;
import com.mart.mymartbee.model.UploadingImageList;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.adapters.ImageUploadingAdapter;
import com.mart.mymartbee.viewmodel.implementor.ProductsViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.ProductsViewModel;
import com.zjun.widget.tagflowlayout.TagFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AddProductNew extends AppCompatActivity implements View.OnClickListener, Constants,
        ImageUploadingAdapter.ImageRemoveClickListener {

    ProgressDialog progressDialog;
    File finalPath, tempFile;
    String strPImage = "", strPName, strPPrice, strPQty, strPDiscount, strPPayment, strPDelivery,
            strPSubProduct, strPType = "", strPDesc = "";

    EditText product_payment, product_delivery, product_name, product_qty, product_sellprice,
            product_discount_price, product_description, product_uom, product_subcate;
//    MaterialTextView profile_change_text;
    LinearLayout profile_change;
    TextView addpage_title;
    LinearLayout upload_view, upload_layout, upload_view_img;
    ImageView product_image, product_back;
//    CardView cardImage;
    Button add_product_btn;
    int selSubCateId = -1;
    String cate_id = "";
    String sellerId = "";
    String strProductId = "";
    Boolean isSubCateAdded;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    String sel_subcategory = "";
    ProductsViewModel productsViewModel;
    BottomSheetDialog bottomSheetDialog;
    TagFlowLayout uom_tags;
    ArrayList<UOMModel.UOMList> uomDatasList;
    Products_Model.ProductCategories.ProductsList productsObj;
    String fromActivity = "";
    BottomSheetDialog bottomSheetUpload;
    LinearLayout gallery_layout, camera_layout, imagelist_layout;
    ImageView close_img;
    Uri fileUri;
    int showUOMStatus = 0; // 0 for Initial, 1 for After No Internet Call
    Spinner uom_spinner;
    ArrayList<String> uomSpinnerList;
    RecyclerView photos_recycle;
    ArrayList<UploadingImageList> uploadingImageLists;
    ImageUploadingAdapter imageUploadingAdapter;
    int imageCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_new);

        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModelImpl.class);

        init();
        bottomSheetInitView();
        bottomSheetImageUpload();
        getMyPreferences();
        observeProgress();
        showUOMSheet(0);
        addListeners();
    }

    public void addListeners() {
        uom_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strPType = uom_spinner.getSelectedItem().toString();
                product_uom.setText(strPType);
                Log.e("appSample", "Selected: " + strPType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void bottomSheetImageUpload() {
        bottomSheetUpload = new BottomSheetDialog(this);
        bottomSheetUpload.setContentView(R.layout.bottomsheet_imageupload);
        bottomSheetUpload.setCancelable(false);
        close_img = (ImageView) bottomSheetUpload.findViewById(R.id.close_img);
        gallery_layout = (LinearLayout) bottomSheetUpload.findViewById(R.id.gallery_layout);
        camera_layout = (LinearLayout) bottomSheetUpload.findViewById(R.id.camera_layout);
        close_img.setOnClickListener(this);
        gallery_layout.setOnClickListener(this);
        camera_layout.setOnClickListener(this);
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

    private void bottomSheetInitView() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_uom);
        bottomSheetDialog.setCancelable(true);

        uom_tags = (TagFlowLayout) bottomSheetDialog.findViewById(R.id.uom_tags);
    }

    private void getMyPreferences() {
        myKeyValue = getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(AddProductNew.this);
        cate_id = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY), myKeyValue);
        sellerId = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ID), myKeyValue);
    }

    private void init() {
        uploadingImageLists = new ArrayList<UploadingImageList>();
        uomSpinnerList = new ArrayList<String>();
        uom_spinner = findViewById(R.id.uom_spinner);
        uomDatasList = new ArrayList<UOMModel.UOMList>();
        photos_recycle = findViewById(R.id.photos_recycle);
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
        upload_view_img = findViewById(R.id.upload_view_img);
        profile_change = findViewById(R.id.profile_change);
        upload_layout = findViewById(R.id.upload_layout);
        product_image = findViewById(R.id.product_image);
//        cardImage = findViewById(R.id.cardImage);
        imagelist_layout = findViewById(R.id.imagelist_layout);
        product_back = findViewById(R.id.product_back);
        add_product_btn = findViewById(R.id.add_product_btn);

        product_payment.setEnabled(false);
        product_delivery.setEnabled(false);

        product_uom.setFocusable(false);
        product_uom.setClickable(true);
        product_subcate.setFocusable(false);
        product_subcate.setClickable(true);
        upload_view.setOnClickListener(this);
        upload_view_img.setOnClickListener(this);
        profile_change.setOnClickListener(this);
        product_back.setOnClickListener(this);
        add_product_btn.setOnClickListener(this);
        product_uom.setOnClickListener(this);
        product_subcate.setOnClickListener(this);
        getBundles();
    }

    private void getBundles() {
        Bundle bundle = getIntent().getExtras();
        fromActivity = bundle.getString("fromActivity");

        if (fromActivity.equalsIgnoreCase("HomeFragment")) {
            addpage_title.setText("ADD PRODUCT");
            add_product_btn.setText("ADD PRODUCT");
            showUploadView();
        } else {
            upload_layout.setVisibility(View.GONE);
//            cardImage.setVisibility(View.VISIBLE);
            imagelist_layout.setVisibility(View.VISIBLE);
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
            case R.id.close_img:
                bottomSheetUpload.dismiss();
                break;

            case R.id.camera_layout:
                bottomSheetUpload.dismiss();
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (android.os.Build.VERSION.SDK_INT >= 24) {
                    tempFile = Util.getOutputMediaFile();
                    fileUri = FileProvider.getUriForFile(AddProductNew.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            tempFile);

                    intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                } else {
                    fileUri = Uri.fromFile(Util.getOutputMediaFile());
                }

                intent1.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent1, CAMERA_IMAGE);
                break;

            case R.id.gallery_layout:
                bottomSheetUpload.dismiss();

                if (ContextCompat.checkSelfPermission(AddProductNew.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddProductNew.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    i.setType("image/*");
                    startActivityForResult(i, PICK_IMAGE);
                }
                break;

            case R.id.upload_view_img:
                if(uploadingImageLists.size() < 5) {
                    checkCameraPermission();
                } else {
                    CommonMethods.Toast(AddProductNew.this,  "Sorry! You have reached maximum photos to upload.");
                }
                break;

            case R.id.upload_view:
                if(uploadingImageLists.size() < 5) {
                    checkCameraPermission();
                } else {
                    CommonMethods.Toast(AddProductNew.this,  "Sorry! You have reached maximum photos to upload.");
                }
                /*Intent cropintent = new Intent(AddProduct.this, CrapImageSample.class);
                startActivityForResult(cropintent, ADD_PRODUCT_to_CROP_SAMPLE_ACTIVITY);*/
                break;

            case R.id.profile_change:
                checkCameraPermission();
                /*if (!CameraUtils.isDeviceSupportCamera(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.device_dont_have_camera),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                bottomSheetUpload.show();*/
                /*Intent profintent = new Intent(AddProduct.this, CrapImageSample.class);
                startActivityForResult(profintent, ADD_PRODUCT_to_CROP_SAMPLE_ACTIVITY);*/
                break;

            case R.id.product_back:
                finish();
                break;

            case R.id.product_subcate:
                if (NetworkAvailability.isNetworkAvailable(AddProductNew.this)) {
                    hideKeyboard(product_subcate);
                    Bundle bundle = new Bundle();
                    bundle.putInt("SelectedSubId", selSubCateId);
                    bundle.putString("SellerId", sellerId);
                    bundle.putString("CategoryId", cate_id);
                    Intent intent = new Intent(AddProductNew.this, SubCategorySelection.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, ADD_PRODUCT_to_SUBCATE_SELECTION);
                } else {
                    NetworkAvailability networkAvailability = new NetworkAvailability(this);
                    networkAvailability.noInternetConnection(AddProductNew.this, MOVE_SUB_CATEGORY);
                }
                break;

            case R.id.product_uom:
                if (NetworkAvailability.isNetworkAvailable(AddProductNew.this)) {
                    if(uomDatasList.size() > 0) {
                        bottomSheetDialog.show();
                    } else {
                        showUOMSheet(1);
                    }
                } else {
                    NetworkAvailability networkAvailability = new NetworkAvailability(this);
                    networkAvailability.noInternetConnection(AddProductNew.this, Constants.NETWORK_ENABLE_SETTINGS);
                }
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

                if (strPName.equalsIgnoreCase("")) {
                    showErrorMsg("Please enter product name.");
                    return;
                }

                if (strPDesc.equalsIgnoreCase("")) {
                    showErrorMsg("Please enter product description.");
                    return;
                }

                if (strPPrice.equalsIgnoreCase("")) {
                    showErrorMsg("Please enter product actual price.");
                    return;
                }

                if (strPQty.equalsIgnoreCase("")) {
                    showErrorMsg("Please enter product quantity.");
                    return;
                }

                if (strPType.equalsIgnoreCase("") || strPType.equalsIgnoreCase("Select Status")) {
                    showErrorMsg("Please select product UOM.");
                    return;
                }

                if (strPDiscount.equalsIgnoreCase("")) {
                    showErrorMsg("Please enter product discount price.");
                    return;
                }

                if (strPPayment.equalsIgnoreCase("")) {
                    showErrorMsg("Please select product payment type.");
                    return;
                }

                if (strPDelivery.equalsIgnoreCase("")) {
                    showErrorMsg("Please select product delivery type.");
                    return;
                }

                if (strPSubProduct.equalsIgnoreCase("")) {
                    showErrorMsg("Please select sub product.");
                    return;
                }

                if (Integer.parseInt(strPDiscount) > Integer.parseInt(strPPrice)) {
                    showErrorMsg("Selling price was low comparing to Discount price.");
                    return;
                }

                if (uploadingImageLists.size() == 0) {
                    showErrorMsg("Please select product image.");
                    return;
                }

                /*Map<String, String> params = new HashMap<>();
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
                params.put("uom", strPType);*/

                if(uploadingImageLists.size() > 0) {
                    uploadingImage(uploadingImageLists.get(imageCount).getImage());
                }



                /*if (fromActivity.equalsIgnoreCase("HomeFragment")) {

                    *//*if (finalPath == null) {
                        showErrorMsg("Please select product image.");
                        return;
                    }*//*

                    if (NetworkAvailability.isNetworkAvailable(AddProductNew.this)) {
                        productsViewModel.addProducts(finalPath, params);
                    } else {
                        NetworkAvailability networkAvailability = new NetworkAvailability(this);
                        networkAvailability.noInternetConnection(AddProductNew.this, Constants.ADD_PRODUCT_INFO);
                    }

                    productsViewModel.addProductLV().observe(this, new Observer<Products_Model>() {
                        @Override
                        public void onChanged(Products_Model products_model) {
                            if (products_model.isStrStatus() == true) {
                                showSuccessDialog(products_model, "Product successfully added.", PRODUCT_ADDED_success);
                            }
                        }
                    });
                } else {
                    params.put("product_id", strProductId);
                    Log.e("appSample", "Params: " + params.toString());

                    if (finalPath == null) {
                        if (NetworkAvailability.isNetworkAvailable(AddProductNew.this)) {
                            productsViewModel.editProducts(params);
                        } else {
                            NetworkAvailability networkAvailability = new NetworkAvailability(this);
                            networkAvailability.noInternetConnection(AddProductNew.this, Constants.ADD_PRODUCT_INFO);
                        }
                        productsViewModel.editProductLV().observe(this, new Observer<Products_Model>() {
                            @Override
                            public void onChanged(Products_Model products_model) {
                                if (products_model.isStrStatus() == true) {
                                    showSuccessDialog(products_model, "Product updated successfully.", PRODUCT_UPDATED_success);
                                }
                            }
                        });
                    } else {
                        if (NetworkAvailability.isNetworkAvailable(AddProductNew.this)) {
                            productsViewModel.editProductwithImage(finalPath, params);
                        } else {
                            NetworkAvailability networkAvailability = new NetworkAvailability(this);
                            networkAvailability.noInternetConnection(AddProductNew.this, Constants.ADD_PRODUCT_INFO);
                        }
                        productsViewModel.editProductwithImageLV().observe(this, new Observer<Products_Model>() {
                            @Override
                            public void onChanged(Products_Model products_model) {
                                if (products_model.isStrStatus() == true) {
                                    showSuccessDialog(products_model, "Product updated successfully.", PRODUCT_UPDATED_success);
                                }
                            }
                        });
                    }

                }*/

                break;
        }
    }

    public void uploadingImage(String upd_image) {
        Log.e("appSample", "uploadingImage");
        finalPath = new File(upd_image);

        Map<String, String> params = new HashMap<>();
//        params.put("product_id", "product_id);
        params.put("cat_id", cate_id);
        params.put("seller_id", sellerId);

        if (NetworkAvailability.isNetworkAvailable(AddProductNew.this)) {
            productsViewModel.uploadingProductImage(finalPath, params);
        } else {
            NetworkAvailability networkAvailability = new NetworkAvailability(this);
            networkAvailability.noInternetConnection(AddProductNew.this, Constants.ADD_PRODUCT_INFO);
        }

        productsViewModel.uploadingProductImagesLV().observe(this, new Observer<Products_Model>() {
            @Override
            public void onChanged(Products_Model products_model) {
                Log.e("appSample", "Came");
                if (products_model.isStrStatus() == true) {
                    Log.e("appSample", "TRUE");
                    imageCount = imageCount + 1;

                    if(imageCount == uploadingImageLists.size()) {
                        // Image Sending Completed
                        Map<String, String> my_params = new HashMap<>();
                        my_params.put("title", strPName);
                        my_params.put("description", strPDesc);
                        my_params.put("meta_title", strPName);
                        my_params.put("meta_description", strPDesc);
                        my_params.put("meta_keyword", "MartBee");
                        my_params.put("price", strPDiscount);
                        my_params.put("old_price", strPPrice);
                        my_params.put("cat_id", cate_id);
                        my_params.put("sub_cat_id", "" + selSubCateId);
                        my_params.put("quantity", strPQty);
                        my_params.put("seller_id", sellerId);
                        my_params.put("uom", strPType);

                        if (NetworkAvailability.isNetworkAvailable(AddProductNew.this)) {
                            productsViewModel.addProducts(finalPath, params);
                        } else {
                            NetworkAvailability networkAvailability = new NetworkAvailability(AddProductNew.this);
                            networkAvailability.noInternetConnection(AddProductNew.this, Constants.ADD_PRODUCT_INFO);
                        }

                        productsViewModel.addProductLV().observe(AddProductNew.this, new Observer<Products_Model>() {
                            @Override
                            public void onChanged(Products_Model products_model) {
                                if (products_model.isStrStatus() == true) {
                                    showSuccessDialog(products_model, "Product successfully added.", PRODUCT_ADDED_success);
                                }
                            }
                        });


                    } else {
                        Log.e("appSample", "UploadedImage: " + uploadingImageLists.get(imageCount -1).getImage());
                        uploadingImage(uploadingImageLists.get(imageCount).getImage());
                    }
                } else {
                    Log.e("appSample", "Else_False");
                }
            }
        });
    }

    private void showUOMSheet(int showUOMStatus) {
        if (NetworkAvailability.isNetworkAvailable(AddProductNew.this)) {
            productsViewModel.getUOM();
        } else {
            NetworkAvailability networkAvailability = new NetworkAvailability(this);
            networkAvailability.noInternetConnection(AddProductNew.this, Constants.NETWORK_ENABLE_SETTINGS);
        }
        productsViewModel.getUOMLV().observe(this, new Observer<UOMModel>() {
            @Override
            public void onChanged(UOMModel uomModel) {
                uomDatasList = uomModel.getUomLists();
                showUOMAdapter(showUOMStatus);
            }
        });
    }

    private void showSuccessDialog(Products_Model products_model, String strMessage, int resultCode) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AddProductNew.this, SweetAlertDialog.SUCCESS_TYPE);
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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showErrorMsg(String error) {
        CommonMethods.Toast(AddProductNew.this, error);
    }

    @Override
    public void onItemClick(int position) {
        uploadingImageLists.remove(position);
        imageUploadingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NETWORK_ENABLE_SETTINGS) {
            showUOMSheet(1);
        } else if(requestCode == ADD_PRODUCT_INFO) {
            if (!NetworkAvailability.isNetworkAvailable(AddProductNew.this)) {
                NetworkAvailability networkAvailability = new NetworkAvailability(this);
                networkAvailability.noInternetConnection(AddProductNew.this, Constants.ADD_PRODUCT_INFO);
            }
        }

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Video.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Log.e("appSample", "GalleryPath: " + picturePath);
                finalPath = new File(picturePath);

                int file_size_two = Integer.parseInt(String.valueOf(finalPath.length() / 1024));
                Log.e("appSample", "GalleryPathSize: " + file_size_two);

//                Uri compressUri = Uri.fromFile(finalPath);
                showUploadedImage();

                UploadingImageList item = new UploadingImageList();
                item.setImg_id("" + uploadingImageLists.size() + 1);
                item.setImage(picturePath);
                item.setCreated_date("");
                uploadingImageLists.add(item);

                if(uploadingImageLists != null) {
                    if(uploadingImageLists.size() > 0) {
                        photos_recycle.setHasFixedSize(true);
                        photos_recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                        imageUploadingAdapter = new ImageUploadingAdapter(uploadingImageLists, getApplicationContext(), AddProductNew.this);
                        photos_recycle.setAdapter(imageUploadingAdapter);
                    }
                }

                /*Glide.with(getApplicationContext())
                        .load(compressUri)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(product_image);*/
            }
        }

        if (requestCode == CAMERA_IMAGE) {
            if (resultCode == RESULT_OK) {
                String path1;
                if (android.os.Build.VERSION.SDK_INT >= 24) {
                    path1 = tempFile.getAbsolutePath();
                } else {
                    path1 = fileUri.getPath();
                }

//                String imagePath = Util.compressImage(AddProduct.this, path1);
                finalPath = new File(path1);
                int file_size = Integer.parseInt(String.valueOf(finalPath.length() / 1024));
                Log.e("appSample", "TakenFileSize: " + file_size);

//                Uri compressUri = Uri.fromFile(finalPath);
                showUploadedImage();

                UploadingImageList item = new UploadingImageList();
                item.setImg_id("" + uploadingImageLists.size() + 1);
                item.setImage(path1);
                item.setCreated_date("");
                uploadingImageLists.add(item);

                if(uploadingImageLists != null) {
                    if(uploadingImageLists.size() > 0) {
                        photos_recycle.setHasFixedSize(true);
                        photos_recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                        imageUploadingAdapter = new ImageUploadingAdapter(uploadingImageLists, getApplicationContext(), AddProductNew.this);
                        photos_recycle.setAdapter(imageUploadingAdapter);
                    }
                }


                /*Glide.with(getApplicationContext())
                        .load(compressUri)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(product_image);*/

            }
        }

        if (requestCode == ADD_PRODUCT_to_SUBCATE_SELECTION) {
            if (resultCode == SUB_CATEGORY_SELECTED) {
                hideKeyboard(product_subcate);
                sel_subcategory = data.getStringExtra("SelectedSubCategory");
                selSubCateId = data.getIntExtra("SelectedSubId", 0);
                product_subcate.setText(sel_subcategory);
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
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    private void showUploadView() {
        upload_layout.setVisibility(View.VISIBLE);
//        cardImage.setVisibility(View.GONE);
        imagelist_layout.setVisibility(View.GONE);
    }

    private void showUploadedImage() {
        upload_layout.setVisibility(View.GONE);
//        cardImage.setVisibility(View.VISIBLE);
        imagelist_layout.setVisibility(View.VISIBLE);
    }

    private void showUOMAdapter(int showUOMStatus) {
        for(int i=0; i < uomDatasList.size(); i++) {
            uomSpinnerList.add((uomDatasList.get(i).getStrUom()));
        }

        uomSpinnerList.add("Select Status");

        HintAdapter dataAdapter = new HintAdapter(this, android.R.layout.simple_spinner_item, uomSpinnerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uom_spinner.setAdapter(dataAdapter);
        uom_spinner.setSelection(dataAdapter.getCount());

        TagFlowLayout.Adapter adapter =
                new TagFlowLayout.Adapter(getApplicationContext()) {
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
        if(showUOMStatus == 1) {
            bottomSheetDialog.show();
        }
    }

    public void checkCameraPermission() {
        if (PermissionManager.checkIsGreaterThanM()) {
            if (!PermissionManager.checkPermissionForReadExternalStorage(AddProductNew.this) ||
                    !PermissionManager.checkPermissionForWriteExternalStorage(AddProductNew.this) ||
                    !PermissionManager.checkPermissionForCamara(AddProductNew.this)) {
                PermissionManager.requestPermissionForCamera(AddProductNew.this);
            } else {
                openCamera();
            }

        } else {
            openCamera();
        }
    }

    public void openCamera() {
        if (!CameraUtils.isDeviceSupportCamera(getApplicationContext())) {
            CommonMethods.Toast(AddProductNew.this,  getString(R.string.device_dont_have_camera));
            return;
        }
        bottomSheetUpload.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionManager.ALL_CAMERS_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();

                } else {
                    CommonMethods.Toast(AddProductNew.this, "This App required Location permission." +
                            "Please enable from app settings.");
                }
        }
    }

}
