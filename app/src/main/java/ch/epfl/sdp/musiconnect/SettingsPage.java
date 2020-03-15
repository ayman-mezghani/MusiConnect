package ch.epfl.sdp.musiconnect;

import ch.epfl.sdp.R;

import android.os.Bundle;
import android.view.MenuItem;

public class SettingsPage extends StartPage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.settings)
            return true;
        else
            super.onOptionsItemSelected(item);
        return true;
    }
}
