package ch.epfl.sdp.musiconnect.events;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.Page;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;


public class EventPage extends Page {
    private TextView titleView, creatorView, addressView, timeView, participantsView, descriptionView;
    private ArrayList<String> emails; // emails is an ArrayList to be able to put it in an intent
    private Event event;
    private static int LAUNCH_EVENT_EDIT_INTENT = 105;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO
        // If event is from current user, show if visible or not to public

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        String eid = getIntent().getStringExtra("eid");

        emails = new ArrayList<>();

        titleView = findViewById(R.id.eventTitle);
        creatorView = findViewById(R.id.eventCreatorField);
        addressView = findViewById(R.id.eventAddressField);
        timeView = findViewById(R.id.eventTimeField);
        participantsView = findViewById(R.id.eventParticipantsField);
        descriptionView = findViewById(R.id.eventDescriptionField);

        setupEditButton();
        
        retrieveEventInfo(eid);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LAUNCH_EVENT_EDIT_INTENT && resultCode == Activity.RESULT_OK) {
            Handler handler = new Handler();
            handler.postDelayed(() -> retrieveEventInfo(data.getStringExtra("eid")), 2500);
        }
    }


    private void setupEditButton() {
        Button editEvent = findViewById(R.id.btnEditEvent);
        editEvent.setOnClickListener(v -> {
            Intent intent = new Intent(EventPage.this, EventEdition.class);
            intent.putExtra("eid", getIntent().getStringExtra("eid"));
            intent.putExtra("title", titleView.getText().toString());
            intent.putExtra("address", addressView.getText().toString());
            intent.putExtra("description", descriptionView.getText().toString());
            intent.putExtra("visible", event.isVisible());
            intent.putStringArrayListExtra("emails", emails);
            intent.putExtra("datetime", timeView.getText().toString());

            startActivityForResult(intent, LAUNCH_EVENT_EDIT_INTENT);
        });
    }


    private void retrieveEventInfo(String eid) {
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
    }

    private void loadEventInfo(Event event) {
        if (event == null) {
            loadNullEvent();
        } else {
            this.event = event;
            titleView.setText(event.getTitle());
            creatorView.setText(event.getCreator().getName());
            addressView.setText(event.getAddress());
            timeView.setText(event.getDateTime().toString());


            StringBuilder s = new StringBuilder();
            for (User user : event.getParticipants()) {
                if (!emails.contains(user.getEmailAddress())) {
                    emails.add(user.getEmailAddress());
                }
                s.append(user.getName()).append(System.lineSeparator());
            }

            participantsView.setText(s.toString());
            descriptionView.setText(event.getDescription());
        }
    }

    private void loadNullEvent() {
        setContentView(R.layout.activity_event_page_null);
    }

}
