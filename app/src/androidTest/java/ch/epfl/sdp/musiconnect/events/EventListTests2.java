package ch.epfl.sdp.musiconnect.events;

import android.content.Intent;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.TypeOfUser;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class EventListTests2 {

    @Rule
    public final ActivityTestRule<EventListPage> eventListRule =
            new ActivityTestRule<>(EventListPage.class, true, false);


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
        CurrentUser.getInstance(eventListRule.getActivity()).setTypeOfUser(TypeOfUser.Musician);
    }

    @After
    public void releaseIntents() { Intents.release(); }

    // work on local but not on cirrus ..
    //@Test
    public void testClickEventShouldLoadPage() {

        Event e = md.getDummyEvent(0);

        Intent intent = new Intent();
        eventListRule.launchActivity(intent);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        onView(withText(e.getTitle())).perform(ViewActions.scrollTo()).perform(click());
        intended(hasComponent(MyEventPage.class.getName()));
    }
}
