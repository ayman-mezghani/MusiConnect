package ch.epfl.sdp.musiconnect.events;

import android.os.Bundle;

import ch.epfl.sdp.R;

public class VisitorEventPage extends EventPage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_event_page);

        titleView = findViewById(R.id.eventTitle);
        creatorView = findViewById(R.id.eventCreatorField);
        addressView = findViewById(R.id.eventAddressField);
        timeView = findViewById(R.id.eventTimeField);
        participantsView = findViewById(R.id.eventParticipantsField);
        descriptionView = findViewById(R.id.eventDescriptionField);

        String eid = getIntent().getStringExtra("eid");
        retrieveEventInfo(eid);
    }
}
