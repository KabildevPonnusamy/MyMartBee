package com.mart.mymartbee.view.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.mart.mymartbee.BuildConfig;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.CustomTimePickerDialog;
import com.mart.mymartbee.commons.GPSTracker;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.model.Products_Model;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.view.AddAccount;
import com.mart.mymartbee.view.MobileLogin;
import com.mart.mymartbee.view.ProductDetails;
import com.mart.mymartbee.view.Profile;
import com.mart.mymartbee.view.SettingsAct;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment implements View.OnClickListener, Constants {

    ProgressDialog progressDialog;
    GPSTracker gpsTracker;
    String strCountryCode, strMobile, strAndroidId, strFCM, strShop,
            strCategory, strAddress, strImage, strCategoryName;
    String strLatitude = "", strLongitude= "", strOpenHour, strCloseHour, strBusinessType = "";

    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";

    ImageView store_image;
    TextView store_name, store_category, profile_change, version_number, store_available_hours;
    RelativeLayout settings_layout, help_support_layout, account_details_layout;
    LinearLayout logout_layout;//share_whatsapp_layout
//    TextView timeView;
    Calendar prefCaldate;
    int prefmHour, prefmMinute;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_account, container, false);

        getMyPreferences();
        initView(view);
        return view;
    }

    private void getMyPreferences() {
        myKeyValue = getActivity().getResources().getString(R.string.myTripleKey);
        preferenceDatas = new MyPreferenceDatas(getActivity());
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
        strBusinessType = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_BUSINESS_TYPE), myKeyValue);
    }

    public void initView(View view){
//        timeView = view.findViewById(R.id.timeView);
//        timeView.setOnClickListener(this);
        store_image = view.findViewById(R.id.store_image);
        store_name = view.findViewById(R.id.store_name);
        store_category = view.findViewById(R.id.store_category);
        store_available_hours = view.findViewById(R.id.store_available_hours);
        profile_change = view.findViewById(R.id.profile_change);
        version_number = view.findViewById(R.id.version_number);
        settings_layout = view.findViewById(R.id.settings_layout);
        help_support_layout = view.findViewById(R.id.help_support_layout);
        account_details_layout = view.findViewById(R.id.account_details_layout);
        logout_layout = view.findViewById(R.id.logout_layout);
        logout_layout.setOnClickListener(this);
        profile_change.setOnClickListener(this);
        settings_layout.setOnClickListener(this);
        help_support_layout.setOnClickListener(this);
        account_details_layout.setOnClickListener(this);
        updateValues();
    }

    public void updateValues() {
        version_number.setText( "MART" + BuildConfig.VERSION_NAME);
        store_name.setText(strShop);
        store_category.setText(strCategoryName);
        store_available_hours.setText("Open Hours: " + strOpenHour + " - " + strCloseHour);

        Log.e("appSample", "Businesstype: " + strBusinessType);
        if(strBusinessType.equalsIgnoreCase("2")) {
            store_category.setVisibility(View.GONE);
        } else {
            store_category.setVisibility(View.VISIBLE);
        }

        if(strImage != null) {
            if(!strImage.equalsIgnoreCase("") ) {
                Glide.with(getActivity()).load(strImage).into(store_image);
            }
        }
    }

    private void showProgress() {
        progressDialog = new ProgressDialog(getActivity());
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

    /*protected void showDialog(int hour, int minute) {
        Log.e("appSample", "onShowDialog");
        //return new
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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

                timeView.setText(aTime);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.timeView:
                prefCaldate = Calendar.getInstance();
                prefmHour = prefCaldate.get(Calendar.HOUR_OF_DAY);
                prefmMinute = prefCaldate.get(Calendar.MINUTE);

                showDialog(prefmHour, prefmMinute);
                break;*/

            case R.id.logout_layout:
                showLogoutDialog();

                break;

            case R.id.profile_change:
                Intent profileIntent = new Intent(getActivity(), Profile.class);
                startActivityForResult(profileIntent, ACCOUNT_FRAG_to_PROFILE);
                break;

            case R.id.settings_layout:
                Intent settingsIntent = new Intent(getActivity(), SettingsAct.class);
                startActivityForResult(settingsIntent, ACCOUNT_FRAG_to_SETTINGS);
                break;

            case R.id.help_support_layout:
                openAdminsWhatsapp();
                break;

            case R.id.account_details_layout:
                Intent accDetailsIntent = new Intent(getActivity(), AddAccount.class);
                startActivityForResult(accDetailsIntent, ACCOUNT_FRAG_to_ACCOUNT_DETAILS);
                break;
        }
    }

    public void showLogoutDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("Warning...!");
        sweetAlertDialog.setContentText("Do you want to Logout?");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setConfirmText("Yes");
        sweetAlertDialog.setCancelText("No");
        sweetAlertDialog.show();

        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                preferenceDatas.clearPreference(getActivity());
                Intent intent = new Intent(getActivity(), MobileLogin.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });

        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
    }

    public void openAdminsWhatsapp() {

        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+60167139800&text=" + "");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(sendIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACCOUNT_FRAG_to_PROFILE) {
            strShop = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_SHOP), myKeyValue);
            strImage = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_IMAGE), myKeyValue);
            strCategoryName = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CATEGORY_NAME), myKeyValue);
            strOpenHour = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_START_TIME), myKeyValue);
            strCloseHour = TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_CLOSE_TIME), myKeyValue);

            updateValues();
        }
    }
}
