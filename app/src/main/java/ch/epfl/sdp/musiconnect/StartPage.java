package ch.epfl.sdp.musiconnect;

import androidx.appcompat.app.AppCompatActivity;

import ch.epfl.sdp.R;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


public class StartPage extends AppCompatActivity {
    private double lat = -34;
    private double lon = 151;
    private String mainMarkerName = "You";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_profile:
                Intent profileIntent = new Intent(StartPage.this, ProfilePage.class);
                this.startActivity(profileIntent);
                return true;
            case R.id.settings:
                Intent settingsIntent = new Intent(StartPage.this, SettingsPage.class);
                this.startActivity(settingsIntent);
                return true;
            case R.id.help:
                Intent helpIntent = new Intent(StartPage.this, HelpPage.class);
                this.startActivity(helpIntent);
                return true;
                // In comments right now to avoid duplication
//            case R.id.search:
//                return true;
            case R.id.map:
                startMap();
                return true;
            default:
                displayNotFinishedFunctionalityMessage();
                return false;
        }
    }

    protected void displayNotFinishedFunctionalityMessage() {
        Toast.makeText(this, getString(R.string.not_yet_done), Toast.LENGTH_SHORT).show();
    }

    private void startMap(){
        Intent myIntent = new Intent(StartPage.this, MapsActivity.class);


        myIntent.putExtra("lat", lat);
        myIntent.putExtra("lon", lon);
        myIntent.putExtra("mainMarkerName", mainMarkerName);
        StartPage.this.startActivity(myIntent);
    }
}
