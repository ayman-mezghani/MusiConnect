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

import ch.epfl.sdp.R;


public class StartPage extends AppCompatActivity {
    private double lat = -34;
    private double lon = 151;
    private String mainMarkerName = "You";
    protected GoogleSignInClient mGoogleSignInClient;
    protected GoogleSignInOptions gso;
    private boolean test = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

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
    }



    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_profile:
                Intent profileIntent = new Intent(StartPage.this, ProfilePage.class);
                this.startActivity(profileIntent);
                return true;
            case R.id.settings:
                Intent settingsIntent = new Intent(StartPage.this, SettingsPage.class);
                this.startActivity(settingsIntent);
                return true;
            case R.id.help:
                Intent helpIntent = new Intent(StartPage.this, HelpPage.class);
                this.startActivity(helpIntent);
                return true;
            // In comments right now to avoid duplication
//            case R.id.search:
//                return true;
            case R.id.map:
                startMap();
                return true;
            case R.id.signout:
                signOut();
                return true;
            default:
                displayNotFinishedFunctionalityMessage();
                return false;
        }
    }

    protected void displayNotFinishedFunctionalityMessage() {
        Toast.makeText(this, getString(R.string.not_yet_done), Toast.LENGTH_SHORT).show();
    }

    private void startMap(){
        Intent myIntent = new Intent(StartPage.this, MapsActivity.class);


        myIntent.putExtra("lat", lat);
        myIntent.putExtra("lon", lon);
        myIntent.putExtra("mainMarkerName", mainMarkerName);
        StartPage.this.startActivity(myIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account == null && !test)
            startActivity(new Intent(this, GoogleLogin.class));
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    startActivity(new Intent(StartPage.this, GoogleLogin.class));
                    finish();
                });
    }
}