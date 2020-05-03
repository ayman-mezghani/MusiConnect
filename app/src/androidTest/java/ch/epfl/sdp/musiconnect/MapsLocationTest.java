package ch.epfl.sdp.musiconnect;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Rule;


public class MapsLocationTest {


    @Rule
    public final ActivityTestRule<MapsActivity> mRule =
            new ActivityTestRule<>(MapsActivity.class);


    /**
     * Code to click on the alerts has been found here:
     * https://gist.github.com/rocboronat/65b1187a9fca9eabfebb5121d818a3c4
     */

    static void clickAlert(UiDevice device) {
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

    private static void clickOnDialog(UiDevice device, int pos) {
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
    static void clickAllow() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        clickAlert(device);
        // clickOnDialog(device, 1);
    }

    /**
     * Clicks on the alert boxes such that location permissions are rejected
     */
    /*
    public static void clickDeny() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        clickAlert(device);
        clickOnDialog(device, 0);
    }
    */
}