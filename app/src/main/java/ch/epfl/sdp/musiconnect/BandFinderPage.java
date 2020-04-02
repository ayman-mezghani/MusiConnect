package ch.epfl.sdp.musiconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import ch.epfl.sdp.R;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class BandFinderPage extends Page implements View.OnClickListener {

    private TextView title;
    private TextView bandName;
    private EditText bandBandName;
    private TextView instruments;
    private EditText bandInstruments;
    private TextView levels;
    private EditText bandLevels;
    private Button findBand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_finder_page);

        title = findViewById(R.id.bandFinderTitleID);
        bandName = findViewById(R.id.bandFinderBandNameID);
        bandBandName = findViewById(R.id.myBandFinderBandNameID);
        instruments = findViewById(R.id.bandFinderInstrumentsID);
        bandInstruments = findViewById(R.id.myBandFinderInstrumentsID);
        levels = findViewById(R.id.bandFinderLevelsID);
        bandLevels = findViewById(R.id.myBandFinderLevelsID);

        // If the button findBand is pressed, then the method onClick(view) is executed
        findBand = findViewById(R.id.bandFinderButtonID);
        findBand.setOnClickListener(view -> onClick(view));
    }

    @Override
    public void onClick(View view) {
        super.displayNotFinishedFunctionalityMessage();
    }

}
