package ch.epfl.sdp.musiconnect;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.epfl.sdp.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private View mapView;
    private UiSettings mUiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
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

        // Get Main marker information
        Bundle extras = getIntent().getExtras();
        double lat = extras.getDouble("lat");
        double lon = extras.getDouble("lon");
        String markerName = extras.getString("mainMarkerName");
        LatLng marker = new LatLng(lat,lon);
        mMap.addMarker(new MarkerOptions().position(marker).title(markerName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));

        //add circle around main marker
        CircleOptions circleOptions = new CircleOptions()
                .center(marker)
                .radius(5000); // In meters

        Circle circle = mMap.addCircle(circleOptions);

        mapView.setContentDescription("Google Map Ready");
    }
}
