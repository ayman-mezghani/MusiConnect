package ch.epfl.sdp.musiconnect.location;


import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LocationPermissionTest {

    @Rule
    public final ActivityTestRule<MapsActivity> locationPermissionRule =
            new ActivityTestRule<>(MapsActivity.class);


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
    public void testIsLocationServiceRunning() {
        MapsLocationFunctions.clickAllow();
        Activity activity = locationPermissionRule.getActivity();
        // assertFalse(LocationPermission.isLocationServiceRunning(activity));
        LocationPermission.startLocationService(activity);
        assertTrue(LocationPermission.isLocationServiceRunning(activity));
        LocationPermission.startLocationService(activity);
        assertTrue(LocationPermission.isLocationServiceRunning(activity));
    }

    @Test
    public void testRequestPermissionResultGranted() {
        MapsLocationFunctions.clickAllow();
        Activity activity = locationPermissionRule.getActivity();
        activity.runOnUiThread(() -> {
            int[] results = grantedPerm();
            boolean b = LocationPermission.onRequestPermissionsResult(activity, LocationService.MY_PERMISSIONS_REQUEST_LOCATION, null, results);

            assertTrue(b);
        });

    }

    @Test
    public void testRequestPermissionResultDenied() {
        MapsLocationFunctions.clickDeny();
        Activity activity = locationPermissionRule.getActivity();

        activity.runOnUiThread(() -> {
            int[] results = deniedPerm();
            boolean b = LocationPermission.onRequestPermissionsResult(activity, LocationService.MY_PERMISSIONS_REQUEST_LOCATION, null, results);
            assertFalse(b);

            b = LocationPermission.onRequestPermissionsResult(activity, 0, null, results);
            assertFalse(b);
        });
    }
}
