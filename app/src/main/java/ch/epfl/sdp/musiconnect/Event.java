package ch.epfl.sdp.musiconnect;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private int eid;
    private User creator;

    private List<User> participants;
    private List<Musician> musicians;
    private List<Band> bands;

    private LatLng location;
    private String address;
    private MyDate dateTime;
    private boolean visible;
    private String title;
    private String description;
    private final String DEFAULT_TITLE = "Event";
    private final String DEFAULT_MESSAGE = "Come watch and play!";

    public Event(Musician creator, int eid) {
        setup(creator, eid);
        register(creator);
    }


    public Event(Band creator, int eid) {
        setup(creator, eid);
        register(creator);
    }

    private void setup(User creator, int eid) {
        if (creator == null) {
            throw new IllegalArgumentException();
        }

        this.eid = eid;
        this.creator = creator;

        musicians = new ArrayList<>();
        bands = new ArrayList<>();
        participants = new ArrayList<>();
        location = new LatLng(0, 0);
        dateTime = new MyDate();
        visible = false;
        title = DEFAULT_TITLE;
        description = DEFAULT_MESSAGE;
        address = "";
    }

    protected void setEid(int eid) {
        this.eid = eid;
    }

    public int getEid() {
        return eid;
    }

    public User getCreator() {
        return creator;
    }

    public void register(Musician musician) {
        if (musician == null) {
            throw new IllegalArgumentException();
        }

        musicians.add(musician);
        participants.add(musician);
    }

    public void unregister(Musician musician) {
        if (musician == null) {
            throw new IllegalArgumentException();
        }

        musicians.remove(musician);
        participants.remove(musician);
    }


    public void register(Band band) {
        if (band == null) {
            throw new IllegalArgumentException();
        }

        bands.add(band);
        participants.add(band);
    }

    public void unregister(Band band) {
        if (band == null) {
            throw new IllegalArgumentException();
        }

        bands.remove(band);
        participants.remove(band);
    }

    public List<Musician> getMusicians() {
        return musicians;
    }

    public List<Band> getBands() {
        return bands;
    }

    public List<User> getParticipants() {
        return participants;
    }

    private boolean checkLocationValues(double latitude, double longitude) {
        return (!(latitude > -90)) || (!(latitude < 90)) ||
                (!(longitude > -180)) || (!(longitude < 180));
    }


    public void setAddress(String address) {
        if (address == null) {
            throw new IllegalArgumentException();
        }

        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setLocation(Location location) {
        if (checkLocationValues(location.getLatitude(), location.getLongitude())) {
            throw new IllegalArgumentException();
        }

        this.location = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void setLocation(double latitude, double longitude) {
        if (checkLocationValues(latitude, longitude)) {
            throw new IllegalArgumentException();
        }
        location = new LatLng(latitude, longitude);
    }

    public Location getLocation() {
        Location l = new Location("");
        l.setLatitude(location.latitude);
        l.setLongitude(location.longitude);
        return l;
    }

    public GeoPoint getGeoPoint() {
        return new GeoPoint(location.latitude, location.longitude);
    }

    public void setDateTime(MyDate dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException();
        }

        this.dateTime = dateTime;
    }

    public MyDate getDateTime() {
        return dateTime;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException();
        }
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException();
        }
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
