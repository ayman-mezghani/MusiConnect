package ch.epfl.sdp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ch.epfl.sdp.musiconnect.UserLocationActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.mainGoButton);
        button.setOnClickListener(v -> mainGoButton());
    }

    private void mainGoButton(){
        Intent myIntent = new Intent(MainActivity.this, UserLocationActivity.class);

        MainActivity.this.startActivity(myIntent);
    }

}
