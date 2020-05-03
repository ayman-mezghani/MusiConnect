package ch.epfl.sdp.musiconnect.location;


import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.musiconnect.HelpPage;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
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
