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
import ch.epfl.sdp.musiconnect.users.Band;
import ch.epfl.sdp.musiconnect.pages.BandProfile;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.DbDataType;

public class BandFinderResult extends AppCompatActivity {
    List<Band> listBand = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_finder_result);


        List<String> listBandShow = new ArrayList<>();

        ListView lvMusicianResult = findViewById(R.id.LvBandResult);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(BandFinderResult.this, android.R.layout.simple_list_item_1, listBandShow);
        lvMusicianResult.setAdapter(adapter);

        listViewAddOnItemClickListner(lvMusicianResult);

        Map<String, Object> searchMap = (HashMap<String, Object>)getIntent().getSerializableExtra("searchMap");

        for(Map.Entry<String, Object> entry : searchMap.entrySet()) {
            HashMap<String, Object> innerMap = new HashMap<>();
            innerMap.put(entry.getKey(), (String) entry.getValue());

            DbSingleton.getDbInstance().query(DbDataType.Band, innerMap, new DbCallback() {
                @Override
                public void queryCallback(List userList) {
                    for (Object u: userList) {
                        if(!listBandShow.contains(((Band) u).getEmailAddress())) {
                            listBandShow.add(((Band) u).getName());
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
