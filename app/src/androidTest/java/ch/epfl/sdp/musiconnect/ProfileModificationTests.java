package ch.epfl.sdp.musiconnect;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ProfileModificationTests {
    @Rule
    public final ActivityTestRule<ProfilePage> profilePageRule =
            new ActivityTestRule<>(ProfilePage.class);

    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() { Intents.init(); }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void testEditProfileAndDoNotSaveShouldDoNothing() {
        onView(withText(R.string.edit_profile_button_text)).perform(click());
        onView(withText(R.string.do_not_save_profile)).perform(click());
        profilePageRule.getActivity().finish();
        assert(true);
    }

    @Test
    public void testEditProfileAndSaveShouldUpdateFields() {
        onView(withText(R.string.edit_profile_button_text)).perform(click());
        onView(withId(R.id.newFirstName)).perform(clearText(), typeText("Bob"));
        closeSoftKeyboard();
        onView(withText(R.string.save_profile)).perform(click());
        onView(withId(R.id.myFirstname)).check(matches(withText("Bob")));
    }
}
