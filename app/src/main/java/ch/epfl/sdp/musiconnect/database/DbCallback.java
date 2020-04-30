package ch.epfl.sdp.musiconnect.database;

import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.events.Event;

public interface DbCallback {
    default void readCallback(User user) {}
    default void readCallback(Event e) {}
    default void existsCallback(boolean exists) {}

}