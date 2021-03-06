package ch.epfl.sdp.musiconnect.database;

import java.util.List;

import ch.epfl.sdp.musiconnect.users.User;
import ch.epfl.sdp.musiconnect.events.Event;

public interface DbCallback {
    default void readCallback(User user) {}

    default void readCallback(Event e) {}

    default void readFailCallback() {}

    default void existsCallback(boolean exists) {}

    default void queryCallback(List list) {}

    default void queryFailCallback() {}

    default void locationQueryCallback(List queryResult) {}

    default void queryBandIfMemberCallback(List<User> userList) {}
}