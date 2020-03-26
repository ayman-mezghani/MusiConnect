package ch.epfl.sdp.musiconnect;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class ProfileConsultationTests {
    @Rule
    public final ActivityTestRule<ProfilePage> profilePageRule =
            new ActivityTestRule<>(ProfilePage.class);

    @Test
    public void testEditButtonShouldDisplayMessage() {
        onView(withText(R.string.edit_profile)).perform(click());
        onView(withText(R.string.not_yet_done)).inRoot(withDecorView(not(profilePageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }
}
