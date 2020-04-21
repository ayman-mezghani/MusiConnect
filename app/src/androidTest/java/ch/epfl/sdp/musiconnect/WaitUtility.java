package ch.epfl.sdp.musiconnect;

import java.util.concurrent.TimeUnit;

public class WaitUtility {
    public static void waitALittle(int t) {
        try {
            TimeUnit.SECONDS.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
