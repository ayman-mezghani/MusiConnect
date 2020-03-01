package ch.epfl.sdp.musiconnect;

import java.util.Set;
import java.util.HashSet;

public class EventManager {

    private String firstName;
    private String lastName;
    private String userName;
    private String eventName;
    private String emailAddress;
    private Set<Musician> musicians;
    private Set<Band> bands;
    private String location; // TODO : Setter and getter methods for location

    private int maxNameLength = 16;
    private int maxEmailAddressLength = 64;


    public EventManager(String newFirstName, String newLastName, String newUserName, String newEventName, String newEmailAddress) {
        setFirstName(newFirstName);
        setLastName(newLastName);
        setUserName(newUserName);
        setEventName(newEventName);
        setEmailAddress(newEmailAddress);
        musicians = new HashSet<Musician>();
        bands = new HashSet<Band>();
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

    public void setEventName(String newEventName) {
        if (newEventName.length() > maxNameLength) {
            throw new IllegalArgumentException("Event name too long");
        }

        eventName = newEventName;
    }

    public String getEventName() {
        return eventName;
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

    public void addMusician(Musician musician) {
        if (musician == null) {
            throw new IllegalArgumentException("Musician is invalid");
        } else if (containsMusician(musician)) {
            throw new IllegalArgumentException("Musician already exists");
        }

        musicians.add(musician);
    }

    public void removeMusician(Musician musician) {
        if (!containsMusician(musician)) {
            throw new IllegalArgumentException("Musician does not exist");
        }

        musicians.remove(musician);
    }

    public void removeAllMusicians() {
        musicians.clear();
    }

    public boolean containsAnyMusician() {
        return !musicians.isEmpty();
    }

    public int numberOfMusicians() {
        return musicians.size();
    }

    public boolean containsMusician(Musician musician) {
        return musicians.contains(musician);
    }

    public void addBand(Band band) {
        if (band == null) {
            throw new IllegalArgumentException("Band is invalid");
        } else if (containsBand(band)) {
            throw new IllegalArgumentException("Band already exists");
        }

        bands.add(band);
    }

    public void removeBand(Band band) {
        if (!containsBand(band)) {
            throw new IllegalArgumentException("Band does not exist");
        }

        bands.remove(band);
    }

    public void removeAllBands() {
        bands.clear();
    }

    public boolean containsAnyBand() {
        return !bands.isEmpty();
    }

    public int numberOfBands() {
        return bands.size();
    }

    public boolean containsBand(Band band) {
        return bands.contains(band);
    }


    @Override
    public String toString() {
        String tmp = getEventName() + " (Organizer: " + getFirstName() + " " + getLastName() + ")\n";
        if (containsAnyMusician()) {
            tmp += "Musicians:\n";
            for (Musician musician : musicians) {
                tmp += "    " + musician.getFirstName() + " " + musician.getLastName() + "\n";
            }
        }
        if (containsAnyBand()) {
            tmp += "Bands:\n";
            for (Band band : bands) {
                tmp += "    " + band.getBandName() + "\n";
            }
        }

        return tmp;
    }

}
