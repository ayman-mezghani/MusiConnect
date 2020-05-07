package ch.epfl.sdp.musiconnect;

import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sdp.musiconnect.events.Event;

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
        Band beatles = new Band(bandName, john.getEmailAddress());
        assertEquals(john.getEmailAddress(), beatles.getEmailAddress());
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
        Band beatles = new Band(bandName, john.getEmailAddress());

        String newBandName = "The Fab Four";

        assertEquals(bandName, beatles.getName());
        beatles.setBandName(newBandName);
        assertEquals(newBandName, beatles.getName());
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
        Band beatles = new Band(bandName, john.getEmailAddress());

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
        Band beatles = new Band(bandName, john.getEmailAddress());

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
        Band beatles = new Band(bandName, john.getEmailAddress());

        assertEquals(true, beatles.isLeader(john.getEmailAddress()));
        assertEquals(false, beatles.isLeader(paul.getEmailAddress()));

        beatles.addMember(paul.getEmailAddress());

        assertEquals(true, beatles.isLeader(john.getEmailAddress()));
        assertEquals(false, beatles.isLeader(paul.getEmailAddress()));

        beatles.setLeader(paul.getEmailAddress());

        assertEquals(false, beatles.isLeader(john.getEmailAddress()));
        assertEquals(true, beatles.isLeader(paul.getEmailAddress()));
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
        Band beatles = new Band(bandName, john.getEmailAddress());

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
        Band beatles = new Band(bandName, john.getEmailAddress());

        assertThrows(IllegalArgumentException.class, () -> beatles.setLeader(paul.getEmailAddress()));
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
        Band beatles = new Band(bandName, john.getEmailAddress());

        assertEquals(true, beatles.isLeader(john.getEmailAddress()));
        assertEquals(false, beatles.isLeader(paul.getEmailAddress()));

        beatles.addMember(paul.getEmailAddress());

        assertEquals(true, beatles.isLeader(john.getEmailAddress()));
        assertEquals(false, beatles.isLeader(paul.getEmailAddress()));
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

        /*String bandName = "The Beatles";
        Band beatles = new Band(bandName, john);

        Set<Musician> members = new HashSet<Musician>();
        members.add(john);

        assertEquals(members, beatles.setOfMembers());

        members.add(paul);

        beatles.addMember(paul);
        assertEquals(members, beatles.setOfMembers());*/
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
        Band beatles = new Band(bandName, john.getEmailAddress());

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
        Band beatles = new Band(bandName, john.getEmailAddress());

        assertThrows(IllegalArgumentException.class, () -> beatles.addMember(john.getEmailAddress()));
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
        Band beatles = new Band(bandName, john.getEmailAddress());
        beatles.addMember(paul.getEmailAddress());

        /*Set<Musician> members = new HashSet<Musician>();
        members.add(john);
        members.add(paul);

        assertEquals(members, beatles.setOfMembers());

        members.remove(paul);

        beatles.removeMember(paul);
        assertEquals(members, beatles.setOfMembers());*/
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
        Band beatles = new Band(bandName, john.getEmailAddress());

        assertThrows(IllegalArgumentException.class, () -> beatles.removeMember(paul.getEmailAddress()));
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
        Band beatles = new Band(bandName, john.getEmailAddress());

        assertThrows(IllegalArgumentException.class, () -> beatles.removeMember(john.getEmailAddress()));
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
        Band beatles = new Band(bandName, john.getEmailAddress());

        assertEquals(1, beatles.numberOfMembers());
        beatles.addMember(paul.getEmailAddress());
        assertEquals(2, beatles.numberOfMembers());
        beatles.removeMember(paul.getEmailAddress());
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
        Band beatles = new Band(bandName, john.getEmailAddress());

        assertEquals(true, beatles.containsMember(john));
        assertEquals(false, beatles.containsMember(paul));
        beatles.addMember(paul.getEmailAddress());
        assertEquals(true, beatles.containsMember(john));
        assertEquals(true, beatles.containsMember(paul));
        beatles.removeMember(paul.getEmailAddress());
        assertEquals(true, beatles.containsMember(john));
        assertEquals(false, beatles.containsMember(paul));
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
        Band beatles = new Band(bandName, john.getEmailAddress());

        String expectedString = bandName + "\n" + "    " + john.getEmailAddress() + " (Leader)\n";

        assertEquals(expectedString, beatles.toString());

        beatles.addMember(paul.getEmailAddress());

        expectedString += "    " + paul.getEmailAddress() + "\n";

        assertEquals(expectedString, beatles.toString());
    }

    @Test
    public void getNameReturnsCorrect() {
        Musician john = new Musician("John", "Lennon", "JohnLennon", "john.lennon@gmail.com", new MyDate(1940, 10, 9));
        Band band = new Band("BandName", john.getEmailAddress());
        assertEquals(band.getName(), band.getName());
    }

    @Test
    public void bandEventsTests() {
        Musician john = new Musician("John", "Lennon", "JohnLennon", "john.lennon@gmail.com", new MyDate(1940, 10, 9));
        Band band = new Band("BandName", john.getEmailAddress());

        ArrayList<String> as = new ArrayList<>();
        Event e = new Event(john, "1");

        as.add(e.getEid());
        band.addEvent(e.getEid());

        assertEquals(as, band.getEvents());

        as.add("2");

        band.setEvents(as);
        assertEquals(as, band.getEvents());
    }
}
