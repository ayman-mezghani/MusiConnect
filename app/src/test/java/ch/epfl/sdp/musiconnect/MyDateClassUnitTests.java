package ch.epfl.sdp.musiconnect;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class MyDateClassUnitTests {

    // TODO : Tests for setDate() method

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

    // TODO : Tests for setDate() method

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
        Location location = new Location(latitude, longitude);

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

        String expectedString = String.format("%02d", date) + "." + String.format("%02d", month) + "." + String.format("%04d", year) + "    " + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + "\n";

        assertEquals(expectedString, day.toString());
    }

}
