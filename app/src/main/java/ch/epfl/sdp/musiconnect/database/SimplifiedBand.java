package ch.epfl.sdp.musiconnect.database;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Band;

public class SimplifiedBand extends SimplifiedDbEntry implements Serializable {
    private String leader;
    private String bandName;
    private List<String> members;
    private List<String> events;

    static final String LEADER = "leader";
    static final String BANDNAME = "bandName";
    static final String MEMBERS = "members";
    static final String EVENTS = "events";

    public SimplifiedBand() {
    }

    public SimplifiedBand(Band band) {
        this.leader = band.getEmailAddress();
        this.bandName = band.getName();
        this.members = band.getMusiciansEmailsAdress();
        this.events = band.getEvents();
    }

    public SimplifiedBand(Map<String, Object> map) {
        this.leader = map.get(LEADER) == null ? "" : (String) map.get(LEADER);
        this.bandName = map.get(BANDNAME) == null ? "" : (String) map.get(BANDNAME);
        this.members = new ArrayList<>();
        this.members = map.get(MEMBERS) == null ? null : (ArrayList<String>) map.get(MEMBERS);
        this.events = map.get(EVENTS) == null ? null : (ArrayList<String>) map.get(EVENTS);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> res = new HashMap<>();
        res.put(LEADER, leader);
        res.put(BANDNAME, bandName);
        res.put(MEMBERS, members);
        res.put(EVENTS, events);
        return res;
    }

    public Band toBand() {
        Band b = new Band(this.getBandName(), this.getLeader());
        b.setMusiciansEmailAdresses((ArrayList<String>) this.members);
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
                && this.members == that.getMembers();
    }
}
