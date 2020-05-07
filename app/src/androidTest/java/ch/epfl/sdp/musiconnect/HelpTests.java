package ch.epfl.sdp.musiconnect;

import android.content.Intent;

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
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class HelpTests {

    @Rule
    public final ActivityTestRule<HelpPage> helpPageRule =
            new ActivityTestRule<>(HelpPage.class);


    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() { Intents.release(); }

    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase());
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

    /**
    @Test
    public void testHelpClickShouldDoNothing() {
        onView(withId(R.id.help)).perform(click());
        assert(true);
    }
    */

    /**
    @Test
    public void testSearchClickFromHelpShouldDisplayMessage() {
        onView(withId(R.id.search)).perform(click());

        Intent searchIntent = new Intent();
        helpPageRule.launchActivity(searchIntent);
        intended(hasComponent(FinderPage.class.getName()));
    }
    */
}
