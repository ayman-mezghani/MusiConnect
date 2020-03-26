package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ch.epfl.sdp.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class ProfilePage extends StartPage implements View.OnClickListener {
    private static int VIDEO_REQUEST = 101;
    protected Uri videoUri = null;
    private VideoView mVideoView;
    Button editProfile;
    private ImageView imgVw;
    private TextView name, firstname, mail, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Intent intent = getIntent();
        if (intent.hasExtra("FirstName")) {
            TextView firstNameView = findViewById(R.id.myFirstname);
            firstNameView.setText(intent.getStringExtra("FirstName"));

            TextView lastNameView = findViewById(R.id.myLastname);
            lastNameView.setText(intent.getStringExtra("LastName"));

            TextView userNameView = findViewById(R.id.myUsername);
            userNameView.setText(intent.getStringExtra("UserName"));

            TextView emailView = findViewById(R.id.myMail);
            emailView.setText(intent.getStringExtra("EmailAddress"));

            TextView birthdayView = findViewById(R.id.myBirthday);
            int[] birthday = intent.getIntArrayExtra("Birthday");
            String s = birthday[0] + "." + birthday[1] + "." + birthday[2];
            birthdayView.setText(s);
        }
      
        imgVw = findViewById(R.id.imgView);
        firstname = findViewById(R.id.myFirstname);
        name = findViewById(R.id.myLastname);
        mail = findViewById(R.id.myMail);

        mVideoView = findViewById(R.id.videoView);
        mVideoView.setOnTouchListener((v, event) -> {
            mVideoView.setVideoURI(videoUri);
            mVideoView.start();
            return true;
        });

        editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(this);

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

            firstname.setText(personName.split(" ")[0]);
            name.setText(personName.split(" ")[1]);
            mail.setText(personEmail);

            Glide.with(this).load(String.valueOf(personPhoto)).into(imgVw);
        }
    }

    public void captureVideo(View view) {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if(videoIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(videoIntent, VIDEO_REQUEST);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_REQUEST && resultCode == RESULT_OK) {
            videoUri = data.getData();
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.my_profile)
            return true;
        else
            super.onOptionsItemSelected(item);
        return true;
    }

    public void onClick(View view) {
        super.displayNotFinishedFunctionalityMessage();
    }
}

