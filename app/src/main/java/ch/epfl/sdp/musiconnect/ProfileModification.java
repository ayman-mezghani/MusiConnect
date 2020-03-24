package ch.epfl.sdp.musiconnect;

import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sdp.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileModification extends AppCompatActivity implements View.OnClickListener {

    //ProfilePage profilePage;

    String firstName, lastName, username, mail, birthday;
    EditText[] editFields;
    TextView[] textFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modification);

        //profilePage = new ProfilePage();

        editFields = new EditText[] {
                findViewById(R.id.newFirstName),
                findViewById(R.id.newLastName),
                findViewById(R.id.newUsername),
                findViewById(R.id.newEmailAddress),
                findViewById(R.id.newBirthday)};

        /*textFields = new TextView[] {
                profilePage.findViewById(R.id.myFirstname),
                profilePage.findViewById(R.id.myLastname),
                profilePage.findViewById(R.id.myUsername),
                profilePage.findViewById(R.id.myMail),
                profilePage.findViewById(R.id.myBirthday)};*/

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //setTextViewFields(textFields, editFields);
    }

    /**
     * Set the EditText fields the actual values when opening the intent
     * @param fields
     * @param params
     */
    private void setEditTextFields(EditText[] fields, String[] params) {
        int idx = 0;
        for (EditText f: fields) {
            f.setText(params[idx]);
            ++idx;
        }
    }

    /**
     * Set the ProfilePage TextView fields when saving the new data
     * @param fields
     * @param params
     */
    private void setTextViewFields(TextView[] fields, EditText[] params) {
        int idx = 0;
        for (TextView t: fields) {
            t.setText(params[idx].getText().toString());
            ++idx;
        }
    }
}
