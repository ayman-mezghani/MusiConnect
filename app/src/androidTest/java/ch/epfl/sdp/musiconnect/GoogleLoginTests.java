package ch.epfl.sdp.musiconnect;

/**
 * @Author : Antonio Pisanello
 */

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sdp.musiconnect.TestsFunctions.getCurrentActivity;
import static ch.epfl.sdp.musiconnect.TestsFunctions.waitSeconds;
import static junit.framework.TestCase.assertTrue;

public class GoogleLoginTests {

    @Rule
    public IntentsTestRule<GoogleLogin> activityRule = new IntentsTestRule<>(GoogleLogin.class);

    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase(false));
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
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
        ((GoogleLogin) getCurrentActivity()).onActivityResult(0, 0, null);
    }

    @Test
    public void redirectTestsTrue() {
        // Simulate the on activity result call
        ((GoogleLogin) getCurrentActivity()).redirect(true);
        intended(hasComponent(StartPage.class.getName()));
        assertTrue(CurrentUser.getInstance(activityRule.getActivity()).getCreatedFlag());
    }
}
