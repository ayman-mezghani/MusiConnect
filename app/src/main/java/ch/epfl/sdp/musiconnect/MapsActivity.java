package ch.epfl.sdp.musiconnect;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.epfl.sdp.R;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    LocationRequest locationRequest;
    Location lastLocation;
    FusedLocationProviderClient fusedLocationClient;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private GoogleMap mMap;
    private View mapView;
    private UiSettings mUiSettings;
    private Marker marker;
    private final double THRESHOLD = 0.00002;
    private AlertDialog alert;
    private Toast toast;

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
                for (Location location : locationResult.getLocations()) {
                    if (lastLocation == null ||
                            (location != null &&
                                    (Math.abs(lastLocation.getLatitude() - location.getLatitude()) > THRESHOLD ||
                                            Math.abs(lastLocation.getLatitude() - location.getLatitude()) > THRESHOLD))) {

                        lastLocation = location;

                        double lat = lastLocation.getLatitude();
                        double lon = lastLocation.getLongitude();

                        if (marker != null) {
                            marker.remove();
                        }

                        String markerName = "MarkerName";
                        LatLng latLng = new LatLng(lat, lon);
                        marker = mMap.addMarker(new MarkerOptions().position(latLng).title(markerName));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


                        mapView.setContentDescription("Google Map Ready");
                    }
                }
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


        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5 * 1000);
        locationRequest.setFastestInterval(3 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                startLocationUpdates();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            startLocationUpdates();
            mMap.setMyLocationEnabled(true);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        fusedLocationClient.removeLocationUpdates(locationCallback);

    }

    @Override
    protected void onResume() {
        super.onResume();

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }


    protected Location getLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();

        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(MapsActivity.this, location -> {
                if (location != null) {
                    lastLocation = location;

                }
                /*else {
                    startLocationUpdates();
                }*/
            });
        }

        return lastLocation;
    }

    protected Toast getToast() {
        return toast;
    }

    protected void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                alert = new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(MapsActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                        .create();
                alert.show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) { // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted. Do the location-related task you need to do.
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    toast = Toast.makeText(this, getString(R.string.perm_granted), Toast.LENGTH_LONG);
                    toast.show();
                    startLocationUpdates();
                }

            } else {
                // permission denied. Disable the
                // functionality that depends on this permission.
                toast = Toast.makeText(this, getString(R.string.perm_denied), Toast.LENGTH_LONG);
                toast.show();
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
