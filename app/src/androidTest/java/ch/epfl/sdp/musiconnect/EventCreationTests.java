package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;

import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
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
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventCreationTests {
    @Rule
    public final ActivityTestRule<EventCreation> eventCreationRule =
            new ActivityTestRule<>(EventCreation.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() {
        Intents.init();
        MapsLocationTest.clickAllow();
    }

    @After
    public void releaseIntents() { Intents.release(); }


    private void openActionsMenu(int stringId) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(stringId)).perform(click());
    }

    private void clickButtonWithText(int text) {
        onView(withText(text)).perform(ViewActions.scrollTo()).perform(click());
    }


    @Test
    public void testEventCreationDoNotSaveEnd() {
        closeSoftKeyboard();
        clickButtonWithText(R.string.do_not_save);
        assertTrue(eventCreationRule.getActivity().isFinishing());
    }

    @Test
    public void testEventCreationSaveShouldDoNothing() {
        checkIfNotFinishing();
    }

    private void checkIfNotFinishing() {
        closeSoftKeyboard();
        clickButtonWithText(R.string.save);
        assertFalse(eventCreationRule.getActivity().isFinishing());
    }

    @Test
    public void testHelpClickShouldStartNewIntent() {
        onView(withId(R.id.help)).perform(click());

        Intent helpIntent = new Intent();
        eventCreationRule.launchActivity(helpIntent);
        intended(hasComponent(HelpPage.class.getName()));
    }


    @Test
    public void testMyEventClickShouldDoNothing() {
        openActionsMenu(R.string.create_an_event);
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

    @Test
    public void addMusicianTwiceShouldShowOnlyOnce() {
        onView(withId(R.id.eventCreationNewParticipant)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("TestUser"));
        clickButtonWithText(R.string.add_participant);
        onView(withId(R.id.eventCreationNewParticipant)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("TestUser"));
        clickButtonWithText(R.string.add_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("TestUser" + System.lineSeparator())));
    }

    @Test
    public void removeEmptyMusicianShouldDoNothing() {
        clickButtonWithText(R.string.remove_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("")));
    }

    @Test
    public void removeNonExistentMusicianShouldDoNothing() {
        onView(withId(R.id.eventCreationNewParticipant)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("TestUser"));
        clickButtonWithText(R.string.remove_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("")));
    }

    @Test
    public void testCorrectInputsAndDoNotFinishIfEmptyField() {
        onView(withId(R.id.eventCreationNewEventTitle)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("TestTitle"));
        checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventAddress)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("TestAddress"));
        checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventDescription)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("TestDescription"));
        checkIfNotFinishing();

        closeSoftKeyboard();

        Calendar today = Calendar.getInstance();
        onView(withId(R.id.eventCreationNewEventDate)).perform(ViewActions.scrollTo()).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(today.YEAR, today.MONTH, today.DAY_OF_MONTH));
        onView(withText("OK")).perform(click());
        checkIfNotFinishing();

        onView(withId(R.id.eventCreationNewEventTime)).perform(ViewActions.scrollTo()).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(today.HOUR_OF_DAY, today.MINUTE));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.eventCreationNewEventDate)).check(matches(withText(today.DAY_OF_MONTH + "/" + today.MONTH + "/" + today.YEAR)));
        onView(withId(R.id.eventCreationNewEventTime)).check(matches(withText(today.HOUR_OF_DAY + ":" + today.MINUTE)));

        onView(withId(R.id.eventCreationNewParticipant)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("TestUser"));
        clickButtonWithText(R.string.add_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(withText("TestUser" + System.lineSeparator())));

        onView(withId(R.id.eventCreationNewParticipant)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("TestUser"));
        clickButtonWithText(R.string.remove_participant);
        onView(withId(R.id.eventCreationNewEventParticipants)).check(matches(not(withText("TestUser"))));

        closeSoftKeyboard();
        clickButtonWithText(R.string.save);
        assertTrue(eventCreationRule.getActivity().isFinishing());
    }

}
