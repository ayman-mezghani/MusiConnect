package ch.epfl.sdp.musiconnect.users;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sdp.musiconnect.functionnalities.MyDate;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class EventManager extends Person {

    private String eventName;
    private MyDate eventDate;
    private Set<Musician> musicians;
    private Set<Band> bands;

    private static final int MAX_EVENT_NAME_LENGTH = 16;


    public EventManager(String firstName, String lastName, String userName, String emailAddress, MyDate birthday, String eventName, MyDate eventDate) {
        super(firstName, lastName, userName, emailAddress, birthday);
        setEventName(eventName);
        setEventDate(eventDate);
        musicians = new HashSet<>();
        bands = new HashSet<>();
    }

    public EventManager(String firstName, String lastName, String userName, String emailAddress, MyDate birthday) {
        this(firstName, lastName, userName, emailAddress, birthday, "", new MyDate());
    }


    public void setEventName(String eventName) {
        if (eventName.length() > MAX_EVENT_NAME_LENGTH) {
            throw new IllegalArgumentException("Event name too long");
        }

        this.eventName = eventName;
    }

    public String getEventName() {
        if (eventName.isEmpty()) {
            throw new Error("Event name is not known");
        }

        return eventName;
    }

    public void setEventDate(MyDate eventDate) {
        this.eventDate = new MyDate(eventDate);
    }

    public MyDate getEventDate() {
        return new MyDate(eventDate);
    }

    public void addMusician(Musician musician) {
        if (musician == null) {
            throw new NullPointerException("Musician is invalid");
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

    public Set<Musician> setOfMusicians() {
        return new HashSet<>(musicians);
    }

    public void addBand(Band band) {
        if (band == null) {
            throw new NullPointerException("Band is invalid");
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

    public Set<Band> setOfBands() {
        return new HashSet<>(bands);
    }


    @Override
    public String toString() {
        String tmp;

        try {
            tmp = getEventName();
        } catch (Error error) {
            tmp = "Event";
        }

        tmp += " (Organizer: " + getFirstName() + " " + getLastName() + ")\n";

        if (containsAnyMusician()) {
            tmp += "Musicians:\n";
            for (Musician musician : musicians) {
                tmp += "    " + musician.getFirstName() + " " + musician.getLastName() + "\n";
            }
        }
        if (containsAnyBand()) {
            tmp += "Bands:\n";
            for (Band band : bands) {
                tmp += "    " + band.getName() + "\n";
            }
        }

        return tmp;
    }

}
