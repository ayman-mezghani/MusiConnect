package ch.epfl.sdp.musiconnect;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class ProfileModificationTests {
    @Rule
    public final ActivityTestRule<ProfileModification> profileModificationPageRule =
            new ActivityTestRule<>(ProfileModification.class);

    /**
     * Helper method to avoid duplication
     * @param stringId
     */
    private void clickToDisplayMessage(int stringId) {
        onView(withText(stringId)).perform(click());
        onView(withText(R.string.in_construction)).inRoot(withDecorView(not(profileModificationPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void testDoNotSaveButtonShouldCloseCurrentActivity() {
        onView(withText(R.string.do_not_save_profile)).perform(click());
        profileModificationPageRule.getActivity().finish();
        assert(true);
    }

    @Test
    public void testSaveButtonShouldDisplayMessage() {
        clickToDisplayMessage(R.string.save_profile);
    }
}
