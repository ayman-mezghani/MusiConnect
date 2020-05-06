package ch.epfl.sdp.musiconnect.database;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.TypeOfUser;

public class SimplifiedBand extends SimplifiedDbEntry implements Serializable {
    private String leader;
    private String bandName;
    private String urlVideo;
    private List<String> members;
    private List<String> events;

    static final String LEADER = "Leader";
    static final String BANDNAME = "BandName";
    static final String URLVIDEO = "UrlVideo";
    static final String MEMBERS = "members";
    static final String EVENTS = "events";

    public SimplifiedBand() {
    }

    public SimplifiedBand(Band band) {
        this.leader = band.getEmailAddress();
        this.bandName = band.getBandName();
        this.members = new ArrayList<>();
        for (Musician m : band.setOfMembers()) {
          this.members.add(m.getEmailAddress());
        }
        this.urlVideo = band.getVideoURL();
        this.events = band.getEvents();
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
        this.events = map.get(EVENTS) == null ? null : (ArrayList<String>) map.get(EVENTS);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> res = new HashMap<>();
        res.put(LEADER, leader);
        res.put(BANDNAME, bandName);
        res.put(MEMBERS, members);
        res.put(URLVIDEO, urlVideo);
        res.put(EVENTS, events);
        return res;
    }

    public Band toBand() {
        Musician m = new Musician();
        m.setEmailAddress(this.getLeader());
        m.setTypeOfUser(TypeOfUser.Musician);
        m.setFirstName("firstname");
        m.setLastName("lastname");
        m.setUserName(this.getLeader().split("@")[0]);
        Band b = new Band(this.bandName, m);
        b.setMusicianEmailAdresses((ArrayList<String>) this.members);
        b.setEvents(this.events);
        return b;
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

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> e) {
        this.events = e;
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
