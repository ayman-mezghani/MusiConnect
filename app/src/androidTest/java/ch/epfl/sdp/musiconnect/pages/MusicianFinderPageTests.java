package ch.epfl.sdp.musiconnect.pages;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.finder.MusicianFinderPage;
import ch.epfl.sdp.musiconnect.testsFunctions;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

/**
 * @author Manuel Pellegrini, EPFL
 */
@RunWith(AndroidJUnit4.class)
public class MusicianFinderPageTests {

    @Rule
    public final ActivityTestRule<MusicianFinderPage> musicianFinderPageRule = new ActivityTestRule<>(MusicianFinderPage.class);


    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() { Intents.release(); }

    @Test
    public void testEditTextFieldsOfMusicianFinderWork() {
        String firstName = "John";
        String lastName = "Lennon";
        String userName = "JohnLennon";

        onView(withId(R.id.myMusicianFinderFirstNameID)).perform(scrollTo()).perform(clearText(), typeText(firstName));
        onView(withId(R.id.myMusicianFinderLastNameID)).perform(scrollTo()).perform(clearText(), typeText(lastName));
        onView(withId(R.id.myMusicianFinderUserNameID)).perform(scrollTo()).perform(clearText(), typeText(userName));

        onView(withId(R.id.myMusicianFinderFirstNameID)).check(matches(withText(firstName)));
        onView(withId(R.id.myMusicianFinderLastNameID)).check(matches(withText(lastName)));
        onView(withId(R.id.myMusicianFinderUserNameID)).check(matches(withText(userName)));

        closeSoftKeyboard();

        onView(withId(R.id.myMusicianFinderFirstNameID)).check(matches(withText(firstName)));
        onView(withId(R.id.myMusicianFinderLastNameID)).check(matches(withText(lastName)));
        onView(withId(R.id.myMusicianFinderUserNameID)).check(matches(withText(userName)));
    }

    @Test
    public void testInstrumentSpinnerFieldOfMusicianFinderWorks() {
        onView(withId(R.id.myMusicianFinderInstrumentsID)).perform(click());

        onData(anything())
                .inAdapterView(testsFunctions.childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(13).perform(click());
    }

    @Test
    public void testLevelSpinnerFieldOfMusicianFinderWorks() {
        onView(withId(R.id.myMusicianFinderLevelsID)).perform(click());

        onData(anything())
                .inAdapterView(testsFunctions.childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3).perform(click());
    }

    @Test
    public void testFindMusicianButtonWorks() {
        onView(withId(R.id.musicianFinderButtonID)).perform(click());
    }
}
