package ch.epfl.sdp.musiconnect;

import java.util.Set;

public class Band {

    private String bandName;
    private Musician leader;
    private Set<Musician> members;
    private String videoURL;
    private String location; // TODO : Setter and getter methods for location

    private int maxNameLength = 16;
    private int maxEmailAddressLength = 64;
    private int maxVideoURLLength = 256;


    public Band(String newBandName, Musician newLeader, String newVideoURL) {
        setBandName(newBandName);
        addMember(newLeader);
        setLeader(newLeader);
        setVideoURL(newVideoURL);
    }


    public void setBandName(String newBandName) {
        if (newBandName.length() > maxNameLength) {
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

    public void setVideoURL(String newVideoURL) {
        if (newVideoURL.length() > maxVideoURLLength) {
            throw new IllegalArgumentException("Video URL too long");
        }

        videoURL = newVideoURL;
    }

    public String getVideoURL() {
        return videoURL;
    }


    @Override
    public String toString() {
        String tmp = bandName + "\n";
        tmp = tmp + "    " + getLeaderFirstName() + getLeaderLastName() + " (Leader)\n";
        for (Musician member : members) {
            if (!isLeader(member)) {
                tmp = tmp + "    " + member.getFirstName() + member.getLastName() + "\n";
            }
        }

        return tmp;
    }

}
