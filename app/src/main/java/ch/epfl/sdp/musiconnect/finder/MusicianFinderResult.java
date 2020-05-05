package ch.epfl.sdp.musiconnect.finder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.StartPage;
import ch.epfl.sdp.musiconnect.User;
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

        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (MusicianFinderResult.this, android.R.layout.simple_list_item_1, listMusician);
        lvMusicianResult.setAdapter(adapter);

        Intent intent = getIntent();
        Map<String, Object> searchMap = (HashMap<String, Object>)intent.getSerializableExtra("searchMap");

        DbGenerator.getDbInstance().query(DbUserType.Musician, searchMap, new DbCallback() {
            @Override
            public void queryCallback(List<User> userList) {
                for (User u: userList) {
                    listMusician.add(u.getEmailAddress());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
