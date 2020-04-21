package ch.epfl.sdp.musiconnect;

import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DataBase;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.roomdatabase.AppDatabase;
import ch.epfl.sdp.musiconnect.roomdatabase.MusicianDao;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.WaitUtility.waitALittle;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ProfileModificationTests {

    private AppDatabase roomDb;
    private MusicianDao musicianDao;
    private Executor mExecutor = Executors.newSingleThreadExecutor();
    private Musician defuser = new Musician("default","user","defuser","defuser@gmail.com",new MyDate(2000,1,1));
    private List<Musician> result;          //to fetch from database

    @Rule
    public final ActivityTestRule<MyProfilePage> profilePageRule =
            new ActivityTestRule<>(MyProfilePage.class);

    @Before
    public void waitAndCleanDB(){
        roomDb = AppDatabase.getInstance(profilePageRule.getActivity().getApplicationContext());
        musicianDao = roomDb.musicianDao();
        mExecutor.execute(() -> {
            musicianDao.nukeTable();
            musicianDao.insertAll(new Musician[]{defuser});
        });
    }


    @After
    public void cleanDatabaseAfterTest() {
        mExecutor.execute(() -> {
            musicianDao.nukeTable();
        });
        DataBase db = new DataBase();
        DbAdapter adapter = new DbAdapter(db);
        adapter.update(defuser);
    }
    /**
     * Helper method to avoid duplication
     * @param text: text to recognize on the clickable object
     */
    private void clickButtonWithText(int text) {
        onView(withText(text)).perform(ViewActions.scrollTo()).perform(click());
    }

    @Test
    public void testEditProfileAndDoNotSaveShouldDoNothing() {
        assertTrue(!ProfileModification.changeStaged);
        clickButtonWithText(R.string.edit_profile_button_text);
        onView(withId(R.id.newFirstName)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bob"));
        clickButtonWithText(R.string.do_not_save_profile);
        onView(withId(R.id.myFirstname)).check(matches(not(withText("Bob"))));
        assertTrue(!ProfileModification.changeStaged);
    }

    @Test
    public void testSelectASmallDateShouldDisplayErrorMessage() {
        clickButtonWithText(R.string.edit_profile_button_text);
        onView(withId(R.id.newBirthday)).perform(ViewActions.scrollTo()).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2015, 1, 1));
        onView(withText("OK")).perform(click());
        clickButtonWithText(R.string.save_profile);
        onView(withText(R.string.age_too_low)).inRoot(withDecorView(not(profilePageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void testEditProfileAndSaveShouldUpdateFields() {
        clickButtonWithText(R.string.edit_profile_button_text);
        onView(withId(R.id.newFirstName)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bob"));
        onView(withId(R.id.newLastName)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Mallet"));
        onView(withId(R.id.newUsername)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("BobMallet"));
        closeSoftKeyboard();

        onView(withId(R.id.newBirthday)).perform(ViewActions.scrollTo()).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2000, 1, 1));
        onView(withText("OK")).perform(click());

        clickButtonWithText(R.string.save_profile);
        onView(withId(R.id.myFirstname)).check(matches(withText("Bob")));
        onView(withId(R.id.myLastname)).check(matches(withText("Mallet")));
        onView(withId(R.id.myUsername)).check(matches(withText("BobMallet")));
        onView(withId(R.id.myBirthday)).check(matches(withText("01/01/2000")));
    }

    @Test
    public void testEditProfileAndSaveShouldUpdateCache() {
        clickButtonWithText(R.string.edit_profile_button_text);
        onView(withId(R.id.newFirstName)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Bob"));
        onView(withId(R.id.newLastName)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("Mallet"));
        onView(withId(R.id.newUsername)).perform(ViewActions.scrollTo()).perform(clearText(), typeText("BobMallet"));
        closeSoftKeyboard();

        onView(withId(R.id.newBirthday)).perform(ViewActions.scrollTo()).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2000, 1, 1));
        onView(withText("OK")).perform(click());

        clickButtonWithText(R.string.save_profile);

        waitALittle(2);

        mExecutor.execute(() -> {
            result = musicianDao.loadAllByIds(new String[]{"defuser@gmail.com"});
        });

        waitALittle(2);
        assertTrue(!result.isEmpty());
        Musician bob = result.get(0);
        assertEquals("Bob",bob.getFirstName());
        assertEquals("Mallet",bob.getLastName());
        assertEquals("BobMallet",bob.getUserName());
        assertEquals(new MyDate(2000,1,1),bob.getBirthday());

    }
}
