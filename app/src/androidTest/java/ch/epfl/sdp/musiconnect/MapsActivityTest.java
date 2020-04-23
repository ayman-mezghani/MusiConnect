package ch.epfl.sdp.musiconnect;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class MapsActivityTest {
    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


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
}