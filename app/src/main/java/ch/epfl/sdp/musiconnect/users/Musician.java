package ch.epfl.sdp.musiconnect.users;

import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.epfl.sdp.musiconnect.Instrument;
import ch.epfl.sdp.musiconnect.Level;
import ch.epfl.sdp.musiconnect.functionnalities.MyDate;
import ch.epfl.sdp.musiconnect.UserType;

/**
 * @author Manuel Pellegrini, EPFL
 */
@Entity
public class Musician extends Person {

    private String videoURL;
    private Map<Instrument, Level> instruments;
    @Ignore
    private UserType userType = UserType.Musician;
    private Float distanceToCurrentUser = null;

    private static final int MAX_VIDEO_URL_LENGTH = 2048;

    public Musician(String firstName, String lastName, String userName, String emailAddress, MyDate birthday) {
        super(firstName, lastName, userName, emailAddress, birthday);
        videoURL = "";
        instruments = new HashMap<>();
    }

    public Map<Instrument,Level> getInstruments(){
        return instruments;
    }

    public void setInstruments(Map<Instrument,Level> instr){
        instruments = instr;
    }


    public void setVideoURL(String videoURL) {
        if (videoURL.length() > MAX_VIDEO_URL_LENGTH) {
            throw new IllegalArgumentException("Video URL too long");
        }

        this.videoURL = videoURL;
    }

    public String getVideoURL() {

        return videoURL;
    }

    public void addInstrument(Instrument instrument, Level level) {
        if (containsInstrument(instrument)) {
            throw new IllegalArgumentException("Instrument already exists");
        }

        instruments.put(instrument, level);
    }

    public void removeInstrument(Instrument instrument) {
        if (!containsInstrument(instrument)) {
            throw new IllegalArgumentException("Instrument does not exist");
        }

        instruments.remove(instrument);
    }

    public void removeAllInstruments() {
        instruments.clear();
    }

    public boolean containsAnyInstrument() {
        return !instruments.isEmpty();
    }

    public int numberOfInstruments() {
        return instruments.size();
    }

    public boolean containsInstrument(Instrument instrument) {
        return instruments.containsKey(instrument);
    }

    public Level getLevel(Instrument instrument) {
        if (!containsInstrument(instrument)) {
            throw new IllegalArgumentException("Instrument does not exist");
        }

        return instruments.get(instrument);
    }

    public void changeLevel(Instrument instrument, Level level) {
        if (!containsInstrument(instrument)) {
            throw new IllegalArgumentException("Instrument does not exist");
        }

        removeInstrument(instrument);
        addInstrument(instrument, level);
    }

    public Set<Instrument> setOfInstruments() {
        return instruments.keySet();
    }

    public void setUserType(UserType t) { this.userType = t; }

    public UserType getUserType() { return this.userType; }


    @Override
    public String toString() {
        String tmp = getFirstName() + " " + getLastName() + ", " + getAge() + "\n";
        for (Instrument instrument : setOfInstruments()) {
            tmp += "    " + instrument + " -> level " + getLevel(instrument) + "\n";
        }

        return tmp;
    }

    public Float getDistanceToCurrentUser() {
        return distanceToCurrentUser;
    }

    public void setDistanceToCurrentUser(Float distanceToCurrentUser) {
        this.distanceToCurrentUser = distanceToCurrentUser;
    }
}
