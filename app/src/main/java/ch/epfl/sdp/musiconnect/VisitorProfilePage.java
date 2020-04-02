package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.os.Bundle;

import ch.epfl.sdp.R;

public class VisitorProfilePage extends ProfilePage {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (intent.hasExtra("FirstName")) {
            String sTitle = intent.getStringExtra("UserName") + "'s profile";
            title.setText(sTitle);

            firstName.setText(intent.getStringExtra("FirstName"));

            lastName.setText(intent.getStringExtra("LastName"));

            username.setText(intent.getStringExtra("UserName"));

            mail.setText(intent.getStringExtra("EmailAddress"));

            int[] birthdayContent = intent.getIntArrayExtra("Birthday");
            String sBirthday = birthdayContent[0] + "." + birthdayContent[1] + "." + birthdayContent[2];
            birthday.setText(sBirthday);
        }
    }
}
