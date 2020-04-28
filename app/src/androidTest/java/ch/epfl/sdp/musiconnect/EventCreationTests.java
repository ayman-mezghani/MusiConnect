package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.google.firebase.firestore.GeoPoint;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.events.EventCreation;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static ch.epfl.sdp.musiconnect.testsFunctions.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EventCreationTests {
    @Rule
    public final ActivityTestRule<EventCreation> eventCreationRule =
            new ActivityTestRule<>(EventCreation.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase());
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() { Intents.release(); }


    private void clickButtonWithText(int text) {
        onView(withText(text)).perform(scrollTo()).perform(click());
    }


    @Test
    public void testEventCreationDoNotSaveEnd() {
        closeSoftKeyboard();
        clickButtonWithText(R.string.do_not_save);
        assertTrue(eventCreationRule.getActivity().isFinishing());
    }

    @Test
    public void testEventCreationSaveShouldDoNothing() {
        //checkIfNotFinishing();
    }

    private void checkIfNotFinishing() {
        closeSoftKeyboard();
        clickButtonWithText(R.string.save);
        assertFalse(eventCreationRule.getActivity().isFinishing());
    }

    //@Test
    public void testHelpClickShouldStartNewIntent() {
        onView(withId(R.id.help)).perform(click());

        Intent helpIntent = new Intent();
        eventCreationRule.launchActivity(helpIntent);
        intended(hasComponent(HelpPage.class.getName()));
    }


    @Test
    public void testMyEventClickShouldDoNothing() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.create_an_event)).perform(click());
        Intent intent = new Intent();
        eventCreationRule.launchActivity(intent);

        intended(hasComponent(EventCreation.class.getName()));
    }

    @Test
    public void addEmptyMusicianShouldDoNothing() {
        clickButtonWithText(R.string.add_participant);
        // Toasts take too long / blocked by previous toasts
        // onView(withText("Please add a username")).inRoot(withDecorView(not(eventCreationRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("")));
    }

    //@Test
    public void addMusicianTwiceShouldShowOnlyOnce() {
        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.eventCreationNewParticipant),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                0)));
        appCompatEditText4.perform(scrollTo(), replaceText("palpha@gmail.com"), ViewActions.closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.eventCreationAddParticipants), withText("Add Participant"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                0)));
        appCompatButton.perform(scrollTo(), click());
        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.eventCreationNewParticipant),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                0)));
        appCompatEditText3.perform(scrollTo(), replaceText("palpha@gmail.com"), ViewActions.closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.eventCreationAddParticipants), withText("Add Participant"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                0)));
        appCompatButton2.perform(scrollTo(), click());
        onView(withText(R.string.user_already_in_participation_list)).inRoot(withDecorView(Matchers.not(eventCreationRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText(containsString("palpha@gmail.com"))));
    }

    @Test
    public void removeEmptyMusicianShouldDoNothing() {
        clickButtonWithText(R.string.remove_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("")));
    }

    @Test
    public void removeNonExistentMusicianShouldDoNothing() {
        onView(withId(R.id.eventCreationNewParticipant)).perform(scrollTo()).perform(clearText(), typeText("palpha@gmail.com"));
        clickButtonWithText(R.string.remove_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("")));
    }

    @Test
    public void get() {
        onView(withId(R.id.eventCreationNewParticipant)).perform(scrollTo()).perform(clearText(), typeText("palpha@gmail.com"));
        clickButtonWithText(R.string.remove_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("")));
    }

    //@Test
    public void testCorrectInputsAndDoNotFinishIfEmptyField() {
        onView(withId(R.id.eventCreationNewEventTitle)).perform(scrollTo()).perform(clearText(), typeText("TestTitle"));
        checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventAddress)).perform(scrollTo()).perform(clearText(), typeText("TestAddress"));
        checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventDescription)).perform(scrollTo()).perform(clearText(), typeText("TestDescription"));
        checkIfNotFinishing();

        closeSoftKeyboard();
        closeSoftKeyboard();

        onView(withId(R.id.eventCreationNewParticipant)).perform(scrollTo()).perform(clearText(), typeText("palpha@gmail.com"));
        clickButtonWithText(R.string.add_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText(containsString("palpha@gmail.com" + System.lineSeparator()))));

        onView(withId(R.id.eventCreationNewParticipant)).perform(scrollTo()).perform(clearText(), typeText("palpha@gmail.com"));
        clickButtonWithText(R.string.remove_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(not(withText(containsString("palpha@gmail.com")))));

        closeSoftKeyboard();

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.eventCreationBtnSaveEvent), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1)));
        appCompatButton.perform(scrollTo(), click());
        //assertTrue(eventCreationRule.getActivity().isFinishing());
    }

    @Test
    public void testWithDateTime() {
        checkIfNotFinishing();
        
        onView(withId(R.id.eventCreationNewEventTitle)).perform(scrollTo(), clearText(), typeText("TestTitle"));
        checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventAddress)).perform(scrollTo(), clearText(), typeText("rue de lausanne, genve"));
        checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventDescription)).perform(scrollTo(), clearText(), typeText("TestDescription"));
        checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventDate)).perform(scrollTo(), click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH));
        onView(withText("OK")).perform(click());
        checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventTime)).perform(scrollTo(), click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(Calendar.HOUR_OF_DAY, Calendar.MINUTE));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.eventCreationNewEventDate)).check(matches(withText(Calendar.DAY_OF_MONTH + "/" + Calendar.MONTH + "/" + Calendar.YEAR)));
        onView(withId(R.id.eventCreationNewEventTime)).check(matches(withText(Calendar.HOUR_OF_DAY + ":" + Calendar.MINUTE)));
        //checkIfNotFinishing();
    }

    @Test
    public void testAllInputsFields() {
        CurrentUser.getInstance(eventCreationRule.getActivity()).setMusician((new MockDatabase()).getDummyMusician(0));
        //checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventTitle)).perform(scrollTo(), clearText(), typeText("TestTitle"));

        onView(withId(R.id.eventCreationNewEventDescription)).perform(scrollTo(), clearText(), typeText("TestDescription"));checkIfNotFinishing();
        checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventDate)).perform(scrollTo(), click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH));
        onView(withText("OK")).perform(click());
        //checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventTime)).perform(scrollTo(), click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(Calendar.HOUR_OF_DAY, Calendar.MINUTE));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.eventCreationNewEventDate)).check(matches(withText(Calendar.DAY_OF_MONTH + "/" + Calendar.MONTH + "/" + Calendar.YEAR)));
        onView(withId(R.id.eventCreationNewEventTime)).check(matches(withText(Calendar.HOUR_OF_DAY + ":" + Calendar.MINUTE)));
        checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventAddress)).perform(scrollTo(), clearText(), typeText("rue de lausanne, genve"));
        //checkIfNotFinishing();

        ((EventCreation) getCurrentActivity()).sendToDatabase();
    }

    //@Test
    public void geocoderWillReturnTrueValue() {
        GeoPoint p1 = ((GeoPoint)((EventCreation) getCurrentActivity()).getLocationFromAddress("rue de lausanne, genève"));
        double lat = p1.getLatitude();
        double lng = p1.getLongitude();
        assertEquals(lat, 46.218781199999995, 0);
        assertEquals(lng, 6.1487117, 0);
    }

    @Test
    public void geocoderWillReturnNullValue() {
        GeoPoint p1 = ((GeoPoint)((EventCreation) getCurrentActivity()).getLocationFromAddress(""));
        assertNull(p1);
    }
}
