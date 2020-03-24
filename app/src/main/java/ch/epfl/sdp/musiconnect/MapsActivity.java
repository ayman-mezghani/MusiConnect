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
import java.util.List;

import ch.epfl.sdp.R;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermissionGranted;
    private Location setLoc;

    private GoogleMap mMap;
    private View mapView;
    private UiSettings mUiSettings;
    private List<Musician> profiles = new ArrayList<>();
    private Musician person1;
    private Musician person2;
    private Musician person3;

    private Marker marker;

    private double lat = -34;
    private double lon = 151;
    private Circle circle;
    private double radius = 5000;

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

        person1 = new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25));
        person2 = new Musician("Alice", "Bardon", "Alyx", "alyx92@gmail.com", new MyDate(1992, 9, 20));
        person3 = new Musician("Carson", "Calme", "CallmeCarson", "callmecarson41@gmail.com", new MyDate(1995, 4, 1));

        person1.setLocation(new MyLocation(-51.0, 34.0));
        person2.setLocation(new MyLocation(40, -100));
        person3.setLocation(new MyLocation(20, 90));


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermission();
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
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
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
                .center(new LatLng(lat,lon))
                .radius(radius);
        circle = mMap.addCircle(circleOptions);


        //Get users and place their marker
        profiles.add(person1);
        profiles.add(person2);
        profiles.add(person3);

        loadProfilesMarker(profiles);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        mapView.setContentDescription("Google Map Ready");
    }
          
         
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
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
            locationPermissionGranted = false;
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (LocationPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults)) {
            locationPermissionGranted = true;
            getLastLocation();
        } else {
            locationPermissionGranted = false;
        }
    }

    private void loadProfilesMarker(List<Musician> profiles){
        for(Musician m:profiles){
            LatLng latlng = new LatLng(m.getLocation().getLatitude(), m.getLocation().getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(m.getUserName()));
            marker.setTag(m);
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if(profiles.contains(marker.getTag())) {
            if(!marker.isInfoWindowShown()) {
                marker.showInfoWindow();
                return false;
            }
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(profiles.contains(marker.getTag())) {
            Intent profileIntent = new Intent(MapsActivity.this, ProfilePage.class);
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
}
