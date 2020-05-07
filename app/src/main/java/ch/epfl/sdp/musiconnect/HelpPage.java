package ch.epfl.sdp.musiconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.List;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;
import ch.epfl.sdp.musiconnect.database.FirebaseDatabase;

public class HelpPage extends Page {

    HashMap<String, Object> m = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);

        DbAdapter db = DbGenerator.getDbInstance();

        m.put("firstName", "MusiConnect");
        //m.put("username", "Booba");

        db.query(DbUserType.Musician, m, new DbCallback() {
            @Override
            public void queryCallback(List<User> userList) {
                Log.d("checkcheck", "callback, "+ userList.size());
                if(userList.isEmpty()) {
                    Toast.makeText(HelpPage.this, "No results found", Toast.LENGTH_SHORT).show();
                }
                for (User user : userList)
                    Log.d("checkcheck", user.getClass().getName()+" " +  user.toString());
            }
        });

        // BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        // bottomNavigationView.setOnNavigationItemSelectedListener(item -> super.onOptionsItemSelected(item));
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.help)
            return true;
        else
            super.onOptionsItemSelected(item);
        return true;
    }
}