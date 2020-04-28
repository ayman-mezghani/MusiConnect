package ch.epfl.sdp.musiconnect.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.GoogleLogin;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.Page;
import ch.epfl.sdp.musiconnect.TypeOfUser;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public class EventCreation extends Page {

    private DbAdapter dbAdapter;

    EditText eventTitleView, eventAddressView, eventDescriptionView, eventParticipantView;
    TextView timeView, dateView, participantsView;
    int year, month, dayOfMonth, hour, minute;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    Event event;
    // List<User> participants;
    List<String> participants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);

        dbAdapter = DbGenerator.getDbInstance();

        participants = new ArrayList<>();


        eventTitleView = findViewById(R.id.eventCreationNewEventTitle);
        eventAddressView = findViewById(R.id.eventCreationNewEventAddress);
        eventDescriptionView = findViewById(R.id.eventCreationNewEventDescription);
        eventParticipantView = findViewById(R.id.eventCreationNewParticipant);

        timeView = findViewById(R.id.eventCreationNewEventTime);
        dateView = findViewById(R.id.eventCreationNewEventDate);
        participantsView = findViewById(R.id.eventCreationNewEventParticipants);

        /*eventAddressView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                getLocationFromAddress(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,int before, int count) {
            }
        });*/

        setupDateTimePickerDialog();
        setupButtons();
    }

    private void setupDateTimePickerDialog() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        dateView.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(EventCreation.this,
                    (datePicker, year, month, day) -> dateView.setText(day + "/" + (month + 1) + "/" + year), year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        timeView.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(EventCreation.this,
                    (timePicker, hour, minute) -> timeView.setText(hour + ":" + minute), hour, minute,  DateFormat.is24HourFormat(this));

            timePickerDialog.show();
        });
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
            if (participants.contains(username)) {
                Toast.makeText(this, R.string.user_already_in_participation_list, Toast.LENGTH_SHORT).show();
            } else {
                participants.add(username);
                updateParticipants();
            }
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
        doNotSave.setOnClickListener(v -> {
            Toast.makeText(this, R.string.creation_cancelled, Toast.LENGTH_SHORT).show();
            finish();
        });

        Button save = findViewById(R.id.eventCreationBtnSaveEvent);
        save.setOnClickListener(v -> {
            if (checkEventCreationInput()) {
                sendToDatabase();
                Toast.makeText(this, "Event created", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.create_event) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void updateParticipants() {
        eventParticipantView.getText().clear();

        StringBuilder sb = new StringBuilder();

        /*
        for (User user : participants) {
            sb.append(user.getName()).append(System.lineSeparator());
        }*/

        for (String s : participants) {
            sb.append(s).append(System.lineSeparator());
        }

        participantsView.setText(sb.toString());
    }


    public void sendToDatabase() {
        event = new Event(CurrentUser.getInstance(this).getMusician(), "0");
        event.setTitle(eventTitleView.getText().toString());
        event.setAddress(eventAddressView.getText().toString());
        event.setDescription(eventDescriptionView.getText().toString());

        String time = timeView.getText().toString();
        String date = dateView.getText().toString();

        String[] hourMin = time.split(":");
        String[] dateMonthYear = date.split("/");
        MyDate d = new MyDate(Integer.parseInt(dateMonthYear[2]),
                Integer.parseInt(dateMonthYear[1]),
                Integer.parseInt(dateMonthYear[0]),
                Integer.parseInt(hourMin[0]),
                Integer.parseInt(hourMin[1]));

        GeoPoint gp = getLocationFromAddress(event.getAddress());
        event.setLocation(gp.getLatitude(), gp.getLongitude());
        event.setDateTime(d);

        DbAdapter db = DbGenerator.getDbInstance();

        db.add(event, DbUserType.Band);
    }


    public boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    public boolean isEmpty(TextView textView) {
        return textView.getText().toString().trim().length() == 0;
    }

    public boolean checkEventCreationInput() {
        if (isEmpty(eventTitleView)) {
            Toast.makeText(this, "Please give the event a title", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (isEmpty(eventAddressView)) {
            Toast.makeText(this, "Please give the event an address", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (isEmpty(eventDescriptionView)) {
            Toast.makeText(this, "Please give the event a description", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (isEmpty(timeView)) {
            Toast.makeText(this, "Please give the event a time", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (isEmpty(dateView)) {
            Toast.makeText(this, "Please give the event a date", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public GeoPoint getLocationFromAddress(String strAddress){
        Geocoder coder = new Geocoder(this);
        List<Address> address = null;
        GeoPoint p1 = null;

        try {
            try {
                address = coder.getFromLocationName(strAddress,5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint((double) (location.getLatitude()),
                    (double) (location.getLongitude()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p1;
    }
}
