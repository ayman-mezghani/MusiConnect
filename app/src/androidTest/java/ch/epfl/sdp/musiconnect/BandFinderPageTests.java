package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.action.ViewActions;
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

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author Manuel Pellegrini, EPFL
 */
@RunWith(AndroidJUnit4.class)
public class BandFinderPageTests {

    private String bandName = "The Beatles";

    @Rule
    public final ActivityTestRule<BandFinderPage> bandFinderPageRule = new ActivityTestRule<>(BandFinderPage.class);

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
    public void testEditTextFieldOfBandFinderWorks() {
        onView(withId(R.id.myBandFinderBandNameID)).perform(ViewActions.scrollTo()).perform(clearText(), typeText(bandName));

        onView(withId(R.id.myBandFinderBandNameID)).check(matches(withText(bandName)));

        closeSoftKeyboard();

        onView(withId(R.id.myBandFinderBandNameID)).check(matches(withText(bandName)));
    }

    @Test
    public void testSpinnerFieldsOfBandFinderWork() {
        // TODO
        assert(true);
    }

    @Test
    public void testFindBandButtonWorks() {
        onView(withId(R.id.bandFinderButtonID)).perform(scrollTo(), click());

        // This button does nothing yet
        assert(true);
    }

}
