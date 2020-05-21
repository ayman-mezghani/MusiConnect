package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.events.Event;

import static ch.epfl.sdp.musiconnect.database.TypeConverters.myDateToDate;

public class SimplifiedEvent extends SimplifiedDbEntry {
    private String host;
    private List<String> participants;
    private String address;
    private GeoPoint location;
    private Date dateTime;
    private String eventName;
    private String description;
    private boolean visible;

    public SimplifiedEvent() {
    }

    public SimplifiedEvent(Event e) {
        this.host = e.getCreator().getEmailAddress();
        this.participants = new ArrayList<>();
        for (User m : e.getParticipants()) {
            this.participants.add(m.getEmailAddress());
        }
        this.address = e.getAddress();
        this.location = e.getGeoPoint();
        this.dateTime = myDateToDate(e.getDateTime());
        this.eventName = e.getTitle();
        this.description = e.getDescription();
        this.visible = e.isVisible();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String creatorMail) {
        this.host = creatorMail;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> res = new HashMap<>();
        res.put(Fields.host.toString(), host);
        res.put(Fields.participants.toString(), participants);
        res.put(Fields.address.toString(), address);
        res.put(Fields.location.toString(), location);
        res.put(Fields.dateTime.toString(), dateTime);
        res.put(Fields.eventName.toString(), eventName);
        res.put(Fields.description.toString(), description);
        res.put(Fields.visible.toString(), visible);
        return res;
    }
}
