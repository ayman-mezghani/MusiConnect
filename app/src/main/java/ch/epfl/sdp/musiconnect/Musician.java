package ch.epfl.sdp.musiconnect;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class Musician {

    private String firstName;
    private String lastName;
    private String userName;
    private Date birthday;
    private String emailAddress;
    private String videoURL;
    private Map<Instrument, Level> instruments;
    private Location location;

    private static final int MAX_NAME_LENGTH = 16;
    private static final int MIN_AGE = 13;
    private static final int MAX_AGE = 120;
    private static final int MAX_EMAIL_ADDRESS_LENGTH = 64;
    private static final int MAX_VIDEO_URL_LENGTH = 2048;


    public Musician(String newFirstName, String newLastName, String newUserName, Date newBirthday, String newEmailAddress) {
        setFirstName(newFirstName);
        setLastName(newLastName);
        setUserName(newUserName);
        setBirthday(newBirthday);
        setEmailAddress(newEmailAddress);
        videoURL = "";
        instruments = new HashMap<Instrument, Level>();
        location = null;
    }


    public void setFirstName(String newFirstName) {
        if (newFirstName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("First name too long");
        }

        firstName = newFirstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String newLastName) {
        if (newLastName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Last name too long");
        }

        lastName = newLastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setUserName(String newUserName) {
        if (newUserName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("User name too long");
        }

        userName = newUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setBirthday(Date newBirthday) {
        Date currentDate = new Date();

        if (newBirthday.after(currentDate)) {
            throw new IllegalArgumentException("Birthday is not happened yet");
        }

        int currentAge = currentDate.getYear() - newBirthday.getYear();
        if (currentDate.getMonth() < newBirthday.getMonth() || currentDate.getMonth() == newBirthday.getMonth() && currentDate.getDate() < newBirthday.getDate()) {
            --currentAge;
        }

        if (currentAge < MIN_AGE) {
            throw new IllegalArgumentException("Age too low");
        } else if (currentAge > MAX_AGE) {
            throw new IllegalArgumentException("Age too high");
        }

        birthday = new Date(newBirthday.getYear(), newBirthday.getMonth(), newBirthday.getDate());
    }

    public Date getBirthday() {
        return new Date(birthday.getYear(), birthday.getMonth(), birthday.getDate());
    }

    public int getAge() {
        Date currentDate = new Date();

        int currentAge = currentDate.getYear() - birthday.getYear();
        if (currentDate.getMonth() < birthday.getMonth() || currentDate.getMonth() == birthday.getMonth() && currentDate.getDate() < birthday.getDate()) {
            --currentAge;
        }

        return currentAge;
    }

    public void setEmailAddress(String newEmailAddress) {
        if (newEmailAddress.length() > MAX_EMAIL_ADDRESS_LENGTH) {
            throw new IllegalArgumentException("Email address too long");
        }

        emailAddress = newEmailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setVideoURL(String newVideoURL) {
        if (newVideoURL.length() > MAX_VIDEO_URL_LENGTH) {
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

    public void setLocation(Location newLocation) {
        location.setLocation(newLocation);
    }

    public Location getLocation() {
        return location.getLocation();
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
