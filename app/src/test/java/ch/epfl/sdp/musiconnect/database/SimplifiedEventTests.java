package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.events.Event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ch.epfl.sdp.musiconnect.database.SimplifiedDbEntry.Fields;

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
        assertEquals(e.getHostEmailAddress(), se.getHost());


        ArrayList<String> am = new ArrayList<>(e.getParticipants());
        assertEquals(am, se.getParticipants());
    }

    @Test
    public void simplifiedEventGetterSetter() {
        Event e = testEvent();
        SimplifiedEvent se = new SimplifiedEvent();

        se.setEventName(e.getTitle());
        se.setAddress(e.getAddress());
        se.setDescription(e.getDescription());
        se.setHost(e.getHostEmailAddress());
        se.setDateTime(e.getDateTime().toDate());
        se.setLocation(e.getGeoPoint());
        se.setVisible(e.isVisible());

        assertEquals(e.getTitle(), se.getEventName());
        assertEquals(e.getDescription(), se.getDescription());
        assertEquals(e.getAddress(), se.getAddress());
        assertEquals(e.getDateTime().toDate(), se.getDateTime());
        assertEquals(e.getGeoPoint(), se.getLocation());
        assertEquals(e.getHostEmailAddress(), se.getHost());
        assertEquals(e.isVisible(), se.getVisible());


        ArrayList<String> am = new ArrayList<>();
        am.add("mail1@gmail.com");
        am.add("mail2@gmail.com");
        se.setParticipants(am);
        assertEquals(am, se.getParticipants());
    }

    @Test
    public void toMapTest() {
        Map<String, Object> map = new HashMap<>();
        map.put(Fields.host.toString(), "toto");
        map.put(Fields.participants.toString(), Arrays.asList("p1", "p2"));
        map.put(Fields.address.toString(), "shire");
        map.put(Fields.location.toString(), new GeoPoint(0,0));
        map.put(Fields.dateTime.toString(), new Timestamp(new Date()));
        map.put(Fields.eventName.toString(), "fire");
        map.put(Fields.description.toString(), "fire is bad");
        map.put(Fields.visible.toString(), false);

        SimplifiedEvent se = new SimplifiedEvent(map);

        Map<String, Object> map2 = se.toMap();

        assertEquals((String) map.get(Fields.host.toString()), (String) map2.get(Fields.host.toString()));
        assertEquals((List<String>) map.get(Fields.participants.toString()), (List<String>) map2.get(Fields.participants.toString()));
        assertEquals((String) map.get(Fields.address.toString()), (String) map2.get(Fields.address.toString()));
        assertEquals((GeoPoint) map.get(Fields.location.toString()), (GeoPoint) map2.get(Fields.location.toString()));
        assertEquals((Timestamp) map.get(Fields.dateTime.toString()), new Timestamp((Date) map2.get(Fields.dateTime.toString())));
        assertEquals((String) map.get(Fields.eventName.toString()), (String) map2.get(Fields.eventName.toString()));
        assertEquals((String) map.get(Fields.description.toString()), (String) map2.get(Fields.description.toString()));
        assertEquals((boolean) map.get(Fields.visible.toString()), (boolean) map2.get(Fields.visible.toString()));
    }

    @Test
    public void toEventTest() {
        Event e = testEvent();
        e.register("toto");
        Event e2 = (new SimplifiedEvent(e)).toEvent("1");

        assertEquals(e.getAddress(), e2.getAddress());
        assertEquals(e.getDateTime(), e2.getDateTime());
        assertEquals(e.getEid(), e2.getEid());
        assertEquals(e.getLocation(), e2.getLocation());
        assertEquals(e.getParticipants(), e2.getParticipants());
        assertEquals(e.getTitle(), e2.getTitle());
        assertEquals(e.getDescription(), e2.getDescription());
        assertEquals(e.getHostEmailAddress(), e2.getHostEmailAddress());

    }

    static Event testEvent() {
        Musician m = new Musician("firstName", "lastName", "username", "email@gmail.com", new MyDate(2000, 1, 1));
        return new Event(m.getEmailAddress(), "1");
    }
}
