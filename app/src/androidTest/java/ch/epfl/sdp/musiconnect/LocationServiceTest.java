package ch.epfl.sdp.musiconnect;


import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ServiceTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertThat;
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

    private boolean correctLocation(Location location) {
        if (location != null) {
            return (location.getLatitude() < 90.0) && (location.getLatitude() > -90.0) &&
                    (location.getLongitude() < 180.0) && (location.getLongitude() > -180.0);
        }
        return false;
    }

    @Test
    public void test() {
        boolean c;
        BroadcastReceiver messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getBundleExtra("Location");
                setResultExtras(b);
            }
        };

        Bundle bundle = messageReceiver.getResultExtras(true);
        Location loc = bundle.getParcelable("Location");
        assertTrue(loc == null);

        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bundle = messageReceiver.getResultExtras(true);
        loc = bundle.getParcelable("Location");
        assertTrue(correctLocation(loc));
    }


}
