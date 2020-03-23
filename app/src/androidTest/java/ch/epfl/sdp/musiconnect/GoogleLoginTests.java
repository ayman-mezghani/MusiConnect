package ch.epfl.sdp.musiconnect;

/**
 * @Author : Antonio Pisanello
 */

import android.app.Activity;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

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
        onView(withId(R.id.sign_in_button)).perform(click());
    }
}
