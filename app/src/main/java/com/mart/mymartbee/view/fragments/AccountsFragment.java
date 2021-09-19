package com.mart.mymartbee.view.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;
import com.mart.mymartbee.R;
import com.mart.mymartbee.commons.GPSTracker;
import com.mart.mymartbee.constants.Constants;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountsFragment extends Fragment implements View.OnClickListener, Constants {

    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";
    private static final int REQUEST_CAMERA_ACCESS_PERMISSION =5674;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 5675;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    ProgressDialog progressDialog;
    File tempPath, finalPath;

    String strCountryCode, strMobile, strAndroidId, strFCM, strLatitude, strLongitude, strShop,
            strCategory, strAddress, strImage;
    GPSTracker gpsTracker;
    String profile_str = "";

    CircleImageView store_image;
    ImageView profile_back;
    TextView profile_store;
    MaterialTextView profile_category, profile_address;
    LinearLayout category_profile, address_profile;
    Button update_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_accounts, container, false);

        initView(view);
        return view;
    }

    private  void initView(View view) {
        store_image = view.findViewById(R.id.store_image);
        profile_back = view.findViewById(R.id.profile_back);
        profile_store = view.findViewById(R.id.profile_store);
        profile_category = view.findViewById(R.id.profile_category);
        profile_address = view.findViewById(R.id.profile_address);
        category_profile = view.findViewById(R.id.category_profile);
        address_profile = view.findViewById(R.id.address_profile);
        update_btn = view.findViewById(R.id.update_btn);
        profile_back.setOnClickListener(this);
        category_profile.setOnClickListener(this);
        address_profile.setOnClickListener(this);
        update_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_back:
                break;
            case R.id.category_profile:
                break;
            case R.id.address_profile:
                break;
            case R.id.update_btn:
                break;
        }
    }
}
