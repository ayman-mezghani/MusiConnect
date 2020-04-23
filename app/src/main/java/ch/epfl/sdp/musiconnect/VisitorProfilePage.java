package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.os.Bundle;

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
        title = findViewById(R.id.profileTitle);
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        username = findViewById(R.id.username);
        email = findViewById(R.id.mail);
        birthday = findViewById(R.id.birthday);

        Intent intent = getIntent();
        if (!intent.getBooleanExtra("Test", false)) {
            loadProfileContent();
        } else {
            int[] birthday = intent.getIntArrayExtra("Birthday");
            Musician alyx = new Musician(intent.getStringExtra("FirstName"),
                    intent.getStringExtra("LastName"),
                    intent.getStringExtra("UserName"),
                    intent.getStringExtra("Email"),
                    new MyDate(birthday[2], birthday[1], birthday[0]));
            readCallback(alyx);
        }

        getVideoUri(userEmail);
    }


    // This function should take something like userID as input
    private void loadProfileContent() {
        Intent intent = getIntent();
        if (intent.hasExtra("UserEmail")) {
            userEmail = intent.getStringExtra("UserEmail");
            dbAdapter.read(DbUserType.Musician, userEmail, this);
        }
    }


    public void readCallback(User user) {
        Musician m = (Musician) user;
        String sTitle = m.getUserName() + "'s profile";
        title.setText(sTitle);

        firstName.setText(m.getFirstName());
        lastName.setText(m.getLastName());
        username.setText(m.getUserName());
        email.setText(m.getEmailAddress());

        MyDate date = m.getBirthday();
        String s = date.getDate() + "/" + date.getMonth() + "/" + date.getYear();
        birthday.setText(s);
    }
}
