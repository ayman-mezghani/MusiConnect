package ch.epfl.sdp.musiconnect;

public enum UserType {
    Band("Band"),
    Musician("Musician"),
    EventManager("Event Manager");

    private final String name;

    UserType(String musician) {
        this.name = musician;
    }

    public String toString() {
        return this.name;
    }
}
