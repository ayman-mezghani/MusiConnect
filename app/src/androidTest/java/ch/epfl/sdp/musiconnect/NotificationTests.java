package ch.epfl.sdp.musiconnect;

import android.content.Context;
import android.location.Location;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import androidx.core.app.NotificationCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;
import ch.epfl.sdp.R;

import static ch.epfl.sdp.musiconnect.Notifications.MUSICIAN_CHANNEL;
import static ch.epfl.sdp.musiconnect.Page.*;
import static ch.epfl.sdp.musiconnect.testsFunctions.*;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NotificationTests {
    private UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    Location l1, l2, l3;

    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant("android.permission.ACCESS_FINE_LOCATION");

    @Before
    public void initiateLocations() {
        l1 = new Location("User A");
        l2 = new Location("User B");
        l3 = new Location("User C");
    }

    @SuppressWarnings("unused")
    private void clearAllNotifications() {
        device.openNotification();
        device.wait(Until.hasObject(By.textStartsWith("MusiConnect")), 600);
        UiObject2 clearAll = device.findObject(By.textStartsWith("CLEAR ALL"));
        if (clearAll != null)
            clearAll.click();
    }

    /**
     * Get string value from strings.xml file
     * @param id: string id
     * @return: string value
     */
    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        return targetContext.getResources().getString(id);
    }

    @Test
    public void testSendNotificationOnceShouldReceiveOnce() {
        String expectedTitle = getResourceString(R.string.musiconnect_notification);
        String expectedMessage = "A musician is within " + DISTANCE_LIMIT + " meters";

        ((StartPage) Objects.requireNonNull(getCurrentActivity())).sendNotificationToMusician(
                MUSICIAN_CHANNEL, NotificationCompat.PRIORITY_DEFAULT
        );

        waitALittle(3);
        device.openNotification();
        device.wait(Until.hasObject(By.textStartsWith("MusiConnect")), 600);
        UiObject2 title = device.findObject(By.textStartsWith(expectedTitle));
        UiObject2 message = device.findObject(By.textStartsWith(expectedMessage));

        assertEquals(expectedTitle, title.getText());
        assertEquals(expectedMessage, message.getText());
        assertEquals(1, notificationMessages.size());
        clearAllNotifications();
    }

    @Test
    public void testSendSameNotificationMultipleTimesShouldReceiveOnlyOnce() {
        for (int i = 0; i < 3; ++i)
            ((StartPage) Objects.requireNonNull(getCurrentActivity())).sendNotificationToMusician(
                    MUSICIAN_CHANNEL, NotificationCompat.PRIORITY_DEFAULT
            );

        waitALittle(3);
        assertEquals(1, notificationMessages.size());
        clearAllNotifications();
    }

    private void initBigDistances() {
        l1.setLatitude(1);
        l1.setLongitude(1);
        l2.setLatitude(2);
        l2.setLongitude(2);
    }

    private void initSmallDistances() {
        l1.setLatitude(46.517083);
        l1.setLongitude(6.565630);
        l2.setLatitude(46.517084);
        l2.setLongitude(6.565630);
    }

    private void sendNotificationWhenDistanceIsRespected() {
        if (l1.distanceTo(l2) < DISTANCE_LIMIT)
            ((StartPage) Objects.requireNonNull(getCurrentActivity())).sendNotificationToMusician(
                    MUSICIAN_CHANNEL, NotificationCompat.PRIORITY_DEFAULT
            );
    }

    @Test
    public void testDoNotSendNotificationIfNoUsersAreClose() {
        initBigDistances();
        sendNotificationWhenDistanceIsRespected();
        assertEquals(0, notificationMessages.size());
    }

    @Test
    public void testSendNotificationIfUserIsWithinDistanceLimit() {
        initSmallDistances();
        sendNotificationWhenDistanceIsRespected();
        assertEquals(1, notificationMessages.size());
    }

    @Test
    public void testClickOnNotificationShouldOpenMapsActivity() {
        String expectedMessage = "A musician is within " + DISTANCE_LIMIT + " meters";

        ((StartPage) Objects.requireNonNull(getCurrentActivity())).sendNotificationToMusician(
                MUSICIAN_CHANNEL, NotificationCompat.PRIORITY_DEFAULT
        );

        waitALittle(3);
        device.openNotification();
        device.wait(Until.hasObject(By.textStartsWith(getResourceString(R.string.app_name))), 600);
        device.findObject(By.textStartsWith(expectedMessage)).click();
        device.wait(Until.hasObject(By.text(expectedMessage)), 600);

        device.pressBack();
        device.wait(Until.hasObject(By.text(getResourceString(R.string.app_name))), 600);
        assertEquals(1, notificationMessages.size());
    }
}