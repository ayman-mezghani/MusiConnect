package ch.epfl.sdp.musiconnect;

import ch.epfl.sdp.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class HelpPage extends Page {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.help)
            return true;
        else
            super.onOptionsItemSelected(item);
        return true;
    }
}
