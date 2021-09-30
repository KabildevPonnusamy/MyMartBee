package com.mart.mymartbee.view;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.iceteck.silicompressorr.SiliCompressor;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.GPSTracker;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.model.RegisterModel;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.commons.CameraUtils;
import com.mart.mymartbee.commons.PathUtil;
import com.mart.mymartbee.viewmodel.implementor.RegisterViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.RegisterViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreCreation extends AppCompatActivity implements View.OnClickListener, Constants {

    ProgressDialog progressDialog;
    File tempPath, finalPath;

    String strCountryCode, strMobile, strAndroidId, strFCM, strShop,
            strCategory, strAddress, strImage;
    String strLatitude = "", strLongitude = "";
    String profile_str = "";

    CircleImageView store_image;
    EditText store_name;
    TextView upload_image;
    LinearLayout category_ll, address_ll;
    MaterialTextView category_tv, address_tv;
    Button create_btn;
    ImageView act_back;
    TextInputLayout layout_store;
    String sel_category = "";
    int selectedId = -1;
    RegisterViewModel registerViewModel;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_creation_layout);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModelImpl.class);
        getDatas();
        initView();
        getMyPreferences();
        observeProgress();
    }

    private void getMyPreferences() {
        myKeyValue = getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(StoreCreation.this);
    }

    public void observeProgress() {
        registerViewModel.progressbarObservable().observe(this, new Observer<Boolean>() {
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

    private void getDatas() {
        strFCM = StorageDatas.getInstance().getFirebaseToken();
        strAndroidId  = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Bundle bundle = getIntent().getExtras();
        strCountryCode = bundle.getString("CountryCode");
        strMobile = bundle.getString("MobileNumber");

        /*strCountryCode = "+60";
        strMobile = "123456702";*/
    }

    public void initView() {
        layout_store = findViewById(R.id.layout_store);
        act_back = findViewById(R.id.act_back);
        create_btn = findViewById(R.id.create_btn);
        address_tv = findViewById(R.id.address_tv);
        category_tv = findViewById(R.id.category_tv);
        address_ll = findViewById(R.id.address_ll);
        category_ll = findViewById(R.id.category_ll);
        upload_image = findViewById(R.id.upload_image);
        store_name = findViewById(R.id.store_name);
        store_image = findViewById(R.id.store_image);
        upload_image.setOnClickListener(this);
        act_back.setOnClickListener(this);
        category_ll.setOnClickListener(this);
        address_ll.setOnClickListener(this);
        create_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_image:
                if (!CameraUtils.isDeviceSupportCamera(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.device_dont_have_camera),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                getImage();
                break;

            case R.id.act_back:
                finish();
                break;

            case R.id.category_ll:
                hideKeyboard(category_ll);
                Log.e("appSample", "ID: " + selectedId);

                Bundle bundle = new Bundle();
                bundle.putString("mobileNumber", strCountryCode + strMobile);
//                bundle.putString("categoryName", sel_category);
                bundle.putInt("SelectedId", selectedId);
                Intent intent = new Intent(StoreCreation.this, CategorySelection.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, STORE_CREATION_to_CATEGORY_SELECTION);
                break;

            case R.id.address_ll:
                Bundle addressbundle = new Bundle();
                addressbundle.putString("SelectedLatitude", strLatitude);
                addressbundle.putString("SelectedLongitude", strLongitude);

                Intent addressIntent = new Intent(StoreCreation.this, AddressSelection.class);
                addressIntent.putExtras(addressbundle);
                startActivityForResult(addressIntent, STORE_CREATION_to_ADDRESS_SELECTION);
                break;

            case R.id.create_btn:
                strShop = store_name.getText().toString().trim();
                strCategory = category_tv.getText().toString().trim();
                strAddress = address_tv.getText().toString().trim();

                if(strShop.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please enter shop name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strCategory.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please select category", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strAddress.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please select address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strImage == null) {
                    Toast.makeText(getApplicationContext(), "Please pick store image", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, String> params = new HashMap<>();
                params.put("country_code", "+60");
                params.put("mobile_number", strMobile);
                params.put("imie_no", strAndroidId);
                params.put("gcm_id", StorageDatas.getInstance().getFirebaseToken());
                params.put("latitude", strLatitude);
                params.put("longitude", strLongitude);
                params.put("shop", strShop);
                params.put("cat_id", ""+selectedId);
                params.put("address", strAddress);

                registerViewModel.checkRegister(finalPath, params);
                registerViewModel.checkRegisterLiveData().observe(this, new Observer<RegisterModel>() {
                    @Override
                    public void onChanged(RegisterModel registerModel) {

                        if(registerModel.getStrModule().equalsIgnoreCase("login")) {
                            showShopCreateDialog(registerModel);
                            Log.e("appSample", "Resp: " + registerModel.getSellerDetails().getStrRegImage());
                        }
                    }
                });
                break;
        }
    }

    private void getImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAllowFlipping(false)
                .setActivityTitle("Select Image")
                .setCropShape(CropImageView.CropShape.OVAL)
                .setCropMenuCropButtonTitle("Save")
                .setRequestedSize(200, 200)
                .start(StoreCreation.this);
            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uri = result.getUri();

                if(uri != null) {

                    try {
                        String filePath= PathUtil.getPath(getApplicationContext(), uri);
                        Log.e("appSample", "File_Path: " + filePath);

                        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                        File directory = contextWrapper.getDir(getFilesDir().getName(), Context.MODE_PRIVATE);

                        tempPath = new File(directory + "/uploads/");

                        new ImageCompressionAsyncTask(this, filePath).execute(uri.toString(), "" + tempPath);

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getApplicationContext(), getString(R.string.some_issues_text), Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == STORE_CREATION_to_CATEGORY_SELECTION) {
            if(resultCode == CATEGORY_SELECTED) {
                hideKeyboard(store_name);
                sel_category = data.getStringExtra("SelectedCategory");
                selectedId = data.getIntExtra("SelectedId", 0);
                category_tv.setText(sel_category);
            }
        }

        if(requestCode == STORE_CREATION_to_ADDRESS_SELECTION) {
            if(resultCode == ADDRESS_SELECTED) {
                strLatitude = data.getStringExtra("SelectedLatitude");
                strLongitude = data.getStringExtra("SelectedLongitude");
                strAddress = data.getStringExtra("SelectedAddress");
                address_tv.setText(strAddress);
            }
        }
    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;
        Uri compressUri = null;
        String filePath;

        public ImageCompressionAsyncTask(Context context, String filePath) {
            mContext = context;
            this.filePath = filePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StoreCreation.this);
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progressdialog);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = SiliCompressor.with(mContext).compress(params[0], new File(params[1]));
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            strImage = "" + s;
            finalPath = new File(s);
            compressUri = Uri.fromFile(finalPath);
            Log.e("appSample", "FilePath_2: " + s);
            store_image.setImageURI(compressUri);
            upload_image.setText("Change");

        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showProgress() {
        Log.e("appSample", "showProgress");
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

    private void showShopCreateDialog(RegisterModel registerModel) {
        Dialog dialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.shop_created_dialog);

        RelativeLayout store_created_layout = (RelativeLayout) dialog.findViewById(R.id.store_created_layout);
        LottieAnimationView animView = (LottieAnimationView) dialog.findViewById(R.id.animView);
        CircleImageView created_store_img = (CircleImageView) dialog.findViewById(R.id.created_store_img);
        animView.setVisibility(View.VISIBLE);
        store_created_layout.setVisibility(View.GONE);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.appicon_temp)
                .error(R.drawable.appicon_temp);
        Glide.with(getApplicationContext()).load(registerModel.getSellerDetails().getStrRegImage()).apply(options).into(created_store_img);

        animView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animView.setVisibility(View.GONE);
                store_created_layout.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_ID, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegId(), myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_SHOP, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegshop(), myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_CATEGORY, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegCategoryID(), myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_CATEGORY_NAME, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegCategory(), myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_IMAGE, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegImage(), myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_ADDRESS, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegAddress(), myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_MOBILE, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegMobileNumber(), myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_COUNTRY_CODE, TripleDes.getDESEncryptValue("+60", myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_LATITUDE, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegLatitude(), myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_LONGITUDE, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegLongitude(), myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_PRODUCTS_COUNT, TripleDes.getDESEncryptValue("0", myKeyValue) );

                            Intent homeIntent = new Intent(StoreCreation.this, HomeActivity.class);
                            startActivityForResult(homeIntent, STORE_CREATION_to_HOME);
                            finish();
                        } catch (Exception e) {
                        }
                    }
                }, 2000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        dialog.show();
    }
}
