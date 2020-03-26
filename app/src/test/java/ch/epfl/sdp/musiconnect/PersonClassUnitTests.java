package ch.epfl.sdp.musiconnect;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class PersonClassUnitTests {

    @Test
    public void gettersOfPersonClassWork() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        int age = 79;
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        assertEquals(firstName, john.getFirstName());
        assertEquals(lastName, john.getLastName());
        assertEquals(userName, john.getUserName());
        assertEquals(emailAddress, john.getEmailAddress());
        assertEquals(birthday, john.getBirthday());
        assertEquals(age, john.getAge());
    }

    @Test
    public void setFirstNameWorksWithValidInput() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String newFirstName = "John Winston";

        assertEquals(firstName, john.getFirstName());
        john.setFirstName(newFirstName);
        assertEquals(newFirstName, john.getFirstName());
    }

    @Test
    public void setFirstNameThrowsExceptionIfNameIsEmpty() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        assertThrows(IllegalArgumentException.class, () -> john.setFirstName(""));
    }

    @Test
    public void setFirstNameThrowsExceptionIfNameIsTooLong() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String newFirstName = "John Winston Ono Lennon";

        assertThrows(IllegalArgumentException.class, () -> john.setFirstName(newFirstName));
    }

    @Test
    public void setLastNameWorksWithValidInput() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String newLastName = "Ono Lennon";

        assertEquals(lastName, john.getLastName());
        john.setLastName(newLastName);
        assertEquals(newLastName, john.getLastName());
    }

    @Test
    public void setLastNameThrowsExceptionIfNameIsEmpty() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        assertThrows(IllegalArgumentException.class, () -> john.setLastName(""));
    }

    @Test
    public void setLastNameThrowsExceptionIfNameIsTooLong() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String newLastName = "Winston Ono Lennon";

        assertThrows(IllegalArgumentException.class, () -> john.setLastName(newLastName));
    }

    @Test
    public void setUserNameWorksWithValidInput() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String newUserName = "JohnOnoLennon";

        assertEquals(userName, john.getUserName());
        john.setUserName(newUserName);
        assertEquals(newUserName, john.getUserName());
    }

    @Test
    public void setUserNameThrowsExceptionIfNameIsEmpty() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        assertThrows(IllegalArgumentException.class, () -> john.setUserName(""));
    }

    @Test
    public void setUserNameThrowsExceptionIfNameIsTooLong() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String newUserName = "JohnWinstonOnoLennon";

        assertThrows(IllegalArgumentException.class, () -> john.setUserName(newUserName));
    }

    @Test
    public void setEmailAddressWorksWithValidInput() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String newEmailAddress = "john.winston.lennon@gmail.com";

        assertEquals(emailAddress, john.getEmailAddress());
        john.setEmailAddress(newEmailAddress);
        assertEquals(newEmailAddress, john.getEmailAddress());
    }

    @Test
    public void setEmailAddressThrowsExceptionIfEmailAddressIsEmpty() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        assertThrows(IllegalArgumentException.class, () -> john.setEmailAddress(""));
    }

    @Test
    public void setEmailAddressThrowsExceptionIfEmailAddressIsTooLong() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String newEmailAddress = "john.winston.ono.lennon.vocalist.and.guitarist.of.the.beatles@gmail.com";

        assertThrows(IllegalArgumentException.class, () -> john.setEmailAddress(newEmailAddress));
    }

    @Test
    public void setEmailAddressThrowsExceptionIfEmailAddressHasTheWrongDomain() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String newEmailAddress = "john.lennon@epfl.ch";

        assertThrows(IllegalArgumentException.class, () -> john.setEmailAddress(newEmailAddress));
    }

    @Test
    public void setBirthdayWorksWithValidInput() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        int age = 79;
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        MyDate newBirthday = new MyDate(1980, 12, 8);
        int newAge = 39;

        assertEquals(birthday, john.getBirthday());
        assertEquals(age, john.getAge());
        john.setBirthday(newBirthday);
        assertEquals(newBirthday, john.getBirthday());
        assertEquals(newAge, john.getAge());
    }

    @Test
    public void setBirthdayThrowsExceptionIfBirthdayIsAfterCurrentDate() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        MyDate newBirthday = new MyDate(2020, 10, 9);

        assertThrows(IllegalArgumentException.class, () -> john.setBirthday(newBirthday));
    }

    @Test
    public void setBirthdayThrowsExceptionIfAgeIsTooLow() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        MyDate newBirthday = new MyDate(2010, 10, 9);

        assertThrows(IllegalArgumentException.class, () -> john.setBirthday(newBirthday));
    }

    @Test
    public void setBirthdayThrowsExceptionIfAgeIsTooHigh() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        MyDate newBirthday = new MyDate(1890, 10, 9);

        assertThrows(IllegalArgumentException.class, () -> john.setBirthday(newBirthday));
    }

}
