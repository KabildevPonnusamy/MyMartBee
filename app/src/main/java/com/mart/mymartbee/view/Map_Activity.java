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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.mart.mymartbee.commons.GPSTracker;
import com.mart.mymartbee.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Map_Activity extends FragmentActivity implements View.OnClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    ImageView pdetails_back;
    TextView your_loc, map_skip;
    Button next_btn;
    public static Double lat, lon;
    private GoogleMap mMap;
    Marker mCurrLocationMarker;

    SupportMapFragment mapFragment;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    GPSTracker gpsTracker;
    String Loc_address, loc_lat = "", loc_lng = "";
    LatLng loc;
    AutocompleteSupportFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_example);

        init_View();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.onResume();

        // Initialize Places.
        Places.initialize(Map_Activity.this, "AIzaSyBPWwp0IDRoAsxGIgqv8q85tKibxVeWTF4");
        // AIzaSyB4CPqqhVMPCH9VEOBtMu_gRRhaVh0SWrU
        // AIzaSyCpmn-4cFuVaYu_3VhvQZVi-BirFhf0rk0
        //AIzaSyCUZv5OVGLVRZtPPNBNVbe-uEJr4nZ_CPI

        PlacesClient placesClient = Places.createClient(Map_Activity.this);
        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG,
                Place.Field.ADDRESS));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                mMap.clear();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });
    }

    private void init_View() {
        pdetails_back = (ImageView) findViewById(R.id.pdetails_back);
        next_btn = (Button) findViewById(R.id.next_btn);
        your_loc = (TextView) findViewById(R.id.your_loc);
        setListeners();
    }

    private void setListeners() {
        pdetails_back.setOnClickListener(this);
        next_btn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pdetails_back:
                finish();
                break;

            case R.id.next_btn:
                finish();
                break;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        if(!loc_lat.equalsIgnoreCase("") && !loc_lng.equalsIgnoreCase("") &&
                !loc_lat.equalsIgnoreCase("0") && !loc_lng.equalsIgnoreCase("0")) {
            loc = new LatLng(Double.parseDouble(loc_lat), Double.parseDouble(loc_lng));
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16));
        }

//        getAddress(Map_Activity.this, Double.parseDouble(loc_lat), Double.parseDouble(loc_lng));

        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            //    Toast.makeText(this, "The user gestured on the map.", Toast.LENGTH_SHORT).show();
        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) {
            // Toast.makeText(this, "The user tapped something on the map.", Toast.LENGTH_SHORT).show();
        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
            //Toast.makeText(this, "The app moved the camera.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCameraMove() {
        // Toast.makeText(this, "The camera is moving.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraMoveCanceled() {
        //  Toast.makeText(this, "Camera movement canceled.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraIdle() {
      //  Toast.makeText(this, "The camera has stopped moving.", Toast.LENGTH_SHORT).show();
        LatLng center = mMap.getCameraPosition().target;

        lat = mMap.getCameraPosition().target.latitude;
        lon = mMap.getCameraPosition().target.longitude;

        getAddress(Map_Activity.this, lat, lon);
    }

    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {
        //Set Address
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

                Loc_address =  String.valueOf(address);
                loc_lat = String.valueOf(LATITUDE);
                loc_lng = String.valueOf(LONGITUDE);
                your_loc.setText(String.valueOf(address));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        if(!loc_lat.equalsIgnoreCase("") && !loc_lng.equalsIgnoreCase("") &&
                !loc_lat.equalsIgnoreCase("0") && !loc_lng.equalsIgnoreCase("0")) {
            loc = new LatLng(Double.parseDouble(loc_lat), Double.parseDouble(loc_lng));
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16));

            /*Log.e("appSample", "Old_Latitude: " + loc_lat);
            Log.e("appSample", "Old_Longitude: " + loc_lng);*/
        } else {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

            /*Log.e("appSample", "New_Latitude: " + location.getLatitude());
            Log.e("appSample", "New_Longitude: " + location.getLongitude());*/

        }

        /*LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));*/

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        String status = autocompleteFragment.getSavedStateRegistry().toString();
//        Log.e("appSample", status.toString());
        Status str = Autocomplete.getStatusFromIntent(data);
        Log.e("appSample", str.toString());
    }
}

