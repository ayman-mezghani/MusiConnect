package ch.epfl.sdp.musiconnect.pages;

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
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.events.EventCreationPage;
import ch.epfl.sdp.musiconnect.pages.HelpPage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class HelpPageTests {

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
        DbSingleton.setDatabase(new MockDatabase(false));
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }

    /**
     * Helper method to avoid duplicate code
     * @param stringId
     */
    private void openActionsMenu(int stringId) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(stringId)).perform(click());
    }

    @Test
    public void testHelpClickShouldDoNothing() {
        openActionsMenu(R.string.help);
    }

    @Test
    public void testCreateEventClickFromHelpShouldOpenIntent() {
        openActionsMenu(R.string.create_an_event);

        Intent eventIntent = new Intent();
        helpPageRule.launchActivity(eventIntent);
        intended(hasComponent(EventCreationPage.class.getName()));
    }
}
