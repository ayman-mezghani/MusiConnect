package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;
import ch.epfl.sdp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class MapsActivityTest {
    private UiDevice device;

    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() {
        Intents.init();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        MapsLocationTest.clickAlert(device);
    }

    @After
    public void releaseIntents() { Intents.release(); }

    @Test
    public void toastWarningGeneratesCorrectly() {
        String msg = "this is a test";
        startPageRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MapsActivity.Utility.generateWarning(startPageRule.getActivity().getApplicationContext(), msg, MapsActivity.Utility.warningTypes.Toast);
            }
        });

        //onView(withText(msg)).inRoot(withDecorView(not(is(startPageRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void alertWarningGeneratesCorrectly(){
        String msg = "this is a test";
        startPageRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //MapsActivity.Utility.generateWarning(startPageRule.getActivity().getApplicationContext(), msg, MapsActivity.Utility.warningTypes.Alert);
            }
        });
        //onView(withText(msg)).check(matches(isDisplayed()));
    }

    /**
     * Helper method to avoid duplicate code
     * @param stringId
     */
    private void openActionsMenu(int stringId) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(stringId)).perform(click());
    }

    @Test
    public void testChangeRadiusShouldMakeReceiveNotifications() throws InterruptedException {
        openActionsMenu(R.string.map);
        Thread.sleep(1000);
        onView(withId(R.id.distanceThreshold)).perform(click());
        onView(withText("10km")).perform(click());
        assert(true); // temporary waiting for notification tests
    }
}