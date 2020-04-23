package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.os.Bundle;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DataBase;
import ch.epfl.sdp.musiconnect.database.DbAdapter;


public class VisitorProfilePage extends ProfilePage {
    private static String collection = "newtest";
    private DataBase db;
    private DbAdapter dbAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DataBase();
        dbAdapter = new DbAdapter(db);

        setContentView(R.layout.activity_visitor_profile_page);
        mVideoView = findViewById(R.id.videoView);

        imgVw = findViewById(R.id.imgView);

        titleView = findViewById(R.id.visitorProfileTitle);
        firstNameView = findViewById(R.id.visitorProfileFirstname);
        lastNameView = findViewById(R.id.visitorProfileLastname);
        usernameView = findViewById(R.id.visitorProfileUsername);
        emailView = findViewById(R.id.visitorProfileEmail);
        birthdayView = findViewById(R.id.visitorProfileBirthday);


        loadProfileContent();

        getVideoUri(userEmail);
    }


    private void loadProfileContent() {
        Intent intent = getIntent();
        if (!intent.hasExtra("UserEmail")) {
            loadNullProfile();
        } else {
            userEmail = intent.getStringExtra("UserEmail");

            Musician m = getUserFromEmail(userEmail);

            if (m == null) {
                loadNullProfile();
            } else {
                loadUserProfile(m);

                /*
                dbAdapter.read(collection, userEmail, new DbCallback() {
                    @Override
                    public void readCallback(User user) {
                        if (user == null) {
                            loadNullProfile();
                        } else {
                            loadUserProfile(user);
                        }
                    }
                });*/
            }
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


    // TODO replace by MockDatabase
    private Musician getUserFromEmail(String email) {
        if (email != null) {
            if (email.equals("palpha@gmail.com")) {
                return new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25));
            }
            if (email.equals("alyx92@gmail.com")) {
                return new Musician("Alice", "Bardon", "Alyx", "alyx92@gmail.com", new MyDate(1992, 9, 20));
            }
            if (email.equals("callmecarson41@gmail.com")) {
                return new Musician("Carson", "Calme", "CallmeCarson", "callmecarson41@gmail.com", new MyDate(1995, 4, 1));
            }
        }
        return null;
    }
}
