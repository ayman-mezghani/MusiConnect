package ch.epfl.sdp.musiconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ch.epfl.sdp.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class StartPage extends AppCompatActivity {
    private double lat = -34;
    private double lon = 151;
    private String mainMarkerName = "You";
    private GoogleSignInClient mGoogleSignInClient;
    private boolean tests = true; // variable telling us if we are running the travis's tests

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
            case R.id.logOut:
                signOut();
                return true;
            default:
                displayNotFinishedFunctionalityMessage();
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        /*if (acct != null) {
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "!Connected", Toast.LENGTH_SHORT).show();
        }
         */
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account == null && !tests) { // for the moment : if we are running travis's tests we can't enter in this block
            Intent i = new Intent(StartPage.this, GoogleLogin.class);
            startActivity(i);
            finish();
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(StartPage.this, "Signed out succefully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
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
}
