package ch.epfl.sdp.musiconnect.events;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.Page;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;


public class EventPage extends Page {
    private DbAdapter dbAdapter;
    private TextView titleView, creatorView, addressView, timeView, participantsView, descriptionView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        dbAdapter = DbGenerator.getDbInstance();

        titleView = findViewById(R.id.eventTitle);
        creatorView = findViewById(R.id.eventCreatorField);
        addressView = findViewById(R.id.eventAddressField);
        timeView = findViewById(R.id.eventTimeField);
        participantsView = findViewById(R.id.eventParticipantsField);
        descriptionView = findViewById(R.id.eventDescriptionField);

        retrieveEventInfo();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.my_events) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void retrieveEventInfo() {
        // TODO retrieve event from database
        // TODO test using mock database instead
        // TODO setup new DbCallback for events

        String eid = getIntent().getStringExtra("eid");
        if(eid != null) {
            DbGenerator.getDbInstance().read(DbUserType.Events, eid, new DbCallback() {
                @Override
                public void readCallback(Event e) {
                    loadEventInfo(e);
                }
            });
        } else {
            loadNullEvent();
        }

        //createDummyEvent(String.valueOf(eid));

        /*
        event = dbAdapter.read(getIntent().getIntExtra("EID", -1), new DbCallback() {
            @Override
            public void onCallback(Event event) {
                loadEventInfo(event);
            }
        });
        */
    }

    private void loadEventInfo(Event event) {
        if (event == null) {
            loadNullEvent();
        } else {
            titleView.setText(event.getTitle());
            creatorView.setText(event.getCreator().getName());
            addressView.setText(event.getAddress());
            timeView.setText(event.getDateTime().toString());

            StringBuilder s = new StringBuilder();
            for (User user : event.getParticipants()) {
                s.append(user.getName()).append(System.lineSeparator());
            }
            participantsView.setText(s.toString());
            descriptionView.setText(event.getDescription());
        }
    }

    private void loadNullEvent() {
        setContentView(R.layout.activity_event_page_null);
    }

    // TODO This function is to be deleted / replaced by MockDatabase query
    private void createDummyEvent(String eid) {
        if (eid.equals("1")) {
            Musician m2 = new Musician("Carson", "Calme", "CallmeCarson", "callmecarson41@gmail.com", new MyDate(1995, 4, 1));

            dbAdapter.read(DbUserType.Musician, CurrentUser.getInstance(this).email, new DbCallback() {
                @Override
                public void readCallback(User user) {
                    Event e1 = new Event(user, eid);
                    e1.setAddress("Westminster, London, England");
                    e1.setLocation(51.5007, 0.1245);
                    e1.setDateTime(new MyDate(2020, 9, 21, 14, 30));
                    e1.setTitle("Event at Big Ben!");
                    e1.register(m2);
                    e1.setDescription("Playing at Big Ben, come watch us play!");

                    loadEventInfo(e1);
                }
            });
        } else {
            loadNullEvent();
        }
    }
}
