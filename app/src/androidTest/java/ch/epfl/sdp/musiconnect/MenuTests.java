package ch.epfl.sdp.musiconnect;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.events.EventCreationPage;
import ch.epfl.sdp.musiconnect.events.EventListPage;
import ch.epfl.sdp.musiconnect.finder.FinderPage;
import ch.epfl.sdp.musiconnect.location.MapsActivity;
import ch.epfl.sdp.musiconnect.location.MapsLocationFunctions;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MenuTests {
    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase());
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() {
        Intents.init();
        MapsLocationFunctions.clickPermissionAlert();
    }

    @After
    public void releaseIntents() { Intents.release(); }

    /**
     * Helper method to avoid duplicate code
     * @param stringId
     */
    private void openActionsMenu(int stringId) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(stringId)).perform(click());
    }

    @Test
    public void testHomeClickFromMenuShouldDoNothing() {
        openActionsMenu(R.string.navigation_home);
    }

    @Test
    public void testCreateEventClickFromMenuShouldStartNewIntent() {
        openActionsMenu(R.string.create_an_event);

        Intent eventIntent = new Intent();
        startPageRule.launchActivity(eventIntent);
        intended(hasComponent(EventCreationPage.class.getName()));
    }

    @Test
    public void testHelpClickFromMenuShouldStartNewIntent() {
        openActionsMenu(R.string.help);

        Intent helpIntent = new Intent();
        startPageRule.launchActivity(helpIntent);
        intended(hasComponent(HelpPage.class.getName()));
    }

    @Test
    public void testSignOutClickShouldLeaveStartPage() {
        openActionsMenu(R.string.signout);
    }

    private void clickOnMenuId(int id) {
        onView(withId(id)).perform(click());
    }

    @Test
    public void testHomeClickFromBottomMenuShouldDoNothing() {
        clickOnMenuId(R.id.home);
    }

    @Test
    public void testMyProfileClickFromBottomMenuShouldStartNewIntent() {
        clickOnMenuId(R.id.my_profile);

        Intent profileIntent = new Intent();
        startPageRule.launchActivity(profileIntent);
        intended(hasComponent(MyProfilePage.class.getName()));
    }

    @Test
    public void testMapClickFromBottomMenuShouldStartNewIntent() {
        clickOnMenuId(R.id.map);

        Intent mapIntent = new Intent();
        startPageRule.launchActivity(mapIntent);
        intended(hasComponent(MapsActivity.class.getName()));
    }

    @Test
    public void testSearchClickFromBottomMenuShouldStartNewIntent() {
        clickOnMenuId(R.id.search);

        Intent searchIntent = new Intent();
        startPageRule.launchActivity(searchIntent);
        intended(hasComponent(FinderPage.class.getName()));
    }
}
