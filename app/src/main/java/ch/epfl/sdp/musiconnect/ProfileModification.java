package ch.epfl.sdp.musiconnect;

import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sdp.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ProfileModification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modification);

        View.OnClickListener listener = view -> {
            Toast.makeText(this, getString(R.string.in_construction), Toast.LENGTH_SHORT).show();
        };
        Button doNotSaveProfile = findViewById(R.id.btnDoNotSaveProfile);
        doNotSaveProfile.setOnClickListener(listener);
        Button SaveProfile = findViewById(R.id.btnSaveProfile);
        SaveProfile.setOnClickListener(listener);
    }
}
