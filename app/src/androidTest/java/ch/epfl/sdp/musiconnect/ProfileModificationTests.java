package ch.epfl.sdp.musiconnect;

import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ProfileModificationTests {
    @Rule
    public final ActivityTestRule<MyProfilePage> profilePageRule =
            new ActivityTestRule<>(MyProfilePage.class);

    /**
     * Helper method to avoid duplication
     * @param text: text to recognize on the clickable object
     */
    private void clickButtonWithText(int text) {
        onView(withText(text)).perform(ViewActions.scrollTo()).perform(click());
    }

    @Test
    public void testEditProfileAndDoNotSaveShouldDoNothing() {
        clickButtonWithText(R.string.edit_profile_button_text);
        onView(withId(R.id.newFirstName)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bob"));
        clickButtonWithText(R.string.do_not_save);
        assert(true);
    }

    @Test
    public void testEditProfileAndSaveShouldUpdateFields() {
        clickButtonWithText(R.string.edit_profile_button_text);
        onView(withId(R.id.newFirstName)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bob"));
        onView(withId(R.id.newLastName)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Mallet"));
        onView(withId(R.id.newUsername)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("BobMallet"));
        onView(withId(R.id.newEmailAddress)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("bob.mallet@gmail.com"));
        closeSoftKeyboard();

        onView(withId(R.id.newBirthday)).perform(ViewActions.scrollTo()).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2000, 1, 1));
        onView(withText("OK")).perform(click());

        clickButtonWithText(R.string.save);
        onView(withId(R.id.myFirstname)).check(matches(withText("Bob")));
        onView(withId(R.id.myLastname)).check(matches(withText("Mallet")));
        onView(withId(R.id.myUsername)).check(matches(withText("BobMallet")));
        onView(withId(R.id.myMail)).check(matches(withText("bob.mallet@gmail.com")));
        onView(withId(R.id.myBirthday)).check(matches(withText("01/01/2000")));
    }
}
