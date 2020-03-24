package ch.epfl.sdp.musiconnect;

import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sdp.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProfileModification extends AppCompatActivity implements View.OnClickListener {

    String firstName, lastName, username, mail, birthday;

    /**
     * Set the EditText fields the actual values when opening the intent
     * @param params
     */
    private void setTextFields(String [] params) {
        EditText[] fields = {
                findViewById(R.id.newFirstName),
                findViewById(R.id.newLastName),
                findViewById(R.id.newUsername),
                findViewById(R.id.newEmailAddress),
                findViewById(R.id.newBirthday)};

        int idx = 0;
        for (EditText f: fields) {
            f.setText(params[idx]);
            ++idx;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modification);

        firstName = getIntent().getStringExtra("FIRST_NAME");
        lastName = getIntent().getStringExtra("LAST_NAME");
        username = getIntent().getStringExtra("USERNAME");
        mail = getIntent().getStringExtra("MAIL");
        birthday = getIntent().getStringExtra("BIRTHDAY");

        setTextFields(new String[]{firstName, lastName, username, mail, birthday});

        Button saveProfile = findViewById(R.id.btnSaveProfile);
        saveProfile.setOnClickListener(this);
        Button doNotSaveProfile = findViewById(R.id.btnDoNotSaveProfile);
        doNotSaveProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveProfile:
                Toast.makeText(this, getString(R.string.in_construction), Toast.LENGTH_SHORT).show();
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
}
