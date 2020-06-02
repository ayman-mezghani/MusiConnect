package ch.epfl.sdp.musiconnect;

import android.content.Intent;

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
import ch.epfl.sdp.musiconnect.finder.FinderPage;
import ch.epfl.sdp.musiconnect.finder.MusicianFinderPage;
import ch.epfl.sdp.musiconnect.pages.StartPage;

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

    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() { Intents.release(); }

    @Test
    public void testSearchClickShouldDoNothing() {
        onView(withId(R.id.search)).perform(click());
    }

    @Test
    public void testHomeClickShouldStartNewIntent() {
        onView(withId(R.id.home)).perform(click());

        Intent homeIntent = new Intent();
        finderPageRule.launchActivity(homeIntent);
        intended(hasComponent(StartPage.class.getName()));
    }

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
