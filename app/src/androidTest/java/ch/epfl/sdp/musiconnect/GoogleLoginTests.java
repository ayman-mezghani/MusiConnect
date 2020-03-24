package ch.epfl.sdp.musiconnect;

/**
 * @Author : Antonio Pisanello
 */

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.IsNot.not;

public class GoogleLoginTests {

    @Rule
    public IntentsTestRule<GoogleLogin> activityRule = new IntentsTestRule<>(GoogleLogin.class);

    @Test
    public void signInButtonClick(){
        onView(withId(R.id.sign_in_button)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_in_button)).perform(click());
    }
}
