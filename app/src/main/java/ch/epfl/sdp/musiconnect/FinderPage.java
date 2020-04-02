package ch.epfl.sdp.musiconnect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;

import ch.epfl.sdp.R;

public class FinderPage extends Page implements View.OnClickListener {

    private TextView title;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder_page);

        title = findViewById(R.id.finderTitleID);

        Button findMusician = findViewById(R.id.findMusicianButtonID);
        editProfile.setOnClickListener(v -> {
            Intent profileModificationIntent = new Intent(this, ProfileModification.class);
            sendInformation(profileModificationIntent);
            // Permits sending information from child to parent activity
            startActivityForResult(profileModificationIntent, LAUNCH_PROFILE_MODIF_INTENT);
        });

        Button findBand = findViewById(R.id.findBandButtonID);
        editProfile.setOnClickListener(v -> {
            Intent profileModificationIntent = new Intent(this, ProfileModification.class);
            sendInformation(profileModificationIntent);
            // Permits sending information from child to parent activity
            startActivityForResult(profileModificationIntent, LAUNCH_PROFILE_MODIF_INTENT);
        });


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            //String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            firstName.setText(personName.split(" ")[0]);
            lastName.setText(personName.split(" ")[1]);
            mail.setText(personEmail);

            Glide.with(this).load(String.valueOf(personPhoto)).into(imgVw);
        }
    }

}
