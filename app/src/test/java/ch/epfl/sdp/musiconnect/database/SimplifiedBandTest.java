package ch.epfl.sdp.musiconnect.database;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.users.Band;
import ch.epfl.sdp.musiconnect.users.Musician;
import ch.epfl.sdp.musiconnect.functionnalities.MyDate;

import static ch.epfl.sdp.musiconnect.database.SimplifiedMusicianTest.testMusician1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ch.epfl.sdp.musiconnect.database.SimplifiedDbEntry.Fields;

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
        ArrayList<String> emailAddresses = new ArrayList<String>();
        emailAddresses.add("email@gmail.com");
        emailAddresses.add("email3@gmail.com");
        emailAddresses.add("email2@gmail.com");
        sb.setMembers(emailAddresses);

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

        assertEquals((String) map.get(Fields.leader.toString()), sb.getLeader());
        assertEquals((String) map.get(Fields.bandName.toString()), sb.getBandName());
        assertEquals(map.get(Fields.members.toString()), sb.getMembers());
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

        assertEquals((String) map.get(Fields.leader.toString()), (String) mapClone.get(Fields.leader.toString()));
        assertEquals((String) map.get(Fields.bandName.toString()), (String) mapClone.get(Fields.bandName.toString()));
        assertEquals(map.get(Fields.members.toString()), mapClone.get(Fields.members.toString()));
        assertEquals(map.get(Fields.events.toString()), mapClone.get(Fields.events.toString()));
    }

    @Test
    public void toBandTest() {
        Map<String, Object> map = testMap();

        Band b = (new SimplifiedBand(map)).toBand();

        assertEquals((String) map.get(Fields.leader.toString()), b.getEmailAddress());
        assertEquals((String) map.get(Fields.bandName.toString()), b.getName());
        assertEquals((List<String>) map.get(Fields.members.toString()), b.getMusiciansEmailsAdress());
        assertEquals((List<String>) map.get(Fields.events.toString()), b.getEvents());
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
        map.put(Fields.leader.toString(), "email@gmail.com");
        map.put(Fields.bandName.toString(), "BandName");
        map.put(Fields.members.toString(), al);
        map.put(Fields.events.toString(), el);
        return map;
    }

    private Map<String, Object> ghostMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(Fields.leader.toString(), null);
        map.put(Fields.bandName.toString(), null);
        map.put(Fields.members.toString(), null);
        map.put(Fields.events.toString(), null);
        return map;
    }
}