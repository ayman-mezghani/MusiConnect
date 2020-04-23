package ch.epfl.sdp.musiconnect.database;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.Musician;

public class SimplifiedBand extends SimplifiedDbEntry implements Serializable {
    private String leader;
    private String bandName;
    private String urlVideo;
    private List<String> members;

    static final String LEADER = "Leader";
    static final String BANDNAME = "BandName";
    static final String URLVIDEO = "UrlVideo";
    static final String MEMBERS = "members";

    public SimplifiedBand() {
    }

    public SimplifiedBand(Band band) {
        this.leader = band.getLeaderEmailAddress();
        this.bandName = band.getBandName();
        this.members = new ArrayList<>();
        for (Musician m : band.setOfMembers()) {
          this.members.add(m.getEmailAddress());
        }
        this.urlVideo = band.getVideoURL();
    }

    public SimplifiedBand(Map<String, Object> map) {
        this.leader = map.get(LEADER) == null ? "" : (String) map.get(LEADER);
        this.bandName = map.get(BANDNAME) == null ? "" : (String) map.get(BANDNAME);
        this.members = new ArrayList<>();
        if(map.get(MEMBERS) != null) {
            for (String emailAddress : (ArrayList<String>) map.get(MEMBERS)) {
                this.members.add(emailAddress);
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

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        SimplifiedBand that = (SimplifiedBand) obj;
        return this.leader == that.getLeader()
                && this.bandName == that.getBandName()
                && this.urlVideo == that.getUrlVideo()
                && this.members == that.getMembers();
    }
}
