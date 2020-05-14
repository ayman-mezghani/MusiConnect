package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.events.Event;

public class SimplifiedEvent {
    private String creatorMail;
    private List<String> participants;
    private String adress;
    private GeoPoint loc;
    private Date dateTime;
    private String title;
    private String description;
    private boolean visible;

    static final String CREATORMAIL = "creatorMail";
    static final String PARTICIPANTS = "participants";
    static final String ADRESS = "adress";
    static final String LOC = "location";
    static final String DATETIME = "dateTime";
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String VISIBLE = "visible";

    private static final int YEAR_BIAS = 1900;
    private static final int MONTH_BIAS = 1;

    public SimplifiedEvent() {
    }

    public SimplifiedEvent(Event e) {
//        this.uid = uid;
        this.creatorMail = e.getCreator().getEmailAddress();
        this.participants = new ArrayList<>();
        for (User m :  e.getParticipants()) {
            this.participants.add(m.getEmailAddress());
        }
        this.adress = e.getAddress();
        this.loc = e.getGeoPoint();
        this.dateTime = myDateToDate(e.getDateTime());
        this.title = e.getTitle();
        this.description = e.getDescription();
        this.visible = e.isVisible();
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
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

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    private Date myDateToDate(MyDate myDate) {
        return new Date(myDate.getYear() - YEAR_BIAS, myDate.getMonth() - MONTH_BIAS, myDate.getDate(), myDate.getHours(), myDate.getMinutes());
    }

    /*
    private MyDate dateToMyDate(Date date) {
        return new MyDate(date.getYear() + YEAR_BIAS, date.getMonth() + MONTH_BIAS, date.getDate(), date.getHours(), date.getMinutes());
    }

     */

    public Map<String, Object> toMap() {
        Map<String, Object> res = new HashMap<>();
        res.put(CREATORMAIL, creatorMail);
        res.put(PARTICIPANTS, participants);
        res.put(ADRESS, adress);
        res.put(LOC, loc);
        res.put(DATETIME, dateTime);
        res.put(TITLE, title);
        res.put(DESCRIPTION, description);
        res.put(VISIBLE, visible);
        return res;
    }
}
