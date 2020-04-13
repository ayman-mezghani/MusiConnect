package ch.epfl.sdp.musiconnect.RoomDatabase;

import androidx.room.TypeConverter;

import ch.epfl.sdp.musiconnect.MyLocation;

public class MyLocationConverter {
    @TypeConverter
    public static String myLocationToString(MyLocation location){
        return Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
    }

    @TypeConverter
    public static MyLocation strToMyLocation(String location){
        String[] latlon = location.split(",");
        return new MyLocation(Double.valueOf(latlon[0]),Double.valueOf(latlon[1]));
    }
}
