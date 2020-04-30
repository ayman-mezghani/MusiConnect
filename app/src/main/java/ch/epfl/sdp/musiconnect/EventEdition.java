package ch.epfl.sdp.musiconnect;

import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public class EventEdition extends EventCreation {
    private String eventTitle, eventAddress, eventDescription;
    private boolean visible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edition);

        eventTitleView = findViewById(R.id.eventEditionNewEventTitle);
        eventAddressView = findViewById(R.id.eventEditionNewEventAddress);
        eventDescriptionView = findViewById(R.id.eventEditionNewEventDescription);
        eventParticipantView = findViewById(R.id.eventEditionNewParticipant);

        timeView = findViewById(R.id.eventEditionNewEventTime);
        dateView = findViewById(R.id.eventEditionNewEventDate);
        participantsView = findViewById(R.id.eventEditionNewEventParticipants);

        rdg = findViewById(R.id.eventEditionRdg);

        onCreateGetIntentsFields();

        setupDateTimePickerDialog();
        setDateTimePickerToDefaultValue();
        setupButtons();

    }

    private void onCreateGetIntentsFields() {
        eventTitle = getIntent().getStringExtra("title");
        eventAddress = getIntent().getStringExtra("address");
        eventDescription = getIntent().getStringExtra("description");

        setFieldTexts();


        visible = getIntent().getBooleanExtra("visible", false);
        if (visible) {
            rdg.check(R.id.visible);
        } else {
            rdg.check(R.id.notVisible);
        }


        emails = getIntent().getStringArrayListExtra("emails");
        retrieveParticipantsFromEmails();
    }

    private void setFieldTexts() {
        eventTitleView.setText(eventTitle);
        eventAddressView.setText(eventAddress);
        eventDescriptionView.setText(eventDescription);
    }

    private void retrieveParticipantsFromEmails() {
        for (String email: emails) {
            dbAdapter.read(DbUserType.Musician, email, new DbCallback() {
                @Override
                public void readCallback(User user) {
                    participants.add((Musician)user);
                    updateParticipants();
                }
            });
        }
    }

    private void setDateTimePickerToDefaultValue() {
        datePickerDialog.updateDate();
        timePickerDialog.updateTime();
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
