package ch.epfl.sdp.musiconnect;

import java.util.Set;
import java.util.HashSet;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class Band extends User implements Performer {

    private String bandName;
    private Musician leader;
    private Set<Musician> members;
    private String videoURL;

    private static final int MAX_BAND_NAME_LENGTH = 16;
    private static final int MAX_VIDEO_URL_LENGTH = 2048;


    public Band(String bandName, Musician leader) {
        super();
        setBandName(bandName);
        members = new HashSet<Musician>();
        addMember(leader);
        setLeader(leader);
        videoURL = "";
    }


    public void setBandName(String bandName) {
        if (bandName.isEmpty()) {
            throw new IllegalArgumentException("Band name cannot be empty");
        } else if (bandName.length() > MAX_BAND_NAME_LENGTH) {
            throw new IllegalArgumentException("Band name too long");
        }

        this.bandName = bandName;
    }

    public String getBandName() {
        return bandName;
    }

    public void setLeader(Musician leader) {
        if (leader == null) {
            throw new NullPointerException("Leader is invalid");
        } else if (!containsMember(leader)) {
            throw new IllegalArgumentException("Leader must already be member of the band");
        }

        this.leader = leader;
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
            throw new NullPointerException("Member is invalid");
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

    public void setVideoURL(String videoURL) {
        if (videoURL.length() > MAX_VIDEO_URL_LENGTH) {
            throw new IllegalArgumentException("Video URL too long");
        }

        this.videoURL = videoURL;
    }

    public String getVideoURL() {
        if (videoURL.isEmpty()) {
            throw new Error("No video URL is present");
        }

        return videoURL;
    }

    @Override
    public String getName() {
        return bandName;
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
