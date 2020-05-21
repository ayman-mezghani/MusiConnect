package ch.epfl.sdp.musiconnect.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.Page;
import ch.epfl.sdp.musiconnect.TypeOfUser;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbDataType;
import ch.epfl.sdp.musiconnect.database.DbSingleton;

public class EventListPage extends Page {

    private DbAdapter dbAdapter;
    private String userEmail;
    private String visitorName;
    private TypeOfUser typeOfUser;


    private List<String> eventTitlesToShow;
    private List<Pair<String, String>> titleToIds; // [(EID, TITLE)]
    private boolean isVisitor;

    private ArrayAdapter<Pair<String, String>> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_page);

        dbAdapter = DbSingleton.getDbInstance();

        setupListTitle();


        ListView lv = findViewById(R.id.eventListView);
        eventTitlesToShow = new ArrayList<>();
        titleToIds = new ArrayList<>();


        adapter = new ArrayAdapter<>(EventListPage.this, android.R.layout.simple_list_item_1, titleToIds);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) -> loadEventPage(((Pair<String, String>)lv.getItemAtPosition(position)).first));
    }

    private void setupListTitle() {
        TextView eventListTitle = findViewById(R.id.eventListTitle);

        Intent intent = getIntent();
        isVisitor = intent.hasExtra("UserEmail");
        if (isVisitor) {
            userEmail = intent.getStringExtra("UserEmail");

            dbAdapter.read(DbDataType.Musician, userEmail, new DbCallback() {
                @Override
                public void readCallback(User user) {
                    visitorName = user.getName();
                    typeOfUser = ((Musician) user).getTypeOfUser();
                    eventListTitle.setText(String.format("%s's events", visitorName));
                }
            });
        } else {
            userEmail = CurrentUser.getInstance(this).email;
            typeOfUser = CurrentUser.getInstance(this).getTypeOfUser();
            eventListTitle.setText(R.string.your_events);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            eventTitlesToShow.clear();
            titleToIds.clear();
            adapter.notifyDataSetChanged();


            readFromDbAndLoadEvents(DbDataType.Musician);
        }, 500);
    }

    private void readFromDbAndLoadEvents(DbDataType dbDataType) {
        dbAdapter.read(dbDataType, userEmail, new DbCallback() {
            @Override
            public void readCallback(User user) {
                loadIds(user.getEvents(), dbDataType);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.my_events)
            return true;
        return super.onOptionsItemSelected(item);
    }


    private void loadIds(List<String> eventIds, DbDataType dbDataType) {
        final int[] counter = {0};
        if (eventIds.isEmpty()) {
            checkIfAllEventsAreLoaded(counter[0], 0, dbDataType);
        } else {
            for (String eid: eventIds) {
                dbAdapter.read(DbDataType.Events, eid, new DbCallback() {
                    @Override
                    public void readCallback(Event e) {
                        counter[0]++;
                        showEvent(e);
                        checkIfAllEventsAreLoaded(counter[0], eventIds.size(), dbDataType);
                    }

                    @Override
                    public void readFailCallback() {
                        counter[0]++;
                        // error: the event does not exist, so the entry in the database should be deleted
                        // Update only if user is on his own list (and after all callbacks have returned)
                        checkIfAllEventsAreLoaded(counter[0], eventIds.size(), dbDataType);
                    }
                });
            }

        }

    }

    private void showEvent(Event e) {
        String eid = e.getEid();
        String title = e.getTitle();

        // Show only events that are public or belong to this user or this user is a participant
        if (!containsId(eid) &&
                (e.isVisible() || e.containsParticipant(CurrentUser.getInstance(this).email)
                || e.getHostEmailAddress().equals(CurrentUser.getInstance(this).email))) {
            eventTitlesToShow.add(title);
            titleToIds.add(new Pair<>(eid, title));
            EventListPage.this.runOnUiThread(() -> adapter.notifyDataSetChanged());
        }
    }

    private boolean containsId(String eid) {
        for (Pair<String, String> pair : titleToIds) {
            if (pair.first.equals(eid)) {
                return true;
            }
        }
        return false;
    }

    private void checkIfAllEventsAreLoaded(int counter, int listSize, DbDataType dbDataType) {
        if (counter != listSize) {
            return;
        }

        if (dbDataType.toString().equals("musician") && typeOfUser.toString().equals("Band")) {
            // If we have loaded all events of the musician, we now need to load the events of his band
            readFromDbAndLoadEvents(DbDataType.Band);
        } else {
            // We have loaded everything from this user, now find all events he/she participates in
            HashMap<String, Object> h = new HashMap<>();
            h.put("participants", CurrentUser.getInstance(this).getMusician().getEmailAddress());
            DbSingleton.getDbInstance().query(DbDataType.Events, h, new DbCallback() {
                @Override
                public void queryCallback(List eventList) {
                    for (Object e: eventList) {
                        showEvent((Event) e);
                    }
                    checkIfNoEventsToShowPage();
                }

                @Override
                public void queryFailCallback() {
                    checkIfNoEventsToShowPage();
                }
            });


        }
    }

    private void checkIfNoEventsToShowPage() {
        // After loading all possible events, check if any events is shown.
        // If not, tell the user no events can be shown
        if (eventTitlesToShow.isEmpty()) {
            setContentView(R.layout.activity_event_list_page_none);
            TextView eventListTitle = findViewById(R.id.eventListTitle);

            if (isVisitor) {
                eventListTitle.setText(String.format("%s's events", visitorName));
            } else {
                eventListTitle.setText(R.string.your_events);
            }
        }
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
