package ch.epfl.sdp.musiconnect;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.location.MapsActivity;

@SuppressLint("Registered")
public class Notifications {

    static final String MUSICIAN_CHANNEL = "musician_channel";
    private static final String MANAGER_CHANNEL = "manager_channel";
    private Context context;

    public Notifications(Context context) {
        this.context = context;
    }

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

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(mChannel);
            manager.createNotificationChannel(eChannel);
        }
    }

    /**
     * Send a notification
     * @param channel: channel to send the notification on: Musician or Manager.
     * @param context: current calling activity context
     * @param notificationMessage: main message displayed to user
     * @param priority: priority level (DEFAULT for standard user, HIGH for Manager)
     */
    public void sendNotification(String channel, Context context, String notificationMessage, int priority) {
        createNotificationChannels();

        // Open MapsActivity when a notification is clicked on
        Intent mapsIntent = new Intent(context, MapsActivity.class);
        PendingIntent mapsPendingIntent = PendingIntent.getActivity(
                context,
                1,
                mapsIntent,
                PendingIntent.FLAG_UPDATE_CURRENT // Updates Maps Activity if it is already running
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle("MusiConnect Notification")
                .setContentText(notificationMessage)
                .setPriority(priority)
                .setAutoCancel(true) // Notification disappears when notification is clicked on
                .setContentIntent(mapsPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());
    }
}