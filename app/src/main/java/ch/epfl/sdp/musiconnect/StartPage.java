package ch.epfl.sdp.musiconnect;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;
import ch.epfl.sdp.musiconnect.events.Event;
import ch.epfl.sdp.musiconnect.location.LocationPermission;

public class StartPage extends Page {
    private static final String TAG = "MainActivity";
    private FusedLocationProviderClient fusedLocationClient;
    private FloatingActionButton fab_menu, fab_button_1, fab_button_2;
    private Animation fabOpen, fabClose, fabClockWise, fabAntiClockWise;
    private TextView fabTv1, fabTv2;
    private boolean isOpen = false;
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
        fab_button_1.setOnClickListener(v -> button1Click());

        fab_button_2.setOnClickListener(v -> {
            updateCurrentUser(this);
            Context ctx = this;
            ListView lv = findViewById(R.id.LvEvent);
            ArrayList<String> events = new ArrayList<>();

            final ArrayAdapter<String> adapter = new ArrayAdapter<>
                    (StartPage.this, android.R.layout.simple_list_item_1, events);
            lv.setAdapter(adapter);

            for(String e : CurrentUser.getInstance(this).getBand().getEvents()) {
                DbGenerator.getDbInstance().read(DbUserType.Events, e.trim(), new DbCallback() {
                    @Override
                    public void readCallback(Event u) {
                        events.add(u.getTitle());
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        updateCurrentUser(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);

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
    }

    /**
     * Save the preferences when on a new switch toggle
     * @param nightMode: is night mode activated
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
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("GPSLocationUpdates"));

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

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getBundleExtra("Location");
            Location location;
            if (b != null) {
                location = b.getParcelable("Location");
                if (location != null) {
                    sendToDatabase(location);
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

    private void sendToDatabase(Location location) {
        User user = CurrentUser.getInstance(this).getMusician();
        if(user != null) {
            user.setLocation(new MyLocation(location.getLatitude(), location.getLongitude()));
            DbGenerator.getDbInstance().update(DbUserType.Musician, user);
        }
    }


    protected void button1Click() {
        getBandIfMember();



    }

    protected void fabMenuClick() {
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
}