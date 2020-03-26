package ch.epfl.sdp.musiconnect;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import ch.epfl.sdp.R;

import static android.content.Context.ACTIVITY_SERVICE;

public class LocationPermission {


    static void startLocationService(Activity activity) {
        if (!isLocationServiceRunning(activity)) {
            Intent serviceIntent = new Intent(activity, LocationService.class);
            activity.startService(serviceIntent);
        }
    }

    private static boolean isLocationServiceRunning(Activity activity) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(ACTIVITY_SERVICE);
        String className = "ch.epfl.sdp.musiconnect.LocationService";

        if (manager == null) {
            return false;
        }

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(className.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



    static void sendLocationPermission(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            new AlertDialog.Builder(activity)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LocationService.MY_PERMISSIONS_REQUEST_LOCATION);
                    })
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();


        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LocationService.MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }


    static boolean onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != LocationService.MY_PERMISSIONS_REQUEST_LOCATION) {
            return false;
        }

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, activity.getString(R.string.perm_granted), Toast.LENGTH_LONG)
                    .show();
            return true;
        } else {
            Toast.makeText(activity, activity.getString(R.string.perm_denied), Toast.LENGTH_LONG)
                    .show();
            return false;
        }
    }
}
