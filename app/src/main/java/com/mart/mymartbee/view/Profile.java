package com.mart.mymartbee.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mart.mymartbee.BuildConfig;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.CameraUtils;
import com.mart.mymartbee.commons.CustomTimePickerDialog;
import com.mart.mymartbee.commons.GPSTracker;
import com.mart.mymartbee.commons.PermissionManager;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.commons.Util;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.model.RegisterModel;
import com.mart.mymartbee.model.UploadingImageList;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.adapters.ImageUploadingAdapter;
import com.mart.mymartbee.viewmodel.implementor.RegisterViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.RegisterViewModel;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity implements View.OnClickListener, Constants {

    ProgressDialog progressDialog;
    File finalPath;

    String strCountryCode, strMobile, strAndroidId, strFCM, strShop,
            strCategory, strAddress, strImage, strCategoryName;
    String strLatitude = "", strLongitude= "", strBusinessType = "";
    GPSTracker gpsTracker;
    String profile_str = "";

    LinearLayout business_category_layout;
    ImageView store_image;
    ImageView profile_back;
    EditText profile_store, profile_mobile_tv, business_category_et, address_et, start_time, close_time;
    TextView profile_change;
    Button update_btn;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    String sel_category = "";
    int selectedId = -1;
    RegisterViewModel registerViewModel;
    SweetAlertDialog sweetAlertDialog;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModelImpl.class);
        getMyPreferences();
        initView();
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
        preferenceDatas = new MyPreferenceDatas(Profile.this);
        strShop = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_SHOP), myKeyValue);
        strLatitude = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_LATITUDE), myKeyValue);
        strLongitude = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_LONGITUDE), myKeyValue);
        strMobile = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_MOBILE), myKeyValue);
        strAddress = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ADDRESS), myKeyValue);
        strCountryCode = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_COUNTRY_CODE), myKeyValue);
        strImage = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_IMAGE), myKeyValue);
        strCategory = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY), myKeyValue);
        strCategoryName = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY_NAME), myKeyValue);
        strStartTime = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_START_TIME), myKeyValue);
        strCloseTime = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CLOSE_TIME), myKeyValue);
        strBusinessType = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_BUSINESS_TYPE), myKeyValue);
        Log.e("appSample", "BusinessTypeProfile: " + strBusinessType);
        if(strStartTime == null) {
            strStartTime = "";
        }
        if(strCloseTime == null) {
            strCloseTime = "";
        }

        selectedId = Integer.parseInt(strCategory);
        strAndroidId  = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void initView() {
        startCalDate = Calendar.getInstance();
        prefCaldate = Calendar.getInstance();
        profile_change = findViewById(R.id.profile_change);
        business_category_layout = findViewById(R.id.business_category_layout);
        store_image = findViewById(R.id.store_image);
        profile_back = findViewById(R.id.profile_back);
        profile_store = findViewById(R.id.profile_store);
        business_category_et = findViewById(R.id.business_category_et);
        address_et = findViewById(R.id.address_et);
        start_time = findViewById(R.id.start_time);
        close_time = findViewById(R.id.close_time);
        profile_mobile_tv = findViewById(R.id.profile_mobile);
        update_btn = findViewById(R.id.update_btn);

        profile_mobile_tv.setFocusable(false);
        profile_mobile_tv.setClickable(false);
        business_category_et.setFocusable(false);
        business_category_et.setClickable(true); // false
        business_category_et.setOnClickListener(this);

        start_time.setFocusable(false);
        start_time.setClickable(true);
        close_time.setFocusable(false);
        close_time.setClickable(true);
        address_et.setFocusable(false);
        address_et.setClickable(true);

        profile_back.setOnClickListener(this);
        start_time.setOnClickListener(this);
        close_time.setOnClickListener(this);
        address_et.setOnClickListener(this);
        profile_change.setOnClickListener(this);
        update_btn.setOnClickListener(this);
        updateFields();
    }

    private void updateFields() {
        start_time.setText(strStartTime);
        close_time.setText(strCloseTime);
        address_et.setText(strAddress);
        profile_mobile_tv.setText(strCountryCode + " " + strMobile);
        business_category_et.setText(strCategoryName);
        profile_store.setText(strShop);
        if(strBusinessType.equalsIgnoreCase("2")) { // He is an Re-seller. So he won't update his business category.
            business_category_layout.setVisibility(View.GONE);
        } else {
            business_category_layout.setVisibility(View.VISIBLE);
        }

        if(strImage != null) {
            if(!strImage.equalsIgnoreCase("") ) {
                Glide.with(getApplicationContext()).load(strImage).into(store_image);
            }
        }
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

    public void startTimePickerDialog(int hour, int minute) {
        mTimePicker = new TimePickerDialog(Profile.this, new TimePickerDialog.OnTimeSetListener() {
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
        mTimePicker = new TimePickerDialog(Profile.this, new TimePickerDialog.OnTimeSetListener() {
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

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.business_category_et:
                if (NetworkAvailability.isNetworkAvailable(Profile.this)) {
                    hideKeyboard(business_category_et);
                    Log.e("appSample", "ID: " + selectedId);

                    Bundle bundle = new Bundle();
                    bundle.putString("mobileNumber", strCountryCode + strMobile);
                    bundle.putInt("SelectedId", selectedId);
                    Intent intent = new Intent(Profile.this, BusinessCategorySelection.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, ACCOUNT_FRAG_to_CATEGORY_SELECTION);
                } else {
                    NetworkAvailability networkAvailability = new NetworkAvailability(this);
                    networkAvailability.noInternetConnection(Profile.this, Constants.NETWORK_ENABLE_SETTINGS);
                }
                break;

            case R.id.close_img:
                bottomSheetUpload.dismiss();
                break;

            case R.id.camera_layout:
                bottomSheetUpload.dismiss();
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (android.os.Build.VERSION.SDK_INT >= 24) {
                    tempFile = Util.getOutputMediaFile();
                    fileUri = FileProvider.getUriForFile(Profile.this,
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

                if (ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Profile.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
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

            case R.id.profile_change:
                checkCameraPermission();
                break;

            case R.id.profile_back:
                finish();
                break;

            case R.id.address_et:
                if (NetworkAvailability.isNetworkAvailable(Profile.this)) {
                    Bundle addressbundle = new Bundle();
                    addressbundle.putString("SelectedLatitude", strLatitude);
                    addressbundle.putString("SelectedLongitude", strLongitude);

                    Intent addressIntent = new Intent(Profile.this, AddressSelection.class);
                    addressIntent.putExtras(addressbundle);
                    startActivityForResult(addressIntent, ACCOUNT_FRAG_to_ADDRESS_SELECTION);
                } else {
                    noInternetConnection(MOVE_TO_ADDRESS_SELECTION);
                }

                break;

            case R.id.update_btn:
                strShop = profile_store.getText().toString().trim();
                strCategoryName = business_category_et.getText().toString().trim();
                strAddress = address_et.getText().toString().trim();
                strStartTime = start_time.getText().toString().trim();
                strCloseTime = close_time.getText().toString().trim();

                /*boolean val = checktimings(strStartTime, strCloseTime);
                if (!val) {
                    ShowToastMessages.Toast(Profile.this, "Store timings are mis-matching.");
                    return;
                }*/

                if(strShop.equalsIgnoreCase("")) {
                    CommonMethods.Toast(Profile.this, "Please enter shop name.");
                    return;
                }

                if(strCategoryName.equalsIgnoreCase("")) {
                    CommonMethods.Toast(Profile.this, "Please select business category.");
                    return;
                }

                if(strAddress.equalsIgnoreCase("")) {
                    CommonMethods.Toast(Profile.this, "Please select address.");
                    return;
                }

                if(profile_mobile_tv.getText().toString().trim().equalsIgnoreCase("")) {
                    CommonMethods.Toast(Profile.this, "Please enter mobile number.");
                    return;
                }

                if(strStartTime.equalsIgnoreCase("")) {
                    CommonMethods.Toast(Profile.this, "Please select start time.");
                    return;
                }

                if(strCloseTime.equalsIgnoreCase("")) {
                    CommonMethods.Toast(Profile.this, "Please select close time.");
                    return;
                }

                Map<String, String> params = new HashMap<>();
                params.put("country_code", strCountryCode);
                params.put("mobile_number", strMobile);
                params.put("imie_no", strAndroidId);
                params.put("gcm_id", StorageDatas.getInstance().getFirebaseToken());
                params.put("latitude", strLatitude);
                params.put("longitude", strLongitude);
                params.put("shop", strShop);
                params.put("cat_id", ""+selectedId);
                params.put("address", strAddress);
                params.put("open_time", strStartTime);
                params.put("close_time", strCloseTime);
                params.put("business", strBusinessType);

                if(finalPath == null) {
                    Log.e("appSample", "Params: " + params.toString());
                    params.put("image", "");

                    if (NetworkAvailability.isNetworkAvailable(Profile.this)) {
                        registerViewModel.checkProfileUpdate(params);
                    } else {
                        noInternetConnection(PROFILE_UPD_WITHOUT_IMAGE);
                    }

                    registerViewModel.checkProfileUpdateLiveData().observe(this, new Observer<RegisterModel>() {
                        @Override
                        public void onChanged(RegisterModel registerModel) {
                            Log.e("appSample", "SHOP: " + registerModel.getSellerDetails().getStrRegshop());
                            if(registerModel.getStrModule().equalsIgnoreCase("login")) {
                                showProfileSuccess(registerModel);
                            }
                        }
                    });
                } else {
                    if (NetworkAvailability.isNetworkAvailable(Profile.this)) {
                        registerViewModel.checkRegister(finalPath, params);
                    } else {
                        noInternetConnection(PROFILE_UPD_WITH_IMAGE);
                    }

                    registerViewModel.checkRegisterLiveData().observe(this, new Observer<RegisterModel>() {
                        @Override
                        public void onChanged(RegisterModel registerModel) {
                            if(registerModel.getStrModule().equalsIgnoreCase("login")) {
                                showProfileSuccess(registerModel);
                            }
                        }
                    });
                }
                break;
        }
    }

    private void showProfileSuccess(RegisterModel registerModel) {
        Log.e("appSample", "Address: " + registerModel.getSellerDetails().getStrRegAddress());
        Log.e("appSample", "Category: " + registerModel.getSellerDetails().getStrRegshop());
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_ID, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegId(), myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_SHOP, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegshop(), myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_CATEGORY, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegCategoryID(), myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_CATEGORY_NAME, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegCategory(), myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_IMAGE, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegImage(), myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_ADDRESS, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegAddress(), myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_MOBILE, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegMobileNumber(), myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_COUNTRY_CODE, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegCountryCode(), myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_LATITUDE, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegLatitude(), myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_LONGITUDE, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegLongitude(), myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_START_TIME, TripleDes.getDESEncryptValue(strStartTime, myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_CLOSE_TIME, TripleDes.getDESEncryptValue(strCloseTime, myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_PRODUCTS_COUNT, TripleDes.getDESEncryptValue("0", myKeyValue) );

        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_ACC_HOLDER_NAME, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrRegAccountHolderName(), myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_ACC_NUMBER, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrAccountNumber(), myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_BANK_NAME, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrBankName(), myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_BUSINESS_TYPE, TripleDes.getDESEncryptValue(registerModel.getSellerDetails().getStrBusiness(), myKeyValue) );


        sweetAlertDialog = new SweetAlertDialog(Profile.this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Success...!");
        sweetAlertDialog.setContentText(getString(R.string.profile_updated_success));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sweetAlertDialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

                int file_size_two = Integer.parseInt(String.valueOf(finalPath.length() / 1024));
                Log.e("appSample", "GalleryPathSize: " + file_size_two);

                Uri compressUri = Uri.fromFile(finalPath);

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
                int file_size = Integer.parseInt(String.valueOf(finalPath.length() / 1024));
                Log.e("appSample", "TakenFileSize: " + file_size);

                Uri compressUri = Uri.fromFile(finalPath);

                Glide.with(getApplicationContext())
                        .load(compressUri)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(store_image);
            }
        }

        if(requestCode == PROFILE_UPD_WITHOUT_IMAGE) {

        } else if(requestCode == PROFILE_UPD_WITH_IMAGE) {

        }

        if(requestCode == ACCOUNT_FRAG_to_SETTINGs) {
            if(resultCode == LOGOUT) {
                Intent intent = new Intent(Profile.this, MobileLogin.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAffinity();
            }
        }

        /*if(requestCode == ACCOUNT_FRAG_to_CROP_SAMPLE_ACTIVITY) {
            if(resultCode == CROP_success) {
                strImage = "" + data.getStringExtra("FilePath");
                finalPath = new File(strImage);
                Uri compressUri = Uri.fromFile(finalPath);

                Glide.with(getApplicationContext())
                        .load(compressUri)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(store_image);
            }
        }*/

        if(requestCode == ACCOUNT_FRAG_to_CATEGORY_SELECTION) {
            if(resultCode == CATEGORY_SELECTED) {
//                hideKeyboard(store_name);
                strCategoryName = data.getStringExtra("SelectedCategory");
                selectedId = data.getIntExtra("SelectedId", 0);
                business_category_et.setText(strCategoryName);
            }
        }

        if(requestCode == ACCOUNT_FRAG_to_ADDRESS_SELECTION) {
            if(resultCode == ADDRESS_SELECTED) {
                strLatitude = data.getStringExtra("SelectedLatitude");
                strLongitude = data.getStringExtra("SelectedLongitude");
                strAddress = data.getStringExtra("SelectedAddress");
                address_et.setText(strAddress);
            }
        }
    }

    public void noInternetConnection(int requestCode) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Profile.this, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.no_internet));
        sweetAlertDialog.setContentText(getResources().getString(R.string.open_settings));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        sweetAlertDialog.setCancelText(getResources().getString(R.string.option_no));
        sweetAlertDialog.setConfirmText(getResources().getString(R.string.option_yes));
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), requestCode);
            }
        });
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
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

    public void checkCameraPermission() {
        if (PermissionManager.checkIsGreaterThanM()) {
            if (!PermissionManager.checkPermissionForReadExternalStorage(Profile.this) ||
                    !PermissionManager.checkPermissionForWriteExternalStorage(Profile.this) ||
                    !PermissionManager.checkPermissionForCamara(Profile.this)
            )
            {
                PermissionManager.requestPermissionForCamera(Profile.this);
            } else {
                openCamera();
            }

        } else {
            openCamera();
        }
    }

    public void openCamera() {
        if (!CameraUtils.isDeviceSupportCamera(getApplicationContext())) {
            CommonMethods.Toast(Profile.this, getString(R.string.device_dont_have_camera));
            return;
        }

        bottomSheetUpload.show();

        /*Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            tempFile = Util.getOutputMediaFile();
            fileUri = FileProvider.getUriForFile(Profile.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    tempFile);

            intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else {
            fileUri = Uri.fromFile(Util.getOutputMediaFile());
        }

        intent1.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent1, CAMERA_IMAGE);*/

        /*Intent intent = new Intent(Profile.this, CrapImageSample.class);
        startActivityForResult(intent, ACCOUNT_FRAG_to_CROP_SAMPLE_ACTIVITY);*/
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
                    CommonMethods.Toast(Profile.this, "This App required Location permission." +
                            "Please enable from app settings.");
                }
        }
    }

    public void showStartTime() {
        startCalDate = Calendar.getInstance();
        int mHour = startCalDate.get(Calendar.HOUR_OF_DAY);
        int mMinute = startCalDate.get(Calendar.MINUTE);
        Log.e("appSample", "Hour: " + mHour);
        Log.e("appSample", "Hour: " + mMinute);

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

                    String aTime = new StringBuilder().append(hour).append(':')
                            .append(min).append(" ").append(timeSet).toString();

                    start_time.setText(aTime);

                }
            }
        };

        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(Profile.this,
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

        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(Profile.this,
                myTimeListener,
                mHour, mMinute, false);

        timePickerDialog.setTitle("Choose hour:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
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
