package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Instrument;
import ch.epfl.sdp.musiconnect.Level;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.MyLocation;
import ch.epfl.sdp.musiconnect.TypeOfUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ch.epfl.sdp.musiconnect.database.SimplifiedDbEntry.Fields;


public class SimplifiedMusicianTest {
    @Test
    public void CreationFromMusicianTest() {
        Musician musician = testMusician1();

        SimplifiedMusician sMusician = new SimplifiedMusician(musician);

        assertEquals(musician.getUserName(), sMusician.getUsername());
        assertEquals(musician.getFirstName(), sMusician.getFirstName());
        assertEquals(musician.getLastName(), sMusician.getLastName());
        assertEquals(musician.getEmailAddress(), sMusician.getEmail());

        MyDate bday = musician.getBirthday();
        Date sbday = sMusician.getBirthday();

        assertEquals(bday.getYear(), sbday.getYear() + 1900);
        assertEquals(bday.getMonth(), sbday.getMonth() + 1);
        assertEquals(bday.getDate(), sbday.getDate());
        assertEquals(bday.getHours(), sbday.getHours());
        assertEquals(bday.getMinutes(), sbday.getMinutes());

        MyDate joindate = musician.getJoinDate();
        Date sjoindate = sMusician.getJoinDate();

        assertEquals(joindate.getYear(), sjoindate.getYear() + 1900);
        assertEquals(joindate.getMonth(), sjoindate.getMonth() + 1);
        assertEquals(joindate.getDate(), sjoindate.getDate());
        assertEquals(joindate.getHours(), sjoindate.getHours());
        assertEquals(joindate.getMinutes(), sjoindate.getMinutes());

        MyLocation loc = musician.getLocation();
        GeoPoint sloc = sMusician.getLocation();

        assertEquals(loc.getLatitude(), sloc.getLatitude());
        assertEquals(loc.getLongitude(), sloc.getLongitude());

        TypeOfUser t = musician.getTypeOfUser();
        String t2 = sMusician.getTypeOfUser();

        assertEquals(t, TypeOfUser.valueOf(t2));
    }

    @Test
    public void CreationFromMapTest() {
        Map<String, Object> map = testMap();

        SimplifiedMusician sMusician = new SimplifiedMusician(map);

        assertEquals((String) map.get(Fields.username.toString()), sMusician.getUsername());
        assertEquals((String) map.get(Fields.firstName.toString()), sMusician.getFirstName());
        assertEquals((String) map.get(Fields.lastName.toString()), sMusician.getLastName());
        assertEquals((String) map.get(Fields.email.toString()), sMusician.getEmail());
        assertEquals((String) map.get(Fields.typeOfUser.toString()).toString(), sMusician.getTypeOfUser().toString());
        assertEquals(((Timestamp) map.get(Fields.birthday.toString())).toDate(), sMusician.getBirthday());
        assertEquals(((Timestamp) map.get(Fields.joinDate.toString())).toDate(), sMusician.getJoinDate());
        assertEquals((GeoPoint) map.get(Fields.location.toString()), sMusician.getLocation());
    }

    @Test
    public void createInstanceWithMapContainingNulls() {
        Map<String, Object> map = new HashMap<>();
        map.put(Fields.username.toString(), null);
        map.put(Fields.firstName.toString(), null);
        map.put(Fields.lastName.toString(), null);
        map.put(Fields.email.toString(), null);
        map.put(Fields.typeOfUser.toString(), null);
        map.put(Fields.birthday.toString(), null);
        map.put(Fields.joinDate.toString(), null);
        map.put(Fields.location.toString(), null);

        SimplifiedMusician sMusician = new SimplifiedMusician(map);

        assertEquals("", sMusician.getUsername());
        assertEquals("", sMusician.getFirstName());
        assertEquals("", sMusician.getLastName());
        assertEquals("", sMusician.getEmail());
        assertEquals("", sMusician.getTypeOfUser());
        assertEquals((Timestamp) map.get(Fields.birthday.toString()), sMusician.getBirthday());
        assertEquals((Timestamp) map.get(Fields.joinDate.toString()), sMusician.getJoinDate());
        assertEquals((GeoPoint) map.get(Fields.location.toString()), sMusician.getLocation());
    }

