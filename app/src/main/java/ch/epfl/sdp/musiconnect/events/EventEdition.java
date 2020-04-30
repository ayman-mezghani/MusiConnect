package ch.epfl.sdp.musiconnect.events;

import android.os.Bundle;
import android.widget.Button;


import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public class EventEdition extends EventModification {
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

        setDateTimePickerDefaultValue();
        setupDateTimePickerDialog();
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

    private void setDateTimePickerDefaultValue() {
        String datetime = getIntent().getStringExtra("datetime");
        String[] dateAndTime = datetime.split("\\s+");
        String date = dateAndTime[0];
        String time = dateAndTime[1];

        dateView.setText(date);
        timeView.setText(time);

        String[] hourMin = time.split(":");
        String[] dateMonthYear = date.split("\\.");

        year = Integer.parseInt(dateMonthYear[2]);
        month = Integer.parseInt(dateMonthYear[1]) - 1;
        dayOfMonth = Integer.parseInt(dateMonthYear[0]);
        hour = Integer.parseInt(hourMin[0]);
        minute = Integer.parseInt(hourMin[1]);
    }




    @Override
    void setupSaveButtons() {
        Button doNotSave = findViewById(R.id.eventEditionBtnDoNotSaveEvent);
        doNotSave.setOnClickListener(v -> {
            showToastWithText("Creation cancelled");
            finish();
        });

        Button save = findViewById(R.id.eventEditionBtnSaveEvent);
        save.setOnClickListener(v -> {
            if (checkEventCreationInput()) {
                sendToDatabase();
                showToastWithText("Event created");
                finish();
            }
        });
    }
}
