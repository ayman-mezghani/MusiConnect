package ch.epfl.sdp.musiconnect;

import android.util.Log;

public class AppStatus {
    private static Boolean test = null;

    public static boolean isInTest() {
        if (test == null) {
            try {
                Class.forName("androidx.test.espresso.Espresso");
                test = true;
            } catch (ClassNotFoundException e) {
                test = false;
            }
            Log.d("AppStatus", test.toString());

            return test;
        }
        else return test;
    }
}
