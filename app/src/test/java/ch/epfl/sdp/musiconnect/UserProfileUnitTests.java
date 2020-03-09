package ch.epfl.sdp.musiconnect;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Set;
import java.util.HashSet;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class UserProfileUnitTests {

    /**********************************************************/
    /**************** Tests for Musician class ****************/
    /**********************************************************/

    @Test
    public void gettersOfMusicianClassWork() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        int age = 79;
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);
        MyDate currentDate = new MyDate();

        assertEquals(firstName, john.getFirstName());
        assertEquals(lastName, john.getLastName());
        assertEquals(userName, john.getUserName());
        assertEquals(birthday, john.getBirthday());
        assertEquals(age, john.getAge());
        assertEquals(emailAddress, john.getEmailAddress());
        assertEquals(currentDate, john.getJoinDate());
    }

    @Test
    public void setFirstNameOfMusicianWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String newFirstName = "John Winston";
        john.setFirstName(newFirstName);

        assertEquals(newFirstName, john.getFirstName());
    }

    @Test
    public void setLastNameOfMusicianWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String newLastName = "Ono Lennon";
        john.setLastName(newLastName);

        assertEquals(newLastName, john.getLastName());
    }

    @Test
    public void setUserNameOfMusicianWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String newUserName = "JohnOnoLennon";
        john.setUserName(newUserName);

        assertEquals(newUserName, john.getUserName());
    }

    @Test
    public void setBirthdayOfMusicianWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        MyDate newBirthday = new MyDate(1980, 12, 8);
        john.setBirthday(newBirthday);

        assertEquals(newBirthday, john.getBirthday());
    }

    @Test
    public void setEmailAddressOfMusicianWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String newEmailAddress = "john.winston.lennon@gmail.com";
        john.setEmailAddress(newEmailAddress);

        assertEquals(newEmailAddress, john.getEmailAddress());
    }

    @Test
    public void setVideoURLAndGetVideoURLOfMusicianWork() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String newVideoURL = "www.john-lennon.uk/MyVideo";
        john.setVideoURL(newVideoURL);

        assertEquals(newVideoURL, john.getVideoURL());
    }

    @Test
    public void addInstrumentWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);

        Set<Instrument> instruments = new HashSet<Instrument>();
        instruments.add(Instrument.VOICE);

        assertEquals(instruments, john.setOfInstruments());
    }

    @Test
    public void removeInstrumentWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);
        john.removeInstrument(Instrument.VOICE);

        Set<Instrument> instruments = new HashSet<Instrument>();

        assertEquals(instruments, john.setOfInstruments());
    }

    @Test
    public void removeAllInstrumentsWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);
        john.addInstrument(Instrument.PIANO, Level.ADVANCED);
        john.removeAllInstruments();

        Set<Instrument> instruments = new HashSet<Instrument>();

        assertEquals(instruments, john.setOfInstruments());
    }

    @Test
    public void containsAnyInstrumentWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        assertEquals(false, john.containsAnyInstrument());

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);

        assertEquals(true, john.containsAnyInstrument());

        john.removeInstrument(Instrument.VOICE);

        assertEquals(false, john.containsAnyInstrument());
    }

    @Test
    public void numberOfInstrumentsWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        assertEquals(0, john.numberOfInstruments());

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);

        assertEquals(1, john.numberOfInstruments());

        john.addInstrument(Instrument.PIANO, Level.ADVANCED);

        assertEquals(2, john.numberOfInstruments());

        john.addInstrument(Instrument.GUITAR, Level.ADVANCED);

        assertEquals(3, john.numberOfInstruments());

        john.removeInstrument(Instrument.GUITAR);

        assertEquals(2, john.numberOfInstruments());

        john.removeInstrument(Instrument.PIANO);

        assertEquals(1, john.numberOfInstruments());

        john.removeInstrument(Instrument.VOICE);

        assertEquals(0, john.numberOfInstruments());
    }

    @Test
    public void containsInstrumentWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        assertEquals(false, john.containsInstrument(Instrument.VOICE));

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);

        assertEquals(true, john.containsInstrument(Instrument.VOICE));
        assertEquals(false, john.containsInstrument(Instrument.PIANO));
    }

    @Test
    public void getLevelWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);

        assertEquals(Level.PROFESSIONAL, john.getLevel(Instrument.VOICE));
    }

    @Test
    public void changeLevelWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);

        assertEquals(Level.PROFESSIONAL, john.getLevel(Instrument.VOICE));

        john.changeLevel(Instrument.VOICE, Level.ADVANCED);

        assertEquals(Level.ADVANCED, john.getLevel(Instrument.VOICE));
    }

    @Test
    public void setLocationAndGetLocationOfMusicianWork() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        Location location = new Location(51.532005, -0.177331);
        john.setLocation(location);

        assertEquals(location, john.getLocation());
    }

    /******************************************************/
    /**************** Tests for Band class ****************/
    /******************************************************/

    @Test
    public void gettersOfBandClassWork() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);
        MyDate currentDate = new MyDate();

        assertEquals(bandName, beatles.getBandName());
        assertEquals(firstName, beatles.getLeaderFirstName());
        assertEquals(lastName, beatles.getLeaderLastName());
        assertEquals(userName, beatles.getLeaderUserName());
        assertEquals(emailAddress, beatles.getLeaderEmailAddress());
        assertEquals(currentDate, beatles.getJoinDate());
    }

    @Test
    public void isLeaderWorks() {
        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        String johnEmailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String paulFirstName = "Paul";
        String paulLastName = "McCartney";
        String paulUserName = "PaulMcCartney";
        MyDate paulBirthday = new MyDate(1942, 6, 18);
        String paulEmailAddress = "paul.mccartney@gmail.com";
        Musician paul = new Musician(paulFirstName, paulLastName, paulUserName, paulEmailAddress, paulBirthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertEquals(true, beatles.isLeader(john));
        assertEquals(false, beatles.isLeader(paul));

        beatles.addMember(paul);

        assertEquals(true, beatles.isLeader(john));
        assertEquals(false, beatles.isLeader(paul));

        beatles.setLeader(paul);

        assertEquals(false, beatles.isLeader(john));
        assertEquals(true, beatles.isLeader(paul));
    }

    @Test
    public void addMemberAndRemoveMemberWork() {
        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        String johnEmailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String paulFirstName = "Paul";
        String paulLastName = "McCartney";
        String paulUserName = "PaulMcCartney";
        MyDate paulBirthday = new MyDate(1942, 6, 18);
        String paulEmailAddress = "paul.mccartney@gmail.com";
        Musician paul = new Musician(paulFirstName, paulLastName, paulUserName, paulEmailAddress, paulBirthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        Set<Musician> members = new HashSet<Musician>();
        members.add(john);

        assertEquals(members, beatles.setOfMembers());

        beatles.addMember(paul);
        members.add(paul);

        assertEquals(members, beatles.setOfMembers());

        beatles.removeMember(paul);
        members.remove(paul);

        assertEquals(members, beatles.setOfMembers());
    }

    @Test
    public void numberOfMemberWorks() {
        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        String johnEmailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String paulFirstName = "Paul";
        String paulLastName = "McCartney";
        String paulUserName = "PaulMcCartney";
        MyDate paulBirthday = new MyDate(1942, 6, 18);
        String paulEmailAddress = "paul.mccartney@gmail.com";
        Musician paul = new Musician(paulFirstName, paulLastName, paulUserName, paulEmailAddress, paulBirthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertEquals(1, beatles.numberOfMembers());

        beatles.addMember(paul);

        assertEquals(2, beatles.numberOfMembers());

        beatles.removeMember(paul);

        assertEquals(1, beatles.numberOfMembers());
    }

    @Test
    public void containsMemberWorks() {
        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        String johnEmailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String paulFirstName = "Paul";
        String paulLastName = "McCartney";
        String paulUserName = "PaulMcCartney";
        MyDate paulBirthday = new MyDate(1942, 6, 18);
        String paulEmailAddress = "paul.mccartney@gmail.com";
        Musician paul = new Musician(paulFirstName, paulLastName, paulUserName, paulEmailAddress, paulBirthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertEquals(true, beatles.containsMember(john));
        assertEquals(false, beatles.containsMember(paul));

        beatles.addMember(paul);

        assertEquals(true, beatles.containsMember(john));
        assertEquals(true, beatles.containsMember(paul));

        beatles.removeMember(paul);

        assertEquals(true, beatles.containsMember(john));
        assertEquals(false, beatles.containsMember(paul));
    }

    @Test
    public void setVideoURLAndGetVideoURLOfBandWork() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        String newVideoURL = "www.the-beatles.uk/OurVideo";
        beatles.setVideoURL(newVideoURL);

        assertEquals(newVideoURL, beatles.getVideoURL());
    }

    @Test
    public void setLocationAndGetLocationOfBandWork() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        MyDate birthday = new MyDate(1940, 10, 9);
        String emailAddress = "john.lennon@gmail.com";
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        Location location = new Location(51.532005, -0.177331);
        beatles.setLocation(location);

        assertEquals(location, beatles.getLocation());
    }

    /**************************************************************/
    /**************** Tests for EventManager class ****************/
    /**************************************************************/

    @Test
    public void gettersOfEventManagerClassWork() {
        String firstName = "Brian";
        String lastName = "Epstein";
        String userName = "BrianEpstein";
        String emailAddress = "brian.epstein@gmail.com";
        MyDate birthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(firstName, lastName, userName, emailAddress, birthday);
        MyDate currentDate = new MyDate();

        assertEquals(firstName, brian.getFirstName());
        assertEquals(lastName, brian.getLastName());
        assertEquals(userName, brian.getUserName());
        assertEquals(emailAddress, brian.getEmailAddress());
        assertEquals(currentDate, brian.getJoinDate());
    }

    @Test
    public void setFirstNameOfEventManagerWorks() {
        String firstName = "Brian";
        String lastName = "Epstein";
        String userName = "BrianEpstein";
        String emailAddress = "brian.epstein@gmail.com";
        MyDate birthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(firstName, lastName, userName, emailAddress, birthday);

        String newFirstName = "Brian Samuel";
        brian.setFirstName(newFirstName);

        assertEquals(newFirstName, brian.getFirstName());
    }

    @Test
    public void setLastNameOfEventManagerWorks() {
        String firstName = "Brian";
        String lastName = "Epstein";
        String userName = "BrianEpstein";
        String emailAddress = "brian.epstein@gmail.com";
        MyDate birthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(firstName, lastName, userName, emailAddress, birthday);

        String newLastName = "Samuel Epstein";
        brian.setLastName(newLastName);

        assertEquals(newLastName, brian.getLastName());
    }

    @Test
    public void setUserNameOfEventManagerWorks() {
        String firstName = "Brian";
        String lastName = "Epstein";
        String userName = "BrianEpstein";
        String emailAddress = "brian.epstein@gmail.com";
        MyDate birthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(firstName, lastName, userName, emailAddress, birthday);

        String newUserName = "BrianSamuel";
        brian.setUserName(newUserName);

        assertEquals(newUserName, brian.getUserName());
    }

    @Test
    public void setEmailAddressOfEventManagerWorks() {
        String firstName = "Brian";
        String lastName = "Epstein";
        String userName = "BrianEpstein";
        String emailAddress = "brian.epstein@gmail.com";
        MyDate birthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(firstName, lastName, userName, emailAddress, birthday);

        String newEmailAddress = "brian.samuel.epstein@gmail.com";
        brian.setEmailAddress(newEmailAddress);

        assertEquals(newEmailAddress, brian.getEmailAddress());
    }

    @Test
    public void setEventNameAndGetEventNameWork() {
        String firstName = "Brian";
        String lastName = "Epstein";
        String userName = "BrianEpstein";
        String emailAddress = "brian.epstein@gmail.com";
        MyDate birthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(firstName, lastName, userName, emailAddress, birthday);

        String newEventName = "On the rooftop";
        brian.setEventName(newEventName);

        assertEquals(newEventName, brian.getEventName());
    }

    @Test
    public void setEventDateAndGetEventDateWork() {
        String firstName = "Brian";
        String lastName = "Epstein";
        String userName = "BrianEpstein";
        String emailAddress = "brian.epstein@gmail.com";
        MyDate birthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(firstName, lastName, userName, emailAddress, birthday);

        MyDate newEventDate = new MyDate(1969, 1, 30);
        brian.setEventDate(newEventDate);

        assertEquals(newEventDate, brian.getEventDate());
    }

    @Test
    public void setLocationAndGetLocationOfEventManagerWork() {
        String firstName = "Brian";
        String lastName = "Epstein";
        String userName = "BrianEpstein";
        String emailAddress = "brian.epstein@gmail.com";
        MyDate birthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(firstName, lastName, userName, emailAddress, birthday);

        Location location = new Location(51.532005, -0.177331);
        brian.setLocation(location);

        assertEquals(location, brian.getLocation());
    }

    /********************************************************/
    /**************** Tests for MyDate class ****************/
    /********************************************************/

    @Test
    public void gettersOfMyDateClassWork() {
        int year = 2000;
        int month = 1;
        int date = 31;
        int hours = 12;
        int minutes = 30;
        MyDate calendar = new MyDate(year, month, date, hours, minutes);

        assertEquals(year, calendar.getYear());
        assertEquals(month, calendar.getMonth());
        assertEquals(date, calendar.getDate());
        assertEquals(hours, calendar.getHours());
        assertEquals(minutes, calendar.getMinutes());
    }

    @Test
    public void setYearWorks() {
        int year = 2000;
        int month = 1;
        int date = 31;
        int hours = 12;
        int minutes = 30;
        MyDate calendar = new MyDate(year, month, date, hours, minutes);

        int newYear = 2020;
        calendar.setYear(newYear);

        assertEquals(newYear, calendar.getYear());
    }

    @Test
    public void setMonthWorks() {
        int year = 2000;
        int month = 1;
        int date = 31;
        int hours = 12;
        int minutes = 30;
        MyDate calendar = new MyDate(year, month, date, hours, minutes);

        int newMonth = 2;
        calendar.setMonth(newMonth);

        assertEquals(newMonth, calendar.getMonth());
    }

    @Test
    public void setDateWorks() {
        int year = 2000;
        int month = 1;
        int date = 31;
        int hours = 12;
        int minutes = 30;
        MyDate calendar = new MyDate(year, month, date, hours, minutes);

        int newDate = 1;
        calendar.setDate(newDate);

        assertEquals(newDate, calendar.getDate());
    }

    @Test
    public void setHoursWorks() {
        int year = 2000;
        int month = 1;
        int date = 31;
        int hours = 12;
        int minutes = 30;
        MyDate calendar = new MyDate(year, month, date, hours, minutes);

        int newHours = 20;
        calendar.setHours(newHours);

        assertEquals(newHours, calendar.getHours());
    }

    @Test
    public void setMinutesWorks() {
        int year = 2000;
        int month = 1;
        int date = 31;
        int hours = 12;
        int minutes = 30;
        MyDate calendar = new MyDate(year, month, date, hours, minutes);

        int newMinutes = 0;
        calendar.setMinutes(newMinutes);

        assertEquals(newMinutes, calendar.getMinutes());
    }

    @Test
    public void afterAndBeforeWork() {
        int firstYear = 2000;
        int firstMonth = 1;
        int firstDate = 31;
        int firstHours = 12;
        int firstMinutes = 20;
        MyDate firstCalendar = new MyDate(firstYear, firstMonth, firstDate, firstHours, firstMinutes);

        int secondYear = 2000;
        int secondMonth = 1;
        int secondDate = 31;
        int secondHours = 12;
        int secondMinutes = 40;
        MyDate secondCalendar = new MyDate(secondYear, secondMonth, secondDate, secondHours, secondMinutes);

        assertEquals(false, firstCalendar.after(secondCalendar));
        assertEquals(true, firstCalendar.before(secondCalendar));

        assertEquals(true, secondCalendar.after(firstCalendar));
        assertEquals(false, secondCalendar.before(firstCalendar));

        assertEquals(false, firstCalendar.after(firstCalendar));
        assertEquals(false, firstCalendar.before(firstCalendar));

        assertEquals(false, secondCalendar.after(secondCalendar));
        assertEquals(false, secondCalendar.before(secondCalendar));
    }

    @Test
    public void equalsOfMyDateWorks() {
        int firstYear = 2000;
        int firstMonth = 1;
        int firstDate = 31;
        int firstHours = 12;
        int firstMinutes = 20;
        MyDate firstCalendar = new MyDate(firstYear, firstMonth, firstDate, firstHours, firstMinutes);

        int secondYear = 2000;
        int secondMonth = 1;
        int secondDate = 31;
        int secondHours = 12;
        int secondMinutes = 20;
        MyDate secondCalendar = new MyDate(secondYear, secondMonth, secondDate, secondHours, secondMinutes);

        int thirdYear = 2000;
        int thirdMonth = 1;
        int thirdDate = 31;
        int thirdHours = 12;
        int thirdMinutes = 40;
        MyDate thirdCalendar = new MyDate(thirdYear, thirdMonth, thirdDate, thirdHours, thirdMinutes);

        assertEquals(true, firstCalendar.equals(secondCalendar));
        assertEquals(false, firstCalendar.equals(thirdCalendar));
        assertEquals(false, secondCalendar.equals(thirdCalendar));
        assertEquals(true, secondCalendar.equals(firstCalendar));
        assertEquals(false, thirdCalendar.equals(firstCalendar));
        assertEquals(false, thirdCalendar.equals(secondCalendar));
    }

    /**********************************************************/
    /**************** Tests for Location class ****************/
    /**********************************************************/

    @Test
    public void gettersOfLocationClassWork() {
        double latitude = 45.0;
        double longitude = 54.5;
        Location location = new Location(latitude, longitude);

        assertEquals(latitude, location.getLatitude(), 1e-20);
        assertEquals(longitude, location.getLongitude(), 1e-20);
        assertEquals(location, location.getLocation());
    }

    @Test
    public void setLatitudeWorks() {
        double latitude = 45.0;
        double longitude = 54.5;
        Location location = new Location(latitude, longitude);

        double newLatitude = -45.0;
        location.setLatitude(newLatitude);

        assertEquals(newLatitude, location.getLatitude(), 1e-20);
    }

    @Test
    public void setLongitudeWorks() {
        double latitude = 45.0;
        double longitude = 54.5;
        Location location = new Location(latitude, longitude);

        double newLongitude = -145.0;
        location.setLongitude(newLongitude);

        assertEquals(newLongitude, location.getLongitude(), 1e-20);
    }

    @Test
    public void setLocationOfLocationClassWorks() {
        double latitude = 45.0;
        double longitude = 54.5;
        Location location = new Location(latitude, longitude);

        double newLatitude = -45.0;
        double newLongitude = -145.0;
        Location newLocation = new Location(newLatitude, newLongitude);
        location.setLocation(newLocation);

        assertEquals(newLocation, location.getLocation());
    }

    @Test
    public void equalsOfLocationClassWorks() {
        double firstLatitude = 45.0;
        double firstLongitude = 54.5;
        Location firstLocation = new Location(firstLatitude, firstLongitude);

        double secondLatitude = 45.0;
        double secondLongitude = 54.5;
        Location secondLocation = new Location(secondLatitude, secondLongitude);

        double thirdLatitude = -45.0;
        double thirdLongitude = -145.0;
        Location thirdLocation = new Location(thirdLatitude, thirdLongitude);

        assertEquals(true, firstLocation.equals(secondLocation));
        assertEquals(false, firstLocation.equals(thirdLocation));
        assertEquals(false, secondLocation.equals(thirdLocation));
        assertEquals(true, secondLocation.equals(firstLocation));
        assertEquals(false, thirdLocation.equals(firstLocation));
        assertEquals(false, thirdLocation.equals(secondLocation));
    }

}
