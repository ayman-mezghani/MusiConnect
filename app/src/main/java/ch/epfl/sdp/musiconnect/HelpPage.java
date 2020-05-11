package ch.epfl.sdp.musiconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public class HelpPage extends Page {

    HashMap<String, Object> m = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);

        DbAdapter db = DbGenerator.getDbInstance();

        m.put("firstName", "MusiConnect");

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
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.help)
            return true;
        return super.onOptionsItemSelected(item);
    }
}