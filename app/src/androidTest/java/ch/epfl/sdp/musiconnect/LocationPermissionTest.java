package ch.epfl.sdp.musiconnect;



import android.app.Activity;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class LocationPermissionTest {

    @Rule
    public final ActivityTestRule<HelpPage> rule =
            new ActivityTestRule<>(HelpPage.class);


    @Test
    public void test() {
        Activity activity = rule.getActivity();
        // assertFalse(LocationPermission.isLocationServiceRunning(activity));
        LocationPermission.startLocationService(activity);
        assertTrue(LocationPermission.isLocationServiceRunning(activity));
        LocationPermission.startLocationService(activity);
        assertTrue(LocationPermission.isLocationServiceRunning(activity));
    }
}
