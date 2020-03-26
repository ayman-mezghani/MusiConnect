package ch.epfl.sdp.musiconnect;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ch.epfl.sdp.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.VideoView;

public class ProfilePage extends StartPage implements View.OnClickListener {
    private static int VIDEO_REQUEST = 101;
    private static int LAUNCH_PROFILE_MODIF_INTENT = 102;
    protected Uri videoUri = null;

    private TextView firstName, lastName, username, mail, birthday;
    private VideoView mVideoView;
    private Button editProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        firstName = findViewById(R.id.myFirstname);
        lastName = findViewById(R.id.myLastname);
        username = findViewById(R.id.myUsername);
        mail = findViewById(R.id.myMail);
        birthday = findViewById(R.id.myBirthday);

        mVideoView = findViewById(R.id.videoView);
        mVideoView.setOnClickListener(v -> {
            mVideoView.setVideoURI(videoUri);
            mVideoView.start();
        });

        editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(v -> {
            Intent profileModificationIntent = new Intent(this, ProfileModification.class);
            sendInformation(profileModificationIntent);
            // Permits sending information from child to parent activity
            startActivityForResult(profileModificationIntent, LAUNCH_PROFILE_MODIF_INTENT);
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_REQUEST && resultCode == RESULT_OK)
            videoUri = data.getData();
        if (requestCode == LAUNCH_PROFILE_MODIF_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                String[] newFields = data.getStringArrayExtra("newFields");
                assert newFields != null;
                firstName.setText(newFields[0]);
                lastName.setText(newFields[1]);
                username.setText(newFields[2]);
                mail.setText(newFields[3]);
                birthday.setText(newFields[4]);
            }
        }
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

    /**
     * Automatically fill the edit texts of profile modification page with actual string values
     * @param intent
     */
    private void sendInformation(Intent intent) {
        intent.putExtra("FIRST_NAME", firstName.getText().toString());
        intent.putExtra("LAST_NAME", lastName.getText().toString());
        intent.putExtra("USERNAME", username.getText().toString());
        intent.putExtra("MAIL", mail.getText().toString());
        intent.putExtra("BIRTHDAY", birthday.getText().toString());
    }

    public void captureVideo(View view) {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (videoIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(videoIntent, VIDEO_REQUEST);
    }
}