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
import android.util.Pair;
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

    private static final String TAG = "MapsActivity";
    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermissionGranted;
    private Location setLoc;

    private GoogleMap mMap;
    private View mapView;
    private UiSettings mUiSettings;
    private List<Pair<String,LatLng>> profiles = new ArrayList<>();
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
        profiles.add(new Pair<>("User1", new LatLng(lat+0.1,lon)));
        profiles.add(new Pair<>("User2", new LatLng(lat,lon+0.1)));
        profiles.add(new Pair<>("User3", new LatLng(lat-0.1,lon-0.1)));
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
                    // Here it could be either location is turned off or there was not enough time for
                    // the first location to arrive
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
        if (!isLocationServiceRunning()) {
            Intent serviceIntent = new Intent(this, LocationService.class);
            startService(serviceIntent);
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("ch.epfl.sdp.musiconnect.LocationService".equals(service.service.getClassName())) {
                Log.d(TAG, "isLocationServiceRunning: location service is already running.");
                return true;
            }
        }
        Log.d(TAG, "isLocationServiceRunning: location service is not running.");
        return false;
    }



    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = false;

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LocationService.MY_PERMISSIONS_REQUEST_LOCATION);

        } else {
            locationPermissionGranted = true;
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case LocationService.MY_PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted. Do the location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, getString(R.string.perm_granted), Toast.LENGTH_LONG)
                                .show();

                        locationPermissionGranted = true;
                        getLastLocation();
                    }

                } else {
                    // permission denied. Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, getString(R.string.perm_denied), Toast.LENGTH_LONG)
                            .show();

                    locationPermissionGranted = false;
                }
                // other 'case' lines to check for other
                // permissions this app might request
        }
    }

    private void loadProfilesMarker(List<Pair<String,LatLng>> profiles){
        for(Pair<String,LatLng> p:profiles){
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(p.second)
                    .title(p.first));
            marker.setTag(p);

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
            this.startActivity(profileIntent);
        }
    }
}
