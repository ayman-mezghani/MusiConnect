package ch.epfl.sdp.musiconnect;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.location.LocationPermission;

public class StartPage extends Page {
    private static final String TAG = "MainActivity";
    private FusedLocationProviderClient fusedLocationClient;
    public static boolean test = true;

    // All necessary for dark mode switch
    private Switch darkModeSwitch;
    public static final String MY_PREFERENCES = "nightModePrefs";
    public static final String KEY_ISNIGHTMODE = "isNightMode";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermission();

        // Dark mode part
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        checkIfNightModeIsActivated();

        darkModeSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveNightModeState(true);
                recreate();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveNightModeState(false);
                recreate();
            }
        });

        updateCurrentUser(this);
    }


    /**
     * Save the preferences when restarting the app
     * @param nightMode: on or off
     */
    private void saveNightModeState(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ISNIGHTMODE, nightMode);
        editor.apply();
    }

    public void checkIfNightModeIsActivated() {
        if (sharedPreferences.getBoolean(KEY_ISNIGHTMODE, false)) {
            darkModeSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            darkModeSwitch.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home)
            return true;
        return super.onOptionsItemSelected(item);
    }


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
                    // Store the position we got to database
                    sendToDatabase(location);
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