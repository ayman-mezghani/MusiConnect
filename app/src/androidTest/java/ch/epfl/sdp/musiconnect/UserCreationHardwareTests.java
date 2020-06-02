package ch.epfl.sdp.musiconnect;

import android.widget.DatePicker;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.location.MapsLocationFunctions;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.testsFunctions.childAtPosition;
import static ch.epfl.sdp.musiconnect.testsFunctions.waitSeconds;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

public class UserCreationHardwareTests {
    @Rule
    public IntentsTestRule<UserCreation> activityRule = new IntentsTestRule<>(UserCreation.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void setMocks() {
        DbSingleton.setDatabase(new MockDatabase(false));
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }

    @Before
    public void clickOnLocationPermission() {
        MapsLocationFunctions.clickPermissionAlert();
    }


    @After
    public void bePatient() {
        waitSeconds(3);
    }

    @Test
    public void singleInputEmptyTest() {
        Assert.assertTrue(((UserCreation) testsFunctions.getCurrentActivity()).isEmpty(((UserCreation) testsFunctions.getCurrentActivity()).etUserName));
    }

    @Test
    public void AllEmptyInputTest() {
        onView(withId(R.id.btnUserCreationCreate)).perform(ViewActions.scrollTo()).perform(click());
        waitSeconds(1);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activityRule.getActivity().getApplicationContext());
        if (account != null) {
            onView(withText("Fill Username field")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        } else {
            onView(withText("Fill Firstname field")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        }
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
        waitSeconds(1);
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
        waitSeconds(1);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activityRule.getActivity().getApplicationContext());
        if (account != null) {
            onView(withText("Select a date of birth")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        } else {
            onView(withText("Fill Email field")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        }
    }

    @Test
    public void etLastNameEmptyInputTest() {
        onView(withId(R.id.etFirstname)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bob"));
        closeSoftKeyboard();

        onView(withId(R.id.btnUserCreationCreate)).perform(ViewActions.scrollTo()).perform(click());
        waitSeconds(1);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activityRule.getActivity().getApplicationContext());
        if (account != null) {
            onView(withText("Fill Username field")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        } else {
            onView(withText("Fill Lastname field")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        }
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
        waitSeconds(1);
        onView(withText("Select a date of birth")).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void allInputsSet() {
        String instrument = "Bass";
        String level = "Beginner";

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

        onView(withId(R.id.newProfileSelectedInstrument)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(instrument))).perform(click());
        onView(withId(R.id.newProfileSelectedInstrument)).check(matches(withSpinnerText(containsString(instrument))));

        onView(withId(R.id.newProfileSelectedLevel)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(level))).perform(click());
        onView(withId(R.id.newProfileSelectedLevel)).check(matches(withSpinnerText(containsString(level))));

        onView(withId(R.id.btnUserCreationCreate)).perform(ViewActions.scrollTo()).perform(click());
    }

    @Test
    public void createBandFails() {
        ViewInteraction appCompatRadioButton = onView(allOf(withId(R.id.rbBand), withText("Band"),
                childAtPosition(allOf(withId(R.id.rdg), childAtPosition(
                        withClassName(is("android.widget.LinearLayout")),0)),1)));
        appCompatRadioButton.perform(ViewActions.scrollTo(), click());

        onView(withId(R.id.etFirstname)).perform(ViewActions.scrollTo(), clearText(), typeText("Bob"));
        closeSoftKeyboard();
        onView(withId(R.id.etLastName)).perform(ViewActions.scrollTo(), clearText(), typeText("bernard"));
        closeSoftKeyboard();
        onView(withId(R.id.etUsername)).perform(ViewActions.scrollTo(), clearText(), typeText("Bobbeber"));
        closeSoftKeyboard();
        onView(withId(R.id.etMail)).perform(ViewActions.scrollTo(), clearText(), typeText("Bob@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.etDate)).perform(ViewActions.scrollTo()).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2000, 1, 1));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btnUserCreationCreate)).perform(ViewActions.scrollTo(), click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btnBandCreationCreate), withText("Start using application"),childAtPosition(childAtPosition(
                        withClassName(is("android.widget.LinearLayout")),0),1)));
        appCompatButton3.perform(scrollTo(), click());

        onView(withText(R.string.band_name_cant_be_empty)).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

    }

    @Test
    public void getJoinDateWorks() {
        assertEquals(((UserCreation) testsFunctions.getCurrentActivity()).getAge(1995, 10, 19), "24");
    }
}
