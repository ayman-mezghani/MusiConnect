package ch.epfl.sdp.musiconnect.events;


import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.database.DbUserType;

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
        setupDoNotSaveButton(R.id.eventCreationBtnDoNotSaveEvent);
        setupSaveButton();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        bottomNavigationView.setSelectedItemId(R.id.my_events);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> super.onOptionsItemSelected(item));
    }

    @Override
    void setupSaveButton() {
        Button save = findViewById(R.id.eventCreationBtnSaveEvent);
        save.setOnClickListener(v -> {
            if (checkEventCreationInput()) {
                sendToDatabase();
                showToastWithText("Event created");
                finish();
            }
        });
    }

    @Override
    void updateDatabase(Event event) {
        dbAdapter.add(event, DbUserType.valueOf(CurrentUser.getInstance(this).getTypeOfUser().toString()));
    }
}
