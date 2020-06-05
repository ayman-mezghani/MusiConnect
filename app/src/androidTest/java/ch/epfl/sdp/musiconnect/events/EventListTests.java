package ch.epfl.sdp.musiconnect.events;


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
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.users.Musician;
import ch.epfl.sdp.musiconnect.UserType;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.testsFunctions.waitSeconds;

@RunWith(AndroidJUnit4.class)
public class EventListTests {
    private final static String failCallback = "FailCallback";


    @Rule
    public final ActivityTestRule<EventListPage> eventListRule =
            new ActivityTestRule<>(EventListPage.class, true, false);


    private static MockDatabase md;

    @BeforeClass
    public static void setMocks() {
        md = new MockDatabase(false);
        DbSingleton.setDatabase(md);
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }



    @Before
    public void initIntents() {
        Intents.init();
        CurrentUser.getInstance(eventListRule.getActivity()).setTypeOfUser(UserType.Musician);
    }

    @After
    public void releaseIntents() { Intents.release(); }

    @Test
    public void testEventListTitle() {
        CurrentUser.getInstance(eventListRule.getActivity()).setTypeOfUser(UserType.Musician);

        Intent intent = new Intent();
        eventListRule.launchActivity(intent);

        onView(withId(R.id.eventListTitle)).check(matches(withText("Your events")));
    }

    @Test
    public void testEventListTitleOfOtherUser() {
        Musician m = md.getDummyMusician(1);

        Intent intent = new Intent();
        intent.putExtra("UserEmail", m.getEmailAddress());
        eventListRule.launchActivity(intent);

        onView(withId(R.id.eventListTitle)).check(matches(withText(m.getName() + "'s events")));
    }

    @Test
    public void testClickEventShouldLoadPage() {
        CurrentUser.getInstance(eventListRule.getActivity()).setTypeOfUser(UserType.Musician);

        Event e = md.getDummyEvent(0);

        Intent intent = new Intent();
        eventListRule.launchActivity(intent);

        waitSeconds(3);

        onView(withText(e.getTitle())).perform(ViewActions.scrollTo()).perform(click());
        intended(hasComponent(MyEventPage.class.getName()));
    }

    @Test
    public void testNoEventsToShow() {
        Intent intent = new Intent();
        intent.putExtra("UserEmail", failCallback);
        eventListRule.launchActivity(intent);
        waitSeconds(3);
        onView(withId(R.id.eventListNone)).check(matches(withText("This user has no events to show...")));
    }

    @Test
    public void testEventListContent() {
        CurrentUser.getInstance(eventListRule.getActivity()).setTypeOfUser(UserType.Musician);

        Intent intent = new Intent();
        eventListRule.launchActivity(intent);

        waitSeconds(3);

        Event e1 = md.getDummyEvent(0);
        Event e6 = md.getDummyEvent(5);

        onView(withText(e1.getTitle())).check(matches(isDisplayed()));
        onView(withText(e6.getTitle())).check(matches(isDisplayed()));
    }

    @Test
    public void testClickOthersEventShouldLoadPage() {
        Event e = md.getDummyEvent(1);
        Musician m = md.getDummyMusician(1);

        Intent intent = new Intent();
        intent.putExtra("UserEmail", m.getEmailAddress());
        eventListRule.launchActivity(intent);

        waitSeconds(3);

        onView(withText(e.getTitle())).perform(ViewActions.scrollTo()).perform(click());
        intended(hasComponent(VisitorEventPage.class.getName()));
    }


    @Test
    public void testListShouldOnlyShowPublicOrParticipant() {
        CurrentUser.getInstance(eventListRule.getActivity()).setTypeOfUser(UserType.Musician);
        Event e3 = md.getDummyEvent(2);
        Event e6 = md.getDummyEvent(5);
        Musician m = md.getDummyMusician(2);

        Intent intent = new Intent();
        intent.putExtra("UserEmail", m.getEmailAddress());
        eventListRule.launchActivity(intent);

        waitSeconds(3);

        onView(withText(e3.getTitle())).check(matches(isDisplayed()));
        onView(withText(e6.getTitle())).check(matches(isDisplayed()));
    }
}
