package ch.epfl.sdp.musiconnect;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.musiconnect.events.Event;

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

        Event e = new Event(m, "0");

        assertEquals(m.getFirstName(), ((Musician)e.getCreator()).getFirstName());

        assertEquals("0", e.getEid());
        e.setEid("1");
        assertEquals("1", e.getEid());

        e.register(n);
        assertTrue(e.getParticipants().contains(n));
        assertTrue(e.containsParticipant(n.getEmailAddress()));

        e.unregister(n);
        assertFalse(e.getParticipants().contains(n));
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
        Event e = new Event(b, "0");

        assertEquals(b.getName(), e.getCreator().getName());
        assertEquals(b.getName(), e.getParticipants().get(0).getName());

        e.register(b1);
        assertTrue(e.getParticipants().contains(b1));

        e.unregister(b1);
        assertFalse(e.getParticipants().contains(b1));

    }

    @Test
    public void eventCreationThrowsIllegalArgumentExceptionTests() {
        Musician m = new Musician("firstName", "lastName", "username", "email@gmail.com", new MyDate(2000, 1, 1));
        Event e = new Event(m, "0");

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
        Event e = new Event(m, "0");

        List<User> lu = new ArrayList<>();
        lu.add(m);
        lu.add(m2);

        e.setEid("eid");
        assertEquals(e.getEid(), "eid");

        e.register(m2);
        assertEquals(e.getParticipants(), lu);

        e.register(m2);
        assertEquals(e.getParticipants(), lu);

        lu.remove(m2);
        e.unregister(m2);
        assertEquals(e.getParticipants(), lu);

        /*
        e.setLocation(46.17, 6.17);
        Location l = e.getLocation();
        assertEquals(l.getLatitude(), 0);
        assertEquals(l.getLongitude(), 0);

        e.setLocation(l);
        assertEquals(l.getLatitude(), 0);
        assertEquals(l.getLongitude(), 0);
         */

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
        Musician m2 = new Musician("firstName2", "lastName2", "username2", "email2@gmail.com", new MyDate(2000, 1, 1));

        assertThrows(IllegalArgumentException.class, () -> new Event(null, ""));

        Event e = new Event(m, "0");
        assertThrows(IllegalArgumentException.class, () -> e.register(null));
        assertThrows(IllegalArgumentException.class, () -> e.unregister(null));
        assertThrows(IllegalArgumentException.class, () -> e.setAddress(null));
        assertThrows(IllegalArgumentException.class, () -> e.setLocation(300, 300));
        assertThrows(IllegalArgumentException.class, () -> e.setDateTime(null));
        assertThrows(IllegalArgumentException.class, () -> e.setTitle(null));
        assertThrows(IllegalArgumentException.class, () -> e.setDescription(null));
    }
}
