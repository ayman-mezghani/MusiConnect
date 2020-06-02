package ch.epfl.sdp.musiconnect.pages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.BandProfile;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.Notifications;
import ch.epfl.sdp.musiconnect.TypeOfUser;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.DbDataType;
import ch.epfl.sdp.musiconnect.events.EventCreationPage;
import ch.epfl.sdp.musiconnect.events.EventListPage;
import ch.epfl.sdp.musiconnect.finder.FinderPage;
import ch.epfl.sdp.musiconnect.location.MapsActivity;

import static ch.epfl.sdp.musiconnect.pages.StartPage.test;

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
    Menu main_menu;

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

        updateCurrentUser(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (!test) {
            if (!CurrentUser.getInstance(this).getCreatedFlag() && this.getClass() != UserCreationPage.class) {
                startActivity(new Intent(this, GoogleLoginPage.class));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("GPSLocationUpdates"));

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateCurrentUser(this);
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
                    sendToDatabase(location);
                    if (!test && isUserClose(location))
                        sendNotificationToMusician(Notifications.MUSICIAN_CHANNEL, NotificationCompat.PRIORITY_DEFAULT);
                }
            }
        }
    };

    protected void sendToDatabase(Location location) {
        CurrentUser.getInstance(getApplicationContext()).setLocation(location);
        DbSingleton.getDbInstance().update(DbDataType.Musician, CurrentUser.getInstance(getApplicationContext()).getMusician());
    }


    public boolean isUserClose(Location loc) {
        helper();
        for (Map.Entry<String, Location> val: userLocations.entrySet())
            if (loc.distanceTo(val.getValue()) < DISTANCE_LIMIT)
                return true;
        return false;
    }

    public void sendNotificationToMusician(String channel, int priority) {
        if (!notificationMessages.contains(notificationMessage)) {
            notifications.sendNotification(channel, getApplicationContext(), notificationMessage, priority);
            notificationMessages.add(notificationMessage);
        }
    }

    /**
     * Helper method to provide temporary dummy user locations
     */
    public void helper() {
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
        main_menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent homeIntent = new Intent(this, StartPage.class);
                this.startActivity(homeIntent);
                break;
            case R.id.my_profile:
                Intent profileIntent = new Intent(this, MyProfilePage.class);
                this.startActivity(profileIntent);
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
                Intent createEventIntent = new Intent(this, EventCreationPage.class);
                this.startActivity(createEventIntent);
                break;
            case R.id.signout:
                signOut();
                break;
            case R.id.my_profileBand:
                this.startActivity(new Intent(this, BandProfile.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        if(CurrentUser.getInstance(this).getBands() != null) {      // if user is member of a band
            MenuItem bandItem = menu.findItem(R.id.my_profileBand); // then show a menu item to bands profile
            if(bandItem != null)
                bandItem.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    protected void displayNotFinishedFunctionalityMessage() {
        Toast.makeText(this, getString(R.string.not_yet_done), Toast.LENGTH_SHORT).show();
    }

    protected void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    CurrentUser.flush();
                    Intent loginIntent = new Intent(this, GoogleLoginPage.class);
                    this.startActivity(loginIntent);
                    finish();
                });
    }

    public void updateCurrentUser(Context ctx) {
        DbAdapter db = DbSingleton.getDbInstance();
        db.read(DbDataType.Musician, CurrentUser.getInstance(ctx).email, new DbCallback() {
            @Override
            public void readCallback(User user) {
                CurrentUser.getInstance(ctx).setMusician((Musician) user);
                if(((Musician) user).getTypeOfUser() == TypeOfUser.Band) {
                    db.read(DbDataType.Band, CurrentUser.getInstance(ctx).email, new DbCallback() {
                        @Override
                        public void readCallback(User u) {
                        CurrentUser.getInstance(ctx).setBand((Band) u);
                        }
                    });
                }
                getBandIfMember();
            }
        });
    }

    public void getBandIfMember() {
        HashMap<String, Object> h = new HashMap<>();
        h.put("members", CurrentUser.getInstance(this).getMusician().getEmailAddress());
        DbSingleton.getDbInstance().query(DbDataType.Band, h, new DbCallback() {
            @Override
            public void queryCallback(List userList) {
                List<Band> b = new ArrayList<>();
                for (Object u: userList) {
                    b.add((Band) u);
                }
                CurrentUser.getInstance(Page.this).setBands(b);
            }
        });
    }
}