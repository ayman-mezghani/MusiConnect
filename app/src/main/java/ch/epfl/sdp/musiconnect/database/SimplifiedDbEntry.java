package ch.epfl.sdp.musiconnect.database;

public abstract class SimplifiedDbEntry {
    enum Fields {
        username, firstName, lastName, email, typeOfUser, birthday, joinDate, location, events,
        instruments, instrument, level,
        host, participants, address, eventName, dateTime, description, visible,
        leader, bandName, members
    }
}
