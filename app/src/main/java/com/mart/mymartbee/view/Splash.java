package com.mart.mymartbee.view;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
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
import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.storage.StorageDatas;
import com.mart.mymartbee.commons.PermissionManager;

public class Splash extends AppCompatActivity {

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
            if (!PermissionManager.checkPermissionForReadExternalStorage(Splash.this) ||
                    !PermissionManager.checkPermissionForWriteExternalStorage(Splash.this) ||
                    !PermissionManager.checkPermissionForReadPhoneState(Splash.this) ||
                    !PermissionManager.checkPermissionForCoarseLocation(Splash.this) ||
                    !PermissionManager.checkPermissionForCamara(Splash.this)) {
                PermissionManager.requestPermissionForAll(Splash.this);
            } else {
                launchApp();
            }
        } else {
            launchApp();
        }
    }

    public void launchApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {

                    Intent intent= new Intent(Splash.this, MobileLogin.class); //MobileLogin, StoreCreation
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                }
            }
        }, Constants.SPLASH_TIME_OUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionManager.ALL_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED
                        && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                    launchApp();
                } else {
                    Log.e("appSample", "Reaches");
                    finish();
                }
        }
    }

}
