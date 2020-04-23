package ch.epfl.sdp.musiconnect.database;

import androidx.annotation.NonNull;

public enum DbUserType {
    Musician, Band;

    @NonNull
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
