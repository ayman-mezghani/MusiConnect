package ch.epfl.sdp.musiconnect.finder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.BandProfile;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public class BandFinderResult extends AppCompatActivity {
    List<Band> listBand = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_finder_result);


        List<String> listBandShow = new ArrayList<>();

        ListView lvMusicianResult = findViewById(R.id.LvBandResult);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (BandFinderResult.this, android.R.layout.simple_list_item_1, listBandShow);
        lvMusicianResult.setAdapter(adapter);

        listViewAddOnItemClickListner(lvMusicianResult);

        Intent intent = getIntent();
        Map<String, Object> searchMap = (HashMap<String, Object>)intent.getSerializableExtra("searchMap");

        for(Map.Entry<String, Object> entry : searchMap.entrySet()) {
            String key = entry.getKey();
            String value = (String) entry.getValue();

            HashMap<String, Object> innerMap = new HashMap<>();
            innerMap.put(key, value);

            DbGenerator.getDbInstance().query(DbUserType.Band, innerMap, new DbCallback() {
                @Override
                public void queryCallback(List<User> userList) {
                    for (User u: userList) {
                        if(!listBandShow.contains((String) u.getEmailAddress())) {
                            listBandShow.add(u.getName());
                            listBand.add((Band) u);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void listViewAddOnItemClickListner(ListView l) {
        l.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(BandFinderResult.this, BandProfile.class);
            i.putExtra("leaderMail", BandProfile.getBandFromName(listBand, (String) l.getItemAtPosition(position)).getEmailAddress());
            startActivity(i);
        });
    }
}
