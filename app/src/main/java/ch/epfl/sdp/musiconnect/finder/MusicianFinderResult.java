package ch.epfl.sdp.musiconnect.finder;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.Musician;
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

        List<Musician> listMusician = new ArrayList<>();
        List<String> listMusiciaEmails = new ArrayList<>();

        ListView lvMusicianResult = findViewById(R.id.LvMusicianResult);

        listViewAddOnItemClickListner(lvMusicianResult);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (MusicianFinderResult.this, android.R.layout.simple_list_item_1, listMusiciaEmails);
        lvMusicianResult.setAdapter(adapter);

        Intent intent = getIntent();
        Map<String, Object> searchMap = (HashMap<String, Object>)intent.getSerializableExtra("searchMap");

        for(Map.Entry<String, Object> entry : searchMap.entrySet()) {
            HashMap<String, Object> innerMap = new HashMap<>();
            innerMap.put(entry.getKey(), (String) entry.getValue());

            DbGenerator.getDbInstance().query(DbUserType.Musician, innerMap, new DbCallback() {
                @Override
                public void queryCallback(List<User> userList) {
                    Location userLoc = CurrentUser.getInstance(MusicianFinderResult.this).getLocation();
                    for (User u: userList) {
                        if(!listMusiciaEmails.contains((String) u.getEmailAddress())) {
                            listMusiciaEmails.add(u.getEmailAddress());
                            Location uLoc = new Location("");
                            uLoc.setLatitude(u.getLocation().getLatitude());
                            uLoc.setLongitude(u.getLocation().getLongitude());
                            ((Musician) u).setDistanceToCurrentUser(userLoc.distanceTo(uLoc));
                            listMusician.add((Musician) u);
                        }
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        listMusician.sort(Comparator.comparingDouble(Musician::getDistanceToCurrentUser));
                    }

                    listMusiciaEmails.clear();
                    for(Musician m: listMusician) {
                        String s = m.getName() + "\n" + m.getEmailAddress() + "\n" + String.valueOf(Math.round(m.getDistanceToCurrentUser())) + " m";
                        listMusiciaEmails.add(s);
                    }
                
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void listViewAddOnItemClickListner(ListView l) {
        l.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(MusicianFinderResult.this, VisitorProfilePage.class);
            i.putExtra("UserEmail", getMusicianEmailFromList((String) l.getItemAtPosition(position)));
            startActivity(i);
        });
    }

    private String getMusicianEmailFromList(String listViewItem) {
        return listViewItem.split("\n")[1].trim();
    }
}
