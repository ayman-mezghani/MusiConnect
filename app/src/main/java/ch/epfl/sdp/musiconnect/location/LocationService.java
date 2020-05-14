package ch.epfl.sdp.musiconnect.location;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


public class LocationService extends Service {
    private static final String TAG = "LocationService";

    private Location lastLocation;
    private FusedLocationProviderClient fusedLocationClient;

    public final static int MY_PERMISSIONS_REQUEST_LOCATION = 10;
    private final static long UPDATE_INTERVAL = 10 * 1000;
    private final static long FASTEST_INTERVAL = 5 * 1000;

    private static final float THRESHOLD = 10.0f; // 10 meters

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d(TAG, "onLocationResult: got location result.");

            Location location = locationResult.getLastLocation();


            if (lastLocation == null || location.distanceTo(lastLocation) > THRESHOLD) {
                Log.d(TAG, "LocationService sent a message containing location ");


                lastLocation = location;
                sendMessageToActivity(location);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "LocationService created");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: called.");
        updateLocation();
        return START_NOT_STICKY;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void updateLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocation: getting location information.");
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } else {
            Log.d(TAG, "getLocation: stopping the location service.");
            stopSelf();
        }
    }


    private void sendMessageToActivity(Location l) {
        Intent intent = new Intent("GPSLocationUpdates");
        Bundle b = new Bundle();
        b.putParcelable("Location", l);
        intent.putExtra("Location", b);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }



}
