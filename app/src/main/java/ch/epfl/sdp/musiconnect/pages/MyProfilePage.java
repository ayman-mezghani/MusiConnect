package ch.epfl.sdp.musiconnect.pages;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.Instrument;
import ch.epfl.sdp.musiconnect.Level;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbSingleton;

public class MyProfilePage extends ProfilePage implements View.OnClickListener {
    private static int LAUNCH_PROFILE_MODIF_INTENT = 102;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = DbSingleton.getDbInstance();

        setContentView(R.layout.activity_profile_page);

        mVideoView = findViewById(R.id.videoView);
        imgVw = findViewById(R.id.imgView);
        firstNameView = findViewById(R.id.myFirstname);
        lastNameView = findViewById(R.id.myLastname);
        usernameView = findViewById(R.id.myUsername);
        emailView = findViewById(R.id.myMail);
        birthdayView = findViewById(R.id.myBirthday);
        userEmail = CurrentUser.getInstance(this).email;

        Button editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(v -> {
            Intent profileModificationIntent = new Intent(this, ProfileModificationPage.class);
            sendInformation(profileModificationIntent);
            // Permits sending information from child to parent activity
            startActivityForResult(profileModificationIntent, LAUNCH_PROFILE_MODIF_INTENT);
        });

        loadProfileContent();
        getVideoUri(userEmail);

        instrument = findViewById(R.id.myProfileInstrument);
        selectedInstrument = findViewById(R.id.myProfileSelectedInstrument);
        level = findViewById(R.id.myProfileLevel);
        selectedLevel = findViewById(R.id.myProfileSelectedLevel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        bottomNavigationView.setSelectedItemId(R.id.my_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.my_profile)
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void loadUserProfile(User user) {
        Musician m = (Musician) user;

        firstNameView.setText(m.getFirstName());
        lastNameView.setText(m.getLastName());
        usernameView.setText(m.getUserName());

        MyDate date = m.getBirthday();
        String s = date.getDate() + "/" + date.getMonth() + "/" + date.getYear();
        birthdayView.setText(s);

        emailView.setText(m.getEmailAddress());

        loadInstrument(m);
    }

    @Override
    public void onStart() {
        super.onStart();
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

            setInstrumentRelatedViews(data);

            String videoUriString = data.getStringExtra("videoUri");

            if (videoUriString != null) {
                videoUri = Uri.parse(videoUriString);
                showVideo();
            } else {
                getVideoUri(userEmail);
            }
        }
    }

    private void setInstrumentRelatedViews(Intent data) {
        String i = data.getStringExtra("INSTRUMENT");
        String l = data.getStringExtra("LEVEL");
        String[] instrArray = getResources().getStringArray(R.array.instruments_array);
        String[] lvlArray = getResources().getStringArray(R.array.levels_array);

        if (!(l.equals(lvlArray[0]) || i.equals(instrArray[0]))) {
            selectedInstrument.setText(i);
            selectedLevel.setText(l);
        } else {
            selectedInstrument.setText("");
            selectedLevel.setText("");
        }
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
        intent.putExtra("INSTRUMENT", selectedInstrument.getText().toString());
        intent.putExtra("LEVEL", selectedLevel.getText().toString());
    }
}