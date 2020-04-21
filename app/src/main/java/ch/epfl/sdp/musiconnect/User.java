package ch.epfl.sdp.musiconnect;

/**
 * @author Manuel Pellegrini, EPFL
 */
public abstract class User {

    private MyDate joinDate;
    private MyLocation location;

    private static final double EPFL_LATITUDE = 46.5185941;
    private static final double EPFL_LONGITUDE = 6.5618969;


    public User() {
        joinDate = new MyDate();
        location = new MyLocation(EPFL_LATITUDE, EPFL_LONGITUDE);
    }


    public MyDate getJoinDate() {
        return new MyDate(joinDate);
    }

    public void setJoinDate(MyDate join){ joinDate = new MyDate(join);}

    public void setLocation(MyLocation location) {
        this.location.setLocation(location);
    }

    public MyLocation getLocation() {
        return location.getLocation();
    }

    public abstract String getName();
}
