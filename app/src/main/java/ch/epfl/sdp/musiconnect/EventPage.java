package ch.epfl.sdp.musiconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

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

        try {
            Class.forName("androidx.test.espresso.Espresso");
            isTest = true;
        } catch (ClassNotFoundException e) {
            isTest = false;
        }

        if (isTest) {
            db = new DataBase();
            // db = new MockDatabase();
        } else {
            db = new DataBase();
        }

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
        Event event = createDummyEvent();

        loadEventInfo(event);
    }

    private void loadEventInfo(Event event) {
        if (event == null) {
            throw new IllegalArgumentException();
        }

        titleView.setText(event.getTitle());
        creatorView.setText(event.getCreator().toString());
        locationView.setText(event.getAddress());
        timeView.setText(event.getDateTime().toString());
        descriptionView.setText(event.getMessage());
    }

    private Event createDummyEvent() {
        Musician dummyUser = new Musician("Alice", "LeGrand", "Alice", "a@gmail.com", new MyDate());
        Event dummyEvent = new Event(dummyUser, 0);
        dummyEvent.setAddress("Westminster, London, England");
        dummyEvent.setLocation(51.5007, 0.1245);
        dummyEvent.setDateTime(new MyDate(2020, 9, 21, 14, 30));
        dummyEvent.setTitle("Event at Big Ben!");
        dummyEvent.setMessage("Playing at Big Ben, come watch us play!");

        return dummyEvent;
    }
}
