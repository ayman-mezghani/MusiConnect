package ch.epfl.sdp.musiconnect;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ch.epfl.sdp.R;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
    private Date timeLastUpdt;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");


    private static final String TAG = "MapsActivity";
    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermissionGranted;
    private Location setLoc;

    private boolean updatePos = true;

    private static final String FILE_NAME = "cachePos.txt";

    private GoogleMap mMap;
    private View mapView;
    private UiSettings mUiSettings;

    private int delay;
    private List<Pair<String, LatLng>> profiles = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();
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
            setLocation(location);

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

        //load cached profiles
        loadPos();


        //Set UI settings
        mUiSettings.setZoomControlsEnabled(true);

        //Set circle
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(lat, lon))
                .radius(radius);
        circle = mMap.addCircle(circleOptions);


        //place users' markers
        loadProfilesMarker();
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        //Handler that updates nearby users list
        Handler handler = new Handler();
        delay = 1000; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                boolean co = checkConnection();
                boolean loc = checkLocationServices();

                if (!co) {
                    updatePos = false;
                    generateWarning("Error: No internet connection. Showing the only last musicians found since " + sdf.format(timeLastUpdt), 1);
                } else if(!loc){
                    updatePos = false;
                    generateWarning("Error: couldn't update your location", 1);
                } else {
                    updatePos = true;
                    timeLastUpdt = Calendar.getInstance().getTime();
                    updateProfileList();
                    loadProfilesMarker();
                    savePos();
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
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
                    //either way, we remove all markers, stop updating the location, and send an error message to the user
                    updatePos = false;
                    for(Marker m:markers){
                        m.remove();
                    }
                    markers.clear();
                    generateWarning("There was a problem retrieving your location; Please check you are connected to a network", 2);
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

        if(!updatePos){
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
    }


    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent serviceIntent = new Intent(this, LocationService.class);
            startService(serviceIntent);
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("ch.epfl.sdp.musiconnect.LocationService".equals(service.service.getClassName())) {
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

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    LocationService.MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LocationService.MY_PERMISSIONS_REQUEST_LOCATION);
            }
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

    private void loadProfilesMarker() {
        for (Marker m : markers) {
            m.remove();
        }
        markers.clear();


        for (Pair<String, LatLng> p : profiles) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(p.second)
                    .title(p.first));
            marker.setTag(p);
            markers.add(marker);

        }
    }

    private void updateProfileList() {
        Random random = new Random();
        if(setLoc == null){
            return;
        } else{
            delay = 20000;
        }
        profiles.clear();
        for (int i = 0; i < 3; i++) {
            double lat = ((double)random.nextInt(10)-5) /100 ;
            double lng = ((double)random.nextInt(10)-5) /100 ;

            LatLng userPos = new LatLng(setLoc.getLatitude() + lat, setLoc.getLongitude() + lng);
            profiles.add(new Pair<>("User" + i, userPos));
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (profiles.contains(marker.getTag())) {
            if (!marker.isInfoWindowShown()) {
                marker.showInfoWindow();
                return false;
            }
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (profiles.contains(marker.getTag())) {
            Intent profileIntent = new Intent(MapsActivity.this, ProfilePage.class);
            this.startActivity(profileIntent);
        }
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else {
            return false;
        }
    }

    private boolean checkLocationServices(){
        LocationManager lm = (LocationManager)MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            return false;
        } else{
            return true;
        }

    }

    //creates warning message when something goes wrong; type 1 is a simple message at the bottom of the screen, type 2 is an alertDialog box
    private void generateWarning(String message, int type) {
        switch (type) {
            case 1:
                delay = 5000;
                Toast.makeText(MapsActivity.this, message, Toast.LENGTH_LONG).show();
                break;
            case 2:
                delay = 20000;
                AlertDialog wrng = new AlertDialog.Builder(MapsActivity.this).create();
                wrng.setTitle("Warning!");
                wrng.setMessage(message);
                wrng.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                wrng.show();
                break;
        }
    }

    private void savePos() {
        FileOutputStream fos = null;
        String toCache = sdf.format(timeLastUpdt) + "\n";
        for(Pair<String,LatLng> p:profiles){
            toCache = toCache + p.first + "," + String.valueOf(p.second.latitude) + "," + String.valueOf(p.second.longitude) + "\n";
        }
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(toCache.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadPos() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            List<String> cached = new ArrayList<>();

            while ((text = br.readLine()) != null) {
                cached.add(text);
            }

            timeLastUpdt = sdf.parse(cached.get(0));

            profiles.clear();
            for(int i = 1; i < cached.size();i++){
                String[] strProfile = cached.get(i).split(",");
                Pair<String,LatLng> p = new Pair<>(strProfile[0],
                        new LatLng(Double.parseDouble(strProfile[1]),Double.parseDouble(strProfile[2])));
                profiles.add(p);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            timeLastUpdt = Calendar.getInstance().getTime();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
