package ch.epfl.sdp.musiconnect;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataBaseTest {
    private DataBase db = new DataBase();

    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    private static void waitALittle() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> steven = new HashMap<String, Object>() {{
        put("name", "steven");
        put("age", 30L);
    }};

    private Map<String, Object> bob = new HashMap<String, Object>() {{
        put("name", "bob");
        put("age", 44L);
        put("rating", 3.5);
    }};

    @Test
    public void addAndReadDocTest() {
        String stevenDocName = steven.get("name").toString();
        db.addDoc(steven, stevenDocName);

        waitALittle();

        db.readDoc(stevenDocName, new DbCallback() {
            @Override
            public void onCallback(Map data) {
                assertEquals(steven, data);
            }
        });

        waitALittle();

        db.deleteDoc(stevenDocName);
    }

    @Test
    public void updateDocTest() {
        String docName = steven.get("name").toString();
        db.addDoc(steven, docName);
        db.updateDoc(docName, new HashMap<String, Object>() {{
            put("rating", 4);
            put("age", 35);
        }});

        waitALittle();

        db.readDoc(docName, new DbCallback() {
            @Override
            public void onCallback(Map data) {
                assertEquals(3, data.size());
                assertEquals(35L, data.get("age"));
                assertEquals(4L, data.get("rating"));
            }
        });
    }

    @Test
    public void deleteFieldsInDocTest() {
        String docName = bob.get("name").toString();
        db.addDoc(bob, docName);

        waitALittle();

        db.deleteFieldsInDoc(docName, new ArrayList<String>() {{
            add("age");
            add("rating");
        }});

        waitALittle();

        db.readDoc(docName, new DbCallback() {
            @Override
            public void onCallback(Map data) {
                Map<String, Object> m = new HashMap<>();
                m.put("name", "bob");
                assertTrue(m.equals(data));
            }
        });
    }
}
