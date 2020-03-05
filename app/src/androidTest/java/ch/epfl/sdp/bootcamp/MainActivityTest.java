package ch.epfl.sdp.bootcamp;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.MainActivity;
import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.DataBase;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testCanGreetUsers() {
        onView(withId(R.id.mainName)).perform(typeText("from my unit test")).perform(closeSoftKeyboard());
        onView(withId(R.id.mainGoButton)).perform(click());
        //onView(withId(R.id.greetingMessage)).check(matches(withText("Hello from my unit test!")));
    }

    private Map user1 = new HashMap() {{
        put("name", "josh");
        put("age", 30);
    }};

    private Map user2 = new HashMap() {{
        put("name", "tom");
        put("age", 40);
        put("rating", 3.5);
    }};

    @Test
    public void dbTest() {
        DataBase db = new DataBase();
        db.addDoc(user1, "user1");
        db.addDoc(user2, "user2");
        db.deleteDoc("am123");
        db.updateDoc("user1", new HashMap<String, Object>() {{ put("age", 31); }});
        db.deleteFieldsinDoc("user2", new ArrayList<String>() {{ add("rating"); }});
    }
}