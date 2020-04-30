package ch.epfl.sdp.musiconnect;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.events.Event;
import ch.epfl.sdp.musiconnect.events.EventPage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static ch.epfl.sdp.musiconnect.testsFunctions.waitALittle;

@RunWith(AndroidJUnit4.class)
public class EventPageTests {
    @Rule
    public final ActivityTestRule<EventPage> eventPageRule =
            new ActivityTestRule<>(EventPage.class, true, false);

    private static MockDatabase md;

    @BeforeClass
    public static void setMocks() {
        md = new MockDatabase();
        DbGenerator.setDatabase(md);
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }


    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() { Intents.release(); }


    private void openActionsMenu(int stringId) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(stringId)).perform(click());
    }


    @Test
    public void testMyEventClickShouldDoNothing() {
        Intent intent = new Intent();
        eventPageRule.launchActivity(intent);
        openActionsMenu(R.string.my_events);

        intended(hasComponent(EventPage.class.getName()));
    }


    @Test
    public void loadPageShouldShowCorrectEvent() {
        Musician m1 = md.getDummyMusician(0);
        Musician m2 = md.getDummyMusician(3);

        Event event = new Event(m1, "1");
        event.setAddress("Westminster, London, England");
        event.setLocation(51.5007, 0.1245);
        event.setDateTime(new MyDate(2020, 9, 21, 14, 30));
        event.setTitle("Event at Big Ben!");
        event.setDescription("Playing at Big Ben, come watch us play!");
        event.register(m2);

        String s = m1.getName() + System.lineSeparator() + m2.getName() + System.lineSeparator();

        Intent intent = new Intent();
        intent.putExtra("eid", "1");
        eventPageRule.launchActivity(intent);

        waitALittle(3);

        onView(withId(R.id.eventTitle)).check(matches(withText(event.getTitle())));
        onView(withId(R.id.eventCreatorField)).check(matches(withText(event.getCreator().getName())));
        onView(withId(R.id.eventAddressField)).check(matches(withText(event.getAddress())));
        onView(withId(R.id.eventTimeField)).check(matches(withText(event.getDateTime().toString())));
        onView(withId(R.id.eventParticipantsField)).check(matches(withText(s)));
        onView(withId(R.id.eventDescriptionField)).check(matches(withText(event.getDescription())));
    }

    @Test
    public void loadNullEvent() {
        Intent intent = new Intent();
        eventPageRule.launchActivity(intent);

        onView(withId(R.id.title)).check(matches(withText(R.string.event_not_found)));
    }
}
