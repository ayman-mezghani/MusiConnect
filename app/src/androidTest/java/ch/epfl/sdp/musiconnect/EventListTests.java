package ch.epfl.sdp.musiconnect;


import android.content.Intent;

import androidx.test.espresso.action.ViewActions;
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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class EventListTests {

    @Rule
    public final ActivityTestRule<EventListPage> eventListRule =
            new ActivityTestRule<>(EventListPage.class, true, false);

    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase());
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

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
        eventListRule.launchActivity(intent);
        openActionsMenu(R.string.my_events);

        intended(hasComponent(EventListPage.class.getName()));
    }


    @Test
    public void testEventListTitle() {
        Intent intent = new Intent();
        eventListRule.launchActivity(intent);


        onView(withId(R.id.eventListTitle)).check(matches(withText("Your events")));
    }

    @Test
    public void testEventListTitleOfOtherUser() {
        Musician m = new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25));

        Intent intent = new Intent();
        intent.putExtra("UserEmail", m.getEmailAddress());
        eventListRule.launchActivity(intent);


        onView(withId(R.id.eventListTitle)).check(matches(withText(m.getName() + "'s events")));
    }


    @Test
    public void testClickEventShouldLoadPage() {
        Intent intent = new Intent();
        eventListRule.launchActivity(intent);
        onView(withText("Event")).perform(ViewActions.scrollTo()).perform(click());
        intended(hasComponent(EventPage.class.getName()));
    }

}
