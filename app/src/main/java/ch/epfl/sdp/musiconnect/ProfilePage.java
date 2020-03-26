package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ch.epfl.sdp.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.VideoView;

public class ProfilePage extends StartPage implements View.OnClickListener {
    private static int VIDEO_REQUEST = 101;
    protected Uri videoUri = null;
    private VideoView mVideoView;
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
            birthdayView.setText(s);

        }
        mVideoView = findViewById(R.id.videoView);
        mVideoView.setOnClickListener(v->{
            mVideoView.setVideoURI(videoUri);
            mVideoView.start();
        });

        editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(this);
    }

    public void captureVideo(View view) {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if(videoIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(videoIntent, VIDEO_REQUEST);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_REQUEST && resultCode == RESULT_OK) {
            videoUri = data.getData();
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
}

