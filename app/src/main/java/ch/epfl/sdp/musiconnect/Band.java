package ch.epfl.sdp.musiconnect;

import java.util.Set;
import java.util.HashSet;

public class Band {

    private String bandName;
    private Musician leader;
    private Set<Musician> members;
    private MyDate joinDate;
    private String videoURL;
    private Location location;

    private static final int MAX_NAME_LENGTH = 16;
    private static final int MAX_VIDEO_URL_LENGTH = 2048;

    private static final double EPFL_LATITUDE = 46.5185941;
    private static final double EPFL_LONGITUDE = 6.5618969;


    public Band(String newBandName, Musician newLeader) {
        setBandName(newBandName);
        members = new HashSet<Musician>();
        addMember(newLeader);
        setLeader(newLeader);
        joinDate = new MyDate();
        videoURL = "";
        location = new Location(EPFL_LATITUDE, EPFL_LONGITUDE);
    }


    public void setBandName(String newBandName) {
        if (newBandName.isEmpty()) {
            throw new IllegalArgumentException("Band name can not be empty");
        } else if (newBandName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Band name too long");
        }

        bandName = newBandName;
    }

    public String getBandName() {
        return bandName;
    }

    public void setLeader(Musician newLeader) {
        if (newLeader == null) {
            throw new IllegalArgumentException("Leader is invalid");
        } else if (!containsMember(newLeader)) {
            throw new IllegalArgumentException("Leader must already be member of the band");
        }

        leader = newLeader;
    }

    public boolean isLeader(Musician member) {
        return leader.equals(member);
    }

    public String getLeaderFirstName() {
        return leader.getFirstName();
    }

    public String getLeaderLastName() {
        return leader.getLastName();
    }

    public String getLeaderUserName() {
        return leader.getUserName();
    }

    public String getLeaderEmailAddress() {
        return leader.getEmailAddress();
    }

    public void addMember(Musician member) {
        if (member == null) {
            throw new IllegalArgumentException("Member is invalid");
        } else if (containsMember(member)) {
            throw new IllegalArgumentException("Member already exists");
        }

        members.add(member);
    }

    public void removeMember(Musician member) {
        if (!containsMember(member)) {
            throw new IllegalArgumentException("Member does not exist");
        } else if (isLeader(member)) {
            throw new IllegalArgumentException("Impossible to remove the leader from the band");
        }

        members.remove(member);
    }

    public int numberOfMembers() {
        return members.size();
    }

    public boolean containsMember(Musician member) {
        return members.contains(member);
    }

    public Set<Musician> setOfMembers() {
        return new HashSet<Musician>(members);
    }

    public MyDate getJoinDate() {
        return new MyDate(joinDate);
    }

    public void setVideoURL(String newVideoURL) {
        if (newVideoURL.length() > MAX_VIDEO_URL_LENGTH) {
            throw new IllegalArgumentException("Video URL too long");
        }

        videoURL = newVideoURL;
    }

    public String getVideoURL() {
        if (videoURL.isEmpty()) {
            throw new Error("No video URL is present");
        }

        return videoURL;
    }

    public void setLocation(Location newLocation) {
        location.setLocation(newLocation);
    }

    public Location getLocation() {
        return location.getLocation();
    }


    @Override
    public String toString() {
        String tmp = getBandName() + "\n";
        tmp += "    " + getLeaderFirstName() + " " + getLeaderLastName() + " (Leader)\n";
        for (Musician member : members) {
            if (!isLeader(member)) {
                tmp += "    " + member.getFirstName() + " " + member.getLastName() + "\n";
            }
        }

        return tmp;
    }

}
