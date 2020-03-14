package ch.epfl.sdp.musiconnect;

import ch.epfl.sdp.R;

import android.os.Bundle;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ProfilePage extends StartPage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        CloudStorage storage = new CloudStorage(getApplicationContext());
        storage.uploadGoogle("/home/ayman/Documents/3e/prin/sdp/MusiConnect/app/src/test/data/image.jpg");
    }
}
