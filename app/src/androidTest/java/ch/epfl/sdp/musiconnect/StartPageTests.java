package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.pages.StartPage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.testsFunctions.childAtPosition;
import static ch.epfl.sdp.musiconnect.testsFunctions.getCurrentActivity;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StartPageTests {
    @Rule
    public IntentsTestRule<StartPage> activityRule = new IntentsTestRule<>(StartPage.class);

    private static MockDatabase md;

    @BeforeClass
    public static void setMocks() {
        md = new MockDatabase(true);
        DbSingleton.setDatabase(md);
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }

    @Before
    public void setCurrentUser() {
        CurrentUser.getInstance(activityRule.getActivity()).setTypeOfUser(TypeOfUser.Band);
        CurrentUser.getInstance(activityRule.getActivity()).setBand(md.getBand());
    }

    @Test
    public void updateUserGetBand() {
        Musician musiConnect = md.getDummyMusician(4);
        CurrentUser.getInstance(activityRule.getActivity()).setMusician(musiConnect);
        ((StartPage) getCurrentActivity()).updateCurrentUser(activityRule.getActivity());
        assertEquals(CurrentUser.getInstance(activityRule.getActivity()).getBand().getName(), md.getBand().getName());
    }

    //@Test
    public void testBandProfileClickShouldStartNewIntent() {
        /*
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withId(R.id.my_profileBand)).perform(scrollTo(), click());
        */
        ViewInteraction overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        1),
                                2),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("My bands"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        intended(hasComponent(BandProfile.class.getName()));
    }
}
