package ch.epfl.sdp.musiconnect;

public class Location {

    private double latitude;
    private double longitude;

    private static final double MAX_LATITUDE_VALUE = 90.0;
    private static final double MAX_LONGITUDE_VALUE = 180.0;


    public Location(double newLatitude, double newLongitude) {
        setLatitude(newLatitude);
        setLongitude(newLongitude);
    }

    public Location(Location newLocation) {
        this(newLocation.getLatitude(), newLocation.getLongitude());
    }


    public void setLatitude(double newLatitude) {
        if (newLatitude < -MAX_LATITUDE_VALUE || MAX_LATITUDE_VALUE < newLatitude) {
            throw new IllegalArgumentException("Latitude value not valid");
        }

        latitude = newLatitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double newLongitude) {
        if (newLongitude <= -MAX_LONGITUDE_VALUE || MAX_LONGITUDE_VALUE < newLongitude) {
            throw new IllegalArgumentException("Longitude value not valid");
        }

        longitude = newLongitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLocation(Location newLocation) {
        setLatitude(newLocation.getLatitude());
        setLongitude(newLocation.getLongitude());
    }

    public Location getLocation() {
        return new Location(latitude, longitude);
    }


    @Override
    public String toString() {
        return "Location: (" + getLatitude() + ", " + getLongitude() + ")\n";
    }

}
