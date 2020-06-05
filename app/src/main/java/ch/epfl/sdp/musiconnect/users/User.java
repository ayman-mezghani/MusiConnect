package ch.epfl.sdp.musiconnect.users;

import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.musiconnect.functionnalities.MyDate;
import ch.epfl.sdp.musiconnect.functionnalities.MyLocation;

/**
 * @author Manuel Pellegrini, EPFL
 */
public abstract class User {

    private MyDate joinDate;
    private MyLocation location;

    private static final double EPFL_LATITUDE = 46.5185941;
    private static final double EPFL_LONGITUDE = 6.5618969;
    @Ignore
    protected List<String> events;


    public User() {
        joinDate = new MyDate();
        location = new MyLocation(EPFL_LATITUDE, EPFL_LONGITUDE);
        events = new ArrayList<>();
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

    public abstract String getEmailAddress();



    public void addEvent(String eventId) { this.events.add(eventId); }

    public void removeEvent(String eventId) {
        events.remove(eventId);
    }

    public List<String> getEvents() { return this.events; }

    public void setEvents(List<String> e) {
        this.events = new ArrayList<>();

        if (e != null)
            this.events = e;
    }
}
