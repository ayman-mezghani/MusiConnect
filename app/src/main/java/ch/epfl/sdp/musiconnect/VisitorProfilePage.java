package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.os.Bundle;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DataBase;
import ch.epfl.sdp.musiconnect.database.DbAdapter;

public class VisitorProfilePage extends ProfilePage {
    private DataBase db;
    private DbAdapter dbAdapter;
    private String newUsername;
    private boolean isTest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DataBase();
        dbAdapter = new DbAdapter(db);

        setContentView(R.layout.activity_visitor_profile_page);
        mVideoView = findViewById(R.id.videoView);
        getVideoUri();

        imgVw = findViewById(R.id.imgView);
        title = findViewById(R.id.profileTitle);
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        username = findViewById(R.id.username);
        mail = findViewById(R.id.mail);
        birthday = findViewById(R.id.birthday);


        loadProfileContent();
    }


    // This function should take something like userID as input
    private void loadProfileContent() {
        Intent intent = getIntent();
        if (intent.hasExtra("UserName")) {
            newUsername = intent.getStringExtra("UserName");
            dbAdapter.read(newUsername, user -> {
                if (user != null) {
                    Musician m = (Musician) user;
                    String sTitle = m.getUserName() + "'s profile";
                    title.setText(sTitle);

                    firstName.setText(m.getFirstName());
                    lastName.setText(m.getLastName());
                    username.setText(m.getUserName());
                    mail.setText(m.getEmailAddress());

                    MyDate date = m.getBirthday();
                    String s = date.getDate() + "/" + date.getMonth() + "/" + date.getYear();
                    birthday.setText(s);
                } else {
                    // setContentView(ProfileNotFound);
                }
            });
        }
    }
}
