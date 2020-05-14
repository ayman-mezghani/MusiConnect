package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ProfileTests {

    @Rule
    public final ActivityTestRule<MyProfilePage> profilePageRule =
            new ActivityTestRule<>(MyProfilePage.class);

    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase(false));
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() { Intents.init(); }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void testProfileClickShouldDoNothing() {
        onView(withId(R.id.my_profile)).perform(click());
    }

    @Test
    public void testHelpClickFromProfileShouldStartNewIntent() {
        MenuTests m = new MenuTests();
        m.testHelpClickFromMenuShouldStartNewIntent();
    }
}
