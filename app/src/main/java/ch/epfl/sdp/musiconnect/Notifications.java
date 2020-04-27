package ch.epfl.sdp.musiconnect;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import ch.epfl.sdp.R;

@SuppressLint("Registered")
public class Notifications extends AppCompatActivity {

    public static final String MUSICIAN_CHANNEL = "musician_channel";
    private static final String MANAGER_CHANNEL = "manager_channel";

    private void createNotificationChannels() {
        // Oreo API lvl 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Musicians and band with default importance
            NotificationChannel mChannel = new NotificationChannel(
                    MUSICIAN_CHANNEL,
                    "Musician Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            mChannel.setDescription("Notification channel used for musicians and bands.");

            // Event managers with high importance
            NotificationChannel eChannel = new NotificationChannel(
                    MANAGER_CHANNEL,
                    "Event Manager Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            mChannel.setDescription("Notification channel used for event managers.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(mChannel);
            manager.createNotificationChannel(eChannel);
        }
    }

    /**
     * Send a notification
     * @param channel: channel to send the notification on: Musician or Manager.
     * @param id: default = 0
     * @param context: current calling activity context
     * @param priority: priority level (DEFAULT for standard user, HIGH for Manager)
     */
    protected void sendNotification(String channel, int id, Context context, int priority) {
        createNotificationChannels();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle("MusiConnect Notification")
                .setContentText("A musician is nearby !")
                .setPriority(priority);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, builder.build());
    }
}