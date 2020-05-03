package ch.epfl.sdp.musiconnect;

import android.os.Looper;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;

import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;


@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class MapsLocationTest {


    @Rule
    public final ActivityTestRule<MapsActivity> mRule =
            new ActivityTestRule<>(MapsActivity.class);



    @BeforeClass
    public static void set() {
        Looper.prepare();
    }

    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase());
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

    /**
     * Code to click on the alerts has been found here:
     * https://gist.github.com/rocboronat/65b1187a9fca9eabfebb5121d818a3c4
     */

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
}