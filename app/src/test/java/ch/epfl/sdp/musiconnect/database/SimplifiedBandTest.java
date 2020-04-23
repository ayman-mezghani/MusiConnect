package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.MyLocation;
import ch.epfl.sdp.musiconnect.TypeOfUser;

import static ch.epfl.sdp.musiconnect.database.SimplifiedBand.*;
import static ch.epfl.sdp.musiconnect.database.SimplifiedMusicianTest.testMusician;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SimplifiedBandTest {
        @Test
        public void CreationFromBandTest() {
            Musician musician = testMusician();
            Band b = new Band("testBand", musician);
            SimplifiedBand sb = new SimplifiedBand(b);
            sb.setUrlVideo("video");
            b.setVideoURL(sb.getUrlVideo());

            Musician musician2 = new Musician("firstName2", "lastName2", "username2", "email2@gmail.com", new MyDate(2000, 1, 1));
            Musician musician3 = new Musician("firstName3", "lastName3", "username3", "email3@gmail.com", new MyDate(2000, 1, 1));

            b.addMember(musician2);
            b.addMember(musician3);
            ArrayList<String> emailsAdress = new ArrayList<String>();
            emailsAdress.add("email@gmail.com");
            emailsAdress.add("email3@gmail.com");
            emailsAdress.add("email2@gmail.com");
            sb.setMembers(emailsAdress);

            assertEquals(b.getBandName(), sb.getBandName());
            assertEquals(b.getLeaderEmailAddress(), sb.getLeader());
            assertEquals(b.getVideoURL(), sb.getUrlVideo());
            //assertEquals(b.getMusicianEmailsAdress(), sb.getMembers());

            sb.setBandName("bandNameTest");
            sb.setUid("uid");
            sb.setLeader("email2@gmail.com");

            assertEquals("email2@gmail.com", sb.getLeader());
            assertEquals("bandNameTest", sb.getBandName());
            assertEquals(sb.getUid(), "uid");
        }

        @Test
        public void CreationFromMapTest() {
            Map<String, Object> map = testMap();

            SimplifiedBand sb = new SimplifiedBand(map);

            assertEquals((String) map.get(LEADER), sb.getLeader());
            assertEquals((String) map.get(BANDNAME), sb.getBandName());
            assertEquals(map.get(MEMBERS), sb.getMembers());
            assertEquals((String) map.get(URLVIDEO), sb.getUrlVideo());
        }

        @Test
        public void createInsanceWithMapContainingNulls() {
            Map<String, Object> map = ghostMap();
            SimplifiedBand sb = new SimplifiedBand(map);

            assertEquals("", sb.getLeader());
            assertEquals("", sb.getBandName());
            assertEquals(null, sb.getMembers());
            assertEquals("", sb.getUrlVideo());
        }

        @Test
        public void mapToMapIsTheSame() {
            Map<String, Object> map = testMap();
            Map<String, Object> mapClone = (new SimplifiedBand(map)).toMap();

            assertEquals((String) map.get(LEADER), (String) mapClone.get(LEADER));
            assertEquals((String) map.get(BANDNAME), (String) mapClone.get(BANDNAME));
            assertEquals(map.get(MEMBERS), mapClone.get(MEMBERS));
            assertEquals((String) map.get(URLVIDEO), (String) mapClone.get(URLVIDEO));
        }

        @Test
        public void ghostMusician() {
            SimplifiedMusician sm = new SimplifiedMusician();
            Map<String, Object> m = sm.toMap();
            for (String key : m.keySet()) {
                assertNull(m.get(key));;
            }
        }

        private Map<String, Object> testMap() {
            ArrayList<String> al = new ArrayList<>();
            al.add("email@gmail.com");
            al.add("email3@gmail.com");
            al.add("email2@gmail.com");
            Map<String, Object> map = new HashMap<>();
            map.put(LEADER, "email@gmail.com");
            map.put(BANDNAME, "BandName");
            map.put(MEMBERS, al);
            map.put(URLVIDEO, "urlVideo");
            return map;
        }

    private Map<String, Object> ghostMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(LEADER, null);
        map.put(BANDNAME, null);
        map.put(MEMBERS, null);
        map.put(URLVIDEO, null);
        return map;
    }
}