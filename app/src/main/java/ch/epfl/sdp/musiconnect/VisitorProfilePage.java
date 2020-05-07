package ch.epfl.sdp.musiconnect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DbAdapter;

import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public class VisitorProfilePage extends ProfilePage implements DbCallback {
    private DbAdapter dbAdapter;

    private Button contactButton;
    private String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = DbGenerator.getDbInstance();

        setContentView(R.layout.activity_visitor_profile_page);
        mVideoView = findViewById(R.id.videoView);

        imgVw = findViewById(R.id.imgView);

        titleView = findViewById(R.id.visitorProfileTitle);
        firstNameView = findViewById(R.id.visitorProfileFirstname);
        lastNameView = findViewById(R.id.visitorProfileLastname);
        usernameView = findViewById(R.id.visitorProfileUsername);
        emailView = findViewById(R.id.visitorProfileEmail);
        birthdayView = findViewById(R.id.visitorProfileBirthday);

        contactButton = findViewById(R.id.btnContactMusician);

        loadProfileContent();
        getVideoUri(userEmail);

        contactButton.setOnClickListener(view ->
            sendEmail(emailAddress, getResources().getString(R.string.musiconnect_contact_mail))
        );
    }

    // Some of this method code is inspired from stackoverflow.com
    @SuppressLint("IntentReset")
    protected void sendEmail(String to, String subject) {
        String[] TO = {to};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        try {
            startActivity(Intent.createChooser(emailIntent, "Sending mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadProfileContent() {
        Intent intent = getIntent();
        if (!intent.hasExtra("UserEmail")) {
            loadNullProfile();
        } else {
            userEmail = intent.getStringExtra("UserEmail");
            dbAdapter.read(DbUserType.Musician, userEmail, new DbCallback() {
                @Override
                public void readCallback(User user) {
                    loadUserProfile(user);
                }

                @Override
                public void readFailCallback() {
                    loadNullProfile();
                }
            });
        }
    }

    private void loadNullProfile() {
        setContentView(R.layout.activity_visitor_profile_page_null);
    }

    private void loadUserProfile(User user) {
        Musician m = (Musician) user;
        String sTitle = m.getUserName() + "'s profile";
        titleView.setText(sTitle);

        firstNameView.setText(m.getFirstName());
        lastNameView.setText(m.getLastName());
        usernameView.setText(m.getUserName());
        emailView.setText(m.getEmailAddress());
        birthdayView.setText(m.getBirthday().toString());

        emailAddress = m.getEmailAddress();
        addFirstNameToContactButtonText(m.getFirstName());
    }

    @SuppressLint("SetTextI18n")
    private void addFirstNameToContactButtonText(String firstName) {
        contactButton.setText("Contact " + firstName);
    }
}
