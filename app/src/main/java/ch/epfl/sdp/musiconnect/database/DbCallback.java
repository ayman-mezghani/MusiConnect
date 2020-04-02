package ch.epfl.sdp.musiconnect.database;

import ch.epfl.sdp.musiconnect.User;

public interface DbCallback {
    void onCallback(User user);
}