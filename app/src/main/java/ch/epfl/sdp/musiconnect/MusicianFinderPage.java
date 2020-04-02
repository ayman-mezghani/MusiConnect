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
public class MusicianFinderPage extends Page implements View.OnClickListener {

    private TextView title;
    private TextView firstName;
    private EditText musicianFirstName;
    private TextView lastName;
    private EditText musicianLastName;
    private TextView userName;
    private EditText musicianUserName;
    private TextView instruments;
    private EditText musicianInstruments;
    private TextView levels;
    private EditText musicianLevels;
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
        levels = findViewById(R.id.musicianFinderLevelsID);
        musicianLevels = findViewById(R.id.myMusicianFinderLevelsID);

        // If the button findMusician is pressed, then the method onClick(view) is executed
        findMusician = findViewById(R.id.musicianFinderButtonID);
        findMusician.setOnClickListener(view -> onClick(view));
    }

    @Override
    public void onClick(View view) {
        super.displayNotFinishedFunctionalityMessage();
    }

}
