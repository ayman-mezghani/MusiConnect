package ch.epfl.sdp.musiconnect.events;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.Page;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public abstract class EventPage extends Page {
    TextView titleView, creatorView, addressView, timeView, participantsView, descriptionView;
    ArrayList<String> emails; // emails is an ArrayList to be able to put it in an intent
    DbAdapter dbAdapter;
    Event event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = DbGenerator.getDbInstance();
        emails = new ArrayList<>();
    }

    void retrieveEventInfo(String eid) {
        if(eid != null) {
            dbAdapter.read(DbUserType.Events, eid, new DbCallback() {
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
            return;             // Return statement to address cognitive "complexity" "issue" from code clmate
        }

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

    private void loadNullEvent() {
        setContentView(R.layout.activity_event_page_null);
    }

}
