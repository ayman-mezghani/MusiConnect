package ch.epfl.sdp.musiconnect.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.users.Band;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.DbDataType;

public class BandCreationPage extends UserCreationPage {

    private EditText etBandName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_creation);

        findViewById(R.id.btnBandCreationCancel).setOnClickListener(v -> signOut());
        etBandName = findViewById(R.id.etBandName);
        findViewById(R.id.btnBandCreationCreate).setOnClickListener(v -> {
            if (!isEmpty(etBandName)) {
                Band band = new Band(etBandName.getText().toString(), CurrentUser.getInstance(this).getMusician().getEmailAddress());
                DbSingleton.getDbInstance().add(DbDataType.Band, band);

                StartActivityAndFinish(new Intent(BandCreationPage.this, StartPage.class));
            } else {
                Toast.makeText(this, R.string.band_name_cant_be_empty, Toast.LENGTH_LONG).show();
            }
        });
    }
}
