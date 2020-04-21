package ch.epfl.sdp.musiconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DataBase;
import ch.epfl.sdp.musiconnect.database.DbAdapter;

public class EventCreation extends AppCompatActivity {

    private DataBase db;
    private DbAdapter dbAdapter;

    EditText eventTitleView, eventAddressView, eventDescriptionView, eventParticipantView;
    TextView timeView, dateView, participantsView;
    int year, month, dayOfMonth, hour, minute;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;


    // List<User> participants;
    List<String> participants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);

        db = new DataBase();
        dbAdapter = new DbAdapter(db);

        participants = new ArrayList<>();


        eventTitleView = findViewById(R.id.eventCreationNewEventTitle);
        eventAddressView = findViewById(R.id.eventCreationNewEventAddress);
        eventDescriptionView = findViewById(R.id.eventCreationNewEventDescription);
        eventParticipantView = findViewById(R.id.eventCreationNewParticipant);

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
            String username = eventParticipantView.getText().toString();

            if (username.equals("")) {
                Toast.makeText(this, "Please add a username", Toast.LENGTH_SHORT).show();
                return;
            }
            /*
            dbAdapter.read(username, user -> {
                if (user != null){
                    participants.add(user);
                    updateParticipants();
                } else {
                    Toast.makeText(this, "Please add a valid username", Toast.LENGTH_SHORT).show();
                }
            });
             */

            participants.add(username);
            updateParticipants();
        });

        Button removeParticipant = findViewById(R.id.eventCreationRemoveParticipants);
        removeParticipant.setOnClickListener(v -> {
            String username = eventParticipantView.getText().toString();

            if (username.equals("")) {
                Toast.makeText(this, "Please add a username", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!participants.contains(username)) {
                Toast.makeText(this, "This user is not in the participants list", Toast.LENGTH_SHORT).show();
            } else {
                participants.remove(username);
                updateParticipants();
            }
        });

        Button doNotSave = findViewById(R.id.eventCreationBtnDoNotSaveEvent);
        doNotSave.setOnClickListener(v -> finish());
        Button save = findViewById(R.id.eventCreationBtnSaveEvent);
        save.setOnClickListener(v -> {});
    }


    private void updateParticipants() {
        StringBuilder sb = new StringBuilder();

        /*
        for (User user : participants) {
            sb.append(user.getName()).append(System.lineSeparator());
        }*/

        for (String s : participants) {
            sb.append(s).append(System.lineSeparator());
        }

        eventParticipantView.setText(sb.toString());
    }


    private void getTextValues() {
        // TODO get "this" user as creator
        // Event event = new Event()

        Map<String, Object> data = new HashMap<>();
        data.put("title", eventTitleView.getText().toString());
        data.put("address", eventAddressView.getText().toString());
        data.put("description",eventDescriptionView.getText().toString());
        String time = timeView.getText().toString();
        String date = dateView.getText().toString();

        String[] hourMin = time.split(":");
        String[] dateMonthYear = date.split("/");
        MyDate d = new MyDate(Integer.parseInt(dateMonthYear[2]),
                Integer.parseInt(dateMonthYear[1]),
                Integer.parseInt(dateMonthYear[0]),
                Integer.parseInt(hourMin[0]),
                Integer.parseInt(hourMin[1]));


    }

}
