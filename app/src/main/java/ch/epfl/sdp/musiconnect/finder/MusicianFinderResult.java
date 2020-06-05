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
import ch.epfl.sdp.musiconnect.users.Musician;
import ch.epfl.sdp.musiconnect.pages.MyProfilePage;
import ch.epfl.sdp.musiconnect.pages.VisitorProfilePage;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.DbDataType;

public class MusicianFinderResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musician_finder_result);

        List<Musician> listMusician = new ArrayList<>();
        List<String> listMusiciaEmails = new ArrayList<>();

        ListView lvMusicianResult = findViewById(R.id.LvMusicianResult);

        listViewAddOnItemClickListner(lvMusicianResult);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(MusicianFinderResult.this, android.R.layout.simple_list_item_1, listMusiciaEmails);
        lvMusicianResult.setAdapter(adapter);

        Map<String, Object> searchMap = (HashMap<String, Object>)getIntent().getSerializableExtra("searchMap");

        for(Map.Entry<String, Object> entry : searchMap.entrySet()) {
            HashMap<String, Object> innerMap = new HashMap<>();
            innerMap.put(entry.getKey(), (String) entry.getValue());

            DbSingleton.getDbInstance().query(DbDataType.Musician, innerMap, new DbCallback() {
                @Override
                public void queryCallback(List userList) {
                    queryFunction(userList, (ArrayList<Musician>) listMusician, (ArrayList<String>) listMusiciaEmails, adapter);
                }
            });
        }
    }

    private void listViewAddOnItemClickListner(ListView l) {
        l.setOnItemClickListener((parent, view, position, id) -> {
            String musicianEmail = getMusicianEmailFromList((String) l.getItemAtPosition(position));
            Intent i;
            if(musicianEmail.equals(CurrentUser.getInstance(MusicianFinderResult.this).email)) {
                i = new Intent(MusicianFinderResult.this, MyProfilePage.class);
            } else {
                i = new Intent(MusicianFinderResult.this, VisitorProfilePage.class);
                i.putExtra("UserEmail", musicianEmail);
            }
            startActivity(i);
        });
    }

    private String getMusicianEmailFromList(String listViewItem) {
        return listViewItem.split("\n")[1].trim();
    }

    private void queryFunction(List userList, ArrayList<Musician> lm, ArrayList<String> lme, ArrayAdapter<String> adapter) {

        Location userLoc = CurrentUser.getInstance(MusicianFinderResult.this).getLocation();
        for (Object u: userList) {
            if(!lme.contains(((Musician) u).getEmailAddress())) {
                lme.add(((Musician) u).getEmailAddress());
                Location uLoc = new Location("");
                uLoc.setLatitude(((Musician) u).getLocation().getLatitude());
                uLoc.setLongitude(((Musician) u).getLocation().getLongitude());
                ((Musician) u).setDistanceToCurrentUser(userLoc.distanceTo(uLoc));
                lm.add((Musician) u);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lm.sort(Comparator.comparingDouble(Musician::getDistanceToCurrentUser));
        }

        lme.clear();
        for(Musician m: lm) {
            String s = m.getName() + "\n" + m.getEmailAddress() + "\n" + String.valueOf(Math.round(m.getDistanceToCurrentUser())) + " m";
            lme.add(s);
        }

        adapter.notifyDataSetChanged();
    }
}
