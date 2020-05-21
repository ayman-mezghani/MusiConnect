package ch.epfl.sdp.musiconnect;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.finder.MusicianFinderPage;
import ch.epfl.sdp.musiconnect.finder.MusicianFinderResult;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.testsFunctions.childAtPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MusicianFinderTests {
    @Rule
    public IntentsTestRule<MusicianFinderPage> activityRule = new IntentsTestRule<>(MusicianFinderPage.class);

    private static MockDatabase md;

    @BeforeClass
    public static void setMocks() {
        md = new MockDatabase(false);
        DbGenerator.setDatabase(md);
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

    @Test
    public void mustEnterAtLeastOneField() {
        onView(withId(R.id.musicianFinderButtonID)).perform(ViewActions.scrollTo(), click());
        onView(withText(R.string.you_must_fill_at_least_one_field)).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void testSearchShouldWork() {
        onView(withId(R.id.myMusicianFinderFirstNameID)).perform(ViewActions.scrollTo(), typeText("Bob"));
        onView(withId(R.id.myMusicianFinderLastNameID)).perform(ViewActions.scrollTo(), typeText("Minion"));
        onView(withId(R.id.myMusicianFinderUserNameID)).perform(ViewActions.scrollTo(), typeText("bobminion"));
        onView(withId(R.id.musicianFinderButtonID)).perform(scrollTo(), click());
        intended(hasComponent(MusicianFinderResult.class.getName()));

        String minionMail = ((Musician)(new MockDatabase(false)).getDummyMusician(0)).getEmailAddress();
        onView(allOf(withText(minionMail), withParent(withId(R.id.LvMusicianResult))));
    }

    @Test
    public void testSearchShouldOpenProfilePage() {
        onView(withId(R.id.myMusicianFinderFirstNameID)).perform(ViewActions.scrollTo(), typeText("musicConnect"));
        onView(withId(R.id.musicianFinderButtonID)).perform(ViewActions.scrollTo(), click());
        intended(hasComponent(MusicianFinderResult.class.getName()));

        String minionMail = ((Musician)(new MockDatabase(false)).getDummyMusician(0)).getEmailAddress();
        onView(allOf(withText(minionMail), withParent(withId(R.id.LvMusicianResult))));

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withId(R.id.LvMusicianResult),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2)))
                .atPosition(0);
        appCompatTextView.perform(click());
        intended(hasComponent(MyProfilePage.class.getName()));
    }
}