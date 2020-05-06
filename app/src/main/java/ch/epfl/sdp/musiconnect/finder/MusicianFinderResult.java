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
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.VisitorProfilePage;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public class MusicianFinderResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musician_finder_result);

        List<String> listMusician = new ArrayList<>();

        ListView lvMusicianResult = findViewById(R.id.LvMusicianResult);

        lvMusicianResult.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(MusicianFinderResult.this, VisitorProfilePage.class);
            i.putExtra("UserEmail", (String) lvMusicianResult.getItemAtPosition(position));
            startActivity(i);
        });

        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (MusicianFinderResult.this, android.R.layout.simple_list_item_1, listMusician);
        lvMusicianResult.setAdapter(adapter);

        Intent intent = getIntent();
        Map<String, Object> searchMap = (HashMap<String, Object>)intent.getSerializableExtra("searchMap");

        for(Map.Entry<String, Object> entry : searchMap.entrySet()) {
            String key = entry.getKey();
            String value = (String) entry.getValue();

            HashMap<String, Object> innerMap = new HashMap<>();
            innerMap.put(key, value);

            DbGenerator.getDbInstance().query(DbUserType.Musician, innerMap, new DbCallback() {
                @Override
                public void queryCallback(List<User> userList) {
                for (User u: userList) {
                    if(!listMusician.contains((String) u.getEmailAddress()))
                        listMusician.add(u.getEmailAddress());
                }
                adapter.notifyDataSetChanged();
                }
            });
        }

        /*
        DbGenerator.getDbInstance().query(DbUserType.Musician, searchMap, new DbCallback() {
            @Override
            public void queryCallback(List<User> userList) {
                for (User u: userList) {
                    listMusician.add(u.getEmailAddress());
                }
                adapter.notifyDataSetChanged();
            }
        });
        */
    }
}
