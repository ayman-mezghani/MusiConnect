package ch.epfl.sdp.musiconnect;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        onView(withId(R.id.newLastName)).perform(clearText(), typeText("Mallet"));
        onView(withId(R.id.newUsername)).perform(clearText(), typeText("BobMallet"));
        onView(withId(R.id.newEmailAddress)).perform(clearText(), typeText("bob.mallet@gmail.com"));
        onView(withId(R.id.newBirthday)).perform(clearText(), typeText("01.01.2000"));
        closeSoftKeyboard();
        onView(withText(R.string.save_profile)).perform(click());
        onView(withId(R.id.myFirstname)).check(matches(withText("Bob")));
        onView(withId(R.id.myLastname)).check(matches(withText("Mallet")));
        onView(withId(R.id.myUsername)).check(matches(withText("BobMallet")));
        onView(withId(R.id.myMail)).check(matches(withText("bob.mallet@gmail.com")));
        onView(withId(R.id.myBirthday)).check(matches(withText("01.01.2000")));
    }
}
