package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.events.Event;

public class SimplifiedEvent {
    private String creatorMail;
    private List<String> participants;
    private String address;
    private GeoPoint loc;
    private Date dateTime;
    private String title;
    private String description;

    static final String CREATORMAIL = "creatorMail";
    static final String PARTICIPANTS = "participants";
    static final String ADDRESS = "adress";
    static final String LOC = "location";
    static final String DATETIME = "dateTime";
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";

    public SimplifiedEvent() {
    }

    public SimplifiedEvent(Event e) {
//        this.uid = uid;
        this.creatorMail = e.getCreator().getEmailAddress();
        this.participants = new ArrayList<>();
        for (User m :  e.getParticipants()) {
            this.participants.add(m.getEmailAddress());
        }
        this.address = e.getAddress();
        this.loc = e.getGeoPoint();
        this.dateTime = e.getDateTime().toDate();
        this.title = e.getTitle();
        this.description = e.getDescription();
    }

    public String getCreatorMail() {
        return creatorMail;
    }

    public void setCreatorMail(String creatorMail) {
        this.creatorMail = creatorMail;
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

    public GeoPoint getLoc() {
        return loc;
    }

    public void setLoc(GeoPoint loc) {
        this.loc = loc;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Map<String, Object> toMap() {
        Map<String, Object> res = new HashMap<>();
        res.put(CREATORMAIL, creatorMail);
        res.put(PARTICIPANTS, participants);
        res.put(ADDRESS, address);
        res.put(LOC, loc);
        res.put(DATETIME, dateTime);
        res.put(TITLE, title);
        res.put(DESCRIPTION, description);
        return res;
    }
}
