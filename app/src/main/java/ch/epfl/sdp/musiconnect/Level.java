package ch.epfl.sdp.musiconnect;

/**
 * @author Manuel Pellegrini, EPFL
 */
public enum Level {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
    PROFESSIONAL;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
