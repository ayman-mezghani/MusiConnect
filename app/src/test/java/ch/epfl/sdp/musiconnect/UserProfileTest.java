package ch.epfl.sdp.musiconnect;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;

public class UserProfileTest {

    @Test
    public void gettersOfMusicianClassWork() {
        String firstName = "Manuel";
        String lastName = "Pellegrini";
        String userName = "Blabla";
        Date birthday = new Date(1997, 2, 17);
        String emailAddress = "blablabla@epfl.ch";

        Musician musician = new Musician(firstName, lastName, userName, birthday, emailAddress);
    }

}
