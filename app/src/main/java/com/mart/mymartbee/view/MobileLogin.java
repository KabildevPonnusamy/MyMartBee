package com.mart.mymartbee.view;

import android.animation.Animator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.model.OTPModel;
import com.mart.mymartbee.model.OTPVerifyModel;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.viewmodel.implementor.OTPViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.OTPViewModel;

import java.util.HashMap;
import java.util.Map;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class MobileLogin extends AppCompatActivity implements View.OnClickListener, Constants {

    EditText mobile_edit;
    String mobileStr = "", otpStr = "";
    private OtpTextView otp_view;
    TextView resend_otp, otp_seconds, otp_mobile;
    Button verify_btn, submit_btn;
    BottomSheetDialog bottomSheetDialog;
    OTPViewModel otpViewModel;
    ProgressDialog progressDialog;
    SweetAlertDialog sweetAlertDialog;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobilelayout);

        otpViewModel = ViewModelProviders.of(this).get(OTPViewModelImpl.class);
        getLifecycle().addObserver(otpViewModel);
        initViews();
        observeProgress();
    }

    public void observeProgress() {
        otpViewModel.progressbarOTPObservable().observe(this, new Observer<Boolean>() {
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

    public void initViews() {
        myKeyValue = getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(MobileLogin.this);
        mobile_edit = findViewById(R.id.mobile_edit);

        submit_btn = (Button) findViewById(R.id.submit_btn);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.otp_bottomsheet);
        bottomSheetDialog.setCancelable(false);

        otp_view = (OtpTextView)bottomSheetDialog.findViewById(R.id.otp_view);
        resend_otp = (TextView) bottomSheetDialog.findViewById(R.id.resend_otp);
        otp_seconds = (TextView) bottomSheetDialog.findViewById(R.id.otp_seconds);
        verify_btn = (Button) bottomSheetDialog.findViewById(R.id.verify_btn);
        otp_mobile = (TextView) bottomSheetDialog.findViewById(R.id.otp_mobile);

        submit_btn.setOnClickListener(this);
        verify_btn.setOnClickListener(this);
        resend_otp.setOnClickListener(this);
        setListener();
    }

    public void setListener() {
        otpViewModel.getPendingTime().observe(MobileLogin.this, new Observer<String>() {
            @Override
            public void onChanged(String countString) {
                if(!countString.equalsIgnoreCase("DONE")) {
                    otp_seconds.setText(" in " + countString);
                } else if(countString.equalsIgnoreCase("DONE")) {
//                    otpViewModel.getPendingTime().removeObservers(MobileLogin.this);
                    Log.e("appSample", "DONE");
                    otp_seconds.setVisibility(View.GONE);
                    enableResendBtn();
                } else {
                    Log.e("appSample", "Else");
                }
            }
        });

        otp_view.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
            }
            @Override
            public void onOTPComplete(String otp) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(otp_view.getWindowToken(), 0);
                Log.e("appSample", "Completed: " + otp);
                otpStr = "" + otp;

                verifyOTP();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resend_otp:
                sendOTP();
                break;

            case R.id.verify_btn:
                if(otpStr.equalsIgnoreCase("")) {
                    Toast.makeText(MobileLogin.this, "Please enter OTP!", Toast.LENGTH_SHORT).show();
                    return;
                }
                verifyOTP();
                break;

            case R.id.submit_btn:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);

                mobileStr = mobile_edit.getText().toString().trim();
                if(mobileStr.length() < 9) {
                    Toast.makeText(MobileLogin.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendOTP();

                break;
        }
    }

    private void sendOTP() {
        getLifecycle().addObserver(otpViewModel);
        otpViewModel.getOTP("+60", mobileStr);
        otpViewModel.getOTPLiveData().observe(MobileLogin.this, new Observer<OTPModel>() {
            @Override
            public void onChanged(OTPModel otpModel) {
                Log.e("appSample", "OTP: " + otpModel.getStrOtp());
                otp_mobile.setText( "Please enter OTP sent to " + "+60 " + mobileStr);
                disableResendBtn();
                bottomSheetDialog.show();

                otp_seconds.setVisibility(View.VISIBLE);

                otpViewModel.getOTPLiveData().removeObservers(MobileLogin.this);

                otpViewModel.startCounter();
            }
        });
    }

    public void verifyOTP() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("country_code", "+60");
        params.put("mobile_number", mobileStr);
        params.put("otp", otpStr);
        params.put("gcm_id", StorageDatas.getInstance().getFirebaseToken());

        otpViewModel.verifyOTP(params);
        otpViewModel.verifyOTPLiveData().observe(MobileLogin.this, new Observer<OTPVerifyModel>() {
            @Override
            public void onChanged(OTPVerifyModel otpVerifyModel) {
                bottomSheetDialog.dismiss();
                otpViewModel.stopCounter();
                String mobileNum = "";
                mobileNum = "" + mobileStr;

                Log.e("appSample", "Status: " + otpVerifyModel.isStrStatus());
                mobileStr = "";
                mobile_edit.setText(mobileStr);
                otp_view.setOTP("");

                if(otpVerifyModel.isStrStatus() == true) {
                    if(otpVerifyModel.getStrModule().equalsIgnoreCase("login")) {

                        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_ID, TripleDes.getDESEncryptValue(otpVerifyModel.getSellerDetails().getStrRegId(), myKeyValue) );
                        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_SHOP, TripleDes.getDESEncryptValue(otpVerifyModel.getSellerDetails().getStrRegshop(), myKeyValue) );
                        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_CATEGORY, TripleDes.getDESEncryptValue(otpVerifyModel.getSellerDetails().getStrRegCategory(), myKeyValue) );
                        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_IMAGE, TripleDes.getDESEncryptValue(otpVerifyModel.getSellerDetails().getStrRegImage(), myKeyValue) );
                        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_ADDRESS, TripleDes.getDESEncryptValue(otpVerifyModel.getSellerDetails().getStrRegAddress(), myKeyValue) );
                        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_MOBILE, TripleDes.getDESEncryptValue(otpVerifyModel.getSellerDetails().getStrRegMobileNumber(), myKeyValue) );
                        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_LATITUDE, TripleDes.getDESEncryptValue(otpVerifyModel.getSellerDetails().getStrRegLatitude(), myKeyValue) );
                        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_LONGITUDE, TripleDes.getDESEncryptValue(otpVerifyModel.getSellerDetails().getStrRegLongitude(), myKeyValue) );
                        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_PRODUCTS_COUNT, TripleDes.getDESEncryptValue("0", myKeyValue) );

                        Intent homeIntent = new Intent(MobileLogin.this, HomeActivity.class);
                        startActivityForResult(homeIntent, MOBILE_LOGIN_to_HOME);
                        finish();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("CountryCode", "+60");
                        bundle.putString("MobileNumber", mobileNum);
                        Intent intent = new Intent(MobileLogin.this, StoreCreation.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, MOBILELOGIN_to_STORECREATION);
                        finish();
                        Log.e("appSample", "MoveToRegister");
                    }
                } else {
                    showErrorMessage(otpVerifyModel.getStrMessage());
                }

            }
        });
    }

    private void showErrorMessage(String errMsg) {
        sweetAlertDialog = new SweetAlertDialog(MobileLogin.this, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText("Error...!");
        sweetAlertDialog.setContentText(errMsg);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        otpViewModel.verifyOTPLiveData().removeObservers(this);
        otpViewModel.getOTPLiveData().removeObservers(this);
        otpViewModel.getErrorData().removeObservers(this);
        otpViewModel.getPendingTime().removeObservers(this);
        getLifecycle().removeObserver(otpViewModel);
    }

    public void enableVerifyBtn() {
        verify_btn.setEnabled(true);
        verify_btn.setClickable(true);
        verify_btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        verify_btn.setBackgroundColor(R.color.yellow_btn_color);
    }

    public void disableVerifyBtn(){
        verify_btn.setEnabled(false);
        verify_btn.setClickable(false);
        verify_btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        verify_btn.setBackgroundColor(R.color.gray_btn_bg_pressed_color);
    }

    public void enableResendBtn() {
        resend_otp.setClickable(true);
        resend_otp.setEnabled(true);
        resend_otp.setTextColor(getResources().getColor(R.color.red));
    }

    public void disableResendBtn() {
        resend_otp.setClickable(false);
        resend_otp.setEnabled(false);
        resend_otp.setTextColor(getResources().getColor(R.color.gray_btn_bg_pressed_color));
    }

}
