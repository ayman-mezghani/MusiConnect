package ch.epfl.sdp.musiconnect;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

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
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class MapsLocationTest {


    @Rule
    public final ActivityTestRule<MapsActivity> mRule =
            new ActivityTestRule<>(MapsActivity.class);

    private UiDevice device;

    @BeforeClass
    public static void set() {
        Looper.prepare();
    }

    @Before
    public void setUp(){
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }


    private static boolean hasNeededPermission() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        int permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
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

    private void sendMessageToActivity(Location l) {
        Intent intent = new Intent("GPSLocationUpdates");
        Bundle b = new Bundle();
        b.putParcelable("Location", l);
        intent.putExtra("Location", b);
        LocalBroadcastManager.getInstance(InstrumentationRegistry.getInstrumentation().getContext())
                .sendBroadcast(intent);
    }


    /**
     * Clicks on the alert boxes such that location permissions are given
     */
    private void clickAllow() {
        clickAlert();
        allowPermissionsIfNeeded();
    }

    /**
     * Clicks on the alert boxes such that location permissions are rejected
     */
    private void clickDeny() {
        clickAlert();
        denyPermissionsIfNeeded();
    }

    private int[] grantedPerm() {
        int[] results = new int[1];
        results[0] = PackageManager.PERMISSION_GRANTED;
        return results;
    }

    private int[] deniedPerm() {
        int[] results = new int[1];
        results[0] = PackageManager.PERMISSION_DENIED;
        return results;
    }

    @Test
    public void testGetLocationReturnsRight() {
        clickAllow();

        Task<Location> task = mRule.getActivity().getTaskLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    assertTrue(correctLocation(location));
                } else { // location will be null if the location services are not available...
                    assertTrue(location == null);
                }
            }
        });
    }


    /**
     * This test works only if the user rejected the location permissions
     */
    @Test
    public void testGetLocationFails() {
        if (!hasNeededPermission()) {
            clickDeny();
            Task<Location> task = mRule.getActivity().getTaskLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    assertTrue(location == null);
                }
            });
        }
    }


    @Test
    public void testRequestPermissionResultGranted() {
        clickAllow();
        int[] results = grantedPerm();
        mRule.getActivity().onRequestPermissionsResult(LocationService.MY_PERMISSIONS_REQUEST_LOCATION, null, results);

        boolean b = mRule.getActivity().isLocationPermissionGranted();
        assertTrue(b);
    }

    @Test
    public void testRequestPermissionResultDenied() {
        clickDeny();
        int[] results = deniedPerm();
        mRule.getActivity().onRequestPermissionsResult(LocationService.MY_PERMISSIONS_REQUEST_LOCATION, null, results);
        boolean b = mRule.getActivity().isLocationPermissionGranted();
        assertTrue(!b);

        mRule.getActivity().onRequestPermissionsResult(0, null, results);
        b = mRule.getActivity().isLocationPermissionGranted();
        assertTrue(!b);
    }

    @Test
    public void testRequestPermissionResultIgnored() {
        clickDeny();
        int[] results = new int[0];
        mRule.getActivity().onRequestPermissionsResult(LocationService.MY_PERMISSIONS_REQUEST_LOCATION, null, results);
        boolean b = mRule.getActivity().isLocationPermissionGranted();
        assertTrue(!b);
    }


    @Test
    public void testMessageReceiver() {
        Location location = new Location("Test");
        location.setLatitude(0);
        location.setLongitude(0);
        sendMessageToActivity(location);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Location loc = mRule.getActivity().getSetLocation();
        assertTrue(correctLocation(loc));
        assertTrue(loc.getLatitude() == location.getLatitude());

    }
}
