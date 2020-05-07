package ch.epfl.sdp.musiconnect;

import org.junit.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class MyDateClassUnitTests {

    @Test
    public void gettersOfMyDateClassWork() {
        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        assertEquals(year, day.getYear());
        assertEquals(month, day.getMonth());
        assertEquals(date, day.getDate());
        assertEquals(hours, day.getHours());
        assertEquals(minutes, day.getMinutes());
    }

    @Test
    public void setYearWorksWithValidInput() {
        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        int newYear = 2021;

        assertEquals(year, day.getYear());
        day.setYear(newYear);
        assertEquals(newYear, day.getYear());
    }

    @Test
    public void setYearThrowsExceptionIfYearIsNegative() {
        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        int newYear = -1;

        assertThrows(IllegalArgumentException.class, () -> day.setYear(newYear));
    }

    @Test
    public void setMonthWorksWithValidInput() {
        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        int newMonth = 3;

        assertEquals(month, day.getMonth());
        day.setMonth(newMonth);
        assertEquals(newMonth, day.getMonth());
    }

    @Test
    public void setMonthThrowsExceptionWithInvalidInput() {
        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        int lowMonth = 0;
        int highMonth = 13;

        assertThrows(IllegalArgumentException.class, () -> day.setMonth(lowMonth));
        assertThrows(IllegalArgumentException.class, () -> day.setMonth(highMonth));
    }

    @Test
    public void isLeapYearWorks() {
        int year = 2020;
        int month = 1;
        int date = 15;
        MyDate day = new MyDate(year, month, date);

        assertEquals(true, day.isLeapYear());
        day.setYear(2000);
        assertEquals(true, day.isLeapYear());
        day.setYear(1900);
        assertEquals(false, day.isLeapYear());
        day.setYear(2019);
        assertEquals(false, day.isLeapYear());
    }

    @Test
    public void getMaxDateOfMonthWorks() {
        int year = 2020;
        int month = 1;
        int date = 15;
        MyDate day = new MyDate(year, month, date);

        assertEquals(31, day.getMaxDateOfMonth());
        day.setMonth(2);
        assertEquals(29, day.getMaxDateOfMonth());
        day.setYear(2000);
        assertEquals(29, day.getMaxDateOfMonth());
        day.setYear(1900);
        assertEquals(28, day.getMaxDateOfMonth());
        day.setYear(2019);
        assertEquals(28, day.getMaxDateOfMonth());
        day.setMonth(3);
        assertEquals(31, day.getMaxDateOfMonth());
        day.setMonth(4);
        assertEquals(30, day.getMaxDateOfMonth());
        day.setMonth(5);
        assertEquals(31, day.getMaxDateOfMonth());
        day.setMonth(6);
        assertEquals(30, day.getMaxDateOfMonth());
        day.setMonth(7);
        assertEquals(31, day.getMaxDateOfMonth());
        day.setMonth(8);
        assertEquals(31, day.getMaxDateOfMonth());
        day.setMonth(9);
        assertEquals(30, day.getMaxDateOfMonth());
        day.setMonth(10);
        assertEquals(31, day.getMaxDateOfMonth());
        day.setMonth(11);
        assertEquals(30, day.getMaxDateOfMonth());
        day.setMonth(12);
        assertEquals(31, day.getMaxDateOfMonth());
    }

    @Test
    public void setDateWorksWithValidInput() {
        int year = 2020;
        int month = 2;
        int date = 2;
        MyDate day = new MyDate(year, month, date);

        int newDate = 4;

        assertEquals(date, day.getDate());
        day.setDate(newDate);
        assertEquals(newDate, day.getDate());
    }

    @Test
    public void setDateThrowsExceptionWithInvalidInput() {
        int year = 2020;
        int month = 2;
        int date = 2;
        MyDate day = new MyDate(year, month, date);

        int lowDate = 0;
        int highDate = 30;
        int firstInvalidYear = 1582;
        int firstInvalidMonth = 10;
        int firstInvalidDate = 10;
        int secondInvalidYear = 1752;
        int secondInvalidMonth = 9;
        int secondInvalidDate = 9;

        assertThrows(IllegalArgumentException.class, () -> day.setDate(lowDate));
        assertThrows(IllegalArgumentException.class, () -> day.setDate(highDate));

        day.setYear(firstInvalidYear);
        day.setMonth(firstInvalidMonth);
        assertThrows(IllegalArgumentException.class, () -> day.setDate(firstInvalidDate));

        day.setYear(secondInvalidYear);
        day.setMonth(secondInvalidMonth);
        assertThrows(IllegalArgumentException.class, () -> day.setDate(secondInvalidDate));
    }

    @Test
    public void setHoursWorksWithValidInput() {
        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        int newHours = 22;

        assertEquals(hours, day.getHours());
        day.setHours(newHours);
        assertEquals(newHours, day.getHours());
    }

    @Test
    public void setHoursThrowsExceptionWithInvalidInput() {
        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        int lowHours = -5;
        int highHours = 24;

        assertThrows(IllegalArgumentException.class, () -> day.setHours(lowHours));
        assertThrows(IllegalArgumentException.class, () -> day.setHours(highHours));
    }

    @Test
    public void setMinutesWorksWithValidInput() {
        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        int newMinutes = 59;

        assertEquals(minutes, day.getMinutes());
        day.setMinutes(newMinutes);
        assertEquals(newMinutes, day.getMinutes());
    }

    @Test
    public void setMinutesThrowsExceptionWithInvalidInput() {
        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        int lowMinutes = -5;
        int highMinutes = 60;

        assertThrows(IllegalArgumentException.class, () -> day.setMinutes(lowMinutes));
        assertThrows(IllegalArgumentException.class, () -> day.setMinutes(highMinutes));
    }

    @Test
    public void afterOfMyDateWorks() {
        int firstYear = 2020;
        int firstMonth = 2;
        int firstDate = 2;
        int firstHours = 20;
        int firstMinutes = 20;
        MyDate firstDay = new MyDate(firstYear, firstMonth, firstDate, firstHours, firstMinutes);
        MyDate secondDay = new MyDate(firstDay);

        int secondYear = 2019;
        int secondMonth = 1;
        int secondDate = 1;
        int secondHours = 19;
        int secondMinutes = 19;

        assertEquals(false, firstDay.after(secondDay));
        secondDay.setMinutes(secondMinutes);
        assertEquals(true, firstDay.after(secondDay));
        secondDay.setHours(secondHours);
        assertEquals(true, firstDay.after(secondDay));
        secondDay.setDate(secondDate);
        assertEquals(true, firstDay.after(secondDay));
        secondDay.setMonth(secondMonth);
        assertEquals(true, firstDay.after(secondDay));
        secondDay.setYear(secondYear);
        assertEquals(true, firstDay.after(secondDay));
    }

    @Test
    public void beforeOfMyDateWorks() {
        int firstYear = 2020;
        int firstMonth = 2;
        int firstDate = 2;
        int firstHours = 20;
        int firstMinutes = 20;
        MyDate firstDay = new MyDate(firstYear, firstMonth, firstDate, firstHours, firstMinutes);
        MyDate secondDay = new MyDate(firstDay);

        int secondYear = 2019;
        int secondMonth = 1;
        int secondDate = 1;
        int secondHours = 19;
        int secondMinutes = 19;

        assertEquals(false, secondDay.before(firstDay));
        secondDay.setMinutes(secondMinutes);
        assertEquals(true, secondDay.before(firstDay));
        secondDay.setHours(secondHours);
        assertEquals(true, secondDay.before(firstDay));
        secondDay.setDate(secondDate);
        assertEquals(true, secondDay.before(firstDay));
        secondDay.setMonth(secondMonth);
        assertEquals(true, secondDay.before(firstDay));
        secondDay.setYear(secondYear);
        assertEquals(true, secondDay.before(firstDay));
    }

    @Test
    public void equalsOfMyDateClassWorksWithSameInstance() {
        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        assertEquals(true, day.equals(day));
    }

    @Test
    public void equalsOfMyDateClassWorksWithSameDate() {
        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        MyDate thatDay = new MyDate(day);

        assertEquals(true, day.equals(thatDay));
    }

    @Test
    public void equalsOfMyDateClassWorksWithDifferentDate() {
        int firstYear = 2020;
        int firstMonth = 2;
        int firstDate = 2;
        int firstHours = 20;
        int firstMinutes = 20;
        MyDate firstDay = new MyDate(firstYear, firstMonth, firstDate, firstHours, firstMinutes);

        int secondYear = 2019;
        int secondMonth = 1;
        int secondDate = 1;
        int secondHours = 19;
        int secondMinutes = 19;
        MyDate secondDay = new MyDate(secondYear, secondMonth, secondDate, secondHours, secondMinutes);

        assertEquals(false, firstDay.equals(secondDay));
    }

    @Test
    public void equalsOfMyDateClassWorksWithObjectDifferentFromMyDate() {
        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        double latitude = 0.0;
        double longitude = 0.0;
        MyLocation location = new MyLocation(latitude, longitude);

        assertEquals(false, day.equals(location));
    }

    @Test
    public void toStringOfMyDateClassWorks() {
        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        String expectedString = String.format("%02d", date) + "/" + String.format("%02d", month) + "/" + String.format("%04d", year) + "    " + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + "\n";

        assertEquals(expectedString, day.toString());
    }

    @Test
    public void toDateWorks() {
        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        Date d = new Date(year-MyDate.YEAR_BIAS, month-MyDate.MONTH_BIAS, date, hours, minutes);
        assertEquals(d, day.toDate());

        MyDate newMyDate = new MyDate(d);
        assertEquals(d, newMyDate.toDate());

    }

}
