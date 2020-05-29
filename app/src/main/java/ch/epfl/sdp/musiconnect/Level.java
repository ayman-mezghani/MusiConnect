package ch.epfl.sdp.musiconnect;

import androidx.annotation.Nullable;

/**
 * @author Manuel Pellegrini, EPFL
 */
public enum Level {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
    PROFESSIONAL;

    public static Level getLevelFromValue(@Nullable String s) {
        return valueOf(s.toUpperCase());
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
