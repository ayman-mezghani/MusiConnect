package ch.epfl.sdp.musiconnect;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class Musician {

    private String firstName;
    private String lastName;
    private String userName;
    private int age; // TODO : Change 'age' into 'birthday' and add new methods
    private String emailAddress;
    private String videoURL;
    private Map<Instrument, Level> instruments;
    private String location; // TODO : Setter and getter methods for location

    private int maxNameLength = 16;
    private int minAge = 8;
    private int maxAge = 128;
    private int maxEmailAddressLength = 64;
    private int maxVideoURLLength = 256;


    public Musician(String newFirstName, String newLastName, String newUserName, int newAge, String newEmailAddress, String newVideoURL) {
        setFirstName(newFirstName);
        setLastName(newLastName);
        setUserName(newUserName);
        setAge(newAge);
        setEmailAddress(newEmailAddress);
        setVideoURL(newVideoURL);
        instruments = new HashMap<Instrument, Level>();
    }


    public void setFirstName(String newFirstName) {
        if (newFirstName.length() > maxNameLength) {
            throw new IllegalArgumentException("First name too long");
        }

        firstName = newFirstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String newLastName) {
        if (newLastName.length() > maxNameLength) {
            throw new IllegalArgumentException("Last name too long");
        }

        lastName = newLastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setUserName(String newUserName) {
        if (newUserName.length() > maxNameLength) {
            throw new IllegalArgumentException("User name too long");
        }

        userName = newUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setAge(int newAge) {
        if (newAge < minAge) {
            throw new IllegalArgumentException("Age too low");
        } else if (newAge > maxAge) {
            throw new IllegalArgumentException("Age too high");
        }

        age = newAge;
    }

    public int getAge() {
        return age;
    }

    public void setEmailAddress(String newEmailAddress) {
        if (newEmailAddress.length() > maxEmailAddressLength) {
            throw new IllegalArgumentException("Email address too long");
        }

        emailAddress = newEmailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
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


    @Override
    public String toString() {
        String tmp = getFirstName() + " " + getLastName() + ", " + getAge() + "\n";
        for (Instrument instrument : setOfInstruments()) {
            tmp += "    " + instrument + " -> level " + getLevel(instrument) + "\n";
        }

        return tmp;
    }

}
