package ch.epfl.sdp.musiconnect.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.Page;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public class EventListPage extends Page {

    private DbAdapter dbAdapter;
    private String userEmail;
    private List<String> eventTitles;
    private Map<String, String> ids;
    private boolean isVisitor;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_page);

        dbAdapter = DbGenerator.getDbInstance();

        TextView eventListTitle = findViewById(R.id.eventListTitle);

        Intent intent = getIntent();
        isVisitor = intent.hasExtra("UserEmail");
        if (isVisitor) {
            userEmail = intent.getStringExtra("UserEmail");
            dbAdapter.read(DbUserType.Musician, userEmail, new DbCallback() {
                @Override
                public void readCallback(User user) {
                    eventListTitle.setText(String.format("%s's events", user.getName()));
                }
            });
        } else {
            userEmail = CurrentUser.getInstance(this).email;
            eventListTitle.setText(R.string.your_events);
        }


        ListView lv = findViewById(R.id.eventListView);
        eventTitles = new ArrayList<>();
        ids = new HashMap<>();


        adapter = new ArrayAdapter<>(EventListPage.this, android.R.layout.simple_list_item_1, eventTitles);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) -> loadEventPage(ids.get(lv.getItemAtPosition(position))));

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Wait a bit before updating list
        eventTitles.clear();
        ids.clear();
        adapter.notifyDataSetChanged();

        dbAdapter.read(DbUserType.Musician, userEmail, new DbCallback() {
            @Override
            public void readCallback(User user) {
                loadIds(user.getEvents());
            }
        });

        dbAdapter.read(DbUserType.Band, userEmail, new DbCallback() {
            @Override
            public void readCallback(User user) {
                loadIds(user.getEvents());
            }
        });
    }


    private void loadIds(List<String> eventIds) {
        for (String eid: eventIds) {
            dbAdapter.read(DbUserType.Events, eid, new DbCallback() {
                @Override
                public void readCallback(Event e) {
                    showEvent(e);
                }
            });
        }
    }


    private void showEvent(Event e) {
        String eid = e.getEid();
        String title = e.getTitle();

        if (!ids.containsValue(eid)) {
            eventTitles.add(title);
            ids.put(title, eid);

            EventListPage.this.runOnUiThread(() -> adapter.notifyDataSetChanged());
        }
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.my_events && !isVisitor) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void loadEventPage(String eid) {
        Intent intent = new Intent(EventListPage.this, MyEventPage.class);
        intent.putExtra("eid", eid);
        EventListPage.this.startActivity(intent);
    }
}
