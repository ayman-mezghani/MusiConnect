package ch.epfl.sdp.musiconnect;

import org.junit.Test;

import ch.epfl.sdp.musiconnect.pages.UserCreationPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserCreationPageTests {
    @Test
    public void getJoinDateWorks() {
        UserCreationPage uc = new UserCreationPage();
        assertEquals(uc.getAge(1995, 10, 19), "24");
    }
}
