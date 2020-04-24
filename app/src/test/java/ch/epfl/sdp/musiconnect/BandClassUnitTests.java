package ch.epfl.sdp.musiconnect;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class BandClassUnitTests {

    @Test
    public void gettersOfBandClassWork() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertEquals(bandName, beatles.getBandName());
        assertEquals(john.getFirstName(), beatles.getLeaderFirstName());
        assertEquals(john.getLastName(), beatles.getLeaderLastName());
        assertEquals(john.getUserName(), beatles.getLeaderUserName());
        assertEquals(john.getEmailAddress(), beatles.getLeaderEmailAddress());
    }

    @Test
    public void setBandNameWorksWithValidInput() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        String newBandName = "The Fab Four";

        assertEquals(bandName, beatles.getBandName());
        beatles.setBandName(newBandName);
        assertEquals(newBandName, beatles.getBandName());
    }

    @Test
    public void setBandNameThrowsExceptionIfNameIsEmpty() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        String newBandName = "";

        assertThrows(IllegalArgumentException.class, () -> beatles.setBandName(newBandName));
    }

    @Test
    public void setBandNameThrowsExceptionIfNameIsTooLong() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        String newBandName = "The Beatles (The Fab Four)";

        assertThrows(IllegalArgumentException.class, () -> beatles.setBandName(newBandName));
    }

    @Test
    public void setLeaderWorksWithValidInput() {
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
    public void setLeaderThrowsExceptionIfLeaderIsNull() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertThrows(NullPointerException.class, () -> beatles.setLeader(null));
    }

    @Test
    public void setLeaderThrowsExceptionIfLeaderIsNotInTheBand() {
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

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertThrows(IllegalArgumentException.class, () -> beatles.setLeader(paul));
    }

    @Test
    public void isLeaderWorksWithValidInput() {
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

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertEquals(true, beatles.isLeader(john));
        assertEquals(false, beatles.isLeader(paul));

        beatles.addMember(paul);

        assertEquals(true, beatles.isLeader(john));
        assertEquals(false, beatles.isLeader(paul));
    }

    @Test
    public void addMemberWorksWithValidInput() {
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

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        Set<Musician> members = new HashSet<Musician>();
        members.add(john);

        assertEquals(members, beatles.setOfMembers());

        members.add(paul);

        beatles.addMember(paul);
        assertEquals(members, beatles.setOfMembers());
    }

    @Test
    public void addMemberThrowsExceptionIfMemberIsNull() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertThrows(NullPointerException.class, () -> beatles.addMember(null));
    }

    @Test
    public void addMemberThrowsExceptionIfMemberAlreadyExists() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertThrows(IllegalArgumentException.class, () -> beatles.addMember(john));
    }

    @Test
    public void removeMemberWorksWithValidInput() {
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

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);
        beatles.addMember(paul);

        Set<Musician> members = new HashSet<Musician>();
        members.add(john);
        members.add(paul);

        assertEquals(members, beatles.setOfMembers());

        members.remove(paul);

        beatles.removeMember(paul);
        assertEquals(members, beatles.setOfMembers());
    }

    @Test
    public void removeMemberThrowsExceptionIfMemberDoesNotExist() {
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

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertThrows(IllegalArgumentException.class, () -> beatles.removeMember(paul));
    }

    @Test
    public void removeMemberThrowsExceptionIfMemberIsLeader() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        assertThrows(IllegalArgumentException.class, () -> beatles.removeMember(john));
    }

    @Test
    public void numberOfMembersWorks() {
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
        String johnEmailAddress = "john.lennon@gmail.com";
        MyDate johnBirthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(johnFirstName, johnLastName, johnUserName, johnEmailAddress, johnBirthday);

        String paulFirstName = "Paul";
        String paulLastName = "McCartney";
        String paulUserName = "PaulMcCartney";
        String paulEmailAddress = "paul.mccartney@gmail.com";
        MyDate paulBirthday = new MyDate(1942, 6, 18);
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
    public void getterAndSetterOfVideoURLWorkWithValidInput() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        String videoURL = "www.the-beatles.uk/OurVideo";

        //assertThrows(Error.class, () -> beatles.getVideoURL());
        beatles.setVideoURL(videoURL);
        assertEquals(videoURL, beatles.getVideoURL());
    }

    @Test
    public void setVideoURLThrowsExceptionIfVideoURLIsTooLong() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";
        String emailAddress = "john.lennon@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        String videoURL = "www.the-beatles.uk/OurVideo";
        for (int i = 0; i < 255; ++i) {
            videoURL += "/abcdefg";
        }
        String finalVideoURL = videoURL;

        assertThrows(IllegalArgumentException.class, () -> beatles.setVideoURL(finalVideoURL));
    }

    @Test
    public void toStringOfBandClassWorks() {
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

        String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        String expectedString = bandName + "\n" + "    " + johnFirstName + " " + johnLastName + " (Leader)\n";

        assertEquals(expectedString, beatles.toString());

        beatles.addMember(paul);

        expectedString += "    " + paulFirstName + " " + paulLastName + "\n";

        assertEquals(expectedString, beatles.toString());
    }

    @Test
    public void getNameReturnsCorrect() {
        Musician john = new Musician("John", "Lennon", "JohnLennon", "john.lennon@gmail.com", new MyDate(1940, 10, 9));
        Band band = new Band("BandName", john);
        assertEquals(band.getBandName(), band.getName());
    }
}
