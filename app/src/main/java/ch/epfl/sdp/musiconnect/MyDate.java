package ch.epfl.sdp.musiconnect;

import java.util.Date;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class MyDate {

    private int year;
    private int month;
    private int date;
    private int hours;
    private int minutes;

    private static final int YEAR_CORRECTION = 1900;
    private static final int MONTH_CORRECTION = 1;
    private static final int DATE_CORRECTION = 0;
    private static final int HOURS_CORRECTION = 0;
    private static final int MINUTES_CORRECTION = 0;


    public static final int YEAR_BIAS = 1900;
    public static final int MONTH_BIAS = 1;

    public MyDate(int year, int month, int date, int hours, int minutes) {
        setYear(year);
        setMonth(month);
        setDate(date);
        setHours(hours);
        setMinutes(minutes);
    }

    public MyDate(int year, int month, int date) {
        this(year, month, date, 0, 0);
    }

    public MyDate(MyDate date) {
        this(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes());
    }

    public MyDate(Date date) {
        this(date.getYear() + YEAR_BIAS, date.getMonth() + MONTH_BIAS, date.getDate(), date.getHours(), date.getMinutes());
    }

    public MyDate() {
        Date currentDate = new Date();
        setYear(currentDate.getYear() + YEAR_CORRECTION);
        setMonth(currentDate.getMonth() + MONTH_CORRECTION);
        setDate(currentDate.getDate() + DATE_CORRECTION);
        setHours(currentDate.getHours() + HOURS_CORRECTION);
        setMinutes(currentDate.getMinutes() + MINUTES_CORRECTION);
    }


    public void setYear(int year) {
        if (year < 0) {
            throw new IllegalArgumentException("Year cannot be negative");
        }

        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setMonth(int month) {
        if (month < 1 || 12 < month) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    protected boolean isLeapYear() {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    protected int getMaxDateOfMonth() {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                if (isLeapYear()) {
                    return 29;
                } else {
                    return 28;
                }
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                throw new Error("Month value should be between 1 and 12");
        }
    }

    public void setDate(int date) {
        if (date < 1 || getMaxDateOfMonth() < date) {
            throw new IllegalArgumentException("Date is invalid for this month");
        } else if (year == 1582 && month == 10 && 4 < date && date < 15) {
            throw new IllegalArgumentException("Date is invalid for october of 1582");
        } else if (year == 1752 && month == 9 && 2 < date && date < 14) {
            throw new IllegalArgumentException("Date is invalid for september of 1752");
        }

        this.date = date;
    }

    public int getDate() {
        return date;
    }

    public void setHours(int hours) {
        if (hours < 0 || 23 < hours) {
            throw new IllegalArgumentException("Hours must be between 0 and 23");
        }

        this.hours = hours;
    }

    public int getHours() {
        return hours;
    }

    public void setMinutes(int minutes) {
        if (minutes < 0 || 59 < minutes) {
            throw new IllegalArgumentException("Minutes must be between 0 and 59");
        }

        this.minutes = minutes;
    }

    public int getMinutes() {
        return minutes;
    }

    public boolean after(MyDate thatDate) {
        if (year > thatDate.getYear() ||
                year == thatDate.getYear() && month > thatDate.getMonth() ||
                year == thatDate.getYear() && month == thatDate.getMonth() && date > thatDate.getDate() ||
                year == thatDate.getYear() && month == thatDate.getMonth() && date == thatDate.getDate() && hours > thatDate.getHours() ||
                year == thatDate.getYear() && month == thatDate.getMonth() && date == thatDate.getDate() && hours == thatDate.getHours() && minutes > thatDate.getMinutes()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean before(MyDate thatDate) {
        return thatDate.after(this);
    }


    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof MyDate) {
            MyDate thatDate = (MyDate) that;

            if (year == thatDate.getYear() && month == thatDate.getMonth() && date == thatDate.getDate() && hours == thatDate.getHours() && minutes == thatDate.getMinutes()) {
                return true;
            }
        }

        return false;
    }

    public Date toDate() {
        return new Date(this.year - YEAR_BIAS, this.month - MONTH_BIAS, this.getDate(), this.getHours(), this.getMinutes());
    }

    @Override
    public String toString() {
        return String.format("%02d", date) + "/" + String.format("%02d", month) + "/" + String.format("%04d", year) + "    " + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + "\n";
    }

}
