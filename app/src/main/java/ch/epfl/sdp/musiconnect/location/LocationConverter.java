package ch.epfl.sdp.musiconnect.location;

import android.location.Location;

import ch.epfl.sdp.musiconnect.MyLocation;

public abstract class LocationConverter {

    public static MyLocation locationToMyLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException();
        }

        return new MyLocation(location.getLatitude(), location.getLongitude());
    }

    public static Location myLocationToLocation(MyLocation myLocation) {
        if (myLocation == null) {
            throw new IllegalArgumentException();
        }

        Location location = new Location("");
        location.setLatitude(myLocation.getLatitude());
        location.setLongitude(myLocation.getLongitude());
        return location;
    }
}
