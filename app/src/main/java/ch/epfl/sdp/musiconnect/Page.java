package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ch.epfl.sdp.R;

public abstract class Page extends AppCompatActivity {
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
                Intent profileIntent = new Intent(this, ProfilePage.class);
                this.startActivity(profileIntent);
                return true;
            case R.id.settings:
                Intent settingsIntent = new Intent(this, SettingsPage.class);
                this.startActivity(settingsIntent);
                return true;
            case R.id.help:
                Intent helpIntent = new Intent(this, HelpPage.class);
                this.startActivity(helpIntent);
                return true;
            // In comments right now to avoid duplication
//            case R.id.search:
//                return true;
            case R.id.map:
                Intent mapsIntent = new Intent(this, MapsActivity.class);
                this.startActivity(mapsIntent);
                return true;
            default:
                displayNotFinishedFunctionalityMessage();
                return false;
        }
    }

    protected void displayNotFinishedFunctionalityMessage() {
        Toast.makeText(this, getString(R.string.not_yet_done), Toast.LENGTH_SHORT).show();
    }
}
