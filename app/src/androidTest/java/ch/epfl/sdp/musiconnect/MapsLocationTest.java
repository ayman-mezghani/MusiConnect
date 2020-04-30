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

import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class MapsLocationTest {


    @Rule
    public final ActivityTestRule<MapsActivity> mRule =
            new ActivityTestRule<>(MapsActivity.class);


    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase());
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

    /**
     * Code to click on the alerts has been found here:
     * https://gist.github.com/rocboronat/65b1187a9fca9eabfebb5121d818a3c4
     */

    private static boolean hasNeededPermission() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        int permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
    }

    public static void clickAlert(UiDevice device) {
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

    public static void clickOnDialog(UiDevice device, int pos) {
        try {
            UiObject allowPermissions = device.findObject(new UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .index(pos));

            if (allowPermissions.exists()) {
                allowPermissions.click();
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
    public static void clickAllow() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        clickAlert(device);
        // clickOnDialog(device, 1);
    }

    /**
     * Clicks on the alert boxes such that location permissions are rejected
     */
    public static void clickDeny() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        clickAlert(device);
        clickOnDialog(device, 0);
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