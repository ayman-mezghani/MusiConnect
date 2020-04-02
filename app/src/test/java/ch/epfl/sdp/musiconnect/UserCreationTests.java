package ch.epfl.sdp.musiconnect;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserCreationTests {
    @Test
    public void getJoinDateWorks() {
        UserCreation uc = new UserCreation();
        assertEquals(uc.getAge(1995, 10, 19), "24");
    }
}
