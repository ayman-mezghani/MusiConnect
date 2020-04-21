package ch.epfl.sdp.musiconnect;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudCallback;
import ch.epfl.sdp.musiconnect.cloud.CloudStorage;

public abstract class ProfilePage extends Page {
    protected TextView titleView, firstNameView, lastNameView, usernameView, emailView, birthdayView;
    protected static int VIDEO_REQUEST = 101;
    protected Uri videoUri = null;
    protected VideoView mVideoView;
    protected ImageView imgVw;
    protected String userEmail;

    protected void showVideo() {
        if (videoUri != null) {
            mVideoView.setVideoURI(videoUri);
            mVideoView.start();
            mVideoView.setOnCompletionListener(mediaPlayer -> mVideoView.start());
        }
    }

    protected void getVideoUri(String s) {
        CloudStorage storage = new CloudStorage(FirebaseStorage.getInstance().getReference(), this);
        String path = s + "/" + CloudStorage.FileType.video;
        String saveName = s + "_" + CloudStorage.FileType.video;
        try {
            storage.download(path, saveName, new CloudCallback() {
                @Override
                public void onSuccess(Uri fileUri) {
                    videoUri = fileUri;
                    showVideo();
                }
                @Override
                public void onFailure() {
                    videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+ R.raw.minion);
                    showVideo();
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, "An error occured, please contact support.", Toast.LENGTH_LONG).show();
        }
    }
}
