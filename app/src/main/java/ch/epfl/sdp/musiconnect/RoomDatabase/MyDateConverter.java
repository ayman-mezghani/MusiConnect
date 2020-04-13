package ch.epfl.sdp.musiconnect.RoomDatabase;

import androidx.room.TypeConverter;

import java.util.Date;

import ch.epfl.sdp.musiconnect.MyDate;

public class MyDateConverter {
    private static final int YEAR_BIAS = 1900;
    private static final int MONTH_BIAS = 1;

    @TypeConverter
    public static Date myDateToDate(MyDate myDate) {
        return new Date(myDate.getYear() - YEAR_BIAS, myDate.getMonth() - MONTH_BIAS, myDate.getDate(), myDate.getHours(), myDate.getMinutes());
    }

    @TypeConverter
    public static MyDate dateToMyDate(Date date) {
        return new MyDate(date.getYear() + YEAR_BIAS, date.getMonth() + MONTH_BIAS, date.getDate(), date.getHours(), date.getMinutes());
    }

    @TypeConverter
    public static Date fromTimestamp(Long value){
        return value == null ? null:new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date){
        return date == null?null:date.getTime();
    }
}
