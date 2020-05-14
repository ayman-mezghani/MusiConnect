package ch.epfl.sdp.musiconnect;

import androidx.annotation.Nullable;

/**
 * @author Manuel Pellegrini, EPFL
 */
public enum Instrument {
    ACCORDION,
    BAGPIPES,
    BANJO,
    BASS,
    CELLO,
    CLARINET,
    DRUMS,
    FLUTE,
    GUITAR,
    HARP,
    OBOE,
    ORGAN,
    PIANO,
    SAXOPHONE,
    TAMBOURINE,
    TRIANGLE,
    TROMBONE,
    TRUMPET,
    TUBA,
    UKULELE,
    VIOLA,
    VIOLIN,
    VOICE,
    XYLOPHONE;

    public static Instrument getInstrumentFromValue(@Nullable String s) {
        return valueOf(s.toUpperCase());
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
