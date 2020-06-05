package ch.epfl.sdp.musiconnect.events;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.musiconnect.functionnalities.MyDate;
import ch.epfl.sdp.musiconnect.functionnalities.MyLocation;

@Entity
public class Event {
    @PrimaryKey
    @NonNull
    private String eid;
    @Ignore
    private String hostEmailAddress;

    @Ignore
    private List<String> participants;

    private MyLocation location;
    private String address;
    private MyDate dateTime;
    private boolean visible;
    private String title;
    private String description;

    private static final String DEFAULT_TITLE = "Event";
    private static final String DEFAULT_MESSAGE = "Come watch and play!";

    private static final double MAX_LATITUDE = 90;
    private static final double MAX_LONGITUDE = 180;

    public Event(String hostEmailAddress, String eid) {
        if (hostEmailAddress == null) {
            throw new IllegalArgumentException();
        }

        this.eid = eid;
        this.hostEmailAddress = hostEmailAddress;

        participants = new ArrayList<>();
        participants.add(hostEmailAddress);

        location = new MyLocation(0,0);
        dateTime = new MyDate();
        visible = false;
        title = DEFAULT_TITLE;
        description = DEFAULT_MESSAGE;
        address = "";
    }

    //this constructor should only be used with room database caching
    public Event(){
        location = new MyLocation(0,0);
        dateTime = new MyDate();
        visible = false;
        title = DEFAULT_TITLE;
        description = DEFAULT_MESSAGE;
        address = "";
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getEid() {
        return eid;
    }

    public String getHostEmailAddress() {
        return hostEmailAddress;
    }

    public void register(String user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }

        if (!participants.contains(user)) {
            participants.add(user);
        }
    }

    public void unregister(String user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }

        participants.remove(user);
    }

    public boolean containsParticipant(String email) {
        for (String participant: participants) {
            if (participant.equals(email)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getParticipants() {
        return participants;
    }

    private boolean checkLocationValues(double latitude, double longitude) {
        return (!(latitude > -MAX_LATITUDE)) || (!(latitude < MAX_LATITUDE)) ||
                (!(longitude > -MAX_LONGITUDE)) || (!(longitude < MAX_LONGITUDE));
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
        this.location.setLatitude(location.getLatitude());
        this.location.setLongitude(location.getLongitude());
    }

    public void setLocation(MyLocation location) {
        if (checkLocationValues(location.getLatitude(), location.getLongitude())) {
            throw new IllegalArgumentException();
        }
        this.location.setLatitude(location.getLatitude());
        this.location.setLongitude(location.getLongitude());
    }

    public void setLocation(double latitude, double longitude) {
        if (checkLocationValues(latitude, longitude)) {
            throw new IllegalArgumentException();
        }
        location.setLatitude(latitude);
        location.setLongitude(longitude);
    }

    public MyLocation getLocation(){
        return location;
    }

    public GeoPoint getGeoPoint() {
        return new GeoPoint(location.getLatitude(), location.getLongitude());
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
