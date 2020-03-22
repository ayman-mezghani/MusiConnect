package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ch.epfl.sdp.R;

public class ProfilePage extends StartPage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        OnClickListener listener = view -> {
            Intent profileModificationIntent = new Intent(getApplicationContext(), ProfileModification.class);
            ProfilePage.this.startActivity(profileModificationIntent);
        };
        Button editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(listener);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.my_profile)
            return true;
        else
            super.onOptionsItemSelected(item);
        return true;
    }
}
