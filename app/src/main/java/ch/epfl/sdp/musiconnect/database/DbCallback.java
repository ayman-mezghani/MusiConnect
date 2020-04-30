package ch.epfl.sdp.musiconnect.database;

import ch.epfl.sdp.musiconnect.User;

public interface DbCallback {
    default void readCallback(User user) {}
    default void readFailCallback() {}
    default void existsCallback(boolean exists) {}
}