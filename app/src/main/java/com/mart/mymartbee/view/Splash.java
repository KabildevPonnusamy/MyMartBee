package com.mart.mymartbee.view;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mart.mymartbee.R;
import com.mart.mymartbee.algorithm.TripleDes;
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.custom.NetworkAvailability;
import com.mart.mymartbee.custom.SweetAlert.SweetAlertDialog;
import com.mart.mymartbee.storage.MyPreferenceDatas;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.commons.PermissionManager;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Splash extends AppCompatActivity implements Constants{

    MyPreferenceDatas preferenceDatas;
    String myKeyValue = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_layout);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                String token = task.getResult();
                Log.e("appSample", "fcm token : "+token);
                StorageDatas.getInstance().setFirebaseToken(token);
            }
        });

        checkFolderPermission();
    }

    private void checkFolderPermission() {
        if (PermissionManager.checkIsGreaterThanM()) {
            if (!PermissionManager.checkPermissionForReadPhoneState(Splash.this) ||
                    !PermissionManager.checkPermissionForCoarseLocation(Splash.this) ||
                    !PermissionManager.checkPermissionForReadContacts(Splash.this)) {
                PermissionManager.requestPermissionForPhoneandLocationandContacts(Splash.this);
            } else {
                launchApp();
            }
        } else {
            launchApp();
        }
    }

    public String contactExists(Context context, String number) {
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection,
                null, null, null);
        String phoneUserName = "";

        try {
            if (cur.moveToFirst()) {
                phoneUserName = cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                cur.close();
                return phoneUserName;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return phoneUserName;
    }

    public void launchApp() {

        String userName = contactExists(getApplicationContext(), "00000");
        Log.e("appSample", "Username: " + userName);

        if (NetworkAvailability.isNetworkAvailable(Splash.this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        myKeyValue = getResources().getString(R.string.myTripleKey);
                        preferenceDatas = new MyPreferenceDatas(Splash.this);

                        if(TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_MOBILE), myKeyValue) != null &&
                                !TripleDes.getDESDecryptValue(preferenceDatas.getPrefString(MyPreferenceDatas.SELLER_MOBILE), myKeyValue).equalsIgnoreCase("")) {
                            Intent intent= new Intent(Splash.this, HomeActivity.class); // Map_Activity
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent= new Intent(Splash.this, MobileLogin.class); // MobileLogin
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                    }
                }
            }, Constants.SPLASH_TIME_OUT);
        } else {
            NetworkAvailability networkAvailability = new NetworkAvailability(this);
            networkAvailability.noInternetConnection(Splash.this, Constants.NETWORK_ENABLE_SETTINGS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionManager.ALL_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    launchApp();
                } else {
                    finish();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NETWORK_ENABLE_SETTINGS) {
            launchApp();
        }
    }
}
