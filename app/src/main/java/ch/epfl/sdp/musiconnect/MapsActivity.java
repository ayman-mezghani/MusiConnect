package ch.epfl.sdp.musiconnect;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private View mapView;
    private UiSettings mUiSettings;
    private List<Pair<String,LatLng>> profiles = new ArrayList<>();

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
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 10.0f ) );

        //add circle around main marker
        CircleOptions circleOptions = new CircleOptions()
                .center(marker)
                .radius(5000); // In meters

        Circle circle = mMap.addCircle(circleOptions);

        //Get nearby users and place their marker
        profiles.add(new Pair<>("User1", new LatLng(lat+0.1,lon)));
        profiles.add(new Pair<>("User2", new LatLng(lat,lon+0.1)));
        profiles.add(new Pair<>("User3", new LatLng(lat-0.1,lon-0.1)));
        loadProfilesMarker(profiles);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        mapView.setContentDescription("Google Map Ready");
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
