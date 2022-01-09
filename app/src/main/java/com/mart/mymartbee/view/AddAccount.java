package com.mart.mymartbee.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.commons.GPSTracker;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.model.RegisterModel;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.viewmodel.implementor.RegisterViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.RegisterViewModel;

import java.util.HashMap;
import java.util.Map;

public class AddAccount extends AppCompatActivity implements View.OnClickListener, Constants {

    ImageView acc_details_back;
    EditText acc_holder_name, acc_number, bank_name;
    LinearLayout save_layout;
    String strAccHolderName = "";
    String strAccNumber = "";
    String strBankName = "";

    String strCountryCode, strMobile, strAndroidId, strShop,
            strCategory, strAddress, strImage, strCategoryName;
    String strLatitude = "", strLongitude= "", strOpenHour, strCloseHour;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    ProgressDialog progressDialog;
    GPSTracker gpsTracker;
    RegisterViewModel registerViewModel;
    SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account_details);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModelImpl.class);
        observeProgress();
        getMyPreferences();
        initView();

    }

    private void getMyPreferences() {
        myKeyValue = getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(AddAccount.this);
        strShop = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_SHOP), myKeyValue);
        strLatitude = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_LATITUDE), myKeyValue);
        strLongitude = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_LONGITUDE), myKeyValue);
        strMobile = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_MOBILE), myKeyValue);
        strAddress = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ADDRESS), myKeyValue);
        strCountryCode = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_COUNTRY_CODE), myKeyValue);
        strImage = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_IMAGE), myKeyValue);
        strCategory = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY), myKeyValue);
        strCategoryName = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY_NAME), myKeyValue);
        strOpenHour = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_START_TIME), myKeyValue);
        strCloseHour = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CLOSE_TIME), myKeyValue);

        strAndroidId  = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        strAccHolderName = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ACC_HOLDER_NAME), myKeyValue);
        strAccNumber = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_ACC_NUMBER), myKeyValue);
        strBankName = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_BANK_NAME), myKeyValue);
    }

    public void initView() {
        acc_details_back = findViewById(R.id.acc_details_back);
        acc_holder_name = findViewById(R.id.acc_holder_name);
        acc_number = findViewById(R.id.acc_number);
        bank_name = findViewById(R.id.bank_name);
        save_layout = findViewById(R.id.save_layout);
        acc_details_back.setOnClickListener(this);
        save_layout.setOnClickListener(this);

        if(strAccHolderName != null && !strAccHolderName.equalsIgnoreCase("")) {
            acc_holder_name.setText(strAccHolderName);
        }

        if(strAccNumber != null && !strAccNumber.equalsIgnoreCase("")) {
            acc_number.setText(strAccNumber);
        }

        if(strBankName != null && !strBankName.equalsIgnoreCase("")) {
            bank_name.setText(strBankName);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acc_details_back:
                finish();
                break;

            case R.id.save_layout:
                strAccHolderName = acc_holder_name.getText().toString().trim();
                strAccNumber = acc_number.getText().toString().trim();
                strBankName = bank_name.getText().toString().trim();

                if(strAccHolderName.equalsIgnoreCase("")) {
                    CommonMethods.Toast(AddAccount.this, "Please enter Account Holder Name.");
                    return;
                }

                if(strAccNumber.equalsIgnoreCase("")) {
                    CommonMethods.Toast(AddAccount.this, "Please enter Account Number.");
                    return;
                }

                if(strBankName.equalsIgnoreCase("")) {
                    CommonMethods.Toast(AddAccount.this, "Please enter Bank name.");
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
                params.put("cat_id", ""+ strCategory);
                params.put("address", strAddress);
                params.put("open_time", strOpenHour);
                params.put("close_time", strCloseHour);
                params.put("image", "");

                params.put("acc_holder_name", strAccHolderName);
                params.put("acc_no", strAccNumber);
                params.put("bank_name", strBankName);

                Log.e("appSample", "Params: " + params.toString());

                if (NetworkAvailability.isNetworkAvailable(AddAccount.this)) {
                    registerViewModel.checkProfileUpdate(params);
                } else {
                    noInternetConnection(PROFILE_UPD_WITHOUT_IMAGE);
                }

                registerViewModel.checkProfileUpdateLiveData().observe(this, new Observer<RegisterModel>() {
                    @Override
                    public void onChanged(RegisterModel registerModel) {
                        Log.e("appSample", "SHOP: " + registerModel.getSellerDetails().getStrRegshop());

                        if(registerModel.isStrStatus() == false) {
                            showErrorMessage(registerModel.getStrMessage());
                        } else {
                            if(registerModel.getStrModule().equalsIgnoreCase("login")) {
                                showProfileSuccess(registerModel);
                            }
                        }
                    }
                });

                break;
        }
    }

    public void showErrorMessage(String errMessage) {
        CommonMethods.Toast(AddAccount.this,  errMessage);
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
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_START_TIME, TripleDes.getDESEncryptValue(strOpenHour, myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_CLOSE_TIME, TripleDes.getDESEncryptValue(strCloseHour, myKeyValue) );

        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_ACC_HOLDER_NAME, TripleDes.getDESEncryptValue(strAccHolderName, myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_ACC_NUMBER, TripleDes.getDESEncryptValue(strAccNumber, myKeyValue) );
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_BANK_NAME, TripleDes.getDESEncryptValue(strBankName, myKeyValue) );

        sweetAlertDialog = new SweetAlertDialog(AddAccount.this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Success...!");
        sweetAlertDialog.setContentText(getString(R.string.profile_updated_success));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sweetAlertDialog.dismiss();
                finish();
            }
        });
    }

    public void noInternetConnection(int requestCode) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AddAccount.this, SweetAlertDialog.ERROR_TYPE);
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
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
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

}
