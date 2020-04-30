package ch.epfl.sdp.musiconnect;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;
import ch.epfl.sdp.musiconnect.roomdatabase.AppDatabase;
import ch.epfl.sdp.musiconnect.roomdatabase.MusicianDao;

import static ch.epfl.sdp.musiconnect.ConnectionCheck.checkConnection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {


    private static final String CHANNEL_ID = "1";
    private static final int CONNECTION_ID = 10;
    private static final int LOCATION_ID = 11;
    private static final int INITLOCATION_ID = 12;
    private static final int CLOUD_ID = 13;
    private NotificationManagerCompat notificationManager;


    private DbAdapter Adb = DbGenerator.getDbInstance();

    private AppDatabase localDb;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermissionGranted;
    private Location setLoc;
    private Spinner spinner;

    private boolean updatePos = true;

    private GoogleMap mMap;
    private UiSettings mUiSettings;

    private int delay;                                          //delay to updating the users list in ms
    private List<Musician> allUsers = new ArrayList<>();        //all users "near" the current user's position
    private List<Musician> profiles = new ArrayList<>();        //all users within the radius set by the user in the app
    private List<Marker> markers = new ArrayList<>();           //markers on the map associated to profiles


    private Marker marker;                                      //main user's marker

    private Circle circle;

    private int threshold = 50; // meters

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getBundleExtra("Location");
            Location location = b.getParcelable("Location");
            setLocation(location);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setupSpinner();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getView();
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermission();

        localDb = AppDatabase.getInstance(this);

        createNotificationChannel();
        notificationManager = NotificationManagerCompat.from(MapsActivity.this);

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


    private void setupSpinner() {
        spinner = findViewById(R.id.distanceThreshold);
        String[] items = getResources().getStringArray(R.array.distance_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString()
                        .replaceAll("m", "");
                int meters = 1;

                if (selected.contains("k")) {
                    meters = 1000;
                    selected = selected.replaceAll("k", "");
                }

                try {
                    threshold = Integer.parseInt(selected) * meters;
                } catch (NumberFormatException e) {
                    threshold = 0;
                }

                spinner.setSelection(position);

                updateProfileList();
                loadProfilesMarker();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner.setSelection(2);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();

        //If there's a connection, fetch Users in the general area; else, load them from cache
        if (checkConnection(MapsActivity.this)) {
            createPlaceHolderUsers();
            clearCachedUsers();
        } else {
            loadUsersFromCache();
        }


        //Set UI settings
        mUiSettings.setZoomControlsEnabled(true);

        //Set circle
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(-34, 151))
                .radius(threshold);
        circle = mMap.addCircle(circleOptions);


        //place users' markers
        updateProfileList();
        loadProfilesMarker();

        //sets listeners on map markers
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        //Handler that updates users list
        Handler handler = new Handler();
        delay = 1000;

        handler.postDelayed(new Runnable() {
            public void run() {
                boolean co = checkConnection(MapsActivity.this);
                boolean loc = checkLocationServices();

                if (!co) {
                    updatePos = false;
                    delay = 5000;
                    notificationManager.notify(CONNECTION_ID, buildNotification("Error: No internet connection. Showing the only last musicians found before losing connection").build());
                } else{
                    notificationManager.cancel(CONNECTION_ID);
                }
                if (!loc) {
                    updatePos = false;
                    delay = 10000;
                    notificationManager.notify(LOCATION_ID, buildNotification("Error: couldn't update your location to the cloud").build());
                } else{
                    notificationManager.cancel(LOCATION_ID);
                }
                if (co && loc) {
                    updatePos = true;
                    delay = 20000;
                    updateUsers();
                    clearCachedUsers();
                    saveUsersToCache();
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
    }


    //========================================================================


    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    notificationManager.cancel(INITLOCATION_ID);
                    setLocation(location);
                    startLocationService();
                } else {
                    // Here it could be either location is turned off or there was not enough time for
                    // the first location to arrive
                    //either way, we remove all markers, stop updating the location, and send an error message to the user
                    updatePos = false;
                    for (Marker m : markers) {
                        m.remove();
                    }
                    markers.clear();
                    delay = 20000;
                    notificationManager.notify(INITLOCATION_ID, buildNotification("There was a problem retrieving your location; Please check you are connected to a network").build());
                }

            });
        } else {
            checkLocationPermission();
        }
    }

    protected Task<Location> getTaskLocation() {
        return fusedLocationClient.getLastLocation();
    }

    protected boolean isLocationPermissionGranted() {
        return locationPermissionGranted;
    }

    protected Location getSetLocation() {
        return setLoc;
    }


    private void setLocation(Location location) {

        if (!updatePos) {
            return;
        }

        if (marker != null) {
            marker.remove();
        }

        setLoc = location;
        if (mMap != null) {
            String markerName = "You";
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(markerName));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
            circle.setCenter(latLng);

        }

        if (CurrentUser.getInstance(this).getCreatedFlag()) {
            Musician current = CurrentUser.getInstance(this).getMusician();
            current.setLocation(new MyLocation(setLoc.getLatitude(), setLoc.getLongitude()));
            DbAdapter adapter = DbGenerator.getDbInstance();
            adapter.update(DbUserType.Musician, current);
            notificationManager.cancel(CLOUD_ID);
        } else {
            notificationManager.notify(CLOUD_ID, buildNotification("Error: couldn't update your location to the cloud").build());
        }
    }


    private void startLocationService() {
        LocationPermission.startLocationService(this);
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = false;
            LocationPermission.sendLocationPermission(this);
        } else {
            locationPermissionGranted = true;
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (LocationPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults)) {
            locationPermissionGranted = true;
            getLastLocation();
        } else {
            locationPermissionGranted = false;
        }
    }

    //========================================================================


    //fetches users coordinates, updates them, and save them to cache
    //(right now, only creates random nearby position and saves user to cache until database is implemented)
    private void updateUsers() {
        if (setLoc == null) {             //Might be called before we get the first update to the location;
            return;
        }


        for (Musician m : allUsers) {
            Adb.read(DbUserType.Musician, m.getEmailAddress(), new DbCallback() {
                @Override
                public void readCallback(User user) {
                    MyLocation l = user.getLocation();
                    m.setLocation(l);
                    updateProfileList();
                }
            });
        }

    }

    //From the users around the area, picks the ones that are within the threshold distance.
    private void updateProfileList() {
        if (setLoc == null) {             //Might be called before we get the first update to the location;
            return;
        } else {
            delay = 20000;              //sets a 20 sec long delay on updates when everything is in place
        }

        profiles.clear();
        for (Musician m : allUsers) {
            Location l = new Location("");
            l.setLatitude(m.getLocation().getLatitude());
            l.setLongitude(m.getLocation().getLongitude());
            if (setLoc.distanceTo(l) <= threshold) {
                profiles.add(m);
            }
        }

        circle.setRadius(threshold);

        loadProfilesMarker();
    }

    //Loads the profile on the map as markers, with associated information
    private void loadProfilesMarker() {
        for (Marker m : markers) {
            m.remove();
        }
        markers.clear();

        for (Musician m : profiles) {
            LatLng latlng = new LatLng(m.getLocation().getLatitude(), m.getLocation().getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(m.getUserName()));
            marker.setTag(m);
            markers.add(marker);
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (profiles.contains(marker.getTag())) {
            if (!marker.isInfoWindowShown()) {
                marker.showInfoWindow();
            }
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (profiles.contains(marker.getTag())) {
            Intent profileIntent = new Intent(MapsActivity.this, VisitorProfilePage.class);

            Musician m = (Musician) marker.getTag();
            profileIntent.putExtra("UserEmail", m.getEmailAddress());

            this.startActivity(profileIntent);
        }
    }

    protected boolean checkLocationServices() {
        LocationManager lm = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            return false;
        } else {
            return true;
        }

    }


    private void saveUsersToCache() {

        MusicianDao musicianDao = localDb.musicianDao();

        mExecutor.execute(() -> {
            musicianDao.insertAll(allUsers.toArray(new Musician[allUsers.size()]));
        });


    }

    private void loadUsersFromCache() {

        MusicianDao musicianDao = localDb.musicianDao();

        mExecutor.execute(() -> {
            allUsers = musicianDao.getAll();
            List<Musician> currentUser = musicianDao.loadAllByIds(new String[]{CurrentUser.getInstance(MapsActivity.this).email});
            allUsers.removeAll(currentUser);
        });


    }

    private void clearCachedUsers() {
        MusicianDao musicianDao = localDb.musicianDao();
        mExecutor.execute(() -> {
            musicianDao.nukeTable();
        });
    }


    //Should be replaced by a function that fetch user from the database; right now it generates 3 fixed users
    private void createPlaceHolderUsers() {
        Random random = new Random();

        double r1 = ((double) random.nextInt(5) - 2.5) / 200;
        double r2 = ((double) random.nextInt(5) - 2.5) / 200;
        double r3 = ((double) random.nextInt(5) - 2.5) / 200;


        Musician person1 = new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25));
        Musician person2 = new Musician("Alice", "Bardon", "Alyx", "aymanmezghani97@gmail.com", new MyDate(1992, 9, 20));
        Musician person3 = new Musician("Carson", "Calme", "CallmeCarson", "callmecarson41@gmail.com", new MyDate(1995, 4, 1));

        person1.setLocation(new MyLocation(46.52 + r1, 6.52 + r1));
        person2.setLocation(new MyLocation(46.51 + r2, 6.45 + r2));
        person3.setLocation(new MyLocation(46.519 + r3, 6.57 + r3));

        allUsers.add(person1);
        allUsers.add(person2);
        allUsers.add(person3);

        Adb.add(DbUserType.Musician, person1);
        Adb.add(DbUserType.Musician, person2);
        Adb.add(DbUserType.Musician, person3);
    }

    //==============================================================================================
    @VisibleForTesting
    boolean createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        return true;
    }
    @VisibleForTesting
    NotificationCompat.Builder buildNotification(String warning){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MapsActivity.this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notifwarning)
                .setContentTitle("Warning !")
                .setContentText(warning)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(warning))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOnlyAlertOnce(true);
        notificationManager.notify(CONNECTION_ID, builder.build());
        return builder;
    }
}
