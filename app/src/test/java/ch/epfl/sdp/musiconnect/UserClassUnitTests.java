package ch.epfl.sdp.musiconnect;

import org.junit.Test;

import ch.epfl.sdp.musiconnect.functionnalities.MyDate;
import ch.epfl.sdp.musiconnect.functionnalities.MyLocation;
import ch.epfl.sdp.musiconnect.users.Musician;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class UserClassUnitTests {

    private static final double EPFL_LATITUDE = 46.5185941;
    private static final double EPFL_LONGITUDE = 6.5618969;


    @Test
    public void getJoinDateWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        MyDate currentDate = new MyDate();

        assertEquals(currentDate, john.getJoinDate());
    }

    @Test
    public void getLocationWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        MyLocation initialLocation = new MyLocation(EPFL_LATITUDE, EPFL_LONGITUDE);

        assertEquals(initialLocation, john.getLocation());
    }

    @Test
    public void setLocationWorks() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        MyLocation initialLocation = new MyLocation(EPFL_LATITUDE, EPFL_LONGITUDE);
        MyLocation nextLocation = new MyLocation(51.532005, -0.177331);

        assertEquals(initialLocation, john.getLocation());
        john.setLocation(nextLocation);
        assertEquals(nextLocation, john.getLocation());
    }
}
