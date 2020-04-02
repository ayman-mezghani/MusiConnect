package ch.epfl.sdp.musiconnect;

/**
 * @Author : Antonio Pisanello
 */

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class GoogleLoginTests {

    @Rule
    public IntentsTestRule<GoogleLogin> activityRule = new IntentsTestRule<>(GoogleLogin.class);

    @Test
    public void signInButtonClick(){
        onView(withId(R.id.sign_in_button)).check(matches(isDisplayed()));
        //onView(withId(R.id.sign_in_button)).perform(click());
    }

    @Test
    public void onActivityResult(){
        // Simulate the on activity result call
        ((GoogleLogin ) VideoPlayingTests.getCurrentActivity()).onActivityResult(0  ,0,null);
    }
}
