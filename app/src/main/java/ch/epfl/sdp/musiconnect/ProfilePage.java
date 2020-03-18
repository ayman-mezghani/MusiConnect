package ch.epfl.sdp.musiconnect;

import ch.epfl.sdp.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.VideoView;

public class ProfilePage extends StartPage {
    private static int VIDEO_REQUEST = 101;
    private Uri videoUri = null;
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mVideoView = findViewById(R.id.videoView);
        mVideoView.setOnClickListener(v->{
            mVideoView.setVideoURI(videoUri);
            mVideoView.start();
        });
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
}

