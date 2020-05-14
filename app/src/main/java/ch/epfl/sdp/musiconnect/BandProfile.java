package ch.epfl.sdp.musiconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public class BandProfile extends Page {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_profile);

        Spinner bandSpinner = findViewById(R.id.spinnerBands);

        Button bandEditProfile = findViewById(R.id.btnEditBandProfile);
        if(CurrentUser.getInstance(this).getTypeOfUser() == TypeOfUser.Band) {
            bandEditProfile.setVisibility(View.VISIBLE);
            bandEditProfile.setFocusable(true);
            bandEditProfile.setClickable(true);
        }

        // you need to have a list of data that you want the spinner to display
        List<String> spinnerArray = new ArrayList<String>();

        for (Band b : CurrentUser.getInstance(this).getBands()) {
            spinnerArray.add(b.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bandSpinner.setAdapter(adapter);

        bandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String selected = bandSpinner.getSelectedItem().toString();
                Toast.makeText(BandProfile.this, selected, Toast.LENGTH_SHORT).show();

                DbGenerator.getDbInstance().read(DbUserType.Band,
                        getBandFromName(CurrentUser.getInstance(BandProfile.this).getBands(), selected).getEmailAddress(), new DbCallback() {
                            @Override
                            public void readCallback(User user) {
                                displayBandInfo((Band) user);
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
    }

    private void displayBandInfo(Band b) {
        ((TextView) findViewById(R.id.etLeader)).setText(b.getEmailAddress());
        ((TextView) findViewById(R.id.etBandName)).setText(b.getName());

        List<String> listMusician = b.getMusiciansEmailsAdress();
        ListView lvBandMemebrs = findViewById(R.id.myLvBandMembers);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (BandProfile.this, android.R.layout.simple_list_item_1, listMusician);
        lvBandMemebrs.setAdapter(adapter);

        List<String> listEvent = b.getEvents();
        ListView lvBandEvent = findViewById(R.id.myLvBandEvent);

        final ArrayAdapter<String> adapter2 = new ArrayAdapter<>
                (BandProfile.this, android.R.layout.simple_list_item_1, listEvent);
        lvBandEvent.setAdapter(adapter2);
    }

    private Band getBandFromName(List<Band> lb, String bandName) {
        for(Band b: lb) {
            if(b.getName().equals(bandName))
                return b;
        }
        return null;
    }
}
