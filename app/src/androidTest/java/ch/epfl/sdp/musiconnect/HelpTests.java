package ch.epfl.sdp.musiconnect;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class HelpTests {

    @Rule
    public final ActivityTestRule<HelpPage> helpPageRule =
            new ActivityTestRule<>(HelpPage.class);

    private UiDevice device;

    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() {
        Intents.init();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        MapsLocationTest.clickAlert(device);
    }

    @After
    public void releaseIntents() { Intents.release(); }

    @Test
    public void testHelpClickShouldDoNothing() {
        onView(withId(R.id.help)).perform(click());
        assert(true);
    }

    @Test
    public void testSearchClickFromHelpShouldDisplayMessage() {
        onView(withId(R.id.search)).perform(click());

        Intent searchIntent = new Intent();
        helpPageRule.launchActivity(searchIntent);
        intended(hasComponent(FinderPage.class.getName()));
    }
}
