package ch.epfl.sdp.musiconnect;

import org.junit.Test;

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

        Event e = new Event(m, 0);

        assertEquals(m.getFirstName(), ((Musician)e.getCreator()).getFirstName());
        assertEquals(m.getFirstName(), ((Musician)e.getParticipants().get(0)).getFirstName());

        assertEquals(0, e.getEid());

        e.register(n);
        assertTrue(e.getParticipants().contains(n));

        e.unregister(n);
        assertFalse(e.getParticipants().contains(n));

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
    public void eventCreationThrowsIllegalArgumentExceptionTests() {
        Musician m = new Musician("firstName", "lastName", "username", "email@gmail.com", new MyDate(2000, 1, 1));
        Musician n = new Musician("a", "b", "c", "d@gmail.com", new MyDate(2000, 1, 1));
        Event e = new Event(m, 0);

        assertThrows(IllegalArgumentException.class, () -> new Event(null, 0));
        assertThrows(IllegalArgumentException.class, () -> new Event(m, -1));

        assertThrows(IllegalArgumentException.class, () -> e.register(null));
        assertThrows(IllegalArgumentException.class, () -> e.unregister(null));
        assertThrows(IllegalArgumentException.class, () -> e.unregister(n));

        assertThrows(IllegalArgumentException.class, () -> e.setAddress(null));
        assertThrows(IllegalArgumentException.class, () -> e.setLocation(300, 0));
        assertThrows(IllegalArgumentException.class, () -> e.setDateTime(null));
        assertThrows(IllegalArgumentException.class, () -> e.setTitle(null));
        assertThrows(IllegalArgumentException.class, () -> e.setDescription(null));
    }

}
