package ch.epfl.sdp.musiconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.view.MotionEvent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;

import static android.view.MotionEvent.ACTION_DOWN;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class VisitorProfileTest {

    @Rule
    public final ActivityTestRule<MapsActivity> startPageRule = new ActivityTestRule<>(MapsActivity.class);

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
        Intent intent = new Intent(startPageRule.getActivity(), VisitorProfilePage.class);
        intent.putExtra("UserName", "Alyx");
        startPageRule.getActivity().startActivity(intent);

        onView(withId(R.id.firstname)).check(matches(withText("Alice")));
        onView(withId(R.id.username)).check(matches(withText("Alyx")));
    }

}
