package ch.epfl.sdp.musiconnect.database;

import androidx.annotation.NonNull;

public enum DbDataType {
    Musician, Bands, Events;

    @NonNull
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
