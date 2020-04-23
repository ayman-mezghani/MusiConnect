package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DataBase;
import ch.epfl.sdp.musiconnect.database.DbAdapter;

public class BandCreation extends UserCreation {

    private EditText etBandName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_creation);

        findViewById(R.id.btnBandCreationCancel).setOnClickListener(v -> signOut());
        etBandName = findViewById(R.id.etBandName);
        findViewById(R.id.btnBandCreationCreate).setOnClickListener(v -> {
            if (!isEmpty(etBandName)) {
                Band band = new Band(etBandName.getText().toString(), CurrentUser.getInstance(this).getMusician());
                (new DbAdapter(new DataBase())).add("Band", band);

                StartActivityAndFinish(new Intent(BandCreation.this, StartPage.class));
            } else {
                Toast.makeText(this, R.string.band_name_cant_be_empty, Toast.LENGTH_LONG).show();
            }
        });
    }
}
