package ch.epfl.sdp.musiconnect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import ch.epfl.sdp.R;

public class MyProfilePage extends ProfilePage implements View.OnClickListener {

    private static int LAUNCH_PROFILE_MODIF_INTENT = 102;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mVideoView = findViewById(R.id.videoView);
        getVideoUri();

        imgVw = findViewById(R.id.imgView);
        firstName = findViewById(R.id.myFirstname);
        lastName = findViewById(R.id.myLastname);
        username = findViewById(R.id.myUsername);
        mail = findViewById(R.id.myMail);
        birthday = findViewById(R.id.myBirthday);

        Button editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(v -> {
            Intent profileModificationIntent = new Intent(this, ProfileModification.class);
            sendInformation(profileModificationIntent);
            // Permits sending information from child to parent activity
            startActivityForResult(profileModificationIntent, LAUNCH_PROFILE_MODIF_INTENT);
        });


        googleSignIn();
    }

    private void googleSignIn(){
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

    public void captureVideo(View view) {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (videoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(videoIntent, VIDEO_REQUEST);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_REQUEST && resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == LAUNCH_PROFILE_MODIF_INTENT && resultCode == Activity.RESULT_OK) {
            String[] newFields = data.getStringArrayExtra("newFields");
            assert newFields != null;
            firstName.setText(newFields[0]);
            lastName.setText(newFields[1]);
            username.setText(newFields[2]);
            mail.setText(newFields[3]);
            birthday.setText(newFields[4]);
            showVideo();
        }


//        TODO: refresh the intent, may be useful after video change
//        finish();
//        overridePendingTransition( 0, 0);
//        startActivity(getIntent());
//        overridePendingTransition( 0, 0);
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




    /**
     * Automatically fill the edit texts of profile modification page with actual string values
     * @param intent
     */
    private void sendInformation(Intent intent) {
        intent.putExtra("FIRST_NAME", firstName.getText().toString());
        intent.putExtra("LAST_NAME", lastName.getText().toString());
        intent.putExtra("USERNAME", username.getText().toString());
        intent.putExtra("MAIL", mail.getText().toString());
        intent.putExtra("BIRTHDAY", birthday.getText().toString());
    }
}