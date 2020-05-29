package ch.epfl.sdp.musiconnect.events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbDataType;

public class EventEditionPage extends EventModificationPage {
    private String eventTitle, eventAddress, eventDescription;


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

        if (getIntent().hasExtra("eid")) {
            onCreateGetIntentsFields();
            setDateTimePickerDefaultValue();
        }

        setupDateTimePickerDialog();
        setupButtons();
        setupDoNotSaveButton(R.id.eventEditionBtnDoNotSaveEvent, "Edition cancelled");
        setupSaveButton();
    }

    private void onCreateGetIntentsFields() {
        eventTitle = getIntent().getStringExtra("title");
        eventAddress = getIntent().getStringExtra("address");
        eventDescription = getIntent().getStringExtra("description");

        setFieldTexts();


        boolean visible = getIntent().getBooleanExtra("visible", false);
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
            dbAdapter.read(DbDataType.Musician, email, new DbCallback() {
                @Override
                public void readCallback(User user) {
                    participants.add(user);
                    updateParticipants();
                }
            });
        }
    }

    private void setDateTimePickerDefaultValue() {
        if (getIntent().hasExtra("datetime")) {
            String datetime = getIntent().getStringExtra("datetime");
            String[] dateAndTime = datetime.split("\\s+");
            String date = dateAndTime[0];
            String time = dateAndTime[1];

            dateView.setText(date);
            timeView.setText(time);

            String[] hourMin = time.split(":");
            String[] dateMonthYear = date.split("/");

            year = Integer.parseInt(dateMonthYear[2]);
            month = Integer.parseInt(dateMonthYear[1]) - 1;
            dayOfMonth = Integer.parseInt(dateMonthYear[0]);
            hour = Integer.parseInt(hourMin[0]);
            minute = Integer.parseInt(hourMin[1]);
        }
    }




    @Override
    void setupSaveButton() {
        Button save = findViewById(R.id.eventEditionBtnSaveEvent);
        save.setOnClickListener(v -> {
            if (checkEventCreationInput()) {
                sendToDatabase();
                showToastWithText("Event edited, updating...");


                Intent returnIntent = new Intent();
                returnIntent.putExtra("eid", getIntent().getStringExtra("eid"));

                returnIntent.putExtra("title", eventTitleView.getText().toString());
                returnIntent.putExtra("address", eventAddressView.getText().toString());
                returnIntent.putExtra("description", eventDescriptionView.getText().toString());

                returnIntent.putExtra("visible", rdg.getCheckedRadioButtonId() == R.id.visible);
                returnIntent.putExtra("date", dateView.getText().toString());
                returnIntent.putExtra("time", timeView.getText().toString());

                returnIntent.putStringArrayListExtra("emails", emails);

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }


    @Override
    void updateDatabase(Event event) {
        event.setEid(getIntent().getStringExtra("eid"));
        dbAdapter.update(DbDataType.Events, event);
    }
}
