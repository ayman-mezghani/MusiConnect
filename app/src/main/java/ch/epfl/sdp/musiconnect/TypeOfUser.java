package ch.epfl.sdp.musiconnect;

import androidx.room.Entity;

public enum TypeOfUser {
    Band("Band"),
    Musician("Musician"),
    EventManager("Event Manager");

    private final String name;

    TypeOfUser(String musician) {
        this.name = musician;
    }

    public String toString() {
        return this.name;
    }
}
