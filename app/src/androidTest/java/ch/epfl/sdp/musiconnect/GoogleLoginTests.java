package ch.epfl.sdp.musiconnect;

/**
 * @Author : Antonio Pisanello
 */

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class GoogleLoginTests {

    @Rule
    public IntentsTestRule<GoogleLogin> activityRule = new IntentsTestRule<>(GoogleLogin.class);

    @BeforeClass
    public static void setMockDB() {
        DbGenerator.setDatabase(new MockDatabase());
    }

    @Test
    public void signInButtonClick(){
        onView(withId(R.id.sign_in_button)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_in_button)).perform(click());
        waitALittle(3);
    }

    @Test
    public void onActivityResult(){
        // Simulate the on activity result call
        ((GoogleLogin) VideoPlayingTests.getCurrentActivity()).onActivityResult(0  ,0,null);
    }

    public static void waitALittle(int t) {
        try {
            TimeUnit.SECONDS.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
