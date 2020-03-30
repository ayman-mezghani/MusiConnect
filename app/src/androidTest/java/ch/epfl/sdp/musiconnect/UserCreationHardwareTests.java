package ch.epfl.sdp.musiconnect;

import android.widget.TextView;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorage;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

public class UserCreationHardwareTests {
    //@Rule
    //public IntentsTestRule<UserCreation> activityRule = new IntentsTestRule<>(UserCreation.class);

    @Rule
    public final ActivityTestRule<UserCreation> activityRule =
            new ActivityTestRule<>(UserCreation.class);

    @Test
    public void singleInputEmptyTest(){
        Assert.assertTrue(((UserCreation)VideoPlayingTests.getCurrentActivity()).isEmpty(((UserCreation)VideoPlayingTests.getCurrentActivity()).etUserName));
    }

    @Test
    public void AllEptyInputTest(){
        onView(withId(R.id.btnUserCreationCreate)).perform(click());
        CloudStorageTest.waitALittle(1);
        onView(withText("Fill Firstname field")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void ManyEptyInputTest(){
        onView(withId(R.id.etFirstname)).perform(clearText(), typeText("Bob"));
        closeSoftKeyboard();
        onView(withId(R.id.etLastName)).perform(clearText(), typeText("bernard"));
        closeSoftKeyboard();
        onView(withId(R.id.etMail)).perform(clearText(), typeText("Bob@bob.ch"));
        closeSoftKeyboard();

        onView(withId(R.id.btnUserCreationCreate)).perform(click());
        CloudStorageTest.waitALittle(1);
        onView(withText("Fill Username field")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void etMail(){
        onView(withId(R.id.etFirstname)).perform(clearText(), typeText("Bob"));
        closeSoftKeyboard();
        onView(withId(R.id.etLastName)).perform(clearText(), typeText("bernard"));
        closeSoftKeyboard();
        onView(withId(R.id.etUsername)).perform(clearText(), typeText("Bobbeber"));
        closeSoftKeyboard();

        onView(withId(R.id.btnUserCreationCreate)).perform(click());
        CloudStorageTest.waitALittle(1);
        onView(withText("Fill Email field")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void etLastNameEptyInputTest(){
        onView(withId(R.id.etFirstname)).perform(clearText(), typeText("Bob"));
        closeSoftKeyboard();

        onView(withId(R.id.btnUserCreationCreate)).perform(click());
        CloudStorageTest.waitALittle(1);
        onView(withText("Fill Lastname field")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void onlyDateIputEmptyTest(){
        onView(withId(R.id.etFirstname)).perform(clearText(), typeText("Bob"));
        closeSoftKeyboard();
        onView(withId(R.id.etLastName)).perform(clearText(), typeText("bernard"));
        closeSoftKeyboard();
        onView(withId(R.id.etUsername)).perform(clearText(), typeText("Bobbeber"));
        closeSoftKeyboard();
        onView(withId(R.id.etMail)).perform(clearText(), typeText("Bob@bob.ch"));
        closeSoftKeyboard();

        onView(withId(R.id.btnUserCreationCreate)).perform(click());
        CloudStorageTest.waitALittle(1);
        onView(withText("Select a date of birth")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void allInputSetted(){
        onView(withId(R.id.etFirstname)).perform(clearText(), typeText("Bob"));
        closeSoftKeyboard();
        onView(withId(R.id.etLastName)).perform(clearText(), typeText("bernard"));
        closeSoftKeyboard();
        onView(withId(R.id.etUsername)).perform(clearText(), typeText("Bobbeber"));
        closeSoftKeyboard();
        onView(withId(R.id.etMail)).perform(clearText(), typeText("Bob@bob.ch"));
        closeSoftKeyboard();
        ((TextView)((UserCreation) VideoPlayingTests.getCurrentActivity()).findViewById(R.id.etDate)).setText("19 / 10 / 1995 (24 years)");

        onView(withId(R.id.btnUserCreationCreate)).perform(click());
    }

    @Test
    public void clickOnImgView(){
        //onView(withId(R.id.userProfilePicture)).perform(click());
    }

    @Test
    public void clickOnDatePicker(){
        //onView(withId(R.id.etDate)).perform(click());
    }

    @Test
    public void getJoinDateWorks() {
        assertEquals(((UserCreation) VideoPlayingTests.getCurrentActivity()).getAge(1995, 10, 19), "24");
    }

}
