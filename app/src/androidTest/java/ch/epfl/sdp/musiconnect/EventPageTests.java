package ch.epfl.sdp.musiconnect;


import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.dummies.DummyEvent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EventPageTests {
    @Rule
    public final ActivityTestRule<EventPage> eventPageRule =
            new ActivityTestRule<>(EventPage.class, true, false);

    @Test
    public void loadPageShouldShowCorrectEvent() {
        DummyEvent de = new DummyEvent();
        Event event = de.getEvent(0);

        Intent intent = new Intent();
        intent.putExtra("EID", 0);
        eventPageRule.launchActivity(intent);

        String name = event.getCreator().getName();

        onView(withId(R.id.eventTitle)).check(matches(withText(event.getTitle())));
        onView(withId(R.id.eventCreatorField)).check(matches(withText(event.getCreator().getName())));
        onView(withId(R.id.eventLocationField)).check(matches(withText(event.getAddress())));
        onView(withId(R.id.eventTimeField)).check(matches(withText(event.getDateTime().toString())));
        onView(withId(R.id.eventDescriptionField)).check(matches(withText(event.getMessage())));
    }

    @Test
    public void loadNullEvent() {
        Intent intent = new Intent();
        intent.putExtra("EID", -1);
        eventPageRule.launchActivity(intent);

        onView(withId(R.id.title)).check(matches(withText("Event not found...")));
    }


}
