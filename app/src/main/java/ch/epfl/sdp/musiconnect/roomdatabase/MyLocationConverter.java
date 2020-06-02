package ch.epfl.sdp.musiconnect.roomdatabase;

import androidx.room.TypeConverter;

import ch.epfl.sdp.musiconnect.MyLocation;

public class MyLocationConverter {
    @TypeConverter
    public static String myLocationToString(MyLocation location){
        return location.getLatitude() + "," + location.getLongitude();
    }

    @TypeConverter
    public static MyLocation strToMyLocation(String location){
        String[] latlon = location.split(",");
        return new MyLocation(Double.parseDouble(latlon[0]),Double.parseDouble(latlon[1]));
    }
}
