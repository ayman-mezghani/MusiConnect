package ch.epfl.sdp.musiconnect.database;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Instrument;
import ch.epfl.sdp.musiconnect.Level;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.MyLocation;
import ch.epfl.sdp.musiconnect.TypeOfUser;

public class SimplifiedMusician extends SimplifiedDbEntry {
//    @TODO use this enum instead of the static fields
//    public enum Fields {
//        username, firstName, lastName, email, typeOfUser, birthday, joinDate, location, events;
//
//        @NonNull
//        @Override
//        public String toString() {
//            return super.toString().toLowerCase();
//        }
//    }

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String typeOfUser;
    private Date birthday;
    private Date joinDate;
    private GeoPoint location;
    private List<String> events;


    private List<Map<String, String>> instruments;

    static final String USERNAME = "username";
    static final String FIRSTNAME = "firstName";
    static final String LASTNAME = "lastName";
    static final String EMAIL = "email";
    static final String TYPEOFUSER = "typeOfUser";
    static final String BIRTHDAY = "birthday";
    static final String JOINDATE = "joinDate";
    static final String LOCATION = "location";
    static final String EVENTS = "events";
    static final String INSTRUMENTS = "instruments";
    static final String INSTRUMENT = "instrument";
    static final String LEVEL = "level";


    private static final int YEAR_BIAS = 1900;
    private static final int MONTH_BIAS = 1;

    public SimplifiedMusician() {
    }

    public SimplifiedMusician(Musician musician) {
        this.username = musician.getUserName();
        this.firstName = musician.getFirstName();
        this.lastName = musician.getLastName();
        this.email = musician.getEmailAddress();
        this.typeOfUser = musician.getTypeOfUser().toString();
        this.birthday = myDateToDate(musician.getBirthday());
        this.joinDate = myDateToDate(musician.getJoinDate());
        this.location = myLocationToGeoPoint(musician.getLocation());
        this.events = musician.getEvents();
        this.instruments = instrumentMapToList(musician.getInstruments());
    }

    public SimplifiedMusician(Map<String, Object> map) {
        this.username = map.get(USERNAME) == null ? "" : (String) map.get(USERNAME);
        this.firstName = map.get(FIRSTNAME) == null ? "" : (String) map.get(FIRSTNAME);
        this.lastName = map.get(LASTNAME) == null ? "" : (String) map.get(LASTNAME);
        this.email = map.get(EMAIL) == null ? "" : (String) map.get(EMAIL);
        this.typeOfUser = map.get(TYPEOFUSER) == null ? "" : (String) map.get(TYPEOFUSER);
        this.birthday = map.get(BIRTHDAY) == null ? null : ((Timestamp) map.get(BIRTHDAY)).toDate();
        this.joinDate = map.get(JOINDATE) == null ? null : ((Timestamp) map.get(JOINDATE)).toDate();
        this.location = map.get(LOCATION) == null ? null : (GeoPoint) map.get(LOCATION);
        this.events = map.get(EVENTS) == null ? null : (List<String>) map.get(EVENTS);
        this.instruments = map.get(INSTRUMENTS) == null ? null : (List<Map<String, String>>) map.get(INSTRUMENTS);
    }

    public Musician toMusician() {
        Musician musician = new Musician(firstName, lastName, username, email, dateToMyDate(birthday));
        musician.setLocation(geoPointToMyLocation(location));
        musician.setTypeOfUser(TypeOfUser.valueOf(typeOfUser));
        musician.setEvents(events);
        musician.setInstruments(instrumentListToMap(instruments));
        return musician;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> res = new HashMap<>();
        res.put(USERNAME, username);
        res.put(FIRSTNAME, firstName);
        res.put(LASTNAME, lastName);
        res.put(EMAIL, email);
        res.put(TYPEOFUSER, typeOfUser);
        res.put(BIRTHDAY, birthday);
        res.put(JOINDATE, joinDate);
        res.put(LOCATION, location);
        res.put(EVENTS, events);
        res.put(INSTRUMENTS, instruments);
        return res;
    }

    // TODO Those methods are also in SimplifiedEvent, may need to refactor
    private Date myDateToDate(MyDate myDate) {
        return new Date(myDate.getYear() - YEAR_BIAS, myDate.getMonth() - MONTH_BIAS, myDate.getDate(), myDate.getHours(), myDate.getMinutes());
    }

    private MyDate dateToMyDate(Date date) {
        return new MyDate(date.getYear() + YEAR_BIAS, date.getMonth() + MONTH_BIAS, date.getDate(), date.getHours(), date.getMinutes());
    }

    private GeoPoint myLocationToGeoPoint(MyLocation loc) {
        return new GeoPoint(loc.getLatitude(), loc.getLongitude());
    }

    private MyLocation geoPointToMyLocation(GeoPoint loc) {
        return new MyLocation(loc.getLatitude(), loc.getLongitude());
    }

    private List<Map<String, String>> instrumentMapToList(Map<Instrument, Level> map) {
        List<Map<String, String>> res = new ArrayList<>();
        for (Map.Entry<Instrument, Level> entry : map.entrySet()) {
            Map<String, String> m = new HashMap<>();
            m.put(INSTRUMENT, entry.getKey().toString());
            m.put(LEVEL, entry.getValue().toString());
            res.add(m);
        }
        return res;
    }

    private Map<Instrument, Level> instrumentListToMap(List<Map<String, String>> l) {
        Map<Instrument, Level> res = new HashMap<>();
        for (Map<String, String> m : l) {
            res.put(Instrument.valueOf(m.get(INSTRUMENT).toUpperCase()), Level.valueOf(m.get(LEVEL).toUpperCase()));
        }
        return res;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public List<String> getEvents() {
        return events;
    }

    public List<Map<String, String>> getInstruments() {
        return instruments;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        SimplifiedMusician that = (SimplifiedMusician) obj;
        return this.username.equals(that.getUsername())
                && this.firstName.equals(that.getFirstName())
                && this.lastName.equals(that.getLastName())
                && this.email.equals(that.getEmail())
                && this.birthday.toString().equals(that.getBirthday().toString())
                && this.typeOfUser.equals(that.typeOfUser);
    }
}
