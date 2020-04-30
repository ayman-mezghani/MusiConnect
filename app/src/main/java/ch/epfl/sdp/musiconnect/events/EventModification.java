package ch.epfl.sdp.musiconnect.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.epfl.sdp.R;

import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.Page;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public abstract class EventModification extends Page {

    DbAdapter dbAdapter;

    EditText eventTitleView, eventAddressView, eventDescriptionView, eventParticipantView;
    TextView timeView, dateView, participantsView;
    int year, month, dayOfMonth, hour, minute;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    RadioGroup rdg;

    List<Musician> participants;
    List<String> emails; //List to keep track of users in the list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = DbGenerator.getDbInstance();
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

    abstract void setupSaveButtons();


    void setupDateTimePickerDialog() {
        dateView.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(EventModification.this,
                    (datePicker, year, month, day) -> {
                        dateView.setText(day + "/" + (month + 1) + "/" + year);
                        calendar.set(year, month, day);
                    }
                    , year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        timeView.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(EventModification.this,
                    (timePicker, hour, minute) -> {
                        timeView.setText(hour + ":" + minute);
                        calendar.set(year, month, dayOfMonth, hour, minute);
                    }, hour, minute,  DateFormat.is24HourFormat(this));

            timePickerDialog.show();
        });
    }

    void setupButtons() {
        Button addParticipant = findViewById(R.id.eventAddParticipants);
        addParticipant.setOnClickListener(v -> {
            String email = eventParticipantView.getText().toString();

            if (email.equals("")) {
                showToastWithText("Please add an email");
                return;
            }

            if (emails.contains(email)) {
                showToastWithText("This user is already in the participants list");
            } else {
                emails.add(email);
                dbAdapter.read(DbUserType.Musician, email, new DbCallback() {
                    @Override
                    public void readCallback(User user) {
                        participants.add((Musician)user);
                        updateParticipants();
                    }

                    @Override
                    public void readFailCallback() {
                        showToastWithText("Please add a valid email");
                    }
                });
            }
        });

        Button removeParticipant = findViewById(R.id.eventRemoveParticipants);
        removeParticipant.setOnClickListener(v -> {
            String email = eventParticipantView.getText().toString();

            if (email.equals("")) {
                showToastWithText("Please add an email");
                return;
            }
            if (emails.contains(email)) {
                emails.remove(email);
                for (Musician m: participants) {
                    if (m.getEmailAddress().equals(email)) {
                        participants.remove(m);
                        updateParticipants();
                    }
                }
            } else {
                showToastWithText("This user is not in the participants list");
            }
        });

        setupSaveButtons();
    }


    void showToastWithText(String string) {
        EventModification.this.runOnUiThread(() -> Toast.makeText(EventModification.this,
                string, Toast.LENGTH_LONG).show());
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
        dbAdapter.read(DbUserType.Musician, CurrentUser.getInstance(this).email, new DbCallback() {
            @Override
            public void readCallback(User user) {
                Event event = new Event(user, "0");
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

                for (Musician musician: participants) {
                    event.register(musician);
                }

                event.setDateTime(d);
                event.setVisible(rdg.getCheckedRadioButtonId() == R.id.visible);
            }
        });
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
