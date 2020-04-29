package ch.epfl.sdp.musiconnect.roomdatabase;

import androidx.room.TypeConverter;

import java.util.Date;

import ch.epfl.sdp.musiconnect.MyDate;

public class MyDateConverter {
    private static final int YEAR_BIAS = 1900;
    private static final int MONTH_BIAS = 1;

    public static Date myDateToDate(MyDate myDate) {
        return new Date(myDate.getYear() - YEAR_BIAS, myDate.getMonth() - MONTH_BIAS, myDate.getDate(), myDate.getHours(), myDate.getMinutes());
    }

    public static MyDate dateToMyDate(Date date) {
        return new MyDate(date.getYear() + YEAR_BIAS, date.getMonth() + MONTH_BIAS, date.getDate(), date.getHours(), date.getMinutes());
    }

    @TypeConverter
    public static MyDate fromTimestamp(Long value){
        return value == null ? null:dateToMyDate(new Date(value));
    }

    @TypeConverter
    public static Long dateToTimestamp(MyDate date){
        return date == null?null:myDateToDate(date).getTime();
    }
}
