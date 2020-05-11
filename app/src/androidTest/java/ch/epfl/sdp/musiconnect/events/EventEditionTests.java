package ch.epfl.sdp.musiconnect.events;

import android.content.Intent;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class EventEditionTests {
    @Rule
    public final ActivityTestRule<EventEditionPage> eventEditionRule =
            new ActivityTestRule<>(EventEditionPage.class, true, false);

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
        onView(withText(text)).perform(ViewActions.scrollTo()).perform(click());
    }

    @Test
    public void testDontSave() {
        Intent intent = new Intent();
        eventEditionRule.launchActivity(intent);

        closeSoftKeyboard();
        clickButtonWithText(R.string.do_not_save);
        assertTrue(eventEditionRule.getActivity().isFinishing());
    }



    @Test
    public void testLoadEvent() {
        Intent intent = new Intent();
        intent.putExtra("eid", "1");

        ArrayList<String> emails = new ArrayList<>();
        emails.add("palpha@gmail.com");

        String testTitle = "testTitle";
        String testAddress = "testAddress";
        String testDescription = "testDescription";

        intent.putExtra("title", testTitle);
        intent.putExtra("address", testAddress);
        intent.putExtra("description", testDescription);
        intent.putExtra("visible", false);
        intent.putStringArrayListExtra("emails", emails);
        intent.putExtra("datetime", "01/01/2020   00:00");

        eventEditionRule.launchActivity(intent);

        onView(withId(R.id.eventEditionNewEventTitle)).check(matches(withText(testTitle)));
        onView(withId(R.id.eventEditionNewEventAddress)).check(matches(withText(testAddress)));
        onView(withId(R.id.eventEditionNewEventDescription)).check(matches(withText(testDescription)));
        onView(withId(R.id.eventEditionNewEventParticipants)).check(matches(withText("PAlpha" + System.lineSeparator())));
        onView(withId(R.id.eventEditionNewEventDate)).check(matches(withText("01/01/2020")));
        onView(withId(R.id.eventEditionNewEventTime)).check(matches(withText("00:00")));

        closeSoftKeyboard();
        clickButtonWithText(R.string.save);
        assertTrue(eventEditionRule.getActivity().isFinishing());
    }

    @Test
    public void testLoadEmptyEvent() {
        Intent intent = new Intent();
        eventEditionRule.launchActivity(intent);

        onView(withId(R.id.eventEditionNewEventTitle)).check(matches(withText("")));
        closeSoftKeyboard();
        clickButtonWithText(R.string.save);
        assertFalse(eventEditionRule.getActivity().isFinishing());
        clickButtonWithText(R.string.do_not_save);
        assertTrue(eventEditionRule.getActivity().isFinishing());
    }
}
