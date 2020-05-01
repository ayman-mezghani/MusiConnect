package ch.epfl.sdp.musiconnect;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;


public class MapsLocationTest {


    /**
     * Code to click on the alerts has been found here:
     * https://gist.github.com/rocboronat/65b1187a9fca9eabfebb5121d818a3c4
     */

    // Do not delete yet
    private static boolean hasNeededPermission() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        int permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
    }

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