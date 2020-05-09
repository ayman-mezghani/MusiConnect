package ch.epfl.sdp.musiconnect.database;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;

import static ch.epfl.sdp.musiconnect.database.SimplifiedBand.BANDNAME;
import static ch.epfl.sdp.musiconnect.database.SimplifiedBand.LEADER;
import static ch.epfl.sdp.musiconnect.database.SimplifiedBand.MEMBERS;
import static ch.epfl.sdp.musiconnect.database.SimplifiedMusicianTest.testMusician1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SimplifiedBandTest {
    @Test
    public void CreationFromBandTest() {
        Musician musician = testMusician1();
        Band b = new Band("testBand", musician.getEmailAddress());
        SimplifiedBand sb = new SimplifiedBand(b);

        Musician musician2 = new Musician("firstName2", "lastName2", "username2", "email2@gmail.com", new MyDate(2000, 1, 1));
        Musician musician3 = new Musician("firstName3", "lastName3", "username3", "email3@gmail.com", new MyDate(2000, 1, 1));

        b.addMember(musician2.getEmailAddress());
        b.addMember(musician3.getEmailAddress());
        ArrayList<String> emailsAdress = new ArrayList<String>();
        emailsAdress.add("email@gmail.com");
        emailsAdress.add("email3@gmail.com");
        emailsAdress.add("email2@gmail.com");
        sb.setMembers(emailsAdress);

        assertEquals(b.getName(), sb.getBandName());
        assertEquals(b.getEmailAddress(), sb.getLeader());
        //assertEquals(b.getMusicianEmailsAdress(), sb.getMembers());

        sb.setBandName("bandNameTest");
        sb.setLeader("email2@gmail.com");

        assertEquals("email2@gmail.com", sb.getLeader());
        assertEquals("bandNameTest", sb.getBandName());

        ArrayList<String> s = new ArrayList<>();
        s.add("1");
        s.add("2");
        sb.setEvents(s);
        assertEquals(s, sb.getEvents());
    }

    @Test
    public void CreationFromMapTest() {
        Map<String, Object> map = testMap();

        SimplifiedBand sb = new SimplifiedBand(map);

        assertEquals((String) map.get(LEADER), sb.getLeader());
        assertEquals((String) map.get(BANDNAME), sb.getBandName());
        assertEquals(map.get(MEMBERS), sb.getMembers());
    }

    @Test
    public void createInsanceWithMapContainingNulls() {
        Map<String, Object> map = ghostMap();
        SimplifiedBand sb = new SimplifiedBand(map);

        assertEquals("", sb.getLeader());
        assertEquals("", sb.getBandName());
        assertNull(sb.getMembers());
        assertNull(sb.getEvents());
    }

    @Test
    public void mapToMapIsTheSame() {
        Map<String, Object> map = testMap();
        Map<String, Object> mapClone = (new SimplifiedBand(map)).toMap();

        assertEquals((String) map.get(LEADER), (String) mapClone.get(LEADER));
        assertEquals((String) map.get(BANDNAME), (String) mapClone.get(BANDNAME));
        assertEquals(map.get(MEMBERS), mapClone.get(MEMBERS));
        assertEquals(map.get("events"), mapClone.get("events"));
    }

    @Test
    public void ghostBand() {
        SimplifiedBand sb = new SimplifiedBand();
        Map<String, Object> m = sb.toMap();
        for (String key : m.keySet()) {
            assertNull(m.get(key));
        }
    }

    private Map<String, Object> testMap() {
        ArrayList<String> al = new ArrayList<>();
        al.add("email@gmail.com");
        al.add("email3@gmail.com");
        al.add("email2@gmail.com");

        ArrayList<String> el = new ArrayList<>();
        el.add("1");
        el.add("2");
        el.add("3");

        Map<String, Object> map = new HashMap<>();
        map.put(LEADER, "email@gmail.com");
        map.put(BANDNAME, "BandName");
        map.put(MEMBERS, al);
        map.put("events", el);
        return map;
    }

    private Map<String, Object> ghostMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(LEADER, null);
        map.put(BANDNAME, null);
        map.put(MEMBERS, null);
        map.put("events", null);
        return map;
    }
}