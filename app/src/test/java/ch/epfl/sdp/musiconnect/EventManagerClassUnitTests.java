package ch.epfl.sdp.musiconnect;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;
import java.util.HashSet;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class EventManagerClassUnitTests {

    @Test
    public void getterAndSetterOfEventNameWorkWithValidInput() {
        String firstName = "Brian";
        String lastName = "Epstein";
        String userName = "BrianEpstein";
        String emailAddress = "brian.epstein@gmail.com";
        MyDate birthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(firstName, lastName, userName, emailAddress, birthday);

        String eventName = "On the rooftop";

        assertThrows(Error.class, () -> brian.getEventName());
        brian.setEventName(eventName);
        assertEquals(eventName, brian.getEventName());
    }

    @Test
    public void setEventNameThrowsExceptionIfNameIsTooLong() {
        String firstName = "Brian";
        String lastName = "Epstein";
        String userName = "BrianEpstein";
        String emailAddress = "brian.epstein@gmail.com";
        MyDate birthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(firstName, lastName, userName, emailAddress, birthday);

        String eventName = "The Beatles on the rooftop";

        assertThrows(IllegalArgumentException.class, () -> brian.setEventName(eventName));
    }

    @Test
    public void getterAndSetterOfEventDateWorkWithValidInput() {
        String firstName = "Brian";
        String lastName = "Epstein";
        String userName = "BrianEpstein";
        String emailAddress = "brian.epstein@gmail.com";
        MyDate birthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(firstName, lastName, userName, emailAddress, birthday);

        MyDate eventDate = new MyDate(1969, 1, 30);

        brian.setEventDate(eventDate);
        assertEquals(eventDate, brian.getEventDate());
    }

    @Test
    public void addMusicianWorksWithValidInput() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        Set<Musician> musicians = new HashSet<Musician>();

        assertEquals(musicians, brian.setOfMusicians());

        musicians.add(john);

        brian.addMusician(john);
        assertEquals(musicians, brian.setOfMusicians());
    }

    @Test
    public void addMusicianThrowsExceptionIfMusicianIsNull() {
        String firstName = "Brian";
        String lastName = "Epstein";
        String userName = "BrianEpstein";
        String emailAddress = "brian.epstein@gmail.com";
        MyDate birthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(firstName, lastName, userName, emailAddress, birthday);

        assertThrows(NullPointerException.class, () -> brian.addMusician(null));
    }

    @Test
    public void addMusicianThrowsExceptionIfMusicianAlreadyExists() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        brian.addMusician(john);

        assertThrows(IllegalArgumentException.class, () -> brian.addMusician(john));
    }

    @Test
    public void removeMusicianWorksWithValidInput() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        brian.addMusician(john);

        Set<Musician> musicians = new HashSet<Musician>();
        musicians.add(john);

        assertEquals(musicians, brian.setOfMusicians());

        musicians.remove(john);

        brian.removeMusician(john);
        assertEquals(musicians, brian.setOfMusicians());
    }

    @Test
    public void removeMusicianThrowsExceptionIfMusicianDoesNotExist() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        assertThrows(IllegalArgumentException.class, () -> brian.removeMusician(john));
    }

    @Test
    public void removeAllMusiciansWorks() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String paulFirstName = "Paul";
        String paulLastName = "McCartney";
        String paulUserName = "PaulMcCartney";
        String paulEmailAddress = "paul.mccartney@gmail.com";
        MyDate paulBirthday = new MyDate(1942, 6, 18);
        Musician paul = new Musician(paulFirstName, paulLastName, paulUserName, paulEmailAddress, paulBirthday);

        brian.addMusician(john);
        brian.addMusician(paul);

        Set<Musician> musicians = new HashSet<Musician>();

        brian.removeAllMusicians();
        assertEquals(musicians, brian.setOfMusicians());
    }

    @Test
    public void containsAnyMusicianWorks() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String paulFirstName = "Paul";
        String paulLastName = "McCartney";
        String paulUserName = "PaulMcCartney";
        String paulEmailAddress = "paul.mccartney@gmail.com";
        MyDate paulBirthday = new MyDate(1942, 6, 18);
        Musician paul = new Musician(paulFirstName, paulLastName, paulUserName, paulEmailAddress, paulBirthday);

        assertEquals(false, brian.containsAnyMusician());
        brian.addMusician(john);
        assertEquals(true, brian.containsAnyMusician());
        brian.addMusician(paul);
        assertEquals(true, brian.containsAnyMusician());
        brian.removeMusician(john);
        assertEquals(true, brian.containsAnyMusician());
        brian.removeMusician(paul);
        assertEquals(false, brian.containsAnyMusician());
    }

    @Test
    public void numberOfMusiciansWorks() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String paulFirstName = "Paul";
        String paulLastName = "McCartney";
        String paulUserName = "PaulMcCartney";
        String paulEmailAddress = "paul.mccartney@gmail.com";
        MyDate paulBirthday = new MyDate(1942, 6, 18);
        Musician paul = new Musician(paulFirstName, paulLastName, paulUserName, paulEmailAddress, paulBirthday);

        assertEquals(0, brian.numberOfMusicians());
        brian.addMusician(john);
        assertEquals(1, brian.numberOfMusicians());
        brian.addMusician(paul);
        assertEquals(2, brian.numberOfMusicians());
        brian.removeMusician(john);
        assertEquals(1, brian.numberOfMusicians());
        brian.removeMusician(paul);
        assertEquals(0, brian.numberOfMusicians());
    }

    @Test
    public void containsMusicianWorks() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String paulFirstName = "Paul";
        String paulLastName = "McCartney";
        String paulUserName = "PaulMcCartney";
        String paulEmailAddress = "paul.mccartney@gmail.com";
        MyDate paulBirthday = new MyDate(1942, 6, 18);
        Musician paul = new Musician(paulFirstName, paulLastName, paulUserName, paulEmailAddress, paulBirthday);

        assertEquals(false, brian.containsMusician(john));
        assertEquals(false, brian.containsMusician(paul));
        brian.addMusician(john);
        assertEquals(true, brian.containsMusician(john));
        assertEquals(false, brian.containsMusician(paul));
        brian.addMusician(paul);
        assertEquals(true, brian.containsMusician(john));
        assertEquals(true, brian.containsMusician(paul));
        brian.removeMusician(john);
        assertEquals(false, brian.containsMusician(john));
        assertEquals(true, brian.containsMusician(paul));
        brian.removeMusician(paul);
        assertEquals(false, brian.containsMusician(john));
        assertEquals(false, brian.containsMusician(paul));
    }

    @Test
    public void addBandWorksWithValidInput() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        Set<Band> bands = new HashSet<Band>();

        assertEquals(bands, brian.setOfBands());

        bands.add(beatles);

        brian.addBand(beatles);
        assertEquals(bands, brian.setOfBands());
    }

    @Test
    public void addBandThrowsExceptionIfBandIsNull() {
        String firstName = "Brian";
        String lastName = "Epstein";
        String userName = "BrianEpstein";
        String emailAddress = "brian.epstein@gmail.com";
        MyDate birthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(firstName, lastName, userName, emailAddress, birthday);

        assertThrows(NullPointerException.class, () -> brian.addBand(null));
    }

    @Test
    public void addBandThrowsExceptionIfBandAlreadyExists() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        brian.addBand(beatles);

        assertThrows(IllegalArgumentException.class, () -> brian.addBand(beatles));
    }

    @Test
    public void removeBandWorksWithValidInput() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        brian.addBand(beatles);

        Set<Band> bands = new HashSet<Band>();
        bands.add(beatles);

        assertEquals(bands, brian.setOfBands());

        bands.remove(beatles);

        brian.removeBand(beatles);
        assertEquals(bands, brian.setOfBands());
    }

    @Test
    public void removeBandThrowsExceptionIfBandDoesNotExist() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertThrows(IllegalArgumentException.class, () -> brian.removeBand(beatles));
    }

    @Test
    public void removeAllBandsWorks() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        brian.addBand(beatles);

        Set<Band> bands = new HashSet<Band>();

        brian.removeAllBands();
        assertEquals(bands, brian.setOfBands());
    }

    @Test
    public void containsAnyBandWorks() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertEquals(false, brian.containsAnyBand());
        brian.addBand(beatles);
        assertEquals(true, brian.containsAnyBand());
        brian.removeBand(beatles);
        assertEquals(false, brian.containsAnyBand());
    }

    @Test
    public void numberOfBandsWorks() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertEquals(0, brian.numberOfBands());
        brian.addBand(beatles);
        assertEquals(1, brian.numberOfBands());
        brian.removeBand(beatles);
        assertEquals(0, brian.numberOfBands());
    }

    @Test
    public void containsBandWorks() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertEquals(false, brian.containsBand(beatles));
        brian.addBand(beatles);
        assertEquals(true, brian.containsBand(beatles));
        brian.removeBand(beatles);
        assertEquals(false, brian.containsBand(beatles));
    }

    @Test
    public void toStringOfEventManagerClassWorks() {
        String brianFirstName = "Brian";
        String brianLastName = "Epstein";
        String brianUserName = "BrianEpstein";
        String brianEmailAddress = "brian.epstein@gmail.com";
        MyDate brianBirthday = new MyDate(1934, 9, 19);
        EventManager brian = new EventManager(brianFirstName, brianLastName, brianUserName, brianEmailAddress, brianBirthday);

        String johnFirstName = "John";
        String johnLastName = "Lennon";
        String johnUserName = "JohnLennon";
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        String firstExpectedString = "Event (Organizer: " + brianFirstName + " " + brianLastName + ")\n";

        assertEquals(firstExpectedString, brian.toString());

        String secondEventName = "On the rooftop";
        brian.setEventName(secondEventName);
        brian.addMusician(john);

        String secondExpectedString = secondEventName + " (Organizer: " + brianFirstName + " " + brianLastName + ")\n";
        secondExpectedString += "Musicians:\n" + "    " + johnFirstName + " " + johnLastName + "\n";

        assertEquals(secondExpectedString, brian.toString());

        String thirdEventName = "The Beatles show";
        brian.setEventName(thirdEventName);
        brian.addBand(beatles);

        String thirdExpectedString = thirdEventName + " (Organizer: " + brianFirstName + " " + brianLastName + ")\n";
        thirdExpectedString += "Musicians:\n" + "    " + johnFirstName + " " + johnLastName + "\n";
        thirdExpectedString += "Bands:\n" + "    " + bandName + "\n";

        assertEquals(thirdExpectedString, brian.toString());
    }

}
