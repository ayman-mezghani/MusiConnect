package ch.epfl.sdp.musiconnect;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.location.LocationPermission;

public class StartPage extends Page {
    private static final String TAG = "MainActivity";
    private FusedLocationProviderClient fusedLocationClient;
    private Location userLocation;
    private FloatingActionButton fab_menu, fab_button_1, fab_button_2;
    private Animation fabOpen, fabClose, fabClockWise, fabAntiClockWise;
    private TextView fabTv1, fabTv2;
    private boolean isOpen = false;
    public static boolean test = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermission();

        updateCurrentUser(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("GPSLocationUpdates"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home)
            return true;
        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getBundleExtra("Location");
            Location location;
            if (b != null) {
                location = b.getParcelable("Location");
                if (location != null) {
                    userLocation = location;
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (LocationPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults)) {
            getLastLocation();
        }
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
                    startLocationService();
                }
            });
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            LocationPermission.sendLocationPermission(this);
        }
        else {
            getLastLocation();
        }
    }
}