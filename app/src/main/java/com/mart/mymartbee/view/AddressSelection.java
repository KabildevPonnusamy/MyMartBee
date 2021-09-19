package com.mart.mymartbee.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.mart.mymartbee.R;
import com.mart.mymartbee.commons.GPSTracker;
import com.mart.mymartbee.constants.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressSelection extends AppCompatActivity implements View.OnClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Constants {

    Button select_btn;
    ImageView address_back;
    TextView your_location;

    public static Double lat, lon;
    private GoogleMap mMap;
    Marker mCurrLocationMarker;
    SupportMapFragment mapFragment;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    GPSTracker gpsTracker;
    String S_lat = "", S_lon = "";
    String strLoc = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_selection);

        getBundles();
        initView();
        locationInit();
    }

    private void getBundles() {
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("SelectedLatitude").equalsIgnoreCase("")) {
        } else {
            S_lat = bundle.getString("SelectedLatitude");
            S_lon = bundle.getString("SelectedLongitude");
        }
    }

    public void initView(){
        select_btn = findViewById(R.id.select_btn);
        address_back = findViewById(R.id.address_back);
        your_location = findViewById(R.id.your_location);
        select_btn.setOnClickListener(this);
        address_back.setOnClickListener(this);
    }

    private void locationInit() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_btn:
                strLoc = your_location.getText().toString().trim();
                if(strLoc.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please pick location", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*Log.e("appSample", "SelectedLat: " + lat);
                Log.e("appSample", "SelectedLon: " + lon);*/

                Intent intent = new Intent();
                intent.putExtra("SelectedLatitude", "" + lat);
                intent.putExtra("SelectedLongitude", "" + lon);
                intent.putExtra("SelectedAddress", strLoc);
                setResult(ADDRESS_SELECTED, intent);
                finish();
                break;

            case R.id.address_back:
                finish();
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try{
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        LatLng latLng = null;

        if(!S_lat.equalsIgnoreCase("")) {
            latLng = new LatLng(Double.parseDouble(S_lat), Double.parseDouble(S_lon));
        } else {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        }

//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onCameraIdle() {
        LatLng center = mMap.getCameraPosition().target;

        lat = mMap.getCameraPosition().target.latitude;
        lon = mMap.getCameraPosition().target.longitude;

        getAddress(getApplicationContext(), lat, lon);
    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        mMap = map;
        gpsTracker = new GPSTracker(AddressSelection.this);
        if(!S_lat.equalsIgnoreCase("")) {

            lat = Double.parseDouble(S_lat);
            lon = Double.parseDouble(S_lon);
        } else {
            lat = gpsTracker.getLatitude();
            lon = gpsTracker.getLongitude();
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);

        /*LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(lat, lon));
        mMap.setLatLngBoundsForCameraTarget(builder.build());*/
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                your_location.setText( String.valueOf(address));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragment.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapFragment.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }
}
