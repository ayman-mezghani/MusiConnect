package ch.epfl.sdp.musiconnect;

import android.location.Location;

import androidx.test.rule.ActivityTestRule;


import org.junit.Rule;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sdp.musiconnect.database.DataBase;

public class Dbt {
    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    private final String TAG = "DbTest";

    private DataBase db = new DataBase();

    @Test
    public void db1() {
        Map<String, Object> res = new HashMap<>();
        res.put("uid", null);
        res.put("username", "user1");
        res.put("first_name", "us");
        res.put("last_name", "er");
        res.put("birthday", new Date());
        res.put("email", "user@gmail.com");
        res.put("join_date", new Date());
        Location loc = new Location("dummy");
        loc.setLatitude(0);
        loc.setLongitude(0);
        res.put("location", loc);

        db.addDoc(res, "adapter test");
    }
}
