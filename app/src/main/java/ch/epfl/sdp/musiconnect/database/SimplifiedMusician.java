package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.MyLocation;

public class SimplifiedMusician {
    private String uid = null;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Date birthday;
    private Date joinDate;
    private GeoPoint location;

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
        this.birthday = myDateToDate(musician.getBirthday());
        this.joinDate = myDateToDate(musician.getJoinDate());
        this.location = myLocationToGeoPoint(musician.getLocation());
    }

    public Musician toMusician() {
        Musician musician = new Musician(firstName, lastName, username, email, dateToMyDate(birthday));
        musician.setLocation(geoPointToMyLocation(location));
        return musician;
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

    public String getUid() {
        return uid;
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
}
