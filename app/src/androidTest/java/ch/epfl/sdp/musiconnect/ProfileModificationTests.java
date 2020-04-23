package ch.epfl.sdp.musiconnect;

import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.testsFunctions.childAtPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class ProfileModificationTests {
    @Rule
    public final ActivityTestRule<MyProfilePage> profilePageRule =
            new ActivityTestRule<>(MyProfilePage.class);

    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase());
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }
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
        onView(withId(R.id.myFirstname)).check(matches(not(withText("Bob"))));
    }

    @Test
    public void testSelectASmallDateShouldDisplayErrorMessage() {
        clickButtonWithText(R.string.edit_profile_button_text);
        onView(withId(R.id.newBirthday)).perform(ViewActions.scrollTo()).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2015, 1, 1));
        onView(withText("OK")).perform(click());
        clickButtonWithText(R.string.save);
        onView(withText(R.string.age_too_low)).inRoot(withDecorView(not(profilePageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void testEditProfileAndSaveShouldUpdateFields() {
        String firstName = "Espresso";
        String lastName = "Tests";
        String userName = "testsEspresso";
        String emailAddress = "espressotests@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);
        john.setLocation(new MyLocation(0, 0));
        john.setTypeOfUser(TypeOfUser.Musician);
        CurrentUser.getInstance(profilePageRule.getActivity()).setMusician(john);

        clickButtonWithText(R.string.edit_profile_button_text);
        onView(withId(R.id.newFirstName)).perform(ViewActions.scrollTo()).perform(clearText(), typeText(firstName));
        closeSoftKeyboard();
        onView(withId(R.id.newLastName)).perform(ViewActions.scrollTo()).perform(clearText(), typeText(lastName));
        closeSoftKeyboard();
        onView(withId(R.id.newUsername)).perform(ViewActions.scrollTo()).perform(clearText(), typeText(userName));
        closeSoftKeyboard();
        onView(withId(R.id.newEmailAddress)).perform(ViewActions.scrollTo()).perform(clearText(), typeText(emailAddress));
        closeSoftKeyboard();

        onView(withId(R.id.newBirthday)).perform(ViewActions.scrollTo()).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(1940, 10, 9));
        onView(withText("OK")).perform(click());

        closeSoftKeyboard();
        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.btnSaveProfile), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1)));
        appCompatButton4.perform(scrollTo(), click());
        onView(withId(R.id.myFirstname)).check(matches(withText(firstName)));
        onView(withId(R.id.myLastname)).check(matches(withText(lastName)));
        onView(withId(R.id.myUsername)).check(matches(withText(userName)));
        onView(withId(R.id.myMail)).check(matches(withText(emailAddress)));
        onView(withId(R.id.myBirthday)).check(matches(withText("09/10/1940")));
    }
}
