package ch.epfl.sdp.musiconnect;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.core.internal.deps.guava.collect.Maps;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
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

import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;


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
        mRule.getActivity().updateLastLocation();
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
    public void testGetLocationReturnsRight() {
        clickAlert();
        allowPermissionsIfNeeded();


        Location loc = mRule.getActivity().getLocation();
        assert(correctLocation(loc));

        Location loc2 = mRule.getActivity().getLocation();
        assert(correctLocation(loc2));
        assertThat(loc.getLatitude(), is(loc2.getLatitude()));
        assertThat(loc.getLongitude(), is(loc2.getLongitude()));
    }

    @Test
    public void testGetLocationFails() {
        clickAlert();
        denyPermissionsIfNeeded();

        Location loc = mRule.getActivity().getLocation();
        assert(loc == null);
    }

    @Test
    public void testRequestPermissionResultGranted() {
        clickAlert();
        allowPermissionsIfNeeded();
        int[] results = new int[1];
        results[0] = PackageManager.PERMISSION_GRANTED;
        mRule.getActivity().onRequestPermissionsResult(MapsActivity.MY_PERMISSIONS_REQUEST_LOCATION, null, results);
        mRule.getActivity().getToast().getView().isShown();
        assert(mRule.getActivity().getToast().getView().isShown());
        // onView(withText(R.string.perm_granted)).inRoot(withDecorView(not(mRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void testRequestPermissionResultDenied() {
        clickAlert();
        denyPermissionsIfNeeded();
        int[] results = new int[1];
        results[0] = PackageManager.PERMISSION_DENIED;
        mRule.getActivity().onRequestPermissionsResult(MapsActivity.MY_PERMISSIONS_REQUEST_LOCATION, null, results);
        assert(mRule.getActivity().getToast().getView().isShown());
        // onView(withText(R.string.perm_denied)).inRoot(withDecorView(not(mRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void testRequestPermissionResultIgnored() {
        clickAlert();
        denyPermissionsIfNeeded();
        int[] results = new int[1];
        results[0] = PackageManager.PERMISSION_GRANTED;
        mRule.getActivity().onRequestPermissionsResult(0, null, results);
        assert(!mRule.getActivity().getToast().getView().isShown());

        int[] noResults = new int[0];
        mRule.getActivity().onRequestPermissionsResult(MapsActivity.MY_PERMISSIONS_REQUEST_LOCATION, null, noResults);
        assert(!mRule.getActivity().getToast().getView().isShown());
    }
}
