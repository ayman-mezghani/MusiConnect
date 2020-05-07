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
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Manuel Pellegrini, EPFL
 */
@RunWith(AndroidJUnit4.class)
public class FinderPageTests {

    @Rule
    public final ActivityTestRule<FinderPage> finderPageRule = new ActivityTestRule<>(FinderPage.class);

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
    public void testClickOnFindMusicianButtonStartsNewIntent() {
        onView(withId(R.id.findMusicianButtonID)).perform(scrollTo(), click());

        Intent musicianFinderIntent = new Intent();
        finderPageRule.launchActivity(musicianFinderIntent);
        intended(hasComponent(MusicianFinderPage.class.getName()));
    }

    @Test
    public void testClickOnFindBandButtonStartsNewIntent() {
        onView(withId(R.id.findBandButtonID)).perform(scrollTo(), click());

        Intent bandFinderIntent = new Intent();
        finderPageRule.launchActivity(bandFinderIntent);
        intended(hasComponent(BandFinderPage.class.getName()));
    }

}