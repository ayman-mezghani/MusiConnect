package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static ch.epfl.sdp.musiconnect.testsFunctions.getCurrentActivity;
import static org.junit.Assert.assertTrue;

public class StartPageTests {
    @Rule
    public IntentsTestRule<StartPage> activityRule = new IntentsTestRule<>(StartPage.class);

    private static MockDatabase md;

    @BeforeClass
    public static void setMocks() {
        md = new MockDatabase();
        DbGenerator.setDatabase(md);
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

    @Test
    public void updateUserGetBand() {
        Musician musiConnect = md.getDummyMusician(4);
        CurrentUser.getInstance(activityRule.getActivity()).setMusician(musiConnect);
        ((StartPage) getCurrentActivity()).updateCurrentUser(activityRule.getActivity());
        assertTrue(CurrentUser.getInstance(activityRule.getActivity()).getBand().getName().equals(md.getBand().getName()));
    }
}
