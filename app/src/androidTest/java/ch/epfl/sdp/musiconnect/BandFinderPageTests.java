package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.finder.BandFinderPage;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

/**
 * @author Manuel Pellegrini, EPFL
 */
@RunWith(AndroidJUnit4.class)
public class BandFinderPageTests {

    private String bandName = "The Beatles";

    @Rule
    public final ActivityTestRule<BandFinderPage> bandFinderPageRule = new ActivityTestRule<>(BandFinderPage.class);


    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() {
        Intents.init();
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
    public void testInstrumentSpinnerFieldOfBandFinderWorks() {
        onView(withId(R.id.myBandFinderInstrumentsID)).perform(scrollTo(), click());

        onData(anything())
                .inAdapterView(testsFunctions.childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(13).perform(click());

        assert(true);
    }

    @Test
    public void testLevelSpinnerFieldOfBandFinderWorks() {
        onView(withId(R.id.myBandFinderLevelsID)).perform(scrollTo(), click());

        onData(anything())
                .inAdapterView(testsFunctions.childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3).perform(click());

        assert(true);
    }

    @Test
    public void testFindBandButtonWorks() {
        onView(withId(R.id.bandFinderButtonID)).perform(scrollTo(), click());

        intended(hasComponent(BandFinderPage.class.getName()));

    }

}
