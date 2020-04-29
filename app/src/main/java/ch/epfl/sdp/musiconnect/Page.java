package ch.epfl.sdp.musiconnect;

import android.content.Intent;
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
import java.util.List;

import androidx.core.app.NotificationCompat;
import ch.epfl.sdp.R;

import static ch.epfl.sdp.musiconnect.StartPage.test;


public abstract class Page extends AppCompatActivity {

    protected GoogleSignInClient mGoogleSignInClient;
    protected GoogleSignInOptions gso;

    // HELPER VARIABLE
    private int DISTANCE = 100;
    private List<String> notificationMessages;

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

        notificationMessages = new ArrayList<>();
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

        String notificationMessage = "A musician is within " + DISTANCE + " meters";
        if (DISTANCE <= 100) {
            if (!notificationMessages.contains(notificationMessage))
                sendNotificationToMusician(Notifications.MUSICIAN_CHANNEL, NotificationCompat.PRIORITY_DEFAULT, notificationMessage);
        }
    }

    private void sendNotificationToMusician(String channel, int priority, String notificationMessage) {
        Notifications notif = new Notifications();
        notif.sendNotification(channel, this, notificationMessage, priority);
        notificationMessages.add(notificationMessage);
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
                Intent eventIntent = new Intent(this, EventPage.class);
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
}
