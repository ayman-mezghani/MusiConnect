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
import ch.epfl.sdp.musiconnect.users.Musician;
import ch.epfl.sdp.musiconnect.UserType;

import static ch.epfl.sdp.musiconnect.database.TypeConverters.dateToMyDate;
import static ch.epfl.sdp.musiconnect.database.TypeConverters.geoPointToMyLocation;
import static ch.epfl.sdp.musiconnect.database.TypeConverters.myDateToDate;
import static ch.epfl.sdp.musiconnect.database.TypeConverters.myLocationToGeoPoint;

public class SimplifiedMusician extends SimplifiedDbEntry {

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

    public SimplifiedMusician() {
    }

    public SimplifiedMusician(Musician musician) {
        this.username = musician.getUserName();
        this.firstName = musician.getFirstName();
        this.lastName = musician.getLastName();
        this.email = musician.getEmailAddress();
        this.typeOfUser = musician.getUserType().toString();
        this.birthday = myDateToDate(musician.getBirthday());
        this.joinDate = myDateToDate(musician.getJoinDate());
        this.location = myLocationToGeoPoint(musician.getLocation());
        this.events = musician.getEvents();
        this.instruments = instrumentMapToList(musician.getInstruments());
    }

    public SimplifiedMusician(Map<String, Object> map) {
        this.username = map.get(Fields.username.toString()) == null ? "" : (String) map.get(Fields.username.toString());
        this.firstName = map.get(Fields.firstName.toString()) == null ? "" : (String) map.get(Fields.firstName.toString());
        this.lastName = map.get(Fields.lastName.toString()) == null ? "" : (String) map.get(Fields.lastName.toString());
        this.email = map.get(Fields.email.toString()) == null ? "" : (String) map.get(Fields.email.toString());
        this.typeOfUser = map.get(Fields.typeOfUser.toString()) == null ? "" : (String) map.get(Fields.typeOfUser.toString());
        this.birthday = map.get(Fields.birthday.toString()) == null ? null : ((Timestamp) map.get(Fields.birthday.toString())).toDate();
        this.joinDate = map.get(Fields.joinDate.toString()) == null ? null : ((Timestamp) map.get(Fields.joinDate.toString())).toDate();
        this.location = map.get(Fields.location.toString()) == null ? null : (GeoPoint) map.get(Fields.location.toString());
        this.events = map.get(Fields.events.toString()) == null ? null : (List<String>) map.get(Fields.events.toString());
        this.instruments = map.get(Fields.instruments.toString()) == null ? null : (List<Map<String, String>>) map.get(Fields.instruments.toString());
    }

    public Musician toMusician() {
        Musician musician = new Musician(firstName, lastName, username, email, dateToMyDate(birthday));
        musician.setLocation(geoPointToMyLocation(location));
        musician.setUserType(UserType.valueOf(typeOfUser));
        musician.setEvents(events);
        if (instruments != null)
            musician.setInstruments(instrumentListToMap(instruments));
        return musician;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> res = new HashMap<>();
        res.put(Fields.username.toString(), username);
        res.put(Fields.firstName.toString(), firstName);
        res.put(Fields.lastName.toString(), lastName);
        res.put(Fields.email.toString(), email);
        res.put(Fields.typeOfUser.toString(), typeOfUser);
        res.put(Fields.birthday.toString(), birthday);
        res.put(Fields.joinDate.toString(), joinDate);
        res.put(Fields.location.toString(), location);
        res.put(Fields.events.toString(), events);
        res.put(Fields.instruments.toString(), instruments);
        return res;
    }

    private List<Map<String, String>> instrumentMapToList(Map<Instrument, Level> map) {
        List<Map<String, String>> res = new ArrayList<>();
        for (Map.Entry<Instrument, Level> entry : map.entrySet()) {
            Map<String, String> m = new HashMap<>();
            m.put(Fields.instrument.toString(), entry.getKey().toString());
            m.put(Fields.level.toString(), entry.getValue().toString());
            res.add(m);
        }
        return res;
    }

    private Map<Instrument, Level> instrumentListToMap(List<Map<String, String>> l) {
        Map<Instrument, Level> res = new HashMap<>();
        for (Map<String, String> m : l) {
            res.put(Instrument.getInstrumentFromValue(m.get(Fields.instrument.toString())), Level.getLevelFromValue(m.get(Fields.level.toString())));
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
