package ch.epfl.sdp.musiconnect.users;

import java.util.ArrayList;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class Band extends User {

    private String bandName;
    private String leader;
    private ArrayList<String> musicianEmails;

    private static final int MAX_BAND_NAME_LENGTH = 16;


    public Band(String bandName, String leader) {
        super();
        setBandName(bandName);
        events = new ArrayList<>();
        musicianEmails = new ArrayList<>();
        addMember(leader);
        setLeader(leader);
    }


    public void setBandName(String bandName) {
        if (bandName.isEmpty()) {
            throw new IllegalArgumentException("Band name cannot be empty");
        } else if (bandName.length() > MAX_BAND_NAME_LENGTH) {
            throw new IllegalArgumentException("Band name too long");
        }

        this.bandName = bandName;
    }

    public void setLeader(String leader) {
        if (leader == null) {
            throw new NullPointerException("Leader is invalid");
        } else if (!this.musicianEmails.contains(leader)) {
            throw new IllegalArgumentException("Leader must already be member of the band");
        }

        this.leader = leader;
    }

    public boolean isLeader(String member) {
        return leader.equals(member);
    }

    public void addMember(String member) {
        if (member == null) {
            throw new NullPointerException("Member is invalid");
        } else if (this.containsMember(member)) {
            throw new IllegalArgumentException("Member already exists");
        }

        musicianEmails.add(member);
    }

    public void removeMember(String member) {
        if (!containsMember(member)) {
            throw new IllegalArgumentException("Member does not exist");
        } else if (isLeader(member)) {
            throw new IllegalArgumentException("Impossible to remove the leader from the band");
        }

        musicianEmails.remove(member);
    }

    public int numberOfMembers() {
        return musicianEmails.size();
    }

    public boolean containsMember(Musician member) {
        for(String s: musicianEmails) {
            if (s.equals(member.getEmailAddress()))
                return true;
        }
        return musicianEmails.contains(member.getEmailAddress());
    }

    public boolean containsMember(String memberEmail) {
        for(String s: musicianEmails) {
            if (s.equals(memberEmail))
                return true;
        }
        return musicianEmails.contains(memberEmail);
    }

    //@Override
    public String getName() {
        return this.bandName;
    }


    @Override
    public String getEmailAddress() {
        return leader;
    }

    @Override
    public String toString() {
        String tmp = this.getName() + "\n";
        tmp += "    " + this.leader + " (Leader)\n";
        for (String member : this.musicianEmails) {
            if (!isLeader(member)) {
                tmp += "    " + member + "\n";
            }
        }

        return tmp;
    }

    public void setMusiciansEmailAdresses(ArrayList<String> listEmailAdress) {
        this.musicianEmails = listEmailAdress;
    }

    public ArrayList<String> getMusiciansEmailsAdress() {
        return this.musicianEmails;
    }

}
