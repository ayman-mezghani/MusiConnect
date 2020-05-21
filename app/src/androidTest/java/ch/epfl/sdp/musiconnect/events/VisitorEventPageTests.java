package ch.epfl.sdp.musiconnect.events;


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
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.location.MapsActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.testsFunctions.waitSeconds;

@RunWith(AndroidJUnit4.class)
public class VisitorEventPageTests {
    @Rule
    public final ActivityTestRule<VisitorEventPage> eventPageRule =
            new ActivityTestRule<>(VisitorEventPage.class, true, false);

    private static MockDatabase md;

    @BeforeClass
    public static void setMocks() {
        md = new MockDatabase(false);
        DbGenerator.setDatabase(md);
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }


    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() { Intents.release(); }


    @Test
    public void testLoadOtherEvent() {
        Musician m1 = md.getDummyMusician(1);
        Musician m2 = md.getDummyMusician(3);
        String s = m1.getName() + System.lineSeparator() + m2.getName() + System.lineSeparator();
        Event event = md.getDummyEvent(1);

        testMatchInfoOnView(event, s);
    }

    @Test
    public void testLoadOtherEventParticipating() {
        Musician current = md.getDummyMusician(0);
        Musician m1 = md.getDummyMusician(2);
        Musician m2 = md.getDummyMusician(3);
        String s = m1.getName() + System.lineSeparator() + m2.getName() + System.lineSeparator() + current.getName() + System.lineSeparator();
        Event event = md.getDummyEvent(4);

        testMatchInfoOnView(event, s);
    }

    private void testMatchInfoOnView(Event event, String s) {
        Intent intent = new Intent();
        intent.putExtra("eid", event.getEid());
        eventPageRule.launchActivity(intent);


        waitSeconds(3);

        onView(withId(R.id.eventTitle)).check(matches(withText(event.getTitle())));
        onView(withId(R.id.eventCreatorField)).check(matches(withText(event.getCreator().getName())));
        onView(withId(R.id.eventAddressField)).check(matches(withText(event.getAddress())));
        onView(withId(R.id.eventTimeField)).check(matches(withText(event.getDateTime().toString())));
        onView(withId(R.id.eventParticipantsField)).check(matches(withText(s)));
        onView(withId(R.id.eventDescriptionField)).check(matches(withText(event.getDescription())));
    }


    @Test
    public void loadPrivateEvent() {
        Intent intent = new Intent();
        Event event = md.getDummyEvent(3);
        intent.putExtra("eid", event.getEid());
        eventPageRule.launchActivity(intent);

        onView(withId(R.id.title)).check(matches(withText(R.string.event_is_private)));
    }


    @Test
    public void loadNullEvent() {
        Intent intent = new Intent();
        eventPageRule.launchActivity(intent);

        onView(withId(R.id.title)).check(matches(withText(R.string.event_not_found)));
    }

    @Test
    public void testMapButtonGoesToMap(){
        Event event = md.getDummyEvent(4);
        Intent intent = new Intent();
        intent.putExtra("eid", event.getEid());
        eventPageRule.launchActivity(intent);

        waitSeconds(3);

        onView(withId(R.id.toMap)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        waitSeconds(3);

        intended(hasComponent(MapsActivity.class.getName()));
    }

}
