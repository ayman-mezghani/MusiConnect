package ch.epfl.sdp.musiconnect.database;

import androidx.annotation.NonNull;

public enum DbDataType {
    Musician, Band, Events;

    @NonNull
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
