package ch.epfl.sdp.musiconnect;

import android.location.Location;
import android.os.Looper;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.rule.ActivityTestRule;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;



@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class LocationTest {


    @Rule
    public final ActivityTestRule<MapsActivity> mRule =
            new ActivityTestRule<>(MapsActivity.class);

    UiDevice device;

    @BeforeClass
    public static void set() {
        Looper.prepare();
    }

    @Before
    public void setUp(){
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    private void clickAlert() {
        try {
            UiObject alert = device.findObject(new UiSelector().className("android.widget.Button")
                    .text("OK"));

            if (alert.exists()) {
                alert.clickAndWaitForNewWindow();
            }
        } catch (UiObjectNotFoundException e) {
            System.out.println("There is no permissions dialog to interact with");
        }
    }

    private void allowPermissionsIfNeeded() {
        try {

            UiObject allowPermissions = device.findObject(new UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .index(1));
            if (allowPermissions.exists()) {
                allowPermissions.click();
            }

        } catch (UiObjectNotFoundException e) {
            System.out.println("There is no permissions dialog to interact with");
        }
    }


    private void denyPermissionsIfNeeded() {
        try {

                UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
                UiObject denyPermissions = device.findObject(new UiSelector()
                        .clickable(true)
                        .checkable(false)
                        .index(0));

                if (denyPermissions.exists()) {
                    denyPermissions.click();
                }

        } catch (UiObjectNotFoundException e) {
            System.out.println("There is no permissions dialog to interact with");
        }
    }


    private boolean correctLocation(Location location) {
        if (location != null) {
            return (location.getLatitude() < 90.0) && (location.getLatitude() > -90.0) &&
                    (location.getLongitude() < 180.0) && (location.getLongitude() > -180.0);
        }
        return false;
    }



    @Test
    public void testSetFakeLocationReturnsRight() {

        clickAlert();
        allowPermissionsIfNeeded();

        Location loc = mRule.getActivity().getLocation();
        assert(correctLocation(loc));

    }

    @Test
    public void testSetFakeLocationFails() {
        clickAlert();
        denyPermissionsIfNeeded();


        Location loc = mRule.getActivity().getLocation();
        assert(loc == null);

    }
}
