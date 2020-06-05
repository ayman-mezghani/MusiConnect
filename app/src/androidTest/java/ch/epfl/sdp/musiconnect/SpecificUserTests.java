package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Objects;

import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.pages.StartPage;
import ch.epfl.sdp.musiconnect.users.Musician;

import static ch.epfl.sdp.musiconnect.testsFunctions.getCurrentActivity;
import static org.junit.Assert.assertEquals;

public class SpecificUserTests {
    @Rule
    public IntentsTestRule<StartPage> activityRule = new IntentsTestRule<>(StartPage.class);

    private static MockDatabase md;

    @BeforeClass
    public static void setMocks() {
        md = new MockDatabase(true);
        DbSingleton.setDatabase(md);
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }

    @Before
    public void setCurrentUser() {
        CurrentUser.getInstance(activityRule.getActivity()).setTypeOfUser(UserType.Band);
        CurrentUser.getInstance(activityRule.getActivity()).setBand(md.getBand());
    }

    @Test
    public void updateUserGetBand() {
        Musician musiConnect = md.getDummyMusician(4);
        CurrentUser.getInstance(activityRule.getActivity()).setMusician(musiConnect);
        ((StartPage) Objects.requireNonNull(getCurrentActivity())).updateCurrentUser(activityRule.getActivity());
        assertEquals(CurrentUser.getInstance(activityRule.getActivity()).getBand().getName(), md.getBand().getName());
    }
}
