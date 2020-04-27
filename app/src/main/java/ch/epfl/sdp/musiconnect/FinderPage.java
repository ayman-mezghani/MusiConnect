package ch.epfl.sdp.musiconnect;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Button;

import ch.epfl.sdp.R;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class FinderPage extends Page {

    private Button findMusician;
    private Button findBand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder_page);

        findViewById(R.id.finderTitleID);

        // If the button findMusician is pressed, then the method openMusicianFinderPage() is executed
        findMusician = findViewById(R.id.findMusicianButtonID);
        findMusician.setOnClickListener(view -> openMusicianFinderPage());

        // If the button findBand is pressed, then the method openBandFinderPage() is executed
        findBand = findViewById(R.id.findBandButtonID);
        findBand.setOnClickListener(view -> openBandFinderPage());
    }

    private void openMusicianFinderPage() {
        Intent findMusicianIntent = new Intent(this, MusicianFinderPage.class);
        startActivity(findMusicianIntent);
    }

    private void openBandFinderPage() {
        Intent findBandIntent = new Intent(this, BandFinderPage.class);
        startActivity(findBandIntent);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.search)
            return true;
        else
            super.onOptionsItemSelected(item);
        return true;
    }
}
