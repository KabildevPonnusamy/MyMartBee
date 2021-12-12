package com.mart.mymartbee.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.mart.mymartbee.BuildConfig;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.CameraUtils;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.commons.PermissionManager;
import com.mart.mymartbee.commons.Util;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.adapters.ViewPagerAdapter;
import com.mart.mymartbee.viewmodel.implementor.ProductsViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.ProductsViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDetails extends AppCompatActivity implements View.OnClickListener, Constants {

    ImageView prod_details_back, prod_details_image, upload_img;
    TextView prod_details_title, prod_details_subcate, prod_details_oldprice, prod_details_price, prod_details_desc,
            prod_details_stock_with_uom;
    TextView product_edit_btn, product_delete_btn;
    Products_Model.ProductCategories.ProductsList productsObj;
    ArrayList<Products_Model.ProductCategories.ProductsList.OtherImages> imagesArrayList;

    ProductsViewModel productsViewModel;
    ProgressDialog progressDialog;

    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    String strCateId, strSellerId, strProductId;
    String strSubCateId;
    ViewPager viewPager;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 2000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    ViewPagerAdapter viewPagerAdapter;
    LinearLayout delete_image_layout;
    File finalPath, tempFile;
    BottomSheetDialog bottomSheetUpload;
    LinearLayout gallery_layout, camera_layout;
    ImageView close_img;
    Uri fileUri;
    boolean isImgDeleted = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModelImpl.class);
        initView();
        observeProgress();
        getMyPreferences();
        bottomSheetImageUpload();

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
        delete_image_layout = (LinearLayout) findViewById(R.id.delete_image_layout);
        imagesArrayList = new ArrayList<Products_Model.ProductCategories.ProductsList.OtherImages>();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        prod_details_back = findViewById(R.id.prod_details_back);
        prod_details_image = findViewById(R.id.prod_details_image);
        upload_img = findViewById(R.id.upload_img);
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
        delete_image_layout.setOnClickListener(this);
        upload_img.setOnClickListener(this);

        setValues();

    }

    private void setValues() {
        productsObj = StorageDatas.getsObj().getProductsObj();
        strProductId = productsObj.getStrProdut_id();
        imagesArrayList.addAll(productsObj.getOtherImages());

        Bundle bundle = getIntent().getExtras();
        strSubCateId = bundle.getString("subcateId");
        prod_details_title.setText(productsObj.getStrProduct_title());
        prod_details_subcate.setText(bundle.getString("subcateName"));

        if(productsObj.getStrProduct_oldprice().equalsIgnoreCase(productsObj.getStrProduct_price())) {
            prod_details_oldprice.setVisibility(View.GONE);
        } else {
            prod_details_oldprice.setVisibility(View.VISIBLE);
            String oldPrice = productsObj.getStrProduct_oldprice().replace(".00", "");
            prod_details_oldprice.setText("RM. " + oldPrice);
        }


        String newPrice = productsObj.getStrProduct_price().replace(".00", "");
        prod_details_price.setText("RM. " + newPrice);

        prod_details_desc.setText(productsObj.getStrProduct_description());
        prod_details_stock_with_uom.setText(productsObj.getStrProduct_quantity() + " " + productsObj.getStrProduct_uom());
        Glide.with(getApplicationContext()).load(productsObj.getStrProduct_image()).into(prod_details_image);

        prod_details_oldprice.setPaintFlags(prod_details_oldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        setAdapter();
        checkDeleteImageStatus();
    }

    public void checkDeleteImageStatus() {
        if (imagesArrayList.size() == 1) {
            delete_image_layout.setVisibility(View.GONE);
        } else {
            delete_image_layout.setVisibility(View.VISIBLE);
        }

        if (imagesArrayList.size() == 3) {
            upload_img.setVisibility(View.GONE);
        } else {
            upload_img.setVisibility(View.VISIBLE);
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
                    fileUri = FileProvider.getUriForFile(ProductDetails.this,
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

                if (ContextCompat.checkSelfPermission(ProductDetails.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProductDetails.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    i.setType("image/*");
                    startActivityForResult(i, PICK_IMAGE);
                }
                break;

            case R.id.prod_details_back:
                onBackPressed();
//                finish();
                break;

            case R.id.product_edit_btn:
                if (NetworkAvailability.isNetworkAvailable(ProductDetails.this)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("fromActivity", "ProductDetails");
                    bundle.putString("fromActivitySubcategory", prod_details_subcate.getText().toString().trim());
                    bundle.putInt("fromActivitySubcategoryID", Integer.parseInt(strSubCateId));
                    Intent intent = new Intent(ProductDetails.this, UpdateProduct.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, PRODUCT_DETAILS_to_PRODUCT_EDIT);
                } else {
                    NetworkAvailability networkAvailability = new NetworkAvailability(this);
                    networkAvailability.noInternetConnection(ProductDetails.this, Constants.NETWORK_ENABLE_SETTINGS);
                }
                break;

            case R.id.delete_image_layout:
                // Get View Pagers current Position
                int pos = viewPager.getCurrentItem();

                Map<String, String> delparams = new HashMap<>();
                delparams.put("seller_id", strSellerId);
                delparams.put("cat_id", strCateId);
                delparams.put("product_id", strProductId);
                delparams.put("image", imagesArrayList.get(pos).getStrImageName());

                Log.e("appSample", "seller_id: " + strSellerId);
                Log.e("appSample", "cat_id: " + strCateId);
                Log.e("appSample", "product_id: " + strProductId);
                Log.e("appSample", "image: " + imagesArrayList.get(pos).getStrImageName());

                if (NetworkAvailability.isNetworkAvailable(ProductDetails.this)) {
                    productsViewModel.deleteProductImages(delparams);
                } else {
                    NetworkAvailability networkAvailability = new NetworkAvailability(this);
                    networkAvailability.noInternetConnection(ProductDetails.this, Constants.NETWORK_ENABLE_SETTINGS);
                }

                productsViewModel.deleteProductImagesLV().observe(this, new Observer<Products_Model>() {
                    @Override
                    public void onChanged(Products_Model products_model) {

                        if (products_model.isStrStatus() == false) {
                            showErrorDialog(products_model.getStrMessage());
                        } else {
                            for (int i = 0; i < imagesArrayList.size(); i++) {
                                if (imagesArrayList.get(i).getStrImageName().
                                        equalsIgnoreCase(imagesArrayList.get(pos).getStrImageName())) {
                                    imagesArrayList.remove(i);
                                }
                            }
                            viewPagerAdapter.notifyDataSetChanged();
                            checkDeleteImageStatus();
                            showImgDeleteSuccessDialog(products_model);
                        }
                    }
                });

                break;

            case R.id.upload_img:
                if (imagesArrayList.size() < 3) {
                    checkCameraPermission();
                } else {
                    CommonMethods.Toast(ProductDetails.this, "Sorry! You have reached maximum photos to upload.");
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

    @Override
    public void onBackPressed() {
//        setResult(PRODUCT_UPDATED_success);

        if (isImgDeleted == true) {
            setResult(IMAGE_DELETION_success);
            finish();
        } else {
            finish();
        }

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

    public void checkCameraPermission() {
        if (PermissionManager.checkIsGreaterThanM()) {
            if (!PermissionManager.checkPermissionForReadExternalStorage(ProductDetails.this) ||
                    !PermissionManager.checkPermissionForWriteExternalStorage(ProductDetails.this) ||
                    !PermissionManager.checkPermissionForCamara(ProductDetails.this)) {
                PermissionManager.requestPermissionForCamera(ProductDetails.this);
            } else {
                openCamera();
            }

        } else {
            openCamera();
        }
    }

    public void openCamera() {
        if (!CameraUtils.isDeviceSupportCamera(getApplicationContext())) {
            CommonMethods.Toast(ProductDetails.this, getString(R.string.device_dont_have_camera));
            return;
        }
        bottomSheetUpload.show();
    }

    private void setAdapter() {
        viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), imagesArrayList);
        viewPager.setAdapter(viewPagerAdapter);
        TabLayout tabDots = (TabLayout) findViewById(R.id.tabDots);
        tabDots.setupWithViewPager(viewPager, true);

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == imagesArrayList.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
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

    public void showSuccessMessageDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ProductDetails.this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Success");
        sweetAlertDialog.setContentText("Image uploaded successfully.");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sweetAlertDialog.dismiss();
            }
        });
    }

    private void showImgDeleteSuccessDialog(Products_Model products_model) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ProductDetails.this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Success");
        sweetAlertDialog.setContentText("Image deleted successfully.");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sweetAlertDialog.dismiss();
                StorageDatas.getInstance().setProducts_model(products_model);
                isImgDeleted = true;
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

                uploadImage(finalPath);

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

                finalPath = new File(path1);
                int file_size = Integer.parseInt(String.valueOf(finalPath.length() / 1024));
                Log.e("appSample", "TakenFileSize: " + file_size);
                uploadImage(finalPath);

            }
        }
    }

    public void uploadImage(File file) {
        Map<String, String> params = new HashMap<>();
        params.put("seller_id", strSellerId);
        params.put("cat_id", strCateId);
        params.put("product_id", strProductId);

        if (NetworkAvailability.isNetworkAvailable(ProductDetails.this)) {
            productsViewModel.uploadingProductImage(file, params);
        } else {
            NetworkAvailability networkAvailability = new NetworkAvailability(this);
            networkAvailability.noInternetConnection(ProductDetails.this, Constants.NETWORK_ENABLE_SETTINGS);
        }

        productsViewModel.uploadingProductImagesLV().observe(this, new Observer<Products_Model>() {
            @Override
            public void onChanged(Products_Model products_model) {
                if (products_model.isStrStatus() == true) {

                    StorageDatas.getInstance().setProducts_model(products_model);
                    Log.e("appSample", "Status_Success");

                    for (int i = 0; i < products_model.getProductCategories().size(); i++) {
                        Log.e("appSample", "ProductCateSize: " + products_model.getProductCategories().size());
                        for (int j = 0; j < products_model.getProductCategories().get(i).getProductsLists().size(); j++) {

                            if (strProductId.equalsIgnoreCase(products_model.getProductCategories().get(i).getProductsLists().get(j).getStrProdut_id())) {
                                imagesArrayList.clear();
                                imagesArrayList.addAll(products_model.getProductCategories().get(i).getProductsLists().get(j).getOtherImages());
                                viewPagerAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    showSuccessMessageDialog();
                    checkDeleteImageStatus();


                }
            }
        });
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
                    CommonMethods.Toast(ProductDetails.this, "This App required Location permission." +
                            "Please enable from app settings.");
                }
        }
    }
}
