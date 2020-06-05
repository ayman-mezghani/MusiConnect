package ch.epfl.sdp.musiconnect.database;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.users.Band;

public class SimplifiedBand extends SimplifiedDbEntry implements Serializable {
    private String leader;
    private String bandName;
    private List<String> members;
    private List<String> events;

    public SimplifiedBand() {
    }

    public SimplifiedBand(Band band) {
        this.leader = band.getEmailAddress();
        this.bandName = band.getName();
        this.members = band.getMusiciansEmailsAdress();
        this.events = band.getEvents();
    }

    public SimplifiedBand(Map<String, Object> map) {
        this.leader = map.get(Fields.leader.toString()) == null ? "" : (String) map.get(Fields.leader.toString());
        this.bandName = map.get(Fields.bandName.toString()) == null ? "" : (String) map.get(Fields.bandName.toString());
        this.members = new ArrayList<>();
        this.members = map.get(Fields.members.toString()) == null ? null : (ArrayList<String>) map.get(Fields.members.toString());
        this.events = map.get(Fields.events.toString()) == null ? null : (ArrayList<String>) map.get(Fields.events.toString());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> res = new HashMap<>();
        res.put(Fields.leader.toString(), leader);
        res.put(Fields.bandName.toString(), bandName);
        res.put(Fields.members.toString(), members);
        res.put(Fields.events.toString(), events);
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
