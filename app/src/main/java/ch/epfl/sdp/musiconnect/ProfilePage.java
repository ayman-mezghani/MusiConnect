package ch.epfl.sdp.musiconnect;

import ch.epfl.sdp.BuildConfig;
import ch.epfl.sdp.R;

import android.net.Uri;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProfilePage extends StartPage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
    }
}
