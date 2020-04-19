package ch.epfl.sdp.musiconnect;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DataBase;
import ch.epfl.sdp.musiconnect.database.DbAdapter;


public class EventPage extends AppCompatActivity {
    private DataBase db;
    private DbAdapter dbAdapter;
    private boolean isTest;
    private TextView titleView, creatorView, locationView, timeView, descriptionView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        db = new DataBase();
        dbAdapter = new DbAdapter(db);

        titleView = findViewById(R.id.eventTitle);
        creatorView = findViewById(R.id.eventCreatorField);
        locationView = findViewById(R.id.eventLocationField);
        timeView = findViewById(R.id.eventTimeField);
        descriptionView = findViewById(R.id.eventDescriptionField);

        retrieveEventInfo();
    }

    private void retrieveEventInfo() {
        // TODO retrieve event from database
        // TODO test using mock database instead
        // TODO setup new Dbcallback for events

        int eid = getIntent().getIntExtra("EID", -1);

        Event event;
        event = createDummyEvent(eid);

        /*
        event = dbAdapter.read(getIntent().getIntExtra("EID", -1), new DbCallback() {
            @Override
            public void onCallback(Event event) {
                loadEventInfo(event);
            }
        });
        */


        loadEventInfo(event);
    }

    private void loadEventInfo(Event event) {
        if (event == null) {
            loadNullEvent();
        } else {
            titleView.setText(event.getTitle());
            creatorView.setText(event.getCreator().getName());
            locationView.setText(event.getAddress());
            timeView.setText(event.getDateTime().toString());
            descriptionView.setText(event.getMessage());
        }
    }

    private void loadNullEvent() {
        setContentView(R.layout.null_event_page);
    }

    // TODO This function is to be deleted / replaced by MockDatabase query
    private Event createDummyEvent(int eid) {
        if (eid == 0) {
            Musician m1 = new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25));
            m1.setLocation(new MyLocation(46.52, 6.52));

            Event e1 = new Event(m1, 0);
            e1.setAddress("Westminster, London, England");
            e1.setLocation(51.5007, 0.1245);
            e1.setDateTime(new MyDate(2020, 9, 21, 14, 30));
            e1.setTitle("Event at Big Ben!");
            e1.setMessage("Playing at Big Ben, come watch us play!");

            return e1;
        }
        return null;
    }
}
