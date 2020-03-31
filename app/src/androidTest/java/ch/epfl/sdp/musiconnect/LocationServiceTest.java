package ch.epfl.sdp.musiconnect;


import android.content.Intent;
import android.os.IBinder;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.rule.ServiceTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class LocationServiceTest {

    @Rule
    public final ServiceTestRule serviceRule =
            new ServiceTestRule();



    @Test
    public void testNullBinder() {
        Intent serviceIntent =
                new Intent(ApplicationProvider.getApplicationContext(), LocationService.class);
        IBinder binder = null;
        try {
            binder = serviceRule.bindService(serviceIntent);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        assertTrue(binder == null);
    }
}
