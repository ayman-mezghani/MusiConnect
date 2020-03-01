package ch.epfl.sdp.musiconnect;

import com.google.android.things.userdriver.location.GnssDriver;
import com.google.android.things.userdriver.UserDriverManager;


import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Return a location (latitude + longitude) from an NMEA GPGGA string
    public Location parseLocationFromString(String rawData) {
        // Tokenize the string input
        String[] nmea = rawData.split(",");

        Location result = new Location(LocationManager.GPS_PROVIDER);

        // Create timestamp from the date + time tokens
        SimpleDateFormat format = new SimpleDateFormat("hhmmss.ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = format.parse(nmea[1]);
            result.setTime(date.getTime());
        } catch (ParseException e) {
            return null;
        }

        // Parse the fix information tokens
        result.setLatitude(parseLatitude(nmea[2], nmea[3]));
        result.setLongitude(parseLongitude(nmea[4], nmea[5]));

        return result;
    }


    public void handleLocationUpdate(String rawData) {
        // Convert raw data into a location object
        Location location = parseLocationFromString(rawData);

        // Send the location update to the framework
        mDriver.reportLocation(location);
    }

    // Convert latitude from DMS to decimal format
    private float parseLatitude(String latString, String hemisphere) {
        float lat = Float.parseFloat(latString.substring(2))/60.0f;
        lat += Float.parseFloat(latString.substring(0, 2));
        if (hemisphere.contains("S")) {
            lat *= -1;
        }
        return lat;
    }

    // Convert longitude from DMS to decimal format
    private float parseLongitude(String longString, String hemisphere) {
        float lat = Float.parseFloat(longString.substring(3))/60.0f;
        lat += Float.parseFloat(longString.substring(0, 3));
        if (hemisphere.contains("W")) {
            lat *= -1;
        }
        return lat;
    }


}
