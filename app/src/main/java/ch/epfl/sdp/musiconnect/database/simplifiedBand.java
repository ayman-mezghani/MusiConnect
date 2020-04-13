package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.Musician;

public class simplifiedBand {
    private String uid = null;
    private String leader;
    private String bandName;
    private String urlVideo;
    private Set<String> members;

    static final String UID = "uid";
    static final String LEADER = "Leader";
    static final String BANDNAME = "BandName";
    static final String URLVIDEO = "UrlVideo";
    static final String MEMBERS = "members";

    public simplifiedBand() {
    }

    public simplifiedBand(Band band) {
//        this.uid = uid;
        this.leader = band.getLeaderEmailAddress();
        this.bandName = band.getBandName();
        for (Musician m : band.setOfMembers()) {
          this.members.add(m.getEmailAddress());
        }
        this.urlVideo = band.getVideoURL();
    }

    public simplifiedBand(Map<String, Object> map) {
        this.leader = map.get(LEADER) == null ? "" : (String) map.get(LEADER);
        this.bandName = map.get(BANDNAME) == null ? "" : (String) map.get(BANDNAME);
        if(map.get(MEMBERS) != null) {
            for (String emailAdress : (List<String>) map.get(MEMBERS)) {
                this.members.add(emailAdress);
            }
        } else {
            this.members = null;
        }
        this.urlVideo = map.get(URLVIDEO) == null ? "" : (String) map.get(URLVIDEO);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> res = new HashMap<>();
        res.put(LEADER, leader);
        res.put(BANDNAME, bandName);
        res.put(MEMBERS, members);
        res.put(URLVIDEO, urlVideo);
        return res;
    }
}
