package ch.epfl.sdp.musiconnect;

import android.widget.DatePicker;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

public class UserCreationHardwareTests {
    @Rule
    public IntentsTestRule<UserCreation> activityRule = new IntentsTestRule<>(UserCreation.class);

    @BeforeClass
    public static void setMockDB() {
        DbGenerator.setDatabase(new MockDatabase());
    }

    @After
    public void bePatient() {
        waitALittle(3);
    }

    @Test
    public void singleInputEmptyTest() {
        Assert.assertTrue(((UserCreation) VideoPlayingTests.getCurrentActivity()).isEmpty(((UserCreation) VideoPlayingTests.getCurrentActivity()).etUserName));
    }

    @Test
    public void AllEmptyInputTest() {
        onView(withId(R.id.btnUserCreationCreate)).perform(ViewActions.scrollTo()).perform(click());
        CloudStorageTest.waitALittle(1);
        onView(withText("Fill Firstname field")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void ManyEmptyInputTest() {
        onView(withId(R.id.etFirstname)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bob"));
        closeSoftKeyboard();
        onView(withId(R.id.etLastName)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("bernard"));
        closeSoftKeyboard();
        onView(withId(R.id.etMail)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bob@gmail.com"));
        closeSoftKeyboard();

        onView(withId(R.id.btnUserCreationCreate)).perform(ViewActions.scrollTo()).perform(click());
        CloudStorageTest.waitALittle(1);
        onView(withText("Fill Username field")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void etMail() {
        onView(withId(R.id.etFirstname)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bob"));
        closeSoftKeyboard();
        onView(withId(R.id.etLastName)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("bernard"));
        closeSoftKeyboard();
        onView(withId(R.id.etUsername)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bobbeber"));
        closeSoftKeyboard();

        onView(withId(R.id.btnUserCreationCreate)).perform(ViewActions.scrollTo()).perform(click());
        CloudStorageTest.waitALittle(1);
        onView(withText("Fill Email field")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void etLastNameEmptyInputTest() {
        onView(withId(R.id.etFirstname)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bob"));
        closeSoftKeyboard();

        onView(withId(R.id.btnUserCreationCreate)).perform(ViewActions.scrollTo()).perform(click());
        CloudStorageTest.waitALittle(1);
        onView(withText("Fill Lastname field")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void onlyDateInputEmptyTest() {
        onView(withId(R.id.etFirstname)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bob"));
        closeSoftKeyboard();
        onView(withId(R.id.etLastName)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("bernard"));
        closeSoftKeyboard();
        onView(withId(R.id.etUsername)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bobbeber"));
        closeSoftKeyboard();
        onView(withId(R.id.etMail)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bob@gmail.com"));
        closeSoftKeyboard();

        onView(withId(R.id.btnUserCreationCreate)).perform(ViewActions.scrollTo()).perform(click());
        CloudStorageTest.waitALittle(1);
        onView(withText("Select a date of birth")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void allInputsSetted() {

        onView(withId(R.id.etFirstname)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bob"));
        closeSoftKeyboard();
        onView(withId(R.id.etLastName)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("bernard"));
        closeSoftKeyboard();
        onView(withId(R.id.etUsername)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bobbeber"));
        closeSoftKeyboard();
        onView(withId(R.id.etMail)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bob@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.etDate)).perform(ViewActions.scrollTo()).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2000, 1, 1));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btnUserCreationCreate)).perform(ViewActions.scrollTo()).perform(click());
    }

    @Test
    public void getJoinDateWorks() {
        assertEquals(((UserCreation) VideoPlayingTests.getCurrentActivity()).getAge(1995, 10, 19), "24");
    }

    @Test
    public void testHelpClickFromProfileShouldStartNewIntent() {
        MenuTests m = new MenuTests();
        m.testHelpClickShouldStartNewIntent();
    }

    public static void waitALittle(int t) {
        try {
            TimeUnit.SECONDS.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
