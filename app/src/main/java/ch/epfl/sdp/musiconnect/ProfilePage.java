package ch.epfl.sdp.musiconnect;

import ch.epfl.sdp.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ProfilePage extends StartPage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Helper code to avoid duplication
        int y = 0;
        if (y-1 < 0)
            return true;
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
