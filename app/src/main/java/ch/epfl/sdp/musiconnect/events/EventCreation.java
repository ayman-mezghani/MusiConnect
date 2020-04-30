package ch.epfl.sdp.musiconnect.events;


import android.os.Bundle;
import android.widget.Button;

import ch.epfl.sdp.R;

public class EventCreation extends EventModification {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);

        eventTitleView = findViewById(R.id.eventCreationNewEventTitle);
        eventAddressView = findViewById(R.id.eventCreationNewEventAddress);
        eventDescriptionView = findViewById(R.id.eventCreationNewEventDescription);
        eventParticipantView = findViewById(R.id.eventCreationNewParticipant);

        timeView = findViewById(R.id.eventCreationNewEventTime);
        dateView = findViewById(R.id.eventCreationNewEventDate);
        participantsView = findViewById(R.id.eventCreationNewEventParticipants);

        rdg = findViewById(R.id.eventCreationRdg);

        setupDateTimePickerDialog();
        setupButtons();
    }

    @Override
    void setupSaveButtons() {
       Button doNotSave = findViewById(R.id.eventCreationBtnDoNotSaveEvent);
        doNotSave.setOnClickListener(v -> {
            showToastWithText("Creation cancelled");
            finish();
        });

        Button save = findViewById(R.id.eventCreationBtnSaveEvent);
        save.setOnClickListener(v -> {
            if (checkEventCreationInput()) {
                sendToDatabase();
                showToastWithText("Event created");
                finish();
            }
        });
    }
}
