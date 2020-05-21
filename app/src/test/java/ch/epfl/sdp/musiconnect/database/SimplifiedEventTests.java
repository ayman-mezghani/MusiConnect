package ch.epfl.sdp.musiconnect.database;

import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.events.Event;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimplifiedEventTests {
    @Test
    public void CreationFromEventTest() {
        Event e = testEvent();
        SimplifiedEvent se = new SimplifiedEvent(e);

        assertEquals(e.getTitle(), se.getEventName());
        assertEquals(e.getDescription(), se.getDescription());
        assertEquals(e.getAddress(), se.getAddress());
        assertEquals(e.getDateTime().toDate(), se.getDateTime());
        assertEquals(e.getGeoPoint(), se.getLocation());
        assertEquals(e.getHostEmailAddress().getEmailAddress(), se.getHost());

        ArrayList<String> am = new ArrayList<>();
        for (User m : e.getParticipants())
            am.add(m.getEmailAddress());

        assertEquals(am, se.getParticipants());
    }

    @Test
    public void simplifiedEventGetterSetter() {
        Event e = testEvent();
        SimplifiedEvent se = new SimplifiedEvent();

        se.setEventName(e.getTitle());
        se.setAddress(e.getAddress());
        se.setDescription(e.getDescription());
        se.setHost(e.getHostEmailAddress().getEmailAddress());
        se.setDateTime(e.getDateTime().toDate());
        se.setLocation(e.getGeoPoint());

        assertEquals(e.getTitle(), se.getEventName());
        assertEquals(e.getDescription(), se.getDescription());
        assertEquals(e.getAddress(), se.getAddress());
        assertEquals(e.getDateTime().toDate(), se.getDateTime());
        assertEquals(e.getGeoPoint(), se.getLocation());
        assertEquals(e.getHostEmailAddress().getEmailAddress(), se.getHost());

        ArrayList<String> am = new ArrayList<>();
        am.add("mail1@gmail.com");
        am.add("mail2@gmail.com");
        se.setParticipants(am);
        assertEquals(am, se.getParticipants());
    }

    static Event testEvent() {
        Musician m = new Musician("firstName", "lastName", "username", "email@gmail.com", new MyDate(2000, 1, 1));
        return new Event(m, "1");
    }
}
