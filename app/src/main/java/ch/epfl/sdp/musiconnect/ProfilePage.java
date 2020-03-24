package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ch.epfl.sdp.R;

public class ProfilePage extends Page implements View.OnClickListener {

    Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Intent intent = getIntent();
        if (intent.hasExtra("FirstName")) {
            TextView firstNameView = findViewById(R.id.myFirstname);
            firstNameView.setText(intent.getStringExtra("FirstName"));

            TextView lastNameView = findViewById(R.id.myLastname);
            lastNameView.setText(intent.getStringExtra("LastName"));

            TextView userNameView = findViewById(R.id.myUsername);
            userNameView.setText(intent.getStringExtra("UserName"));

            TextView emailView = findViewById(R.id.myMail);
            emailView.setText(intent.getStringExtra("EmailAddress"));

            TextView birthdayView = findViewById(R.id.myBirthday);
            int[] birthday = intent.getIntArrayExtra("Birthday");
            String s = birthday[0] + "." + birthday[1] + "." + birthday[2];
            //MyDate date = new MyDate(, birthday[1], birthday[2]);
            birthdayView.setText(s);

        }

        editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(this);
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
}
