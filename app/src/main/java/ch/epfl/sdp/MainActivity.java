package ch.epfl.sdp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Double.parseDouble;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.mainGoButton);
        button.setOnClickListener(v -> mainGoButton());

        final Button mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(v -> mapButton());
    }

    private void mainGoButton(){
        Intent myIntent = new Intent(MainActivity.this, GreetingActivity.class);

        EditText e = findViewById(R.id.mainName);
        myIntent.putExtra("msg", "Hello " + e.getText().toString() + "!"); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }

    private void mapButton(){
        Intent myIntent = new Intent(MainActivity.this, MapsActivity.class);
        double lat = Double.parseDouble(((EditText)findViewById(R.id.lat)).getText().toString());
        double lon = Double.parseDouble(((EditText)findViewById(R.id.lon)).getText().toString());
        String markerName = ((EditText)findViewById(R.id.markerName)).getText().toString();
        myIntent.putExtra("lat", lat);
        myIntent.putExtra("lon", lon);
        myIntent.putExtra("markerName", markerName);
        MainActivity.this.startActivity(myIntent);
    }



}
