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
        Musician m1 = new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25));
        m1.setLocation(new MyLocation(46.52, 6.52));

        Event event = new Event(m1, 0);
        event.setAddress("Westminster, London, England");
        event.setLocation(51.5007, 0.1245);
        event.setDateTime(new MyDate(2020, 9, 21, 14, 30));
        event.setTitle("Event at Big Ben!");
        event.setMessage("Playing at Big Ben, come watch us play!");

        Intent intent = new Intent();
        intent.putExtra("EID", 0);
        eventPageRule.launchActivity(intent);

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
