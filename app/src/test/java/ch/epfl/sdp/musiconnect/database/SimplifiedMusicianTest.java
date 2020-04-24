package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.MyLocation;
import ch.epfl.sdp.musiconnect.TypeOfUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        assertEquals((String) map.get(SimplifiedMusician.USERNAME), sMusician.getUsername());
        assertEquals((String) map.get(SimplifiedMusician.FIRSTNAME), sMusician.getFirstName());
        assertEquals((String) map.get(SimplifiedMusician.LASTNAME), sMusician.getLastName());
        assertEquals((String) map.get(SimplifiedMusician.EMAIL), sMusician.getEmail());
        assertEquals((String) map.get(SimplifiedMusician.TYPEOFUSER).toString(), sMusician.getTypeOfUser().toString());
        assertEquals(((Timestamp) map.get(SimplifiedMusician.BIRTHDAY)).toDate(), sMusician.getBirthday());
        assertEquals(((Timestamp) map.get(SimplifiedMusician.JOINDATE)).toDate(), sMusician.getJoinDate());
        assertEquals((GeoPoint) map.get(SimplifiedMusician.LOCATION), sMusician.getLocation());
    }

    @Test
    public void createInsanceWithMapContainingNulls() {
        Map<String, Object> map = new HashMap<>();
        map.put(SimplifiedMusician.USERNAME, null);
        map.put(SimplifiedMusician.FIRSTNAME, null);
        map.put(SimplifiedMusician.LASTNAME, null);
        map.put(SimplifiedMusician.EMAIL, null);
        map.put(SimplifiedMusician.TYPEOFUSER, null);
        map.put(SimplifiedMusician.BIRTHDAY, null);
        map.put(SimplifiedMusician.JOINDATE, null);
        map.put(SimplifiedMusician.LOCATION, null);

        SimplifiedMusician sMusician = new SimplifiedMusician(map);

        assertEquals("", sMusician.getUsername());
        assertEquals("", sMusician.getFirstName());
        assertEquals("", sMusician.getLastName());
        assertEquals("", sMusician.getEmail());
        assertEquals("", sMusician.getTypeOfUser());
        assertEquals((Timestamp) map.get(SimplifiedMusician.BIRTHDAY), sMusician.getBirthday());
        assertEquals((Timestamp) map.get(SimplifiedMusician.JOINDATE), sMusician.getJoinDate());
        assertEquals((GeoPoint) map.get(SimplifiedMusician.LOCATION), sMusician.getLocation());
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

        assertEquals((String) map.get(SimplifiedMusician.USERNAME), (String) mapClone.get(SimplifiedMusician.USERNAME));
        assertEquals((String) map.get(SimplifiedMusician.FIRSTNAME), (String) mapClone.get(SimplifiedMusician.FIRSTNAME));
        assertEquals((String) map.get(SimplifiedMusician.LASTNAME), (String) mapClone.get(SimplifiedMusician.LASTNAME));
        assertEquals((String) map.get(SimplifiedMusician.EMAIL), (String) mapClone.get(SimplifiedMusician.EMAIL));
        assertEquals((String) map.get(SimplifiedMusician.TYPEOFUSER), (String) mapClone.get(SimplifiedMusician.TYPEOFUSER));
        assertEquals((Timestamp) map.get(SimplifiedMusician.BIRTHDAY), new Timestamp((Date) mapClone.get(SimplifiedMusician.BIRTHDAY)));
        assertEquals((Timestamp) map.get(SimplifiedMusician.JOINDATE), new Timestamp((Date) mapClone.get(SimplifiedMusician.JOINDATE)));
        assertEquals((GeoPoint) map.get(SimplifiedMusician.LOCATION), (GeoPoint) mapClone.get(SimplifiedMusician.LOCATION));
    }

    @Test
    public void ghostMusician() {
        SimplifiedMusician sm = new SimplifiedMusician();
        Map<String, Object> m = sm.toMap();
        for (String key : m.keySet()) {
            assertNull(m.get(key));;
        }
    }

    @Test
    public void equalsTest() {
        Musician m1 = testMusician1();
        Musician m2 = testMusician2();

        assertFalse(m1.equals(m2));

        assertTrue(m1.equals(m1));
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
        map.put(SimplifiedMusician.USERNAME, "username");
        map.put(SimplifiedMusician.FIRSTNAME, "firstName");
        map.put(SimplifiedMusician.LASTNAME, "lastName");
        map.put(SimplifiedMusician.EMAIL, "email@gmail.com");
        map.put(SimplifiedMusician.TYPEOFUSER, TypeOfUser.Musician.toString());
        map.put(SimplifiedMusician.BIRTHDAY, new Timestamp(new Date()));
        map.put(SimplifiedMusician.JOINDATE, new Timestamp(new Date()));
        map.put(SimplifiedMusician.LOCATION, new GeoPoint(0, 0));
        return map;
    }
}
