package ch.epfl.sdp.musiconnect.finder;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Button;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.Page;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class BandFinderPage extends Page implements View.OnClickListener {

    private TextView title;
    private TextView bandName;
    private EditText bandBandName;
    private TextView instruments;
    private Spinner bandInstruments;
    private TextView levels;
    private Spinner bandLevels;
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
        // Create an ArrayAdapter using the string array instruments_array and a default spinner layout
        ArrayAdapter<CharSequence> instrumentsAdapter = ArrayAdapter.createFromResource(this, R.array.instruments_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        instrumentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the bandInstruments spinner
        bandInstruments.setAdapter(instrumentsAdapter);

        levels = findViewById(R.id.bandFinderLevelsID);
        bandLevels = findViewById(R.id.myBandFinderLevelsID);
        // Create an ArrayAdapter using the string array levels_array and a default spinner layout
        ArrayAdapter<CharSequence> levelsAdapter = ArrayAdapter.createFromResource(this, R.array.levels_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        levelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the bandLevels spinner
        bandLevels.setAdapter(levelsAdapter);

        // If the button findBand is pressed, then the method onClick(view) is executed
        findBand = findViewById(R.id.bandFinderButtonID);
        findBand.setOnClickListener(view -> onClick(view));
    }

    @Override
    public void onClick(View view) {
        super.displayNotFinishedFunctionalityMessage();
    }

}
