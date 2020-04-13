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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DataBase;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;

public class StartPage extends Page {
    private static final String TAG = "MainActivity";
    private FusedLocationProviderClient fusedLocationClient;
    private Location userLocation;
    private FloatingActionButton fab_menu, fab_button_1, fab_button_2;
    private Animation fabOpen, fabClose, fabClockWise, fabAntiClockWise;
    private TextView fabTv1, fabTv2;
    private boolean isOpen = false;
    private DbAdapter dbAdapter;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        dbAdapter = new DbAdapter(new DataBase());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermission();

        fab_menu = findViewById(R.id.fab_menu);
        fab_button_1 = findViewById(R.id.fab_button_1);
        fab_button_2 = findViewById(R.id.fab_button_2);

        fabTv1 = findViewById(R.id.fab_tv_1);
        fabTv2 = findViewById(R.id.fab_tv_2);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabClockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        fabAntiClockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anti_clockwise);

        fab_menu.setOnClickListener(v -> fabMenuClick());
        fab_button_1.setOnClickListener(v -> {
            Toast.makeText(this, CurrentUser.getInstance(this).email, Toast.LENGTH_SHORT).show();

            dbAdapter.read("Band", CurrentUser.getInstance(this).email, new DbCallback() {

                @Override
                public void readCallback(User user) {
                    Band b = (Band) user;

                }
            });
        });


    }

    private void fabMenuClick() {
        if(isOpen){
            fab_button_2.startAnimation(fabClose);
            fabTv2.startAnimation(fabClose);
            fab_button_1.startAnimation(fabClose);
            fabTv1.startAnimation(fabClose);
            fab_menu.startAnimation(fabAntiClockWise);

            fab_button_2.setClickable(false);
            fab_button_1.setClickable(false);

            fab_button_2.setFocusable(false);
            fab_button_1.setFocusable(false);

        } else {
            fab_button_2.startAnimation(fabOpen);
            fabTv2.startAnimation(fabOpen);
            fab_button_1.startAnimation(fabOpen);
            fabTv1.startAnimation(fabOpen);
            fab_menu.startAnimation(fabClockWise);

            fab_button_2.setClickable(true);
            fab_button_1.setClickable(true);

            fab_button_2.setFocusable(true);
            fab_button_1.setFocusable(true);
        }

        isOpen = !isOpen;
    }
    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
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
            LocationPermission.sendLocationPermission(this);
        }
        else {
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (LocationPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults)) {
            getLastLocation();
        }
    }
}