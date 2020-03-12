package ch.epfl.sdp.musiconnect;

import ch.epfl.sdp.R;

import android.os.Bundle;

public class ProfilePage extends StartPage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        CloudStorage storage = new CloudStorage(getApplicationContext());
        storage.upload("/home/ayman/Pictures/p.png", "amz");
    }
}
