package com.mart.mymartbee.view.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.mart.mymartbee.BuildConfig;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.GPSTracker;
import com.mart.mymartbee.commons.CommonMethods;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.view.MobileLogin;
import com.mart.mymartbee.view.Profile;
import com.mart.mymartbee.view.SettingsAct;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment implements View.OnClickListener, Constants {

    ProgressDialog progressDialog;
    GPSTracker gpsTracker;
    String strCountryCode, strMobile, strAndroidId, strFCM, strShop,
            strCategory, strAddress, strImage, strCategoryName;
    String strLatitude = "", strLongitude= "", strOpenHour, strCloseHour;

    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";

    CircleImageView store_image;
    TextView store_name, store_category, profile_change, version_number, store_available_hours;
    RelativeLayout settings_layout, help_support_layout;
    LinearLayout logout_layout;//share_whatsapp_layout


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
    }

    public void initView(View view){
        store_image = view.findViewById(R.id.store_image);
        store_name = view.findViewById(R.id.store_name);
        store_category = view.findViewById(R.id.store_category);
        store_available_hours = view.findViewById(R.id.store_available_hours);
        profile_change = view.findViewById(R.id.profile_change);
        version_number = view.findViewById(R.id.version_number);
        settings_layout = view.findViewById(R.id.settings_layout);
        help_support_layout = view.findViewById(R.id.help_support_layout);
        logout_layout = view.findViewById(R.id.logout_layout);
        logout_layout.setOnClickListener(this);
        profile_change.setOnClickListener(this);
        settings_layout.setOnClickListener(this);
        help_support_layout.setOnClickListener(this);
        updateValues();
    }

    public void updateValues() {
        version_number.setText( "MART" + BuildConfig.VERSION_NAME);
        store_name.setText(strShop);
        store_category.setText(strCategoryName);
        store_available_hours.setText("Open Hours: " + strOpenHour + " - " + strCloseHour);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_layout:
                preferenceDatas.clearPreference(getActivity());
                Intent intent = new Intent(getActivity(), MobileLogin.class);
                startActivity(intent);
                getActivity().finishAffinity();
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
        }
    }

    public void openAdminsWhatsapp() {
        String contact = "+60 167139800";
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = getContext().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            CommonMethods.Toast(getActivity(), "WhatsApp have not been installed.");
        }
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
