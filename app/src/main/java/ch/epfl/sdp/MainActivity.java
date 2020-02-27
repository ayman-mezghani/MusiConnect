package ch.epfl.sdp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.mainGoButton);
        button.setOnClickListener(v -> mainGoButton());
    }

    private void mainGoButton(){
        Intent myIntent = new Intent(MainActivity.this, GreetingActivity.class);

        EditText e = findViewById(R.id.mainName);
        myIntent.putExtra("msg", "Hello " + e.getText().toString() + "!"); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }

}
