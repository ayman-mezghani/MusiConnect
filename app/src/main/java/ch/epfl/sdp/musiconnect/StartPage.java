package ch.epfl.sdp.musiconnect;

import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sdp.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_profile:
                return true;
                // In comments right now to avoid duplication
            /*case R.id.map:
                return true;
            case R.id.search:
                return true;
            case R.id.settings:
                return true;
            case R.id.help:
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
