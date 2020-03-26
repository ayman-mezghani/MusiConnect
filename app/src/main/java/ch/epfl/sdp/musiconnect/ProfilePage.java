package ch.epfl.sdp.musiconnect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudCallback;
import ch.epfl.sdp.musiconnect.cloud.CloudStorage;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;


public class ProfilePage extends StartPage implements View.OnClickListener {
    private static int VIDEO_REQUEST = 101;
    protected Uri videoUri = null;
    private VideoView mVideoView;
    private String username = "testUser";
    Button editProfile;
    private ImageView imgVw;
    private TextView name, firstname, mail, id;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mVideoView = findViewById(R.id.videoView);
        getVideoUri();

        mVideoView = findViewById(R.id.videoView);
        imgVw = findViewById(R.id.imgView);
        firstname = findViewById(R.id.myFirstname);
        name = findViewById(R.id.myLastname);
        mail = findViewById(R.id.myMail);

        editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            //String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            firstname.setText(personName.split(" ")[0]);
            name.setText(personName.split(" ")[1]);
            mail.setText(personEmail);

            Glide.with(this).load(String.valueOf(personPhoto)).into(imgVw);
        }
    }

    public void captureVideo(View view) {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (videoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(videoIntent, VIDEO_REQUEST);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_REQUEST && resultCode == RESULT_OK) {
            videoUri = data.getData();
            Log.d("Video", videoUri.getPath());

            Log.d("Video", "uploading video");
            CloudStorage storage = new CloudStorage(FirebaseStorage.getInstance().getReference(), this);
            try {
                storage.upload(videoUri, CloudStorage.FileType.video, username);
            } catch (IOException e) {
                Toast.makeText(this, R.string.cloud_upload_invalid_file_path, Toast.LENGTH_LONG).show();
            }
        }

        showVideo();

//        TODO: refresh the intent, may be useful after video change
//        finish();
//        overridePendingTransition( 0, 0);
//        startActivity(getIntent());
//        overridePendingTransition( 0, 0);
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

    private void showVideo() {
        if (videoUri != null) {
            mVideoView.setVideoURI(videoUri);
            mVideoView.start();
            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mVideoView.start();
                }
            });
        }
    }

    private void getVideoUri() {
        CloudStorage storage = new CloudStorage(FirebaseStorage.getInstance().getReference(), this);
        String path = username + "/" + CloudStorage.FileType.video;
        String saveName = username + "_" + CloudStorage.FileType.video;
        try {
            storage.download(path, saveName, new CloudCallback() {
                @Override
                public void onCallback(Uri fileUri) {
                    videoUri = fileUri;
                    showVideo();
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, "An error occured, please contact support.", Toast.LENGTH_LONG).show();
        }
    }
}