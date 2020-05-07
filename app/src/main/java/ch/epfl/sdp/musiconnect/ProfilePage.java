package ch.epfl.sdp.musiconnect;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudCallback;
import ch.epfl.sdp.musiconnect.cloud.CloudStorage;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.FirebaseCloudStorage;

public abstract class ProfilePage extends Page {
    protected TextView titleView, firstNameView, lastNameView, usernameView, emailView, birthdayView;
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
        CloudStorage storage = CloudStorageGenerator.getCloudInstance(this);
        String path = s + "/" + FirebaseCloudStorage.FileType.video;
        String saveName = s + "_" + FirebaseCloudStorage.FileType.video;
        try {
            storage.download(path, saveName, new CloudCallback() {
                @Override
                public void onSuccess(Uri fileUri) {
                    videoUri = fileUri;
                    showVideo();
                }

                @Override
                public void onFailure() {
                    videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.minion);
                    showVideo();
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, "An error occured, please contact support.", Toast.LENGTH_LONG).show();
        }
    }
}
