package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.os.Bundle;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DataBase;
import ch.epfl.sdp.musiconnect.database.DbAdapter;

public class VisitorProfilePage extends ProfilePage {
    private DataBase db;
    private DbAdapter dbAdapter;
    private String username;
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
        titleView = findViewById(R.id.profileTitle);
        firstNameView = findViewById(R.id.firstname);
        lastNameView = findViewById(R.id.lastname);
        usernameView = findViewById(R.id.username);
        mailView = findViewById(R.id.mail);
        birthdayView = findViewById(R.id.birthday);


        loadProfileContent();
    }


    private void loadProfileContent() {
        Intent intent = getIntent();
        if (!intent.hasExtra("USERNAME")) {
            loadNullProfile();
        } else {
            username = intent.getStringExtra("USERNAME");
            
            Musician m = getUserFromUsername(username);

            if (m == null) {
                loadNullProfile();
            } else {
                String sTitle = m.getUserName() + "'s profile";
                titleView.setText(sTitle);

                firstNameView.setText(m.getFirstName());
                lastNameView.setText(m.getLastName());
                usernameView.setText(m.getUserName());
                mailView.setText(m.getEmailAddress());
                birthdayView.setText(m.getBirthday().toString());

                /*
                dbAdapter.read(newUsername, user -> {
                    if (user != null) {
                        Musician m = (Musician) user;
                        String sTitle = m.getUserName() + "'s profile";
                        titleView.setText(sTitle);

                        firstNameView.setText(m.getFirstName());
                        lastNameView.setText(m.getLastName());
                        usernameView.setText(m.getUserName());
                        mailView.setText(m.getEmailAddress());
                        birthdayView.setText(m.getBirthday().toString());
                    } else {
                        // setContentView(ProfileNotFound);
                    }
                });*/

            }


        }
    }

    private void loadNullProfile() {
        setContentView(R.layout.activity_visitor_profile_page_null);
    }

    // TODO replace by MockDatabase
    private Musician getUserFromUsername(String username) {
        if (username != null) {
            if (username.equals("PAlpha")) {
                return new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25));
            }
            if (username.equals("Alyx")) {
                return new Musician("Alice", "Bardon", "Alyx", "alyx92@gmail.com", new MyDate(1992, 9, 20));
            }
            if (username.equals("CallmeCarson")) {
                return new Musician("Carson", "Calme", "CallmeCarson", "callmecarson41@gmail.com", new MyDate(1995, 4, 1));
            }
        }
        return null;
    }
}
