package ch.epfl.sdp.musiconnect.roomdatabase;

import android.location.Location;

import androidx.room.TypeConverter;

public class LocationConverter {

    @TypeConverter
    public static String locationToString(Location l){
        return l.getLatitude() + ";" + l.getLongitude();
    }

    @TypeConverter
    public static Location stringToLocation(String s){
        String[] split = s.split(";");
        Location l = new Location("");
        l.setLatitude(Double.valueOf(split[0]));
        l.setLongitude(Double.valueOf(split[1]));
        return l;
    }
}
