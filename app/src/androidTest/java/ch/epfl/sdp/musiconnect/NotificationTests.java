package ch.epfl.sdp.musiconnect;

import android.content.Context;

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
import static ch.epfl.sdp.musiconnect.StartPage.*;
import static ch.epfl.sdp.musiconnect.testsFunctions.*;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NotificationTests {
    private UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant("android.permission.ACCESS_FINE_LOCATION");


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
    public void testCheckThatNotificationIsReceived() {
        String expectedTitle = getResourceString(R.string.musiconnect_notification);
        String expectedMessage = "A musician is within " + 100 + " meters";

        ((StartPage) Objects.requireNonNull(getCurrentActivity())).sendNotificationToMusician(
                MUSICIAN_CHANNEL, NotificationCompat.PRIORITY_DEFAULT
        );

        device.openNotification();
        device.wait(Until.hasObject(By.textStartsWith("MusiConnect")), 6000);
        UiObject2 title = device.findObject(By.textStartsWith(expectedTitle));
        UiObject2 message = device.findObject(By.textStartsWith(expectedMessage));

        assertEquals(expectedTitle, title.getText());
        assertEquals(expectedMessage, message.getText());
        clearAllNotifications();
    }
}