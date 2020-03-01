package ch.epfl.sdp.musiconnect;

import android.app.Service;
import com.google.android.things.userdriver.location.GnssDriver;
import com.google.android.things.userdriver.UserDriverManager;
import android.location.Location;
import android.location.LocationManager;

public class LocationDriverServices extends Service {
    private GnssDriver mDriver;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create a new driver implementation
        mDriver = new GnssDriver();

        // Register with the framework
        UserDriverManager manager = UserDriverManager.getInstance();
        manager.registerGnssDriver(mDriver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        UserDriverManager manager = UserDriverManager.getInstance();
        manager.unregisterGnssDriver();
    }

    private Location parseLocationFromString(String data) {
        Location result = new Location(LocationManager.GPS_PROVIDER);

        // ...parse raw GNSS information...

        return result;
    }

    public void handleLocationUpdate(String rawData) {
        // Convert raw data into a location object
        Location location = parseLocationFromString(rawData);

        // Send the location update to the framework
        mDriver.reportLocation(location);
    }
}
