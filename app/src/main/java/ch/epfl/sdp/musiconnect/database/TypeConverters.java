package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.MyLocation;

final class TypeConverters {
    private static final int YEAR_BIAS = 1900;
    private static final int MONTH_BIAS = 1;

    static Date myDateToDate(MyDate myDate) {
        return new Date(myDate.getYear() - YEAR_BIAS, myDate.getMonth() - MONTH_BIAS, myDate.getDate(), myDate.getHours(), myDate.getMinutes());
    }

    static MyDate dateToMyDate(Date date) {
        return new MyDate(date.getYear() + YEAR_BIAS, date.getMonth() + MONTH_BIAS, date.getDate(), date.getHours(), date.getMinutes());
    }

    static GeoPoint myLocationToGeoPoint(MyLocation loc) {
        return new GeoPoint(loc.getLatitude(), loc.getLongitude());
    }

    static MyLocation geoPointToMyLocation(GeoPoint loc) {
        return new MyLocation(loc.getLatitude(), loc.getLongitude());
    }
}
