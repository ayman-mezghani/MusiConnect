package ch.epfl.sdp.musiconnect.functionnalities;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class MyLocation {

    private double latitude;
    private double longitude;

    private static final double MAX_LATITUDE_VALUE = 90.0;
    private static final double MAX_LONGITUDE_VALUE = 180.0;


    private void checkCoordinate(double value, final double maxValue, String exceptionMessage) {
        if (value <= -maxValue || value > maxValue) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }


    public MyLocation(double latitude, double longitude) {
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public MyLocation(MyLocation location) {
        this(location.getLatitude(), location.getLongitude());
    }


    public void setLatitude(double latitude) {
        checkCoordinate(latitude, MAX_LATITUDE_VALUE, "Latitude value not valid");

        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        checkCoordinate(longitude, MAX_LONGITUDE_VALUE, "Longitude value not valid");


        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLocation(MyLocation location) {
        setLatitude(location.getLatitude());
        setLongitude(location.getLongitude());
    }

    public MyLocation getLocation() {
        return new MyLocation(latitude, longitude);
    }


    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof MyLocation) {
            MyLocation thatLocation = (MyLocation) that;

            return latitude == thatLocation.getLatitude() && longitude == thatLocation.getLongitude();
        }

        return false;
    }

    @Override
    public String toString() {
        return "Location: (" + getLatitude() + ", " + getLongitude() + ")\n";
    }
}
