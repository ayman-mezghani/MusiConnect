package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DbAdapter;

import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public class VisitorProfilePage extends ProfilePage implements DbCallback {
    private DbAdapter dbAdapter;


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

        Button addUserToBand = findViewById(R.id.add_user_to_band);
        if(CurrentUser.getInstance(this).getTypeOfUser() == TypeOfUser.Band) {
            addUserToBand.setVisibility(View.VISIBLE);
            addUserToBand.setFocusable(true);
            addUserToBand.setClickable(true);
        }

        loadProfileContent();

        getVideoUri(userEmail);


        /*
        addUserToBand.setOnClickListener(v -> {
            DbAdapter db = ;
            CurrentUser.getInstance(this).getBand()
            DbGenerator.getDbInstance().read(DbUserType.Musician, str, new DbCallback() {
                @Override
                public void readCallback(User u) {
                    b.addMember((Musician) u);
                    (DbGenerator.getDbInstance()).add(DbUserType.Band, b);
                }
            });
        });
         */
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
    }
}
