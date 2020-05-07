package ch.epfl.sdp.musiconnect;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.events.EventListPage;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class FinderPage extends Page implements View.OnClickListener {

    private TextView title;
    private Button findMusician;
    private Button findBand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder_page);

        title = findViewById(R.id.finderTitleID);

        // If the button findMusician is pressed, then the method openMusicianFinderPage() is executed
        findMusician = findViewById(R.id.findMusicianButtonID);
        findMusician.setOnClickListener(view -> openMusicianFinderPage());

        // If the button findBand is pressed, then the method openBandFinderPage() is executed
        findBand = findViewById(R.id.findBandButtonID);
        findBand.setOnClickListener(view -> openBandFinderPage());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        bottomNavigationView.setSelectedItemId(R.id.search);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> super.onOptionsItemSelected(item));
    }

    /**
     *  This method opens the musician finder page
     */
    private void openMusicianFinderPage() {
        Intent findMusicianIntent = new Intent(this, MusicianFinderPage.class);
        startActivity(findMusicianIntent);
    }

    /**
     *  This method opens the band finder page
     */
    private void openBandFinderPage() {
        Intent findBandIntent = new Intent(this, BandFinderPage.class);
        startActivity(findBandIntent);
    }

    @Override
    public void onClick(View view) {
        // This method does nothing
    }

}
