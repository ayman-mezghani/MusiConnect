package ch.epfl.sdp.musiconnect;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class MusicianClassUnitTests {

    @Test
    public void getterAndSetterOfVideoURLWorkWithValidInput() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String videoURL = "www.john-lennon.uk/MyVideo";

        john.setVideoURL(videoURL);
        assertEquals(videoURL, john.getVideoURL());
    }

    @Test
    public void getterAndSetterOfInstruments() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        HashMap<Instrument,Level> instruments = new HashMap<Instrument,Level>(){{
            put(Instrument.ACCORDION,Level.INTERMEDIATE);
            put(Instrument.BAGPIPES,Level.BEGINNER);
        }};

        john.setInstruments(instruments);
        assertEquals(instruments, john.getInstruments());
    }

    @Test
    public void setVideoURLThrowsExceptionIfVideoURLIsTooLong() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String videoURL = "www.john-lennon.uk/MyVideo";
        for (int i = 0; i < 255; ++i) {
            videoURL += "/abcdefg";
        }
        String finalVideoURL = videoURL;

        assertThrows(IllegalArgumentException.class, () -> john.setVideoURL(finalVideoURL));
    }

    @Test
    public void addInstrumentWorksWithValidInput() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        Set<Instrument> instruments = new HashSet<Instrument>();

        assertEquals(instruments, john.setOfInstruments());

        instruments.add(Instrument.VOICE);

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);
        assertEquals(instruments, john.setOfInstruments());
    }

    @Test
    public void addInstrumentThrowsExceptionWithAlreadyExistingInstrument() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);

        assertThrows(IllegalArgumentException.class, () -> john.addInstrument(Instrument.VOICE, Level.ADVANCED));
    }

    @Test
    public void removeInstrumentWorksWithValidInput() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);

        Set<Instrument> instruments = new HashSet<Instrument>();
        instruments.add(Instrument.VOICE);

        assertEquals(instruments, john.setOfInstruments());

        instruments.remove(Instrument.VOICE);

        john.removeInstrument(Instrument.VOICE);
        assertEquals(instruments, john.setOfInstruments());
    }

    @Test
    public void removeInstrumentThrowsExceptionIfInstrumentDoesNotExist() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        assertThrows(IllegalArgumentException.class, () -> john.removeInstrument(Instrument.VOICE));
    }

    @Test
    public void removeAllInstrumentsWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);
        john.addInstrument(Instrument.PIANO, Level.ADVANCED);

        Set<Instrument> instruments = new HashSet<Instrument>();

        john.removeAllInstruments();
        assertEquals(instruments, john.setOfInstruments());
    }

    @Test
    public void containsAnyInstrumentWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        assertEquals(false, john.containsAnyInstrument());
        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);
        assertEquals(true, john.containsAnyInstrument());
        john.addInstrument(Instrument.PIANO, Level.ADVANCED);
        assertEquals(true, john.containsAnyInstrument());
        john.removeInstrument(Instrument.PIANO);
        assertEquals(true, john.containsAnyInstrument());
        john.removeInstrument(Instrument.VOICE);
        assertEquals(false, john.containsAnyInstrument());
    }

    @Test
    public void numberOfInstrumentsWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        assertEquals(0, john.numberOfInstruments());
        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);
        assertEquals(1, john.numberOfInstruments());
        john.addInstrument(Instrument.PIANO, Level.ADVANCED);
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
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        assertEquals(false, john.containsInstrument(Instrument.VOICE));
        assertEquals(false, john.containsInstrument(Instrument.PIANO));
        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);
        assertEquals(true, john.containsInstrument(Instrument.VOICE));
        assertEquals(false, john.containsInstrument(Instrument.PIANO));
        john.addInstrument(Instrument.PIANO, Level.ADVANCED);
        assertEquals(true, john.containsInstrument(Instrument.VOICE));
        assertEquals(true, john.containsInstrument(Instrument.PIANO));
        john.removeInstrument(Instrument.VOICE);
        assertEquals(false, john.containsInstrument(Instrument.VOICE));
        assertEquals(true, john.containsInstrument(Instrument.PIANO));
        john.removeInstrument(Instrument.PIANO);
        assertEquals(false, john.containsInstrument(Instrument.VOICE));
        assertEquals(false, john.containsInstrument(Instrument.PIANO));
    }

    @Test
    public void getLevelWorksWithValidInput() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);
        john.addInstrument(Instrument.PIANO, Level.ADVANCED);

        assertEquals(Level.PROFESSIONAL, john.getLevel(Instrument.VOICE));
        assertEquals(Level.ADVANCED, john.getLevel(Instrument.PIANO));
    }

    @Test
    public void getLevelThrowsExceptionIfInstrumentDoesNotExist() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        assertThrows(IllegalArgumentException.class, () -> john.getLevel(Instrument.VOICE));
    }

    @Test
    public void changeLevelWorksWithValidInput() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);

        assertEquals(Level.PROFESSIONAL, john.getLevel(Instrument.VOICE));

        john.changeLevel(Instrument.VOICE, Level.ADVANCED);

        assertEquals(Level.ADVANCED, john.getLevel(Instrument.VOICE));
    }

    @Test
    public void changeLevelThrowsExceptionIfInstrumentDoesNotExist() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        assertThrows(IllegalArgumentException.class, () -> john.changeLevel(Instrument.VOICE, Level.ADVANCED));
    }

    @Test
    public void toStringOfMusicianClassWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String expectedString = firstName + " " + lastName + ", " + john.getAge() + "\n";

        assertEquals(expectedString, john.toString());

        john.addInstrument(Instrument.VOICE, Level.PROFESSIONAL);

        expectedString += "    " + Instrument.VOICE + " -> level " + Level.PROFESSIONAL + "\n";

        assertEquals(expectedString, john.toString());
    }

}