    @Test
    public void musicianToMusicianIsTheSame() {
        Musician musician = testMusician1();
        Musician musicianClone = (new SimplifiedMusician(musician)).toMusician();

        assertEquals(musician.getUserName(), musicianClone.getUserName());
        assertEquals(musician.getFirstName(), musicianClone.getFirstName());
        assertEquals(musician.getLastName(), musicianClone.getLastName());
        assertEquals(musician.getEmailAddress(), musicianClone.getEmailAddress());
        assertEquals(musician.getTypeOfUser(), musicianClone.getTypeOfUser());
        assertEquals(musician.getBirthday(), musicianClone.getBirthday());
        assertEquals(musician.getJoinDate(), musicianClone.getJoinDate());
        assertEquals(musician.getLocation(), musicianClone.getLocation());
    }

    @Test
    public void mapToMapIsTheSame() {
        Map<String, Object> map = testMap();
        Map<String, Object> mapClone = (new SimplifiedMusician(map)).toMap();

        assertEquals((String) map.get(Fields.username.toString()), (String) mapClone.get(Fields.username.toString()));
        assertEquals((String) map.get(Fields.firstName.toString()), (String) mapClone.get(Fields.firstName.toString()));
        assertEquals((String) map.get(Fields.lastName.toString()), (String) mapClone.get(Fields.lastName.toString()));
        assertEquals((String) map.get(Fields.email.toString()), (String) mapClone.get(Fields.email.toString()));
        assertEquals((String) map.get(Fields.typeOfUser.toString()), (String) mapClone.get(Fields.typeOfUser.toString()));
        assertEquals((Timestamp) map.get(Fields.birthday.toString()), new Timestamp((Date) mapClone.get(Fields.birthday.toString())));
        assertEquals((Timestamp) map.get(Fields.joinDate.toString()), new Timestamp((Date) mapClone.get(Fields.joinDate.toString())));
        assertEquals((GeoPoint) map.get(Fields.location.toString()), (GeoPoint) mapClone.get(Fields.location.toString()));
    }

    @Test
    public void ghostMusician() {
        SimplifiedMusician sm = new SimplifiedMusician();
        Map<String, Object> m = sm.toMap();
        for (String key : m.keySet()) {
            assertNull(m.get(key));
        }
    }

    @Test
    public void equalsTest() {
        Musician m1 = testMusician1();
        Musician m2 = testMusician2();

        assertNotEquals(m1, m2);

        assertEquals(m1, m1);
    }

    @Test
    public void instrumentsTest() {
        Map<Instrument, Level> m = new HashMap<>();
        m.put(Instrument.GUITAR, Level.BEGINNER);

        List<Map<String, String>> l = new ArrayList<>();
        Map<String, String> m2 = new HashMap<>();
        m2.put(Fields.instrument.toString(), Instrument.GUITAR.toString());
        m2.put(Fields.level.toString(), Level.BEGINNER.toString());
        l.add(m2);

        Musician musician = testMusician1();
        musician.setInstruments(m);
        SimplifiedMusician sm = new SimplifiedMusician(musician);

        assertEquals(l, sm.getInstruments());

        m.put(Instrument.ACCORDION, Level.ADVANCED);
        musician.setInstruments(m);
        sm = new SimplifiedMusician(musician);
        Musician musician2 = sm.toMusician();
        assertEquals(m, musician2.getInstruments());
    }

    static Musician testMusician1() {
        Musician m = new Musician("firstName", "lastName", "username", "email@gmail.com", new MyDate(2000, 1, 1));
        m.setTypeOfUser(TypeOfUser.Musician);
        return m;
    }

    static Musician testMusician2() {
        Musician m = new Musician("first", "last", "user", "mail@gmail.com", new MyDate(2001, 1, 1));
        m.setTypeOfUser(TypeOfUser.Musician);
        return m;
    }

    private Map<String, Object> testMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(Fields.username.toString(), "username");
        map.put(Fields.firstName.toString(), "firstName");
        map.put(Fields.lastName.toString(), "lastName");
        map.put(Fields.email.toString(), "email@gmail.com");
        map.put(Fields.typeOfUser.toString(), TypeOfUser.Musician.toString());
        map.put(Fields.birthday.toString(), new Timestamp(new Date()));
        map.put(Fields.joinDate.toString(), new Timestamp(new Date()));
        map.put(Fields.location.toString(), new GeoPoint(0, 0));
        return map;
    }
}
