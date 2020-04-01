package ch.epfl.sdp.musiconnect;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
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
import java.util.Iterator;
import java.util.List;

import ch.epfl.sdp.R;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, AdapterView.OnItemSelectedListener {

    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermissionGranted;
    private Location setLoc;
    private Spinner spinner;

    private GoogleMap mMap;
    private View mapView;
    private UiSettings mUiSettings;

    private List<Musician> allUsers = new ArrayList<>();
    private List<Musician> profiles = new ArrayList<>();
    private Musician person1 = new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25));
    private Musician person2 = new Musician("Alice", "Bardon", "Alyx", "alyx92@gmail.com", new MyDate(1992, 9, 20));
    private Musician person3 = new Musician("Carson", "Calme", "CallmeCarson", "callmecarson41@gmail.com", new MyDate(1995, 4, 1));

    private Marker marker;
    private List<Marker> markers = new ArrayList<>();

    private Circle circle;

    private int threshold = 50; // meters

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getBundleExtra("Location");
            Location location = b.getParcelable("Location");
            if (location != null) {
                setLocation(location);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        spinner = findViewById(R.id.distanceThreshold);
        String[] items = getResources().getStringArray(R.array.distance_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(2);


        person1.setLocation(new MyLocation(46.52, 6.52));
        person2.setLocation(new MyLocation(46.51, 6.45));
        person3.setLocation(new MyLocation(46.519, 6.57));

        allUsers.add(person1);
        allUsers.add(person2);
        allUsers.add(person3);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermission();
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

        //Set UI settings
        mUiSettings.setZoomControlsEnabled(true);

        //Set circle
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(-34, 151))
                .radius(threshold);
        circle = mMap.addCircle(circleOptions);


        loadProfilesMarker(profiles);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        mapView.setContentDescription("Google Map Ready");
    }
          
         
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    setLocation(location);
                } else {
                    // Here it could be either location setting is turned off or there was not
                    // enough time for the first location to arrive
                    Toast.makeText(this, "Location is disabled", Toast.LENGTH_LONG)
                            .show();
                }
                startLocationService();

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
        if (marker != null) {
            marker.remove();
        }

        setLoc = location;
        if (mMap != null) {
            String markerName = "You";
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(markerName));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
            circle.setCenter(latLng);

            updateProfileList();
            mapView.setContentDescription("Google Map Ready");
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


    private void updateProfileList() {
        profiles.clear();

        Iterator<Marker> iter = markers.iterator();
        while (iter.hasNext()) {
            Marker marker = iter.next();
            marker.remove();
            iter.remove();
        }


        if (setLoc == null) {
            return;
        }

        for (Musician m : allUsers) {
            Location l = new Location("");
            l.setLatitude(m.getLocation().getLatitude());
            l.setLongitude(m.getLocation().getLongitude());
            if (setLoc.distanceTo(l) <= threshold) {
                profiles.add(m);
            }
        }

        loadProfilesMarker(profiles);
        circle.setRadius(threshold);
    }


    private void loadProfilesMarker(List<Musician> profiles){
        for(Musician m:profiles){
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
        if(profiles.contains(marker.getTag())) {
            if(!marker.isInfoWindowShown()) {
                marker.showInfoWindow();
            }
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(profiles.contains(marker.getTag())) {
            Intent profileIntent = new Intent(MapsActivity.this, VisitorProfilePage.class);
            Musician m = (Musician) marker.getTag();
            profileIntent.putExtra("FirstName", m.getFirstName());
            profileIntent.putExtra("LastName", m.getLastName());
            profileIntent.putExtra("UserName", m.getUserName());
            profileIntent.putExtra("EmailAddress", m.getEmailAddress());

            // MyDate is not parcelable...
            int[] birthday = {m.getBirthday().getYear(), m.getBirthday().getMonth(), m.getBirthday().getDate()};
            profileIntent.putExtra("Birthday", birthday);

            this.startActivity(profileIntent);
        }
    }


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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
