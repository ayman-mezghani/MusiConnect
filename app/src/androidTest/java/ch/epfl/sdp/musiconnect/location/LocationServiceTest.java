package ch.epfl.sdp.musiconnect.location;


import android.content.Intent;
import android.os.IBinder;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ServiceTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class LocationServiceTest {

    @Rule
    public final ServiceTestRule serviceRule =
            new ServiceTestRule();

    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase());
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

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

        assertNull(binder);
    }
}
