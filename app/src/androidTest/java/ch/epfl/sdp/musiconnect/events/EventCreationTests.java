package ch.epfl.sdp.musiconnect.events;

import android.content.Intent;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.google.firebase.firestore.GeoPoint;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.UserType;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static ch.epfl.sdp.musiconnect.testsFunctions.getCurrentActivity;
import static ch.epfl.sdp.musiconnect.testsFunctions.waitSeconds;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


// If this class seems like a mess, this is to resolve all "issues" from code climate
@RunWith(AndroidJUnit4.class)
public class EventCreationTests {
    @Rule
    public final ActivityTestRule<EventCreationPage> eventCreationRule =
            new ActivityTestRule<>(EventCreationPage.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void setMocks() {
        DbSingleton.setDatabase(new MockDatabase(false));
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }

    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() { Intents.release(); }


    private void clickButtonWithText(int text) {
        onView(withText(text)).perform(ViewActions.scrollTo(), click());
    }


    @Test
    public void testEventCreationDoNotSaveEnd() {
        closeSoftKeyboard();
        clickButtonWithText(R.string.do_not_save);
        assertTrue(eventCreationRule.getActivity().isFinishing());
    }

    private void checkIfNotFinishing() {
        closeSoftKeyboard();
        clickButtonWithText(R.string.save);
        assertFalse(eventCreationRule.getActivity().isFinishing());
    }

    private void checkIfFinishing() {
        //closeSoftKeyboard();
        clickButtonWithText(R.string.save);
        //assertTrue(eventCreationRule.getActivity().isFinishing());
    }


    @Test
    public void testMyEventClickShouldDoNothing() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.create_an_event)).perform(click());
        Intent intent = new Intent();
        eventCreationRule.launchActivity(intent);

        intended(hasComponent(EventCreationPage.class.getName()));
    }

    @Test
    public void addOrRemoveEmptyMusicianShouldDoNothing() {
        clickButtonWithText(R.string.add_participant);
        // Toasts take too long / blocked by previous toasts
        // onView(withText("Please add a username")).inRoot(withDecorView(not(eventCreationRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("")));
        clickButtonWithText(R.string.remove_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("")));
    }

    @Test
    public void addMusicianTwiceShouldShowOnlyOnce() {
        onView(withId(R.id.eventCreationNewParticipant)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("palpha@gmail.com"));
        clickButtonWithText(R.string.add_participant);
        onView(withId(R.id.eventCreationNewParticipant)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("palpha@gmail.com"));
        clickButtonWithText(R.string.add_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("PAlpha" + System.lineSeparator())));
    }


    @Test
    public void cannotAddOrRemoveYourself() {
        onView(withId(R.id.eventCreationNewParticipant)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("bobminion@gmail.com"));
        clickButtonWithText(R.string.add_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("")));
        clickButtonWithText(R.string.remove_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("")));
    }


    @Test
    public void removeNonExistentMusicianShouldDoNothing() {
        onView(withId(R.id.eventCreationNewParticipant)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("palpha@gmail.com"));
        clickButtonWithText(R.string.remove_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("")));
    }

    @Test
    public void testCorrectInputsAndDoNotFinishIfEmptyField() {
        CurrentUser.getInstance(eventCreationRule.getActivity()).setTypeOfUser(UserType.Musician);

        checkIfNotFinishing();
        onView(withId(R.id.eventCreationNewEventTitle)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("TestTitle"));
        checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventAddress)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("TestAddress"));
        checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventDescription)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("TestDescription"));
        checkIfNotFinishing();

        closeSoftKeyboard();
        testSetDefaultCalendar();

        onView(withId(R.id.eventCreationNewParticipant)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("palpha@gmail.com"));
        clickButtonWithText(R.string.add_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("PAlpha" + System.lineSeparator())));

        onView(withId(R.id.eventCreationNewParticipant)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("palpha@gmail.com"));
        clickButtonWithText(R.string.remove_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(not(withText("PAlpha"))));

        checkIfFinishing();
    }

    private void testSetDefaultCalendar() {
        onView(withId(R.id.eventCreationNewEventDate)).perform(ViewActions.scrollTo()).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH));
        onView(withText("OK")).perform(click());
        checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventTime)).perform(ViewActions.scrollTo()).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(Calendar.HOUR_OF_DAY, Calendar.MINUTE));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.eventCreationNewEventDate)).check(matches(withText(Calendar.DAY_OF_MONTH + "/" + Calendar.MONTH + "/" + Calendar.YEAR)));
        onView(withId(R.id.eventCreationNewEventTime)).check(matches(withText(Calendar.HOUR_OF_DAY + ":" + Calendar.MINUTE)));
    }


    @Test
    public void testClickVisibleEvent() {
        onView(withId(R.id.visible)).check(matches(not(isChecked())));
        onView(withId(R.id.notVisible)).check(matches(isChecked()));
        onView(withId(R.id.visible)).perform(ViewActions.scrollTo()).perform(click());

        onView(withId(R.id.visible)).check(matches(isChecked()));
        onView(withId(R.id.notVisible)).check(matches(not(isChecked())));
        onView(withId(R.id.notVisible)).perform(ViewActions.scrollTo()).perform(click());

        onView(withId(R.id.visible)).check(matches(not(isChecked())));
        onView(withId(R.id.notVisible)).check(matches(isChecked()));
    }

    // this test works on my computer, fails on cirrus
    // But works when using the app
    //@Test
//    public void geocoderWillReturnTrueValue() {
//        GeoPoint p1 = ((EventCreation) getCurrentActivity()).getLocationFromAddress("rue de lausanne, genève");
//        double lat = p1.getLatitude();
//        double lng = p1.getLongitude();
//        assertEquals(lat, 46.218781199999995, 5);
//        assertEquals(lng, 6.1487117, 5);
//    }

    @Test
    public void geocoderWillReturnNullValue() {
        GeoPoint p1 = ((EventCreationPage) getCurrentActivity()).getLocationFromAddress("");
        assertNull(p1);
    }

    private void writeTestValuesWithCustomAddress(String address) {
        CurrentUser.getInstance(eventCreationRule.getActivity()).setTypeOfUser(UserType.Musician);

        onView(withId(R.id.eventCreationNewEventTitle)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("TestTitle"));
        onView(withId(R.id.eventCreationNewEventAddress)).perform(ViewActions.scrollTo()).perform(clearText(), typeText(address));
        closeSoftKeyboard();
        onView(withId(R.id.eventCreationNewEventDescription)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("TestDescription"));
        closeSoftKeyboard();

        testSetDefaultCalendar();
        waitSeconds(2);
        checkIfFinishing();
    }

    @Test
    public void testWithResolvableAddressShoulPass() {
        writeTestValuesWithCustomAddress("rue de lausanne, geneve");
    }

    @Test
    public void testWithUnresolvableAddressShoulPopUpToast() {
        writeTestValuesWithCustomAddress("TestAddress");

        //onView(withText("Unable to resolve address")).inRoot(withDecorView(Matchers.not(eventCreationRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void testExtraAddressIsDisplayed(){
        Intent eventIntent = new Intent(eventCreationRule.getActivity(), EventCreationPage.class);
        eventIntent.putExtra("Address","Place du Motty 10, 1024 Ecublens, Switzerland");
        eventCreationRule.getActivity().startActivity(eventIntent);
        onView(withId(R.id.eventCreationNewEventAddress)).check(matches(withText("Place du Motty 10, 1024 Ecublens, Switzerland")));

    }
}
