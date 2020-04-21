package ch.epfl.sdp.musiconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import ch.epfl.sdp.R;

public class EventCreation extends AppCompatActivity {

    EditText eventTitleView, eventAddressView, eventDescriptionView;
    TextView timeView, dateView, participantsView;
    int year, month, dayOfMonth, hour, minute;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    //TODO get "this" user as creator
    Event event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);

        eventTitleView = findViewById(R.id.eventCreationNewEventTitle);
        eventAddressView = findViewById(R.id.eventCreationNewEventAddress);
        eventDescriptionView = findViewById(R.id.eventCreationNewEventDescription);

        timeView = findViewById(R.id.eventCreationNewEventTime);
        dateView = findViewById(R.id.eventCreationNewEventDate);
        participantsView = findViewById(R.id.eventCreationNewEventParticipants);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        dateView.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(EventCreation.this,
                    (datePicker, year, month, day) -> dateView.setText(day + "/" + (month + 1) + "/" + year), year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        timeView.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(EventCreation.this,
                    (timePicker, hour, minute) -> timeView.setText(hour + ":" + minute), hour, minute,  DateFormat.is24HourFormat(this));

            timePickerDialog.show();
        });


        setupButtons();
    }

    private void setupButtons() {
        Button addParticipant = findViewById(R.id.eventCreationAddParticipants);
        addParticipant.setOnClickListener(v -> {
            // Show list of musicians that can be found -> Finder?
        });

        Button removeParticipant = findViewById(R.id.eventCreationRemoveParticipants);
        removeParticipant.setOnClickListener(v -> {

        });

        Button doNotSave = findViewById(R.id.eventCreationBtnDoNotSaveEvent);
        doNotSave.setOnClickListener(v -> finish());
        Button save = findViewById(R.id.eventCreationBtnSaveEvent);
        save.setOnClickListener(v -> {});
    }
}
