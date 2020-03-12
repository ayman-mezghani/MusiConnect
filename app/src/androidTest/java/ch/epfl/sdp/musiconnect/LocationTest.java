package ch.epfl.sdp.musiconnect;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
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


    private void allowPermissionsIfNeeded() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && testPermissionsGranted()) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Cannot execute Thread.sleep()");
                }
                UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
                UiObject allowPermissions = device.findObject(new UiSelector()
                        .clickable(true)
                        .checkable(false)
                        .index(1));
                if (allowPermissions.exists()) {
                    allowPermissions.click();
                }
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


    private boolean testPermissionsGranted() {
        Context context = InstrumentationRegistry.getTargetContext();
        int permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        return (permissionStatus == PackageManager.PERMISSION_GRANTED);
    }

    @Test
    public void testSetFakeLocationReturnsRight() {
        allowPermissionsIfNeeded();
        if (testPermissionsGranted()) {

            Location l = new Location("Test");
            l.setLongitude(10.0);
            l.setLatitude(30.0);

            mRule.getActivity().setLocation(l);

            Location loc = mRule.getActivity().getLocation();

            assert(correctLocation(loc));
            assertThat(l.getLatitude(), is(loc.getLatitude()));
            assertThat(l.getLongitude(), is(loc.getLongitude()));
        }
    }


    @Test
    public void testRequestPermissionResultGranted() {
        allowPermissionsIfNeeded();
        int[] results = new int[1];
        results[0] = PackageManager.PERMISSION_GRANTED;
        mRule.getActivity().onRequestPermissionsResult(99, null, results);

        assert(testPermissionsGranted());
    }
}
