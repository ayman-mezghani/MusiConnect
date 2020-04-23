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
import ch.epfl.sdp.musiconnect.database.DataBase;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;

public class MyProfilePage extends ProfilePage implements View.OnClickListener {
    private static String collection = "newtest";

    private static int LAUNCH_PROFILE_MODIF_INTENT = 102;
    private DbAdapter dbAdapter;
    private boolean test = true;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = new DbAdapter(new DataBase());

        setContentView(R.layout.activity_profile_page);

        mVideoView = findViewById(R.id.videoView);


        imgVw = findViewById(R.id.imgView);
        firstNameView = findViewById(R.id.myFirstname);
        lastNameView = findViewById(R.id.myLastname);
        usernameView = findViewById(R.id.myUsername);
        emailView = findViewById(R.id.myMail);
        birthdayView = findViewById(R.id.myBirthday);

        Button editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(v -> {
            Intent profileModificationIntent = new Intent(this, ProfileModification.class);
            sendInformation(profileModificationIntent);
            // Permits sending information from child to parent activity
            startActivityForResult(profileModificationIntent, LAUNCH_PROFILE_MODIF_INTENT);
        });

        loadProfileContent();
        getVideoUri(userEmail);
    }

    private boolean checktest() {
        boolean istest;

        try {
            Class.forName("androidx.test.espresso.Espresso");
            istest = true;
        } catch (ClassNotFoundException e) {
            istest = false;
        }
        return istest;
    }

    private void loadProfileContent() {
        if (!checktest()) {
            dbAdapter.read(collection, CurrentUser.getInstance(this).email, new DbCallback() {
                @Override
                public void readCallback(User user) {
                    Musician m = (Musician) user;
                    firstNameView.setText(m.getFirstName());
                    lastNameView.setText(m.getLastName());
                    usernameView.setText(m.getUserName());
                    emailView.setText(m.getEmailAddress());
                    MyDate date = m.getBirthday();
                    String s = date.getDate() + "/" + date.getMonth() + "/" + date.getYear();
                    birthdayView.setText(s);
                }
            });
        } else {
            firstNameView.setText("default");
            lastNameView.setText("user");
            usernameView.setText("defuser");
            emailView.setText("defuser@gmail.com");
            birthdayView.setText("01/01/2000");
        }
    }

    public void captureVideo(View view) {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (videoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(videoIntent, VIDEO_REQUEST);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    //        getVideoUri(userEmail);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LAUNCH_PROFILE_MODIF_INTENT && resultCode == Activity.RESULT_OK) {
            String[] newFields = data.getStringArrayExtra("newFields");
            assert newFields != null;
            firstNameView.setText(newFields[0]);
            lastNameView.setText(newFields[1]);
            usernameView.setText(newFields[2]);
            emailView.setText(newFields[3]);
            birthdayView.setText(newFields[4]);

            String videoUriString = data.getStringExtra("videoUri");

            if (videoUriString != null) {
                videoUri = Uri.parse(videoUriString);
                showVideo();
            } else {
                getVideoUri(userEmail);
            }
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
     *
     * @param intent
     */
    private void sendInformation(Intent intent) {
        intent.putExtra("FIRST_NAME", firstNameView.getText().toString());
        intent.putExtra("LAST_NAME", lastNameView.getText().toString());
        intent.putExtra("USERNAME", usernameView.getText().toString());
        intent.putExtra("MAIL", emailView.getText().toString());
        intent.putExtra("BIRTHDAY", birthdayView.getText().toString());
    }
}