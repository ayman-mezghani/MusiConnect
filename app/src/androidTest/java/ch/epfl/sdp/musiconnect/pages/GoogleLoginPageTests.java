package ch.epfl.sdp.musiconnect.pages;

/**
 * @Author : Antonio Pisanello
 */

import junit.framework.TestCase;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.pages.GoogleLoginPage;
import ch.epfl.sdp.musiconnect.pages.StartPage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sdp.musiconnect.testsFunctions.getCurrentActivity;
import static ch.epfl.sdp.musiconnect.testsFunctions.waitSeconds;

public class GoogleLoginPageTests {

    @Rule
    public IntentsTestRule<GoogleLoginPage> activityRule = new IntentsTestRule<>(GoogleLoginPage.class);

    @BeforeClass
    public static void setMocks() {
        DbSingleton.setDatabase(new MockDatabase(false));
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }

    @Test
    public void signInButtonClick() {
        onView(withId(R.id.sign_in_button)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_in_button)).perform(click());
        waitSeconds(3);
    }

    @Test
    public void onActivityResult() {
        // Simulate the on activity result call
        ((GoogleLoginPage) getCurrentActivity()).onActivityResult(0, 0, null);
    }

    @Test
    public void redirectTestsTrue() {
        // Simulate the on activity result call
        ((GoogleLoginPage) getCurrentActivity()).redirect(true);
        intended(hasComponent(StartPage.class.getName()));
        TestCase.assertTrue(CurrentUser.getInstance(activityRule.getActivity()).getCreatedFlag());
    }
}
