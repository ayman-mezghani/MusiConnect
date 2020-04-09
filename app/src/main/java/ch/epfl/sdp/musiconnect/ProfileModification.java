package ch.epfl.sdp.musiconnect;

import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.*;
import ch.epfl.sdp.musiconnect.UserCreation.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileModification extends AppCompatActivity implements View.OnClickListener {

    String firstName, lastName, username, mail, birthday;
    EditText[] editFields;
    final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modification);

        editFields = new EditText[] {
                findViewById(R.id.newFirstName),
                findViewById(R.id.newLastName),
                findViewById(R.id.newUsername),
                findViewById(R.id.newEmailAddress),
                findViewById(R.id.newBirthday)};

        manageDatePickerDialog(editFields[4]);

        firstName = getIntent().getStringExtra("FIRST_NAME");
        lastName = getIntent().getStringExtra("LAST_NAME");
        username = getIntent().getStringExtra("USERNAME");
        mail = getIntent().getStringExtra("MAIL");
        birthday = getIntent().getStringExtra("BIRTHDAY");
        setEditTextFields(editFields, new String[]{firstName, lastName, username, mail, birthday});

        Button saveProfile = findViewById(R.id.btnSaveProfile);
        saveProfile.setOnClickListener(this);
        Button doNotSaveProfile = findViewById(R.id.btnDoNotSaveProfile);
        doNotSaveProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveProfile:
                String[] newFields = getNewTextFields();
                if (!isInputDateValid()) {
                    Toast.makeText(this, R.string.age_too_low, Toast.LENGTH_LONG).show();
                    break;
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("newFields", newFields);
                updateDatabaseFields(newFields);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.btnDoNotSaveProfile:
                finish(); // Close current activity and do not save anything
                break;
            default:
                // In case another thing happens, simply close the activity and display an error message
                Toast.makeText(this, getString(R.string.error_in_current_process), Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Check the correctness of the input date
     * @return true if age >= 13
     */
    private boolean isInputDateValid() {
        UserCreation uc = new UserCreation();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return Integer.parseInt(uc.getAge(year, month, day)) >= 13;
    }

    private void setEditTextFields(EditText[] fields, String[] params) {
        int idx = 0;
        for (EditText f: fields) {
            f.setText(params[idx]);
            ++idx;
        }
    }

    /**
     * Update the new values to the database
     * @param newFields: new values to write
     */
    private void updateDatabaseFields(String[] newFields) {
        DataBase db = new DataBase();
        DbAdapter adapter = new DbAdapter(db);
        Map<String, Object> data = new HashMap<>();
        String[] keys = {"firstName", "lastName", "username", "email", "birthday"};
        for (int i = 0; i < keys.length; ++i) {
            if (keys[i].equals("birthday")) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy"); // KEEP THIS DATE FORMAT !
                try {
                    Date d = format.parse(newFields[i]);
                    data.put(keys[i], new Timestamp(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else
                data.put(keys[i], newFields[i]);
        }
        data.put("location", new GeoPoint(0, 0));

        Musician me = new SimplifiedMusician(data).toMusician();
        adapter.update(me);
    }

    private String[] getNewTextFields() {
        int l = editFields.length;
        String[] newFields = new String[l];
        for (int i = 0; i < editFields.length; i++)
            newFields[i] = editFields[i].getText().toString();
        return newFields;
    }

    /**
     * Helper method to initialize the datepicker dialog
     * @param bdayField
     */
    private void manageDatePickerDialog(EditText bdayField) {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(editFields[4]);
        };
        bdayField.setOnClickListener(v -> new DatePickerDialog(
                ProfileModification.this, date, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void updateLabel(EditText et) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        et.setText(sdf.format(calendar.getTime()));
    }
}
