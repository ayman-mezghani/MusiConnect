package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import ch.epfl.sdp.R;

public class ProfilePage extends StartPage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        // Open an intent when the profile button is clicked
        OnClickListener listener = view -> {
            Intent profileModificationIntent = new Intent(getApplicationContext(), ProfileModification.class);
            sendInformation(profileModificationIntent);
            startActivity(profileModificationIntent);
        };
        Button editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(listener);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.my_profile)
            return true;
        else
            super.onOptionsItemSelected(item);
        return true;
    }

    /**
     * Automatically fill the edit texts of profile modification page with actual string values
     * @param intent
     */
    private void sendInformation(Intent intent) {
        TextView firstName = findViewById(R.id.myFirstname);
        TextView lastName = findViewById(R.id.myLastname);
        TextView username = findViewById(R.id.myUsername);
        TextView mail = findViewById(R.id.myMail);
        TextView birthday = findViewById(R.id.myBirthday);

        intent.putExtra("FIRST_NAME", firstName.getText().toString());
        intent.putExtra("LAST_NAME", lastName.getText().toString());
        intent.putExtra("USERNAME", username.getText().toString());
        intent.putExtra("MAIL", mail.getText().toString());
        intent.putExtra("BIRTHDAY", birthday.getText().toString());
    }
}
