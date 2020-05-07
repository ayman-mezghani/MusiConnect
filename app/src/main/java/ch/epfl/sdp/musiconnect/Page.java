package ch.epfl.sdp.musiconnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;
import ch.epfl.sdp.musiconnect.events.EventCreation;
import ch.epfl.sdp.musiconnect.events.EventListPage;
import ch.epfl.sdp.musiconnect.location.MapsActivity;

import static ch.epfl.sdp.musiconnect.StartPage.test;

public abstract class Page extends AppCompatActivity {

    protected GoogleSignInClient mGoogleSignInClient;
    protected GoogleSignInOptions gso;

    // NOTIFICATION HELPER VARIABLES
    private Notifications notifications;
    private String notificationMessage;
    protected static int DISTANCE_LIMIT = 200;
    protected Map<String, Location> userLocations;
    public static List<String> notificationMessages;
    Location l1, l2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        notifications = new Notifications(this);
        notificationMessage = "A musician is within " + DISTANCE_LIMIT + " meters";
        notificationMessages = new ArrayList<>();
        userLocations = new HashMap<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (!test) {
            if (!CurrentUser.getInstance(this).getCreatedFlag() && this.getClass() != UserCreation.class) {
                startActivity(new Intent(this, GoogleLogin.class));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("GPSLocationUpdates"));

        Context ctx = this;
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (!test)
            updateCurrentUserBand(this);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        super.onPause();
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getBundleExtra("Location");
            Location location;
            if (b != null) {
                location = b.getParcelable("Location");
                if (location != null) {
                    if (!test && isUserClose(location))
                        sendNotificationToMusician(Notifications.MUSICIAN_CHANNEL, NotificationCompat.PRIORITY_DEFAULT);
                }
            }
        }
    };

    public boolean isUserClose(Location loc) {
        helper();
        for (Map.Entry<String, Location> val: userLocations.entrySet())
            if (loc.distanceTo(val.getValue()) < DISTANCE_LIMIT)
                return true;
        return false;
    }

    protected void sendNotificationToMusician(String channel, int priority) {
        if (!notificationMessages.contains(notificationMessage)) {
            notifications.sendNotification(channel, getApplicationContext(), notificationMessage, priority);
            notificationMessages.add(notificationMessage);
        }
    }

    /**
     * Helper method to provide temporary dummy user locations
     */
    protected void helper() {
        l1 = new Location("User A");
        l1.setLatitude(46.517084);
        l1.setLongitude(6.565630);
        l2 = new Location("User B");
        l2.setLatitude(46.521391);
        l2.setLongitude(6.550472);
        userLocations.put("User A", l1);
        userLocations.put("User B", l2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_profile:
                Intent profileIntent = new Intent(this, MyProfilePage.class);
                this.startActivity(profileIntent);
                break;
            case R.id.settings:
                Intent settingsIntent = new Intent(this, SettingsPage.class);
                this.startActivity(settingsIntent);
                break;
            case R.id.help:
                Intent helpIntent = new Intent(this, HelpPage.class);
                this.startActivity(helpIntent);
                break;
            case R.id.search:
                Intent searchIntent = new Intent(this, FinderPage.class);
                this.startActivity(searchIntent);
                break;
            case R.id.map:
                Intent mapsIntent = new Intent(this, MapsActivity.class);
                this.startActivity(mapsIntent);
                break;
            case R.id.my_events:
                Intent eventIntent = new Intent(this, EventListPage.class);
                this.startActivity(eventIntent);
                break;
            case R.id.create_event:
                Intent createEventIntent = new Intent(this, EventCreation.class);
                this.startActivity(createEventIntent);
                break;
            case R.id.signout:
                signOut();
                break;
            default:
                displayNotFinishedFunctionalityMessage();
                return false;
        }
        return true;
    }

    protected void displayNotFinishedFunctionalityMessage() {
        Toast.makeText(this, getString(R.string.not_yet_done), Toast.LENGTH_SHORT).show();
    }

    protected void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    CurrentUser.flush();
                    startActivity(new Intent(Page.this, GoogleLogin.class));
                    finish();
                });
    }

    public static void updateCurrentUserBand(Context ctx) {
        if(CurrentUser.getInstance(ctx).getTypeOfUser() == TypeOfUser.Band) {
            DbGenerator.getDbInstance().read(DbUserType.Band, CurrentUser.getInstance(ctx).email, new DbCallback() {
                @Override
                public void readCallback(User u) {
                    CurrentUser.getInstance(ctx).setBand((Band) u);
                }
            });
        }
    }
}