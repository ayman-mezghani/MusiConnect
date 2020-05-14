package ch.epfl.sdp.musiconnect.location;


import androidx.core.app.NotificationCompat;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.google.android.gms.maps.model.LatLng;

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
import ch.epfl.sdp.musiconnect.events.EventCreationPage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.testsFunctions.waitSeconds;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MapsActivityTest {
    @Rule
    public final ActivityTestRule<MapsActivity> mapsActivityRule =
            new ActivityTestRule<>(MapsActivity.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

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


    @Test
    public void notificationChannelGeneratesCorrectly() {
        assertTrue(mapsActivityRule.getActivity().createNotificationChannel());
    }

    @Test
    public void alertWarningGeneratesCorrectly(){
        NotificationCompat.Builder notif = mapsActivityRule.getActivity().buildNotification("test");
        assertEquals(NotificationCompat.PRIORITY_MAX,notif.getPriority());
    }

    @Test
    public void testEventAlertShowsAndCancels(){
        try {
            mapsActivityRule.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    mapsActivityRule.getActivity().createAlert(new LatLng(0,0));
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        onView(withText("Do you want to create an event?")).check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).perform(click());
        onView(withText("Do you want to create an event?")).check(doesNotExist());
    }

    @Test
    public void testEventAlertGoesToNewEventPage(){
        try {
            mapsActivityRule.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    mapsActivityRule.getActivity().createAlert(new LatLng(46.5253,6.5606));
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        onView(withId(android.R.id.button1)).perform(click());
        intended(hasComponent(EventCreationPage.class.getName()));
        onView(withId(R.id.eventCreationNewEventAddress)).check(matches(withText("Place du Motty 10, 1024 Ecublens, Switzerland")));
    }

}