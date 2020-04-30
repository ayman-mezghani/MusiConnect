package ch.epfl.sdp.musiconnect;

import android.app.Notification;

import androidx.core.app.NotificationCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class MapsActivityTest {
    @Rule
    public final ActivityTestRule<MapsActivity> mapsActivityRule =
            new ActivityTestRule<>(MapsActivity.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase());
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

    @Test
    public void notificationChannelGeneratesCorrectly() {
        assertTrue(mapsActivityRule.getActivity().createNotificationChannel());
    }

    @Test
    public void alertWarningGeneratesCorrectly(){
        NotificationCompat.Builder notif = mapsActivityRule.getActivity().buildNotification("test");
        assertEquals(NotificationCompat.PRIORITY_MAX,notif.getPriority());
    }
}