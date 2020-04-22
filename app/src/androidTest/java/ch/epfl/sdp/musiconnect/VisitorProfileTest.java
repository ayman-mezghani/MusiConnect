package ch.epfl.sdp.musiconnect;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.database.SimplifiedMusician;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class VisitorProfileTest {

    @Rule
    public final ActivityTestRule<VisitorProfilePage> visitorActivityTestRule = new ActivityTestRule<>(VisitorProfilePage.class,
            true, false);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    private UiDevice device;

    @BeforeClass
    public static void setMockDB() {
        DbGenerator.setDatabase(new MockDatabase());
    }

    /* @Test
    public void testClickMarker() {
        onView(withId(R.id.distanceThreshold)).perform(click());
        onView(withText(R.string.max_threshold)).perform(click());

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Alyx"));
        try {
            marker.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        int mWidth= startPageRule.getActivity().getResources().getDisplayMetrics().widthPixels;
        int mHeight= startPageRule.getActivity().getResources().getDisplayMetrics().heightPixels;
        device.click(mWidth/2, mHeight/2);

//         startPageRule.getActivity().findViewById(android.R.id.content).performContextClick(200, 200);

    }*/

    @Before
    public void initIntents() {
        Intents.init();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        MapsLocationTest.clickAlert(device);
    }

    @After
    public void releaseIntents() { Intents.release(); }

    @Test
    public void testNoMarkerTransitionToProfile() {
        Musician m = new Musician("Alice", "Bardon", "Alyx", "alyx92@gmail.com", new MyDate(1992, 9, 20));

        Intent intent = new Intent();
        intent.putExtra("FirstName", m.getFirstName());
        intent.putExtra("LastName", m.getLastName());
        intent.putExtra("UserName", m.getUserName());
        intent.putExtra("Email", m.getEmailAddress());
        int[] birthday = {m.getBirthday().getDate(), m.getBirthday().getMonth(), m.getBirthday().getYear()};
        intent.putExtra("Birthday", birthday);
        intent.putExtra("Test", true);
        visitorActivityTestRule.launchActivity(intent);



        onView(withId(R.id.firstname)).check(matches(withText("Alice")));
        onView(withId(R.id.username)).check(matches(withText("Alyx")));
    }

}
