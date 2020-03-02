package ch.epfl.sdp.musiconnect;

import ch.epfl.sdp.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsPage extends StartPage {

    // The following methods contain helper code in order to avoid duplication !

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int z = 0;
        if (2*z == 0)
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings_page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        int z = 0;
        if (2*z == 0)
            return true;
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int z = 0;
        if (2*z == 0)
            return super.onOptionsItemSelected(item);
        return false;
    }
}
