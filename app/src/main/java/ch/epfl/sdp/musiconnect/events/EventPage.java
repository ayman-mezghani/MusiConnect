package ch.epfl.sdp.musiconnect.events;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.Page;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbDataType;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.location.MapsActivity;

public abstract class EventPage extends Page {
    TextView titleView, creatorView, addressView, timeView, participantsView, descriptionView;
    ArrayList<String> emails; // emails is an ArrayList to be able to put it in an intent
    DbAdapter dbAdapter;
    Event event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = DbSingleton.getDbInstance();
        emails = new ArrayList<>();
    }

    void retrieveEventInfo(String eid) {
        if(eid != null) {
            dbAdapter.read(DbDataType.Events, eid, new DbCallback() {
                @Override
                public void readCallback(Event e) {
                    loadEventInfo(e);
                }
            });
        } else {
            loadNullEvent();
        }
    }

    protected void loadEventInfo(Event event) {
        if (event == null) {
            loadNullEvent();
            return;             // Return statement to address cognitive "complexity" "issue" from code clmate
        }

        this.event = event;
        titleView.setText(event.getTitle());
        creatorView.setText(event.getHostEmailAddress());
        addressView.setText(event.getAddress());
        timeView.setText(event.getDateTime().toString());


        StringBuilder s = new StringBuilder();
        for (String user : event.getParticipants()) {
            if (!emails.contains(user)) {
                emails.add(user);
            }
            s.append(user).append(System.lineSeparator());
        }

        participantsView.setText(s.toString());
        descriptionView.setText(event.getDescription());
    }

    protected void setupMapButton(){
        if(event != null && event.getLocation() != null) {
            Button toMap = findViewById(R.id.toMap);
            toMap.setOnClickListener(v -> new AlertDialog.Builder(EventPage.this)
                    .setMessage("Show the event's location on the map?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        Intent mapIntent = new Intent(EventPage.this, MapsActivity.class);
                        mapIntent.putExtra("Event", event.getEid());
                        this.startActivity(mapIntent);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create()
                    .show());
        }
    }

    private void loadNullEvent() {
        setContentView(R.layout.activity_event_page_null);
    }

}
