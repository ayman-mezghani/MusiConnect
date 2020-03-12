package ch.epfl.sdp.musiconnect;

import ch.epfl.sdp.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ProfilePage extends StartPage implements View.OnClickListener {

    Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(this);
    }

    public void onClick(View view) {
        super.displayNotFinishedFunctionalityMessage();
    }
}
