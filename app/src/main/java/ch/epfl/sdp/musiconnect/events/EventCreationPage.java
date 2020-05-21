package ch.epfl.sdp.musiconnect.events;


import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.database.DbDataType;

public class EventCreationPage extends EventModificationPage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);

        eventTitleView = findViewById(R.id.eventCreationNewEventTitle);
        eventAddressView = findViewById(R.id.eventCreationNewEventAddress);
        if(getIntent().hasExtra("Address")){
            eventAddressView.setText(getIntent().getExtras().getString("Address"));
        }
        eventDescriptionView = findViewById(R.id.eventCreationNewEventDescription);
        eventParticipantView = findViewById(R.id.eventCreationNewParticipant);

        timeView = findViewById(R.id.eventCreationNewEventTime);
        dateView = findViewById(R.id.eventCreationNewEventDate);
        participantsView = findViewById(R.id.eventCreationNewEventParticipants);

        rdg = findViewById(R.id.eventCreationRdg);

        setupDateTimePickerDialog();
        setupButtons();
        setupDoNotSaveButton(R.id.eventCreationBtnDoNotSaveEvent, "Creation cancelled");
        setupSaveButton();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        bottomNavigationView.setSelectedItemId(R.id.my_events);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);
    }

    /**
    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        bottomNavigationView.setSelectedItemId(R.id.my_events);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);
    }
     */

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
        dbAdapter.add(DbDataType.valueOf(CurrentUser.getInstance(this).getTypeOfUser().toString()), event);
    }
}
