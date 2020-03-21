package ch.epfl.sdp.musiconnect;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import ch.epfl.sdp.R;


public class StartPage extends Page {
    private static final String TAG = "MainActivity";
    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermissionGranted;
    private Location userLocation;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getBundleExtra("Location");
            Location location = b.getParcelable("Location");
            if (location != null) {
                userLocation = location;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if(locationPermissionGranted){
            getLastLocation();
        } else {
            checkLocationPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                messageReceiver, new IntentFilter("GPSLocationUpdates"));
    }


    private void startLocationService() {
        LocationPermission.startLocationService(this);
    }



    private void getLastLocation() {
        Log.d(TAG, "getLastKnownLocation: called.");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    // simply store value right now, may need to
                    // store in user information
                    userLocation = location;
                }
                startLocationService();
            });
        }
    }



    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = false;
            LocationPermission.sendLocationPermission(this);
        }

        else {
            locationPermissionGranted = true;
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (LocationPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults)) {
            locationPermissionGranted = true;
            getLastLocation();
        } else {
            locationPermissionGranted = false;
        }
    }
}