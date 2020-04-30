package ch.epfl.sdp.musiconnect.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.Page;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public class EventListPage extends Page {

    DbAdapter dbAdapter;
    TextView eventListTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_page);

        eventListTitle = findViewById(R.id.eventListTitle);

        Intent intent = getIntent();
        String visitorEmail = intent.getStringExtra("UserEmail");
        ListView lv = findViewById(R.id.eventListView);
        List<String> events = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();


        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (EventListPage.this, android.R.layout.simple_list_item_1, events);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) -> loadEventPage(ids.get(position)));



        if (visitorEmail != null) {
            dbAdapter = DbGenerator.getDbInstance();
            dbAdapter.read(DbUserType.Musician, visitorEmail, new DbCallback() {
                @Override
                public void readCallback(User user) {
                    eventListTitle.setText(String.format("%s's events", user.getName()));
                }
            });

        } else {
            eventListTitle.setText("Your events");
        }


        // TODO
        // loads events (title and eids) from the user (either current user or visitor)
        final String DEFAULT_TITLE = "Event";
        events.add(DEFAULT_TITLE);
        ids.add(1);
        adapter.notifyDataSetChanged();

        /*
        for(String e : (ArrayList<String>) CurrentUser.getInstance(this).getBand().getEvents()){
            DbGenerator.getDbInstance().read(DbUserType.Events, e.trim(), new DbCallback() {
                @Override
                public void readCallback(Event u) {
                    events.add(u.getTitle());
                    adapter.notifyDataSetChanged();
                }
            });
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.my_events) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void loadEventPage(int eid) {
        Intent intent = new Intent(EventListPage.this, EventPage.class);
        intent.putExtra("EID", eid);
        EventListPage.this.startActivity(intent);
    }
}
