package ch.epfl.sdp.musiconnect;

import android.location.Location;

public class Event {
    private User creator;
    private Location location;
    private MyDate dateTime;
    private boolean visible;
    private String message;
    private final String defaultMessage = "Come watch and play!";

    public Event(User creator) {
        this.creator = creator;
        location = new Location("");
        dateTime = new MyDate();
        visible = false;
        message = defaultMessage;
    }

    public Event(User creator, Location location, MyDate dateTime, boolean visible) {
        this.creator  = creator;
        this.location = location;
        this.dateTime = dateTime;
        this.visible  = visible;
        message = defaultMessage;
    }

    public User getCreator() {
        return creator;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setDateTime(MyDate dateTime) {
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

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
