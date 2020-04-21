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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;

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
        Musician m1 = new Musician("Alice", "Bardon", "Alyx", "alyx92@gmail.com", new MyDate(1992, 9, 20));
        Musician m2 = new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25));
        Musician m3 = new Musician("Carson", "Calme", "CallmeCarson", "callmecarson41@gmail.com", new MyDate(1995, 4, 1));

        testMusician(m1);
        testMusician(m2);
        testMusician(m3);
    }

    private void testMusician(Musician m) {
        Intent intent = new Intent();
        intent.putExtra("UserEmail", m.getEmailAddress());
        visitorActivityTestRule.launchActivity(intent);

        onView(withId(R.id.visitorProfileFirstname)).check(matches(withText(m.getFirstName())));
        onView(withId(R.id.visitorProfileLastname)).check(matches(withText(m.getLastName())));
        onView(withId(R.id.visitorProfileUsername)).check(matches(withText(m.getUserName())));
        onView(withId(R.id.visitorProfileEmail)).check(matches(withText(m.getEmailAddress())));
        onView(withId(R.id.visitorProfileBirthday)).check(matches(withText(m.getBirthday().toString())));

        visitorActivityTestRule.finishActivity();
    }

    @Test
    public void loadNoProfile() {
        Intent intent = new Intent();
        visitorActivityTestRule.launchActivity(intent);

        onView(withId(R.id.visitorProfileTitle)).check(matches(withText("Profile not found...")));
    }

    @Test
    public void loadNullProfile() {
        Intent intent = new Intent();
        intent.putExtra("Username", (String) null);
        visitorActivityTestRule.launchActivity(intent);

        onView(withId(R.id.visitorProfileTitle)).check(matches(withText("Profile not found...")));
    }

    @Test
    public void testNonExistentProfile() {
        Intent intent = new Intent();
        intent.putExtra("Username", "NotAUsername");
        visitorActivityTestRule.launchActivity(intent);

        onView(withId(R.id.visitorProfileTitle)).check(matches(withText("Profile not found...")));
    }
}
