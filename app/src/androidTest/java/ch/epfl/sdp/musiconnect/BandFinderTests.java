package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.finder.BandFinderPage;
import ch.epfl.sdp.musiconnect.finder.BandFinderResult;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class BandFinderTests {
    @Rule
    public IntentsTestRule<BandFinderPage> activityRule = new IntentsTestRule<>(BandFinderPage.class);

    @BeforeClass
    public static void setMocks() {
        DbSingleton.setDatabase(new MockDatabase(false));
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }

    @Test
    public void mustEnterBandName() {
        onView(withId(R.id.bandFinderButtonID)).perform(scrollTo(), click());
        onView(withText(R.string.you_must_enter_bandname_for_search)).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void testTotoFireShouldDisplayBobminionMail() {
        onView(withId(R.id.myBandFinderBandNameID)).perform(scrollTo(), typeText("totofire"));
        onView(withId(R.id.bandFinderButtonID)).perform(scrollTo(), click());
        intended(hasComponent(BandFinderResult.class.getName()));

        String minionMail = ((Musician)(new MockDatabase(false)).getDummyMusician(0)).getEmailAddress();
        onView(allOf(withText(minionMail), withParent(withId(R.id.LvBandResult))));
    }

    @Test
    public void testBandFinderResultClickShouldDisplayBandProfile() {
        onView(withId(R.id.myBandFinderBandNameID)).perform(scrollTo(), typeText("totofire"));
        onView(withId(R.id.bandFinderButtonID)).perform(scrollTo(), click());
        intended(hasComponent(BandFinderResult.class.getName()));

        String minionMail = ((Musician)(new MockDatabase(false)).getDummyMusician(0)).getEmailAddress();
        onView(withText("totofire")).perform(scrollTo(), click());

        intended(hasComponent(BandProfile.class.getName()));
        onView(withId(R.id.etBandName)).perform(scrollTo()).check(matches(withText("totofire")));
    }
}
