package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.os.Bundle;

import java.util.Map;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DataBase;
import ch.epfl.sdp.musiconnect.database.DbCallback;

public class VisitorProfilePage extends ProfilePage implements DbCallback {
    private DataBase db;
    private String newUsername;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DataBase();

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
            db.readDoc(newUsername, this);
        }
    }

    @Override
    public void onCallback(Map data) {
        String sTitle = newUsername + "'s profile";
        title.setText(sTitle);

        firstName.setText(data.get("first_name").toString());
        lastName.setText(data.get("last_name").toString());
        username.setText(newUsername);
        mail.setText(data.get("email").toString());
        Map<String, Object> date = (Map<String, Object>) data.get("birthday");
        String userBirthday = date.get("date") + "/" + date.get("month") + "/" + date.get("year");

        birthday.setText(userBirthday);
    }
}
