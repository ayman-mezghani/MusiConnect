package ch.epfl.sdp.musiconnect;

import ch.epfl.sdp.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class HelpPage extends StartPage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Helper code to avoid duplication
        int x = 0;
        if (x+1 > 0)
            return true;
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
