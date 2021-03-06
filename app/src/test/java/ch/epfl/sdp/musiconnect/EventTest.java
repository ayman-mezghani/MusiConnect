package ch.epfl.sdp.musiconnect;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.musiconnect.events.Event;
import ch.epfl.sdp.musiconnect.functionnalities.MyDate;
import ch.epfl.sdp.musiconnect.users.Band;
import ch.epfl.sdp.musiconnect.users.Musician;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventTest {


    @Test
    public void eventCreationTests() {
        Musician m = new Musician("firstName", "lastName", "username", "email@gmail.com", new MyDate(2000, 1, 1));
        Musician n = new Musician("a", "b", "c", "d@gmail.com", new MyDate(2000, 1, 1));

        MyDate d = new MyDate();
        final String DEFAULT_TITLE = "Event";
        final String NEW_TITLE = "New Event Title";
        final String DEFAULT_MESSAGE = "Come watch and play!";
        final String NEW_MESSAGE = "New Event Message";
        final String ADDRESS = "Lausanne";

        Event e = new Event(m.getEmailAddress(), "0");
        assertEquals(m.getEmailAddress(), e.getHostEmailAddress());

        assertEquals("0", e.getEid());
        e.setEid("1");
        assertEquals("1", e.getEid());

        e.register(n.getEmailAddress());
        assertTrue(e.getParticipants().contains(n.getEmailAddress()));
        assertTrue(e.containsParticipant(n.getEmailAddress()));

        e.unregister(n.getEmailAddress());
        assertFalse(e.getParticipants().contains(n.getEmailAddress()));
        assertFalse(e.containsParticipant(n.getEmailAddress()));

        e.setLocation(0, 0);
        assertEquals(0.0, e.getGeoPoint().getLatitude());
        assertEquals(0.0, e.getGeoPoint().getLongitude());

        e.setAddress(ADDRESS);
        assertEquals(ADDRESS, e.getAddress());

        e.setDateTime(d);
        assertEquals(d, e.getDateTime());

        assertFalse(e.isVisible());
        e.setVisible(true);
        assertTrue(e.isVisible());

        assertEquals(DEFAULT_TITLE, e.getTitle());
        e.setTitle(NEW_TITLE);
        assertEquals(NEW_TITLE, e.getTitle());

        assertEquals(DEFAULT_MESSAGE, e.getDescription());
        e.setDescription(NEW_MESSAGE);
        assertEquals(NEW_MESSAGE, e.getDescription());
    }

    @Test
    public void eventCreationBandTest() {
        Musician m = new Musician("firstName", "lastName", "username", "email@gmail.com", new MyDate(2000, 1, 1));
        Band b = new Band("test", m.getEmailAddress());
        Band b1 = new Band("Another test", m.getEmailAddress());
        Event e = new Event(b.getEmailAddress(), "0");

        assertEquals(b.getEmailAddress(), e.getHostEmailAddress());
        assertEquals(b.getEmailAddress(), e.getParticipants().get(0));

        e.register(b1.getEmailAddress());
        assertTrue(e.getParticipants().contains(b1.getEmailAddress()));

        e.unregister(b1.getEmailAddress());
        assertFalse(e.getParticipants().contains(b1.getEmailAddress()));

    }

    @Test
    public void eventCreationThrowsIllegalArgumentExceptionTests() {
        Musician m = new Musician("firstName", "lastName", "username", "email@gmail.com", new MyDate(2000, 1, 1));
        Event e = new Event(m.getEmailAddress(), "0");

        assertThrows(IllegalArgumentException.class, () -> new Event(null, "0"));
        assertThrows(IllegalArgumentException.class, () -> new Event(null, "0"));

        assertThrows(IllegalArgumentException.class, () -> e.register(null));
        assertThrows(IllegalArgumentException.class, () -> e.unregister(null));

        assertThrows(IllegalArgumentException.class, () -> e.setAddress(null));
        assertThrows(IllegalArgumentException.class, () -> e.setLocation(300, 0));
        assertThrows(IllegalArgumentException.class, () -> e.setDateTime(null));
        assertThrows(IllegalArgumentException.class, () -> e.setTitle(null));
        assertThrows(IllegalArgumentException.class, () -> e.setDescription(null));
    }

    @Test
    public void getterSetter() {
        Musician m = new Musician("firstName", "lastName", "username", "email@gmail.com", new MyDate(2000, 1, 1));
        Musician m2 = new Musician("firstName2", "lastName2", "username2", "email2@gmail.com", new MyDate(2000, 1, 1));
        Event e = new Event(m.getEmailAddress(), "0");

        List<String> lu = new ArrayList<>();
        lu.add(m.getEmailAddress());
        lu.add(m2.getEmailAddress());

        e.setEid("eid");
        assertEquals(e.getEid(), "eid");

        e.register(m2.getEmailAddress());
        assertEquals(e.getParticipants(), lu);

        e.register(m2.getEmailAddress());
        assertEquals(e.getParticipants(), lu);

        lu.remove(m2.getEmailAddress());
        e.unregister(m2.getEmailAddress());
        assertEquals(e.getParticipants(), lu);

        e.setLocation(46.17, 6.17);
        GeoPoint p = e.getGeoPoint();
        assertEquals(p.getLatitude(), 46.17);
        assertEquals(p.getLongitude(), 6.17);

        e.setVisible(true);
        assertTrue(e.isVisible());
    }

    @Test
    public void exceptions() {
        Musician m = new Musician("firstName", "lastName", "username", "email@gmail.com", new MyDate(2000, 1, 1));

        assertThrows(IllegalArgumentException.class, () -> new Event(null, ""));

        Event e = new Event(m.getEmailAddress(), "0");
        assertThrows(IllegalArgumentException.class, () -> e.register(null));
        assertThrows(IllegalArgumentException.class, () -> e.unregister(null));
        assertThrows(IllegalArgumentException.class, () -> e.setAddress(null));
        assertThrows(IllegalArgumentException.class, () -> e.setLocation(300, 300));
        assertThrows(IllegalArgumentException.class, () -> e.setDateTime(null));
        assertThrows(IllegalArgumentException.class, () -> e.setTitle(null));
        assertThrows(IllegalArgumentException.class, () -> e.setDescription(null));
    }
}
