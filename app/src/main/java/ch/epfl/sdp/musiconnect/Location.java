package ch.epfl.sdp.musiconnect;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class Location {

    private double latitude;
    private double longitude;

    private static final double MAX_LATITUDE_VALUE = 90.0;
    private static final double MAX_LONGITUDE_VALUE = 180.0;


    public Location(double latitude, double longitude) {
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public Location(Location location) {
        this(location.getLatitude(), location.getLongitude());
    }


    public void setLatitude(double latitude) {
        if (latitude < -MAX_LATITUDE_VALUE || MAX_LATITUDE_VALUE < latitude) {
            throw new IllegalArgumentException("Latitude value not valid");
        }

        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        if (longitude <= -MAX_LONGITUDE_VALUE || MAX_LONGITUDE_VALUE < longitude) {
            throw new IllegalArgumentException("Longitude value not valid");
        }

        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLocation(Location location) {
        setLatitude(location.getLatitude());
        setLongitude(location.getLongitude());
    }

    public Location getLocation() {
        return new Location(latitude, longitude);
    }


    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof Location) {
            Location thatLocation = (Location) that;

            if (latitude == thatLocation.getLatitude() && longitude == thatLocation.getLongitude()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "Location: (" + getLatitude() + ", " + getLongitude() + ")\n";
    }

}