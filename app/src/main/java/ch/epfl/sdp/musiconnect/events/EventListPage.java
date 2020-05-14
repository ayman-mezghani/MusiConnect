package ch.epfl.sdp.musiconnect.events;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    private String visitorName;
    private DbUserType dbUserType;


    private List<String> eventTitles;
    private List<String> eventTitlesToShow;
    private Map<String, String> titleToIds;
    private boolean isVisitor;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_page);

        dbAdapter = DbGenerator.getDbInstance();

        setupListTitle();


        ListView lv = findViewById(R.id.eventListView);
        eventTitles = new ArrayList<>();
        eventTitlesToShow = new ArrayList<>();
        titleToIds = new HashMap<>();


        adapter = new ArrayAdapter<>(EventListPage.this, android.R.layout.simple_list_item_1, eventTitlesToShow);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) -> loadEventPage(titleToIds.get(lv.getItemAtPosition(position))));

    }

    private void setupListTitle() {
        TextView eventListTitle = findViewById(R.id.eventListTitle);

        Intent intent = getIntent();
        isVisitor = intent.hasExtra("UserEmail");
        if (isVisitor) {
            userEmail = intent.getStringExtra("UserEmail");

            // TODO get actual usertype from user
            dbUserType = DbUserType.Musician;
            dbAdapter.read(DbUserType.Musician, userEmail, new DbCallback() {
                @Override
                public void readCallback(User user) {
                    visitorName = user.getName();
                    eventListTitle.setText(String.format("%s's events", visitorName));
                }
            });
        } else {
            userEmail = CurrentUser.getInstance(this).email;

            if (CurrentUser.getInstance(this).getTypeOfUser().toString().equals("Musician")) {
                dbUserType = DbUserType.Musician;
            } else {
                dbUserType = DbUserType.Band;
            }

            eventListTitle.setText(R.string.your_events);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            eventTitles.clear();
            eventTitlesToShow.clear();
            titleToIds.clear();
            adapter.notifyDataSetChanged();

            if (!isVisitor) {
                readFromDbAndLoadEvents(dbUserType);
            } else {
                readFromDbAndLoadEvents(DbUserType.Musician);
                readFromDbAndLoadEvents(DbUserType.Band);
            }

        }, 500);
    }

    private void readFromDbAndLoadEvents(DbUserType dbUserType) {
        dbAdapter.read(dbUserType, userEmail, new DbCallback() {
            @Override
            public void readCallback(User user) {
                loadIds(user.getEvents());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.my_events)
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void loadIds(List<String> eventIds) {
        if (eventIds.isEmpty()) {
            setContentView(R.layout.activity_event_list_page_none);
            TextView eventListTitle = findViewById(R.id.eventListTitle);

            if (isVisitor) {
                eventListTitle.setText(String.format("%s's events", visitorName));
            } else {
                eventListTitle.setText(R.string.your_events);
            }
        }


        final int[] counter = {0};
        final boolean[] needUpdate = {false};

        for (String eid: eventIds) {
            dbAdapter.read(DbUserType.Events, eid, new DbCallback() {
                @Override
                public void readCallback(Event e) {
                    counter[0]++;
                    showEvent(e);
                    if (counter[0] == eventIds.size() && needUpdate[0]) {
                        updateListInDatabase();
                    }
                }

                @Override
                public void readFailCallback() {
                    counter[0]++;
                    needUpdate[0] = true;
                    // error: the event does not exist, so the entry in the database should be deleted
                    // Update only if user is on his own list (and after all callbacks have returned)
                    if (counter[0] == eventIds.size()) {
                        updateListInDatabase();
                    }
                }
            });
        }
    }

    private void showEvent(Event e) {
        String eid = e.getEid();
        String title = e.getTitle();

        if (!titleToIds.containsValue(eid)) {
            eventTitles.add(title);
            titleToIds.put(title, eid);

            // Show only events that are public or belong to "this" user
            if ((e.isVisible() || e.getCreator().getEmailAddress().equals(CurrentUser.getInstance(this).email))) {
                eventTitlesToShow.add(title);
                EventListPage.this.runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        }
    }

    private void updateListInDatabase() {
        List<String> ids = new ArrayList<>(titleToIds.values());

        dbAdapter.read(dbUserType, userEmail, new DbCallback() {
            @Override
            public void readCallback(User user) {
                user.setEvents(ids);
                dbAdapter.update(dbUserType, user); }
        });
    }

    private void loadEventPage(String eid) {
        if (!isVisitor) {
            Intent intent = new Intent(EventListPage.this, MyEventPage.class);
            intent.putExtra("eid", eid);
            EventListPage.this.startActivity(intent);
        } else {
            Intent intent = new Intent(EventListPage.this, VisitorEventPage.class);
            intent.putExtra("eid", eid);
            EventListPage.this.startActivity(intent);
        }
    }
}
