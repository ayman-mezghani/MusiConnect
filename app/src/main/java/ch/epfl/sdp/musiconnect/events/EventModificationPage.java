package ch.epfl.sdp.musiconnect.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ch.epfl.sdp.R;

import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.users.Musician;
import ch.epfl.sdp.musiconnect.functionnalities.MyDate;
import ch.epfl.sdp.musiconnect.pages.Page;
import ch.epfl.sdp.musiconnect.users.User;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.DbDataType;

public abstract class EventModificationPage extends Page {

    DbAdapter dbAdapter;

    EditText eventTitleView, eventAddressView, eventDescriptionView, eventParticipantView;
    TextView timeView, dateView, participantsView;
    int year, month, dayOfMonth, hour, minute;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    RadioGroup rdg;

    List<User> participants;
    ArrayList<String> emails; // emails is an ArrayList to be able to put it in an intent

    abstract void setupSaveButton();

    abstract void updateDatabase(Event event);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = DbSingleton.getDbInstance();
        participants = new ArrayList<>();
        emails = new ArrayList<>();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.create_event) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    void setupDateTimePickerDialog() {
        dateView.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(EventModificationPage.this,
                    (datePicker, year, month, day) -> {
                        dateView.setText(String.format(Locale.FRANCE, "%d/%d/%d", day, month + 1, year));
                        calendar.set(year, month, day);
                    }
                    , year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        timeView.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(EventModificationPage.this,
                    (timePicker, hour, minute) -> {
                        timeView.setText(String.format(Locale.FRANCE, "%d:%d", hour, minute));
                        calendar.set(year, month, dayOfMonth, hour, minute);
                    }, hour, minute, DateFormat.is24HourFormat(this));

            timePickerDialog.show();
        });
    }


    void setupButtons() {
        Button addParticipant = findViewById(R.id.eventAddParticipants);
        addParticipant.setOnClickListener(v -> {
            String email = eventParticipantView.getText().toString();

            if (email.equals("")) {
                showToastWithText("Please add an email");
            } else if (email.equals(CurrentUser.getInstance(this).email)) {
                showToastWithText("You are already a participant!");
            } else if (emails.contains(email)) {
                showToastWithText("This user is already in the participants list");
            } else {
                addEmailAndUser(email); // too many lines for code climate...
            }
        });

        setupRemoveButton();
    }

    private void addEmailAndUser(String email) {
        emails.add(email);
        dbAdapter.read(DbDataType.Musician, email, new DbCallback() {
            @Override
            public void readCallback(User user) {
                participants.add((Musician) user);
                updateParticipants();
            }

            @Override
            public void readFailCallback() {
                showToastWithText("Please add a valid email");
            }
        });
    }

    private void setupRemoveButton() {
        Button removeParticipant = findViewById(R.id.eventRemoveParticipants);
        removeParticipant.setOnClickListener(v -> {
            String email = eventParticipantView.getText().toString();

            if (email.equals("")) {
                showToastWithText("Please add an email");
            } else if (email.equals(CurrentUser.getInstance(this).email)) {
                showToastWithText("You cannot remove yourself!");
            } else if (emails.contains(email)) {
                emails.remove(email);
                for (User m : participants) {
                    if (m.getEmailAddress().equals(email)) {
                        participants.remove(m);
                        updateParticipants();
                    }
                }
            } else {
                showToastWithText("This user is not in the participants list");
            }
        });
    }

    void setupDoNotSaveButton(int id, String text) {
        Button doNotSave = findViewById(id);
        doNotSave.setOnClickListener(v -> {
            showToastWithText(text);
            finish();
        });

        setupSaveButton();
    }

    void showToastWithText(String string) {
        Toast.makeText(EventModificationPage.this, string, Toast.LENGTH_LONG).show();
    }

    void updateParticipants() {
        eventParticipantView.getText().clear();

        StringBuilder sb = new StringBuilder();

        for (User user : participants) {
            sb.append(user.getName()).append(System.lineSeparator());
        }

        participantsView.setText(sb.toString());
    }

    void sendToDatabase() {
        Context ctx = this;
        dbAdapter.read(DbDataType.Musician, CurrentUser.getInstance(this).email, new DbCallback() {
            @Override
            public void readCallback(User user) {
                Event event = new Event(user.getEmailAddress(), "0");
                event.setTitle(eventTitleView.getText().toString());
                event.setAddress(eventAddressView.getText().toString());
                event.setDescription(eventDescriptionView.getText().toString());

                event.setDateTime(setEventDateAndTime());

                for (User musician : participants) {
                    event.register(musician.getEmailAddress());
                }

                event.setVisible(rdg.getCheckedRadioButtonId() == R.id.visible);

                GeoPoint p1 = getLocationFromAddress(event.getAddress());

                if (p1 == null) {
                    event.setLocation(0, 0);
                    Toast.makeText(ctx, R.string.unable_to_resolve_address, Toast.LENGTH_SHORT).show();
                } else {
                    event.setLocation(p1.getLatitude(), p1.getLongitude());
                }

                updateDatabase(event);
            }
        });
    }

    private MyDate setEventDateAndTime() {
        String time = timeView.getText().toString();
        String date = dateView.getText().toString();

        String[] hourMin = time.split(":");
        String[] dateMonthYear = date.split("/");

        return new MyDate(Integer.parseInt(dateMonthYear[2]), Integer.parseInt(dateMonthYear[1]), Integer.parseInt(dateMonthYear[0]),
                Integer.parseInt(hourMin[0]), Integer.parseInt(hourMin[1]));
    }

    public GeoPoint getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address = null;
        GeoPoint p1 = null;

        try {
            try {
                address = coder.getFromLocationName(strAddress, 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (address == null || address.isEmpty()) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint(location.getLatitude(),
                    location.getLongitude());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p1;
    }

    public boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    public boolean isEmpty(TextView textView) {
        return textView.getText().toString().trim().length() == 0;
    }

    public boolean checkEventCreationInput() {
        boolean empty = true;
        if (isEmpty(eventTitleView)) {
            Toast.makeText(this, "Please give the event a title", Toast.LENGTH_LONG).show();
            empty = false;
        }
        if (isEmpty(eventAddressView)) {
            Toast.makeText(this, "Please give the event an address", Toast.LENGTH_LONG).show();
            empty = false;
        }
        if (isEmpty(eventDescriptionView)) {
            Toast.makeText(this, "Please give the event a description", Toast.LENGTH_LONG).show();
            empty = false;
        }

        if (isEmpty(timeView)) {
            Toast.makeText(this, "Please give the event a time", Toast.LENGTH_LONG).show();
            empty = false;
        }

        if (isEmpty(dateView)) {
            Toast.makeText(this, "Please give the event a date", Toast.LENGTH_LONG).show();
            empty = false;
        }

        return empty;
    }
}
