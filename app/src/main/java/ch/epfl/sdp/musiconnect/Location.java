package ch.epfl.sdp.musiconnect;

public class Location {

    private double latitude;
    private double longitude;


    public Location(double newLatitude, double newLongitude) {
        latitude = newLatitude;
        longitude = newLongitude;
    }

    public Location(Location newLocation) {
        new Location(newLocation.getLatitude(), newLocation.getLongitude());
    }


    public void setLatitude(double newLatitude) {
        latitude = newLatitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double newLongitude) {
        longitude = newLongitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLocation(Location newLocation) {
        latitude = newLocation.getLatitude();
        longitude = newLocation.getLongitude();
    }

    public Location getLocation() {
        return new Location(latitude, longitude);
    }


    @Override
    public String toString() {
        return "Location: (" + getLatitude() + ", " + getLongitude() + ")\n";
    }

}
