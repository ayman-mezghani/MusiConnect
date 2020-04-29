package ch.epfl.sdp.musiconnect.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.MyLocation;
import ch.epfl.sdp.musiconnect.TypeOfUser;

public class SimplifiedMusician extends SimplifiedDbEntry {
//    @TODO use this enum instead of the static fields
//    public enum Fields {
//        username, firstName, lastName, email, typeOfUser, birthday, joinDate, LOCATlocationION;
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

    static final String USERNAME = "username";
    static final String FIRSTNAME = "firstName";
    static final String LASTNAME = "lastName";
    static final String EMAIL = "email";
    static final String TYPEOFUSER = "typeOfUser";
    static final String BIRTHDAY = "birthday";
    static final String JOINDATE = "joinDate";
    static final String LOCATION = "location";

    private static final int YEAR_BIAS = 1900;
    private static final int MONTH_BIAS = 1;

    public SimplifiedMusician() {
    }

    public SimplifiedMusician(Musician musician) {
//        this.uid = uid;
        this.username = musician.getUserName();
        this.firstName = musician.getFirstName();
        this.lastName = musician.getLastName();
        this.email = musician.getEmailAddress();
        this.typeOfUser = musician.getTypeOfUser().toString();
        this.birthday = myDateToDate(musician.getBirthday());
        this.joinDate = myDateToDate(musician.getJoinDate());
        this.location = myLocationToGeoPoint(musician.getLocation());
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
    }

    public Musician toMusician() {
        Musician musician = new Musician(firstName, lastName, username, email, dateToMyDate(birthday));
        musician.setLocation(geoPointToMyLocation(location));
        musician.setTypeOfUser(TypeOfUser.valueOf(typeOfUser));
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
        return res;
    }

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

    @Override
    public boolean equals(@Nullable Object obj) {
        SimplifiedMusician that = (SimplifiedMusician) obj;
        return this.username == that.getUsername()
                && this.firstName == that.getFirstName()
                && this.lastName == that.getLastName()
                && this.email == that.getEmail()
                && this.birthday.toString().equals(that.getBirthday().toString())
                && this.typeOfUser == that.typeOfUser;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }
}
