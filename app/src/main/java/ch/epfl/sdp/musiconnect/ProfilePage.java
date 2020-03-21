package ch.epfl.sdp.musiconnect;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ch.epfl.sdp.R;

public class ProfilePage extends Page implements View.OnClickListener {

    Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(this);
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
