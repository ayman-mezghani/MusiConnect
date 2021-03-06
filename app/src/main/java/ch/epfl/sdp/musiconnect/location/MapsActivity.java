package ch.epfl.sdp.musiconnect.location;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.functionnalities.CustomInfoWindowGoogleMap;
import ch.epfl.sdp.musiconnect.users.Musician;
import ch.epfl.sdp.musiconnect.functionnalities.MyLocation;
import ch.epfl.sdp.musiconnect.users.User;
import ch.epfl.sdp.musiconnect.pages.VisitorProfilePage;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.DbDataType;
import ch.epfl.sdp.musiconnect.events.Event;
import ch.epfl.sdp.musiconnect.events.EventCreationPage;
import ch.epfl.sdp.musiconnect.events.MyEventPage;
import ch.epfl.sdp.musiconnect.roomdatabase.AppDatabase;
import ch.epfl.sdp.musiconnect.roomdatabase.EventDao;
import ch.epfl.sdp.musiconnect.roomdatabase.MusicianDao;

import static ch.epfl.sdp.musiconnect.functionnalities.ConnectionCheck.checkConnection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {

    private static final String CHANNEL_ID = "1";
    private static final int CONNECTION_ID = 10;
    private static final int LOCATION_ID = 11;
    private static final int INITLOCATION_ID = 12;
    private static final int CLOUD_ID = 13;
    private NotificationManagerCompat notificationManager;

    private DbAdapter dbAdapter = DbSingleton.getDbInstance();

    private AppDatabase localDb;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    private FusedLocationProviderClient fusedLocationClient;
    @VisibleForTesting
    protected Location setLoc;
    private Spinner spinner;

    @VisibleForTesting
    protected boolean updatePos = true;
    @VisibleForTesting
    protected GoogleMap mMap;
    private UiSettings mUiSettings;

    private int delay;                                          //delay to updating the users list in ms
    @VisibleForTesting
    protected List<Musician> allUsers = new ArrayList<>();        //all users "near" the current user's position
    @VisibleForTesting
    protected List<Musician> profiles = new ArrayList<>();        //all users within the radius set by the user in the app
    @VisibleForTesting
    protected List<Marker> markers = new ArrayList<>();           //markers on the map associated to profiles
    @VisibleForTesting
    protected List<Event> events = new ArrayList<>();
    @VisibleForTesting
    protected List<Event> eventNear = new ArrayList<>();
    @VisibleForTesting
    protected List<Marker> eventMarkers = new ArrayList<>();           //markers on the map associated to events
    private Event shownEvent = null;                                //event to be shown on map, when accessed from event pages

    private boolean updateCamera = true;

    private Marker marker;                                      //main user's marker

    private Circle circle;

    private int threshold = 50; // meters
    private static final int MAX_THRESHOLD = 10000; // 10km

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

        setupEventList(CurrentUser.getInstance(this).getMusician().getEvents());
        if (CurrentUser.getInstance(this).getBand() != null) {
            setupEventList(CurrentUser.getInstance(this).getBand().getEvents());
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
                String selected = parent.getItemAtPosition(position).toString();

                Pattern patternKM = Pattern.compile("(\\d+)km");
                Pattern patternM = Pattern.compile("(\\d+)m");

                Matcher matcherKM = patternKM.matcher(selected);
                Matcher matcherM = patternM.matcher(selected);

                if (matcherKM.find()) {
                    threshold = Integer.parseInt(matcherKM.group(1)) * 1000;
                } else if (matcherM.find()) {
                    threshold = Integer.parseInt(matcherM.group(1));
                } else {
                    threshold = 0;
                }

                spinner.setSelection(position);


                updateProfileList();
                loadProfilesMarker();

                updateEvents();
                loadEventMarkers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner.setSelection(2);
    }

    private void setupEventList(List<String> list) {
        for (String se: list) {
            DbSingleton.getDbInstance().read(DbDataType.Events, se, new DbCallback() {
                @Override
                public void readCallback(Event e) {
                    events.add(e);
                }
            });
        }
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
            fetchUsersFromDb();
            clearCache();
        } else {
            loadCache();
        }


        //Set UI settings
        mUiSettings.setZoomControlsEnabled(true);

        //Set circle
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(-34, 151))
                .radius(threshold);
        circle = mMap.addCircle(circleOptions);

        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);

        //check if there's an event to be shown to the user
        if(getIntent().hasExtra("Event")){
            getShownEvent(getIntent().getStringExtra("Event"));
        }
        //place users' markers

        fetchEventsFromDb();

        //sets listeners on map markers and user click
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapLongClickListener(this::createAlert);

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
                    updateEvents();
                    updateUsers();
                    clearCache();
                    saveToCache();
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
    }


    //========================================================================
    // Location functions
    @VisibleForTesting
    protected void getLastLocation() {
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

    @VisibleForTesting
    protected void setLocation(Location location) {
        if (!updatePos) {
            return;
        }

        if (marker != null) {
            marker.remove();
        }

        sendToDatabase(location);
        setLoc = location;
        if (mMap != null) {
            String markerName = "You";
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(markerName)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            if(updateCamera) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
            }
            circle.setCenter(latLng);
        }

        if (CurrentUser.getInstance(this).getCreatedFlag()) {
            Musician current = CurrentUser.getInstance(this).getMusician();
            current.setLocation(LocationConverter.locationToMyLocation(setLoc));
            DbAdapter adapter = DbSingleton.getDbInstance();
            adapter.update(DbDataType.Musician, current);
            notificationManager.cancel(CLOUD_ID);
        } else {
            notificationManager.notify(CLOUD_ID, buildNotification("Error: couldn't update your location to the cloud").build());
        }
    }


    private void startLocationService() {
        LocationPermission.startLocationService(this);
    }

    private void sendToDatabase(Location location) {
        User user = CurrentUser.getInstance(this).getMusician();

        user.setLocation(LocationConverter.locationToMyLocation(location));
        DbSingleton.getDbInstance().update(DbDataType.Musician, user);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            LocationPermission.sendLocationPermission(this);
        } else {
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (LocationPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults)) {
            getLastLocation();
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
            dbAdapter.read(DbDataType.Musician, m.getEmailAddress(), new DbCallback() {
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
    @VisibleForTesting
    protected void updateProfileList() {
        if (setLoc == null) {             //Might be called before we get the first update to the location;
            return;
        } else {
            delay = 20000;              //sets a 20 sec long delay on updates when everything is in place
        }

        profiles.clear();

        for (Musician m: allUsers) {
            if (m.getEmailAddress().equals(CurrentUser.getInstance(getApplicationContext()).email)) {
                allUsers.remove(m);
                break;
            }
        }


        for (Musician m : allUsers) {
            if (setLoc.distanceTo(LocationConverter.myLocationToLocation(m.getLocation())) <= threshold) {
                profiles.add(m);
            }
        }
        if(circle != null){
            circle.setRadius(threshold);
        }
        loadProfilesMarker();
    }

    //From the events around the area, picks the ones that are within the threshold distance.
    @VisibleForTesting
    protected void updateEvents() {
        if (setLoc == null) {            //Might be called before we get the first update to the location;
            return;
        }

        eventNear.clear();
        for (Event e : events) {
            MyLocation ml = e.getLocation();
            if (ml != null || (ml.getLatitude() != 0 && ml.getLongitude() != 0)) {
                // Show event if event is in threshold, public or created by "this" user or participates in
                if (setLoc.distanceTo(LocationConverter.myLocationToLocation(ml)) <= threshold) {
                    eventNear.add(e);
                }
            }
        }

        if(shownEvent != null){
            eventNear.add(shownEvent);
        }

        loadEventMarkers();
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
            marker.setSnippet("Musician");
            marker.setTag(m);
            markers.add(marker);
        }
    }

    //Loads the event on the map as markers, with associated information
    private void loadEventMarkers() {
        for (Marker m : eventMarkers) {
            m.remove();
        }
        eventMarkers.clear();

        for (Event e : eventNear) {
            LatLng latlng = new LatLng(e.getLocation().getLatitude(), e.getLocation().getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(e.getTitle())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.event_marker)));
            marker.setSnippet("Event");
            marker.setTag(e);
            eventMarkers.add(marker);
        }
    }

    private void getShownEvent(String eid){
        dbAdapter.read(DbDataType.Events, eid, new DbCallback() {
            @Override
            public void readCallback(Event e) {
                shownEvent = e;
                updateCamera = false;
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(shownEvent.getLocation().getLatitude(), shownEvent.getLocation().getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
            }});
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (profiles.contains(marker.getTag())) {
            Intent profileIntent = new Intent(MapsActivity.this, VisitorProfilePage.class);

            Musician m = (Musician) marker.getTag();
            profileIntent.putExtra("UserEmail", m.getEmailAddress());

            this.startActivity(profileIntent);
        } else if(eventNear.contains(marker.getTag())) {
            Intent eventPageIntent = new Intent(MapsActivity.this, MyEventPage.class);

            Event e = (Event) marker.getTag();
            eventPageIntent.putExtra("eid", e.getEid());

            this.startActivity(eventPageIntent);
        }
    }

    @VisibleForTesting
    protected void createEvent(LatLng latlng){
        Intent eventIntent = new Intent(MapsActivity.this, EventCreationPage.class);
        Geocoder coder = new Geocoder(this);
        List<Address> address = null;

        try {
            try {
                address = coder.getFromLocation(latlng.latitude,latlng.longitude,5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (address!=null && !address.isEmpty()) {
                Address addr  = address.get(0);
                eventIntent.putExtra("Address",addr.getAddressLine(0));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.startActivity(eventIntent);
    }

    @VisibleForTesting
    protected void createAlert(LatLng latLng){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Do you want to create an event?");
        adb.setIcon(android.R.drawable.ic_dialog_alert);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                createEvent(latLng);
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        adb.show();
    }

    @VisibleForTesting
    protected boolean checkLocationServices() {
        LocationManager lm = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {}

        return gps_enabled || network_enabled;

    }

    @VisibleForTesting
    protected void saveToCache() {

        MusicianDao musicianDao = localDb.musicianDao();
        EventDao eventDao = localDb.eventDao();

        mExecutor.execute(() -> {
            musicianDao.insertAll(allUsers.toArray(new Musician[allUsers.size()]));
            eventDao.insertAll(eventNear.toArray(new Event[eventNear.size()]));
        });


    }
    @VisibleForTesting
    protected void loadCache() {

        MusicianDao musicianDao = localDb.musicianDao();
        EventDao eventDao = localDb.eventDao();

        mExecutor.execute(() -> {
            allUsers = musicianDao.getAll();
            List<Musician> currentUser = musicianDao.loadAllByIds(new String[]{CurrentUser.getInstance(MapsActivity.this).email});
            allUsers.removeAll(currentUser);
            events = eventDao.getAll();
            updateProfileList();
            loadProfilesMarker();
        });


    }
    @VisibleForTesting
    protected void clearCache() {
        MusicianDao musicianDao = localDb.musicianDao();
        EventDao eventDao = localDb.eventDao();

        mExecutor.execute(() -> {
            musicianDao.nukeTable();
            eventDao.nukeTable();
        });
    }

    private void fetchUsersFromDb() {
        dbAdapter.locationQuery(DbDataType.Musician, LocationConverter.locationToMyLocation(CurrentUser.getInstance(this).getLocation()), MAX_THRESHOLD / 1000,
                new DbCallback() {
                    @Override
                    public void locationQueryCallback(List list) {
                        for (Object m : list) {
                            allUsers.add((Musician) m);
                        }
                        updateProfileList();
                        loadProfilesMarker();
                    }
                });
    }


    private void fetchEventsFromDb() {
        dbAdapter.locationQuery(DbDataType.Events, LocationConverter.locationToMyLocation(CurrentUser.getInstance(getApplicationContext()).getLocation()), MAX_THRESHOLD / 1000,
                new DbCallback() {
                    @Override
                    public void locationQueryCallback(List list) {
                        for (Object e : list) {
                            if (((Event) e).isVisible() || ((Event) e).containsParticipant(CurrentUser.getInstance(getApplicationContext()).email)
                                    || ((Event) e).getHostEmailAddress().equals(CurrentUser.getInstance(getApplicationContext()).email)) {

                                events.add((Event) e);
                            }
                        }
                        updateProfileList();
                        loadProfilesMarker();
                    }
                });
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