package com.mart.mymartbee.commons;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * Zco Engineering Dept.
 */
public class PermissionManager {

    public static final int ALL_CAMERS_PERMISSION_REQUEST_CODE = 6;
    public static final int ALL_PERMISSION_REQUEST_CODE = 1;

    public static final int EXTERNAL_STORAGE_WRITE_PERMISSION_REQUEST_CODE = 2;
    public static final int EXTERNAL_STORAGE_READ_PERMISSION_REQUEST_CODE = 3;
    public static final int CAMERA_REQUEST_CODE = 4;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 5;
    public static final int READ_CONTACTS_REQUEST_CODE = 7;
    private static final String TAG = "Permission";


    /**
     * Check Permission
     *
     * @return
     */
    public static boolean checkIsGreaterThanM() {
        if (Build.VERSION.SDK_INT >= 23) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForWriteExternalStorage(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForReadExternalStorage(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForReadPhoneState(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForCamara(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean checkPermissionForLocation(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForCoarseLocation(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForReadContacts(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForFineLocation(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request Permission
     */

    public static void requestPermissionForWriteExternalStorage(Fragment fragment) {
        fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                EXTERNAL_STORAGE_WRITE_PERMISSION_REQUEST_CODE);
    }

    public static void requestPermissionForReadExternalStorage(Fragment fragment) {
        fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_READ_PERMISSION_REQUEST_CODE);
    }
    public static void requestPermissionForLocation(Fragment fragment) {
        fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    public static void requestPermissionForContacts(Fragment fragment) {
        fragment.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_REQUEST_CODE);
    }

    public static void requestPermissionForAll(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CAMERA}, ALL_PERMISSION_REQUEST_CODE);
    }

    public static void requestPermissionForPhoneandLocationandContacts(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS,
        }, ALL_PERMISSION_REQUEST_CODE);
    }

    public static void requestPermissionForCall(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.CALL_PHONE,}, ALL_PERMISSION_REQUEST_CODE);
    }

    public static void requestPermissionForCamera(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,}, ALL_CAMERS_PERMISSION_REQUEST_CODE);
    }
    /**
     * Is permission rationale
     * Used to Check If the permission is not in the application and user decline the popup previously.
     */

    public boolean isExternalStorageWritePermissionRationale(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageReadPermissionRationale(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return true;
        }
        return false;
    }
    public static boolean isLocationPermissionRationale(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            return true;
        }
        return false;
    }

    public static boolean isContactPermissionRationale(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)) {
            return true;
        }
        return false;
    }

}
