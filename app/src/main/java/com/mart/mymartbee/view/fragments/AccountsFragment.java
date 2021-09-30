package com.mart.mymartbee.view.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.textview.MaterialTextView;
import com.iceteck.silicompressorr.SiliCompressor;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.commons.CameraUtils;
import com.mart.mymartbee.commons.GPSTracker;
import com.mart.mymartbee.commons.PathUtil;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.model.RegisterModel;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.view.AddressSelection;
import com.mart.mymartbee.view.CategorySelection;
import com.mart.mymartbee.view.CrapImageSample;
import com.mart.mymartbee.view.MobileLogin;
import com.mart.mymartbee.view.SettingsAct;
import com.mart.mymartbee.view.StoreCreation;
import com.mart.mymartbee.viewmodel.implementor.RegisterViewModelImpl;
import com.mart.mymartbee.viewmodel.interfaces.RegisterViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountsFragment extends Fragment implements View.OnClickListener, Constants {

    ProgressDialog progressDialog;
    File finalPath;

    String strCountryCode, strMobile, strAndroidId, strFCM, strShop,
            strCategory, strAddress, strImage, strCategoryName;
    String strLatitude = "", strLongitude= "";
    GPSTracker gpsTracker;
    String profile_str = "";

    CircleImageView store_image;
    ImageView profile_settings;
    EditText profile_store;
    TextView profile_change;
    MaterialTextView profile_category_tv, profile_address_tv, profile_mobile_tv;
    LinearLayout category_profile_ll, address_profile_ll;
    Button update_btn;
    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";
    String sel_category = "";
    int selectedId = -1;
    RegisterViewModel registerViewModel;
    SweetAlertDialog sweetAlertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_accounts, container, false);

        registerViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModelImpl.class);
        getMyPreferences();
        initView(view);
        observeProgress(view);
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

        selectedId = Integer.parseInt(strCategory);
        strAndroidId  = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private  void initView(View view) {
        profile_change = view.findViewById(R.id.profile_change);
        store_image = view.findViewById(R.id.store_image);
        profile_settings = view.findViewById(R.id.profile_settings);
        profile_store = view.findViewById(R.id.profile_store);
        profile_category_tv = view.findViewById(R.id.profile_category);
        profile_address_tv = view.findViewById(R.id.profile_address);
        profile_mobile_tv = view.findViewById(R.id.profile_mobile);
        category_profile_ll = view.findViewById(R.id.category_profile);
        address_profile_ll = view.findViewById(R.id.address_profile);
        update_btn = view.findViewById(R.id.update_btn);
        profile_settings.setOnClickListener(this);
        category_profile_ll.setOnClickListener(this);
        address_profile_ll.setOnClickListener(this);
        profile_change.setOnClickListener(this);
        update_btn.setOnClickListener(this);
        updateFields();
    }

    private void updateFields() {
        profile_address_tv.setText(strAddress);
        profile_mobile_tv.setText(strCountryCode + " " + strMobile);
        profile_category_tv.setText(strCategoryName);
        profile_store.setText(strShop);
        if(strImage != null) {
            if(!strImage.equalsIgnoreCase("") ) {
                 Glide.with(getActivity()).load(strImage).into(store_image);

            }
        }
    }

    private void observeProgress(View view) {
        registerViewModel.progressbarObservable().observe(getActivity(), new Observer<Boolean>() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_change:
                if (!CameraUtils.isDeviceSupportCamera(getActivity())) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.device_dont_have_camera),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), CrapImageSample.class);
                startActivityForResult(intent, ACCOUNT_FRAG_to_CROP_SAMPLE_ACTIVITY);
                break;

            case R.id.profile_settings:
                Intent settingsIntent = new Intent(getActivity(), SettingsAct.class);
                startActivityForResult(settingsIntent, ACCOUNT_FRAG_to_SETTINGs);
                break;

            case R.id.category_profile:
                Bundle catebundle = new Bundle();
                catebundle.putString("mobileNumber", strCountryCode + strMobile);
                catebundle.putInt("SelectedId", selectedId);

                Intent cateIntent = new Intent(getActivity(), CategorySelection.class);
                cateIntent.putExtras(catebundle);
                startActivityForResult(cateIntent, ACCOUNT_FRAG_to_CATEGORY_SELECTION);
                break;

            case R.id.address_profile:
                Bundle addressbundle = new Bundle();
                addressbundle.putString("SelectedLatitude", strLatitude);
                addressbundle.putString("SelectedLongitude", strLongitude);

                Intent addressIntent = new Intent(getActivity(), AddressSelection.class);
                addressIntent.putExtras(addressbundle);
                startActivityForResult(addressIntent, ACCOUNT_FRAG_to_ADDRESS_SELECTION);
                break;

            case R.id.update_btn:
                strShop = profile_store.getText().toString().trim();
                strCategoryName = profile_category_tv.getText().toString().trim();
                strAddress = profile_address_tv.getText().toString().trim();
                if(strShop.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter shop name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strCategoryName.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please select category", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strAddress.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please select address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(profile_mobile_tv.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter mobile number", Toast.LENGTH_SHORT).show();
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

                if(finalPath == null) {
                    Log.e("appSample", "Params: " + params.toString());
                    params.put("image", "");
                    registerViewModel.checkProfileUpdate(params);
                    registerViewModel.checkProfileUpdateLiveData().observe(getActivity(), new Observer<RegisterModel>() {
                        @Override
                        public void onChanged(RegisterModel registerModel) {
//                            Log.e("appSample", "RegModel: " + registerModel.getSellerDetails().getStrRegshop());
                            if(registerModel.getStrModule().equalsIgnoreCase("login")) {
                                registerViewModel.checkProfileUpdateLiveData().removeObservers(getActivity());
                                showProfileSuccess(registerModel);
                            }
                        }
                    });
                } else {
                    registerViewModel.checkRegister(finalPath, params);
                    registerViewModel.checkRegisterLiveData().observe(getActivity(), new Observer<RegisterModel>() {
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
        preferenceDatas.putPrefString(MyPreferenceDatas.SELLER_PRODUCTS_COUNT, TripleDes.getDESEncryptValue("0", myKeyValue) );

        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Success...!");
        sweetAlertDialog.setContentText(getActivity().getString(R.string.profile_updated_success));
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
        Log.e("appSample", "onActivityResult");

        if(requestCode == ACCOUNT_FRAG_to_SETTINGs) {
            if(resultCode == LOGOUT) {
                Intent intent = new Intent(getActivity(), MobileLogin.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        }

        if(requestCode == ACCOUNT_FRAG_to_CROP_SAMPLE_ACTIVITY) {
            if(resultCode == CROP_success) {
                strImage = "" + data.getStringExtra("FilePath");
                finalPath = new File(strImage);
                Uri compressUri = Uri.fromFile(finalPath);

                Glide.with(getActivity())
                        .load(compressUri)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(store_image);
            }
        }

        if(requestCode == ACCOUNT_FRAG_to_CATEGORY_SELECTION) {
            if(resultCode == CATEGORY_SELECTED) {
//                hideKeyboard(store_name);
                strCategoryName = data.getStringExtra("SelectedCategory");
                selectedId = data.getIntExtra("SelectedId", 0);
                profile_category_tv.setText(strCategoryName);
            }
        }

        if(requestCode == ACCOUNT_FRAG_to_ADDRESS_SELECTION) {
            if(resultCode == ADDRESS_SELECTED) {
                strLatitude = data.getStringExtra("SelectedLatitude");
                strLongitude = data.getStringExtra("SelectedLongitude");
                strAddress = data.getStringExtra("SelectedAddress");
                profile_address_tv.setText(strAddress);
            }
        }
    }

    private void showProgress() {
        Log.e("appSample", "showProgress");
        progressDialog = new ProgressDialog(getActivity());
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
}
