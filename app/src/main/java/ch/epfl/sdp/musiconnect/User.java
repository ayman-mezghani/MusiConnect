package ch.epfl.sdp.musiconnect;

/**
 * @author Manuel Pellegrini, EPFL
 */
public abstract class User {

    private MyDate joinDate;
    private Location location;

    private static final double EPFL_LATITUDE = 46.5185941;
    private static final double EPFL_LONGITUDE = 6.5618969;


    public User() {
        joinDate = new MyDate();
        location = new Location(EPFL_LATITUDE, EPFL_LONGITUDE);
    }


    public MyDate getJoinDate() {
        return new MyDate(joinDate);
    }

    public void setLocation(Location location) {
        this.location.setLocation(location);
    }

    public Location getLocation() {
        return location.getLocation();
    }

}
