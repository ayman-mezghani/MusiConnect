package ch.epfl.sdp.musiconnect;

import ch.epfl.sdp.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsPage extends StartPage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Helper code to avoid duplication
        int z = 0;
        if (2*z == 0)
            return true;
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
