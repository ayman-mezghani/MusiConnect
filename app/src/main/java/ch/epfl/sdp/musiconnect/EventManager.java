package ch.epfl.sdp.musiconnect;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

public class EventManager {

    private String firstName;
    private String lastName;
    private String userName;
    private String emailAddress;
    private String eventName;
    private Date eventDate;
    private Set<Musician> musicians;
    private Set<Band> bands;
    private String location; // TODO : Setter and getter methods for location

    private static final int MAX_NAME_LENGTH = 16;
    private static final int MAX_EMAIL_ADDRESS_LENGTH = 64;


    public EventManager(String newFirstName, String newLastName, String newUserName, String newEmailAddress, String newEventName, Date newEventDate) {
        setFirstName(newFirstName);
        setLastName(newLastName);
        setUserName(newUserName);
        setEmailAddress(newEmailAddress);
        setEventName(newEventName);
        setEventDate(newEventDate);
        musicians = new HashSet<Musician>();
        bands = new HashSet<Band>();
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

    public void setEmailAddress(String newEmailAddress) {
        if (newEmailAddress.length() > MAX_EMAIL_ADDRESS_LENGTH) {
            throw new IllegalArgumentException("Email address too long");
        }

        emailAddress = newEmailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEventName(String newEventName) {
        if (newEventName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Event name too long");
        }

        eventName = newEventName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventDate(Date newEventDate) {
        eventDate = new Date(newEventDate.getYear(), newEventDate.getMonth(), newEventDate.getDate(), newEventDate.getHours(), newEventDate.getMinutes());
    }

    public Date getEventDate() {
        return new Date(eventDate.getYear(), eventDate.getMonth(), eventDate.getDate(), eventDate.getHours(), eventDate.getMinutes());
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
