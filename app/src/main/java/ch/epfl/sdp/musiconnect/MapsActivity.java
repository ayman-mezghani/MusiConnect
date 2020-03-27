package ch.epfl.sdp.musiconnect;

import android.Manifest;
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
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ch.epfl.sdp.R;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, AdapterView.OnItemSelectedListener {
    private Date timeLastUpdt;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");


    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermissionGranted;
    private Location setLoc;
    private Spinner spinner;

    private boolean updatePos = true;

    private static final String FILE_NAME = "cachePos.txt";

    private GoogleMap mMap;
    private View mapView;
    private UiSettings mUiSettings;

    private int delay;
    private List<Marker> markers = new ArrayList<>();
    private List<Musician> allUsers = new ArrayList<>();
    private List<Musician> profiles = new ArrayList<>();
    private Musician person1 = new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25));
    private Musician person2 = new Musician("Alice", "Bardon", "Alyx", "alyx92@gmail.com", new MyDate(1992, 9, 20));
    private Musician person3 = new Musician("Carson", "Calme", "CallmeCarson", "callmecarson41@gmail.com", new MyDate(1995, 4, 1));

    private Marker marker;

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

        //load cached profiles
        loadPos();


        //Set UI settings
        mUiSettings.setZoomControlsEnabled(true);

        //Set circle
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(-34, 151))
                .radius(threshold);
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
                    delay = 5000;
                    generateWarning("Error: No internet connection. Showing the only last musicians found since " + sdf.format(timeLastUpdt), 1);
                } else if(!loc){
                    updatePos = false;
                    delay = 5000;
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
                    delay = 20000;
                    generateWarning("There was a problem retrieving your location; Please check you are connected to a network", 2);
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
        Random random = new Random();
        if(setLoc == null){
            return;
        } else{
            delay = 20000;
        }
        profiles.clear();

        for (Musician m : allUsers) {
            double lat = setLoc.getLatitude() + (((double)random.nextInt(5)-2.5) /100 );
            double lng = setLoc.getLongitude() + (((double)random.nextInt(5)-2.5) /100);
            Location l = new Location("");
            l.setLatitude(lat);
            l.setLongitude(lng);
            m.setLocation(new MyLocation(lat,lng));
            if (setLoc.distanceTo(l) <= threshold) {
                profiles.add(m);
            }
        }

        circle.setRadius(threshold);
    }

    private void loadProfilesMarker() {
        for (Marker m : markers) {
            m.remove();
        }
        markers.clear();

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

    protected boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else {
            return false;
        }
    }

    protected boolean checkLocationServices(){
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
                Toast.makeText(MapsActivity.this, message, Toast.LENGTH_LONG).show();
                break;
            case 2:
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
        for(Musician p:profiles){
            String birthdate = p.getBirthday().getYear() + "/" + p.getBirthday().getMonth() + "/" + p.getBirthday().getDate();
            toCache = toCache + p.getFirstName() + "," + p.getLastName() + "," + p.getUserName() + ","
                    + p.getEmailAddress() + "," + birthdate + ","
                    + String.valueOf(p.getLocation().getLatitude()) + "," + String.valueOf(p.getLocation().getLongitude()) + "\n";
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
            for (int i = 1; i < cached.size(); i++) {
                String[] strProfile = cached.get(i).split(",");
                String[] birthdate = strProfile[4].split("/");
                MyDate birthday = new MyDate(Integer.valueOf(birthdate[0]),
                        Integer.valueOf(birthdate[1]),
                        Integer.valueOf(birthdate[2]),0,0);
                Musician p = new Musician(strProfile[0],strProfile[1],strProfile[2],strProfile[3],
                        birthday);
                p.setLocation(new MyLocation(Double.valueOf(strProfile[5]),Double.valueOf(strProfile[6])));
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
        if(checkConnection()){
            updateProfileList();
            loadProfilesMarker();
            savePos();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
