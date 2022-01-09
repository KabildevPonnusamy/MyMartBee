package com.mart.mymartbee.view;

import android.Manifest;
import android.animation.Animator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.iceteck.silicompressorr.SiliCompressor;
import com.mart.mymartbee.BuildConfig;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.CustomTimePickerDialog;
import com.mart.mymartbee.commons.PermissionManager;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.commons.Util;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.HintAdapter;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.model.RegisterModel;
import com.mart.mymartbee.model.UploadingImageList;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.commons.CameraUtils;
import com.mart.mymartbee.commons.PathUtil;
import com.mart.mymartbee.view.adapters.ImageUploadingAdapter;
import com.mart.mymartbee.viewmodel.implementor.RegisterViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.RegisterViewModel;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreCreation extends AppCompatActivity implements View.OnClickListener, Constants {

    ProgressDialog progressDialog;
    File tempPath, finalPath;

    String strCountryCode, strMobile, strAndroidId, strFCM, strShop,
            strCategory, strAddress, strImage;
    String strLatitude = "", strLongitude = "";
    String profile_str = "";

//    CircleImageView store_image;
    Spinner business_type_spinner;
    ImageView store_image;
    EditText store_name, business_category_et, address_et, start_time, close_time;
    TextView upload_image;
    Button create_btn;
    ImageView act_back;
    String sel_category = "";
    String strBusinessType = "";
    LinearLayout business_cate_layout;
    int selectedId = -1;
    RegisterViewModel registerViewModel;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    String strStartTime = "";
    String strCloseTime = "";
    File tempFile;
    Uri fileUri;
    Calendar startCalDate;
    TimePickerDialog mTimePicker;
    Calendar prefCaldate;
    int prefmHour, prefmMinute;
    BottomSheetDialog bottomSheetUpload;
    LinearLayout gallery_layout, camera_layout;
    ImageView close_img;

    ArrayList<String> businessTypeList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_creation_layout);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModelImpl.class);
        getDatas();
        initView();
        getMyPreferences();
        observeProgress();
        bottomSheetImageUpload();
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
        strMobile = "666666666";*/
    }

    public void initView() {
        business_cate_layout = findViewById(R.id.business_cate_layout);
        act_back = findViewById(R.id.act_back);
        create_btn = findViewById(R.id.create_btn);
        upload_image = findViewById(R.id.upload_image);
        store_name = findViewById(R.id.store_name);
        business_category_et = findViewById(R.id.business_category_et);
        address_et = findViewById(R.id.address_et);
        start_time = findViewById(R.id.start_time);
        close_time = findViewById(R.id.close_time);
        business_type_spinner = findViewById(R.id.business_type_spinner);
        store_image = findViewById(R.id.store_image);
        start_time.setFocusable(false);
        start_time.setClickable(true);
        close_time.setFocusable(false);
        close_time.setClickable(true);
        address_et.setFocusable(false);
        address_et.setClickable(true);
        business_category_et.setFocusable(false);
        business_category_et.setClickable(true);
        business_category_et.setOnClickListener(this);
        address_et.setOnClickListener(this);
        start_time.setOnClickListener(this);
        close_time.setOnClickListener(this);
        upload_image.setOnClickListener(this);
        act_back.setOnClickListener(this);
        create_btn.setOnClickListener(this);

        prefCaldate = Calendar.getInstance();

        businessTypeList = new ArrayList<String>();
        businessTypeList.add("Seller");
        businessTypeList.add("Re-Seller");
        businessTypeList.add("Business Type*");
        setSpinnerListerer();

    }

    public void setSpinnerListerer() {
        HintAdapter dataAdapter = new HintAdapter(this, android.R.layout.simple_spinner_item, businessTypeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        business_type_spinner.setAdapter(dataAdapter);
        business_type_spinner.setSelection(dataAdapter.getCount());

        business_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strBusinessType = business_type_spinner.getSelectedItem().toString();
                Log.e("appSample", "Selected: " + strBusinessType);
                if(strBusinessType.equalsIgnoreCase("Re-Seller")) {
                    business_cate_layout.setVisibility(View.GONE);
                } else{
                    business_cate_layout.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void startTimePickerDialog(int hour, int minute) {
        mTimePicker = new TimePickerDialog(StoreCreation.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String timeSet = "";
                if (selectedHour > 12) {
                    selectedHour -= 12;
                    timeSet = "PM";
                } else if (selectedHour == 0) {
                    selectedHour += 12;
                    timeSet = "AM";
                } else if (selectedHour == 12) {
                    timeSet = "PM";
                } else {
                    timeSet = "AM";
                }

                String min = "";
                if (selectedMinute < 10)
                    min = "0" + selectedMinute;
                else
                    min = String.valueOf(selectedMinute);

                String aTime = new StringBuilder().append(selectedHour).append(':')
                        .append(min).append(" ").append(timeSet).toString();

                start_time.setText(aTime);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void closetimePickerDialog(int hour, int minute) {
        mTimePicker = new TimePickerDialog(StoreCreation.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String timeSet = "";
                if (selectedHour > 12) {
                    selectedHour -= 12;
                    timeSet = "PM";
                } else if (selectedHour == 0) {
                    selectedHour += 12;
                    timeSet = "AM";
                } else if (selectedHour == 12) {
                    timeSet = "PM";
                } else {
                    timeSet = "AM";
                }

                String min = "";
                if (selectedMinute < 10)
                    min = "0" + selectedMinute;
                else
                    min = String.valueOf(selectedMinute);

                String aTime = new StringBuilder().append(selectedHour).append(':')
                        .append(min).append(" ").append(timeSet).toString();

                close_time.setText(aTime);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
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
                    fileUri = FileProvider.getUriForFile(StoreCreation.this,
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

                if (ContextCompat.checkSelfPermission(StoreCreation.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(StoreCreation.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    i.setType("image/*");
                    startActivityForResult(i, PICK_IMAGE);
                }
                break;

            case R.id.start_time:
                prefmHour = prefCaldate.get(Calendar.HOUR_OF_DAY);
                prefmMinute = prefCaldate.get(Calendar.MINUTE);
                startTimePickerDialog(prefmHour, prefmMinute);
//                showStartTime();
                break;

            case R.id.close_time:
                prefmHour = prefCaldate.get(Calendar.HOUR_OF_DAY);
                prefmMinute = prefCaldate.get(Calendar.MINUTE);
                closetimePickerDialog(prefmHour, prefmMinute);
//                showCloseTime();
                break;

            case R.id.business_category_et:
                if (NetworkAvailability.isNetworkAvailable(StoreCreation.this)) {
                    hideKeyboard(business_category_et);
                    Log.e("appSample", "ID: " + selectedId);

                    Bundle bundle = new Bundle();
                    bundle.putString("mobileNumber", strCountryCode + strMobile);
                    bundle.putInt("SelectedId", selectedId);
                    Intent intent = new Intent(StoreCreation.this, BusinessCategorySelection.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, STORE_CREATION_to_CATEGORY_SELECTION);
                } else {
                    NetworkAvailability networkAvailability = new NetworkAvailability(this);
                    networkAvailability.noInternetConnection(StoreCreation.this, Constants.NETWORK_ENABLE_SETTINGS);
                }
                break;

            case R.id.upload_image:

                checkCameraPermission();

                break;

            case R.id.act_back:
                onBackPressed();
//                finish();
                break;

            case R.id.address_et:
                if (NetworkAvailability.isNetworkAvailable(StoreCreation.this)) {
                    Bundle addressbundle = new Bundle();
                    addressbundle.putString("SelectedLatitude", strLatitude);
                    addressbundle.putString("SelectedLongitude", strLongitude);

                    Intent addressIntent = new Intent(StoreCreation.this, AddressSelection.class);
                    addressIntent.putExtras(addressbundle);
                    startActivityForResult(addressIntent, STORE_CREATION_to_ADDRESS_SELECTION);
                } else {
                    NetworkAvailability networkAvailability = new NetworkAvailability(this);
                    networkAvailability.noInternetConnection(StoreCreation.this, Constants.NETWORK_ENABLE_SETTINGS);
                }

                break;

            case R.id.create_btn:
                strShop = store_name.getText().toString().trim();
                strCategory = business_category_et.getText().toString().trim();
                strAddress = address_et.getText().toString().trim();
                strStartTime = start_time.getText().toString().trim();
                strCloseTime = close_time.getText().toString().trim();
                /*boolean val = checktimings(strStartTime, strCloseTime);
                if (!val) {
                    showErrorMessage("Store timings are mis-matching.");
                    return;
                }*/

                if (strBusinessType.equalsIgnoreCase("") || strBusinessType.equalsIgnoreCase("Business Type*")) {
                    showErrorMessage("Please select Business Type.");
                    return;
                }

                if(strBusinessType.equalsIgnoreCase("Seller")) {
                    if(strCategory.equalsIgnoreCase("")) {
                        showErrorMessage("Please select business category.");
                        return;
                    }
                }

                if(strShop.equalsIgnoreCase("")) {
                    showErrorMessage("Please enter shop name.");
                    return;
                }



                if(strAddress.equalsIgnoreCase("")) {
                    showErrorMessage("Please select address.");
                    return;
                }

                if(strStartTime.equalsIgnoreCase("")) {
                    showErrorMessage("Please select start time.");
                    return;
                }

                if(strCloseTime.equalsIgnoreCase("")) {
                    showErrorMessage("Please select close time.");
                    return;
                }

//                if(tempPath == null) {
//                    showErrorMessage("Please pick store image.");
//                    return;
//                }

                String strCateId = "";
                String strBusinessId = "";
                if(strBusinessType.equalsIgnoreCase("Re-Seller")) {
                    strBusinessId = "2";
                    strCateId = "134";
                } else {
                    strBusinessId = "1";
                    strCateId = "" + selectedId;
                }

                Map<String, String> params = new HashMap<>();
                params.put("country_code", strCountryCode );
                params.put("mobile_number", strMobile);
                params.put("imie_no", strAndroidId);
                params.put("gcm_id", StorageDatas.getInstance().getFirebaseToken());
                params.put("latitude", strLatitude);
                params.put("longitude", strLongitude);
                params.put("shop", strShop);
//                params.put("cat_id", ""+selectedId);
                params.put("cat_id", ""+ strCateId);
                params.put("business", strBusinessId);
                params.put("address", strAddress);
                params.put("open_time", strStartTime);
                params.put("close_time", strCloseTime);

                Log.e("appSample", "CountryCode: " + strCountryCode);
                Log.e("appSample", "mobile_number: " + strMobile);
                Log.e("appSample", "imie_no: " + strAndroidId);
                Log.e("appSample", "gcm_id: " + StorageDatas.getInstance().getFirebaseToken());
                Log.e("appSample", "latitude: " + strLatitude);
                Log.e("appSample", "longitude: " + strLongitude);
                Log.e("appSample", "shop: " + strShop);
                Log.e("appSample", "cat_id: " + strCateId);
                Log.e("appSample", "business: " + strBusinessId);
                Log.e("appSample", "address: " + strAddress);
                Log.e("appSample", "open_time: " + strStartTime);
                Log.e("appSample", "close_time: " + strCloseTime);
                Log.e("appSample", "TempPath: " + tempPath);

                if (NetworkAvailability.isNetworkAvailable(StoreCreation.this)) {
                    registerViewModel.checkRegister(tempPath, params);
                } else {
                    NetworkAvailability networkAvailability = new NetworkAvailability(this);
                    networkAvailability.noInternetConnection(StoreCreation.this, Constants.NETWORK_ENABLE_SETTINGS);
                }

                registerViewModel.checkRegisterLiveData().observe(this, new Observer<RegisterModel>() {
                    @Override
                    public void onChanged(RegisterModel registerModel) {

                        if(registerModel.isStrStatus() == false) {
                            showErrorMessage(registerModel.getStrMessage());
                        } else {
                            if(registerModel.getStrModule().equalsIgnoreCase("login")) {
                                showShopCreateDialog(registerModel);
                                Log.e("appSample", "Resp: " + registerModel.getSellerDetails().getStrRegImage());
                            }
                        }
                    }
                });
                break;
        }
    }

    public void showErrorMessage(String errMessage) {
        CommonMethods.Toast(StoreCreation.this,  errMessage);
    }

    public void checkCameraPermission() {
        if (PermissionManager.checkIsGreaterThanM()) {
            if (!PermissionManager.checkPermissionForReadExternalStorage(StoreCreation.this) ||
                    !PermissionManager.checkPermissionForWriteExternalStorage(StoreCreation.this) ||
                    !PermissionManager.checkPermissionForCamara(StoreCreation.this)) {
                PermissionManager.requestPermissionForCamera(StoreCreation.this);
            } else {
                openCamera();
            }

        } else {
            openCamera();
        }
    }

    public void openCamera() {
        if (!CameraUtils.isDeviceSupportCamera(getApplicationContext())) {
            showErrorMessage(getString(R.string.device_dont_have_camera));
            return;
        }

        bottomSheetUpload.show();
//        getImage();
    }

    private void getImage() {

        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            tempFile = Util.getOutputMediaFile();
            fileUri = FileProvider.getUriForFile(StoreCreation.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    tempFile);

            intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else {
            fileUri = Uri.fromFile(Util.getOutputMediaFile());
        }

        intent1.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent1, CAMERA_IMAGE);

        /*CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAllowFlipping(false)
                .setActivityTitle("Select Image")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("Save")
                .setRequestedSize(200, 200)
                .start(StoreCreation.this);*/

            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                tempPath = new File(picturePath);

                int file_size_two = Integer.parseInt(String.valueOf(finalPath.length() / 1024));
                Log.e("appSample", "GalleryPathSize: " + file_size_two);

                Uri compressUri = Uri.fromFile(finalPath);
                upload_image.setText("Change");

                Glide.with(getApplicationContext())
                        .load(compressUri)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(store_image);

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
                tempPath = new File(path1);

                int file_size = Integer.parseInt(String.valueOf(finalPath.length() / 1024));
                Log.e("appSample", "TakenFileSize: " + file_size);

                Uri compressUri = Uri.fromFile(finalPath);
                upload_image.setText("Change");

                Glide.with(getApplicationContext())
                        .load(compressUri)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(store_image);
            }
        }

        /*
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uri = result.getUri();

                if(uri != null) {

                    try {
                        String filePath = PathUtil.getPath(getApplicationContext(), uri);
                        Log.e("appSample", "File_Path: " + filePath);

                        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                        File directory = contextWrapper.getDir(getFilesDir().getName(), Context.MODE_PRIVATE);

                        tempPath = new File(filePath);
                        store_image.setImageURI(uri);
                        upload_image.setText("Change");

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                showErrorMessage(getString(R.string.some_issues_text));
            }
        }
*/
        if(requestCode == STORE_CREATION_to_CATEGORY_SELECTION) {
            if(resultCode == CATEGORY_SELECTED) {
                hideKeyboard(store_name);
                sel_category = data.getStringExtra("SelectedCategory");
                selectedId = data.getIntExtra("SelectedId", 0);
                business_category_et.setText(sel_category);
            }
        }

        if(requestCode == STORE_CREATION_to_ADDRESS_SELECTION) {
            if(resultCode == ADDRESS_SELECTED) {
                strLatitude = data.getStringExtra("SelectedLatitude");
                strLongitude = data.getStringExtra("SelectedLongitude");
                strAddress = data.getStringExtra("SelectedAddress");
                address_et.setText(strAddress);
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
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_COUNTRY_CODE, TripleDes.getDESEncryptValue(strCountryCode, myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_LATITUDE, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegLatitude(), myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_LONGITUDE, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegLongitude(), myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_PRODUCTS_COUNT, TripleDes.getDESEncryptValue("0", myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_START_TIME, TripleDes.getDESEncryptValue(strStartTime, myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_CLOSE_TIME, TripleDes.getDESEncryptValue(strCloseTime, myKeyValue) );

                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_ACC_HOLDER_NAME, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegAccountHolderName(), myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_ACC_NUMBER, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrAccountNumber(), myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_BANK_NAME, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrBankName(), myKeyValue) );
                            preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_BUSINESS_TYPE, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrBusiness(), myKeyValue) );

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

    public void showStartTime() {

        Calendar prefCaldate = Calendar.getInstance();
        int mHour = prefCaldate.get(Calendar.HOUR_OF_DAY);
        int mMinute = prefCaldate.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {

                    view.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            int hour = view.getCurrentHour();
                            int minutes = view.getCurrentMinute();
                        }
                    });

                    view.setFocusable(false);
                    view.setEnabled(false);
                    view.setBackgroundColor(Color.TRANSPARENT);

                    int hour = view.getCurrentHour();
                    int minutes = view.getCurrentMinute();

                    minutes = minutes * 30;

                    String timeSet = "";
                    if (hour > 12) {
                        hour -= 12;
                        timeSet = "PM";
                    } else if (hour == 0) {
                        hour += 12;
                        timeSet = "AM";
                    } else if (hour == 12) {
                        timeSet = "PM";
                    } else {
                        timeSet = "AM";
                    }

                    String min = "";
                    if (minutes < 10)
                        min = "0" + minutes;
                    else
                        min = String.valueOf(minutes);

                    strStartTime = new StringBuilder().append(hour).append(':')
                            .append(min).append(" ").append(timeSet).toString();

                    start_time.setText(strStartTime);
                }
            }
        };

        /*TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener,
                mHour, mMinute, false);*/

        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(StoreCreation.this,
                myTimeListener,
                mHour, mMinute, false);

        timePickerDialog.setTitle("Choose hour:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    public void showCloseTime() {
        Calendar prefCaldate = Calendar.getInstance();
        int mHour = prefCaldate.get(Calendar.HOUR_OF_DAY);
        int mMinute = prefCaldate.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {

                    view.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            int hour = view.getCurrentHour();
                            int minutes = view.getCurrentMinute();
                        }
                    });

                    view.setFocusable(false);
                    view.setEnabled(false);
                    view.setBackgroundColor(Color.TRANSPARENT);

                    int hour = view.getCurrentHour();
                    int minutes = view.getCurrentMinute();

                    minutes = minutes * 30;

                    String timeSet = "";
                    if (hour > 12) {
                        hour -= 12;
                        timeSet = "PM";
                    } else if (hour == 0) {
                        hour += 12;
                        timeSet = "AM";
                    } else if (hour == 12) {
                        timeSet = "PM";
                    } else {
                        timeSet = "AM";
                    }

                    String min = "";
                    if (minutes < 10)
                        min = "0" + minutes;
                    else
                        min = String.valueOf(minutes);

                    strCloseTime = new StringBuilder().append(hour).append(':')
                            .append(min).append(" ").append(timeSet).toString();

                    close_time.setText(strCloseTime);
                }
            }
        };

        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(StoreCreation.this,
                myTimeListener,
                mHour, mMinute, false);

        timePickerDialog.setTitle("Choose hour:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
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
                    showErrorMessage("This App required Location permission." +
                            "Please enable from app settings.");
                }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent= new Intent(StoreCreation.this, MobileLogin.class); // MobileLogin
        startActivity(intent);
        finish();
    }

    private boolean checktimings(String time, String endtime) {
        String pattern = "HH:mm aaa";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            if(date1.before(date2)) {
                Log.e("appSample", "Date1 Before Date2");
                return true;
            } else {
                Log.e("appSample", "Date1 After Date2");
                return false;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }
}
