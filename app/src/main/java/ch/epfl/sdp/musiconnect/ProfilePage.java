package ch.epfl.sdp.musiconnect;

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
    protected Uri videoUri = null;
    private VideoView mVideoView;
    Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mVideoView = findViewById(R.id.videoView);
        mVideoView.setOnClickListener(v -> {
            mVideoView.setVideoURI(videoUri);
            mVideoView.start();
        });

        editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(v -> {
            Intent profileModificationIntent = new Intent(getApplicationContext(), ProfileModification.class);
            sendInformation(profileModificationIntent);
            startActivity(profileModificationIntent);
        });
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

    /**
     * Automatically fill the edit texts of profile modification page with actual string values
     * @param intent
     */
    private void sendInformation(Intent intent) {
        TextView firstName = findViewById(R.id.myFirstname);
        TextView lastName = findViewById(R.id.myLastname);
        TextView username = findViewById(R.id.myUsername);
        TextView mail = findViewById(R.id.myMail);
        TextView birthday = findViewById(R.id.myBirthday);

        intent.putExtra("FIRST_NAME", firstName.getText().toString());
        intent.putExtra("LAST_NAME", lastName.getText().toString());
        intent.putExtra("USERNAME", username.getText().toString());
        intent.putExtra("MAIL", mail.getText().toString());
        intent.putExtra("BIRTHDAY", birthday.getText().toString());
    }

    public void captureVideo(View view) {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if(videoIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(videoIntent, VIDEO_REQUEST);
        }
    }
}