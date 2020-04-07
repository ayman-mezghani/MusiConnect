package ch.epfl.sdp.musiconnect;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private User creator;
    private List<User> participants; // Users that will contribute to the event (e.g. two musicians doing a duo at x event)
    private Location location;
    private MyDate dateTime;
    private boolean visible;
    private String title;
    private String message;
    private final String defaultTitle = "Event";
    private final String defaultMessage = "Come watch and play!";

    public Event(User creator) {
        this.creator = creator;
        participants = new ArrayList<>();
        participants.add(creator);

        location = new Location("");
        dateTime = new MyDate();
        visible = false;
        title = defaultTitle;
        message = defaultMessage;
    }

    public User getCreator() {
        return creator;
    }

    public void register(User user) {
        participants.add(user);
    }

    public void unregister(User user) {
        participants.remove(user);
    }

    public List<User> getParticipants() {
        return participants;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
