package ch.epfl.sdp.musiconnect.finder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Button;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.Page;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class MusicianFinderPage extends Page implements View.OnClickListener {

    private TextView title;
    private TextView firstName;
    private EditText musicianFirstName;
    private TextView lastName;
    private EditText musicianLastName;
    private TextView userName;
    private EditText musicianUserName;
    private TextView instruments;
    private Spinner musicianInstruments;
    private TextView levels;
    private Spinner musicianLevels;
    private Button findMusician;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musician_finder_page);

        title = findViewById(R.id.musicianFinderTitleID);
        firstName = findViewById(R.id.musicianFinderFirstNameID);
        musicianFirstName = findViewById(R.id.myMusicianFinderFirstNameID);
        lastName = findViewById(R.id.musicianFinderLastNameID);
        musicianLastName = findViewById(R.id.myMusicianFinderLastNameID);
        userName = findViewById(R.id.musicianFinderUserNameID);
        musicianUserName = findViewById(R.id.myMusicianFinderUserNameID);

        instruments = findViewById(R.id.musicianFinderInstrumentsID);
        musicianInstruments = findViewById(R.id.myMusicianFinderInstrumentsID);
        // Create an ArrayAdapter using the string array instruments_array and a default spinner layout
        ArrayAdapter<CharSequence> instrumentsAdapter = ArrayAdapter.createFromResource(this, R.array.instruments_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        instrumentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the musicianInstruments spinner
        musicianInstruments.setAdapter(instrumentsAdapter);

        levels = findViewById(R.id.musicianFinderLevelsID);
        musicianLevels = findViewById(R.id.myMusicianFinderLevelsID);
        // Create an ArrayAdapter using the string array levels_array and a default spinner layout
        ArrayAdapter<CharSequence> levelsAdapter = ArrayAdapter.createFromResource(this, R.array.levels_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        levelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the musicianLevels spinner
        musicianLevels.setAdapter(levelsAdapter);

        // If the button findMusician is pressed, then the method onClick(view) is executed
        findMusician = findViewById(R.id.musicianFinderButtonID);
        findMusician.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Map<String, Object> searchMap = new HashMap<>();
        if(!musicianFirstName.getText().toString().equals("")) {
            searchMap.put("firstName", musicianFirstName.getText().toString());
        }
        if(!musicianLastName.getText().toString().equals("")) {
            searchMap.put("lastName", musicianLastName.getText().toString());
        }
        if(!musicianUserName.getText().toString().equals("")) {
            searchMap.put("username", musicianUserName.getText().toString());
        }

        Intent i = new Intent(this, MusicianFinderResult.class);
        i.putExtra("searchMap", (Serializable) searchMap);
        startActivity(i);
    }

}
