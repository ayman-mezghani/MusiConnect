package ch.epfl.sdp.musiconnect.database;

import java.util.List;

import ch.epfl.sdp.musiconnect.User;

public interface DbCallback {
    default void readCallback(User user) {}
    default void existsCallback(boolean exists) {}
    default void queryCallback(List<User> userList) {}
}