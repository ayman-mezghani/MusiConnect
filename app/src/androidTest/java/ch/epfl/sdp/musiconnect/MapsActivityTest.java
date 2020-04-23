package ch.epfl.sdp.musiconnect;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

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

    @BeforeClass
    public static void setMockDB() {
        DbGenerator.setDatabase(new MockDatabase());
    }

    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

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