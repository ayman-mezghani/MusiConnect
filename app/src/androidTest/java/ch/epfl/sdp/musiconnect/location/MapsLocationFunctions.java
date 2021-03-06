package ch.epfl.sdp.musiconnect.location;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;


/**
 * This class contains methods to click on the alerts for location permission
 * There is unfortunately no way to avoid showing the alerts
 * We can directly grant location permissions, but those alerts still show up
 */
public class MapsLocationFunctions {

    /**
     * Code to click on the alerts has been found here:
     * https://gist.github.com/rocboronat/65b1187a9fca9eabfebb5121d818a3c4
     */
    private static void clickAlertOk(UiDevice device) {
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
     * Clicks on the alert to simply dismiss the dialog
     * (The location permission alert dialog still shows up even if we grant the permission via @Rule)
     */
    public static void clickPermissionAlert() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        clickAlertOk(device);
    }

    /**
     * Clicks on the alert boxes such that location permissions are given
     */
    static void clickAllow() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        clickAlertOk(device);
        clickOnDialog(device, 1);
    }

    /**
     * Clicks on the alert boxes such that location permissions are rejected
     */
    static void clickDeny() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        clickAlertOk(device);
        clickOnDialog(device, 0);
    }
}