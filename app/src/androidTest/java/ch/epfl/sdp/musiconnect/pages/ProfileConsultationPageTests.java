package ch.epfl.sdp.musiconnect.pages;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.users.Musician;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.roomdatabase.AppDatabase;
import ch.epfl.sdp.musiconnect.roomdatabase.MusicianDao;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.testsFunctions.waitSeconds;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ProfileConsultationPageTests {
    List<Musician> result;      //used to fetch data from room database

    @Rule
    public final ActivityTestRule<MyProfilePage> profilePageRule =
            new ActivityTestRule<>(MyProfilePage.class);

    @BeforeClass
    public static void setMocks() {
        DbSingleton.setDatabase(new MockDatabase(false));
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }

    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() { Intents.init(); }

    @After
    public void releaseIntents() { Intents.release(); }

    @Test
    public void testEditButtonShouldStartNewIntent() {
        onView(withText(R.string.edit_profile_button_text)).perform(click());
        intended(hasComponent(ProfileModificationPage.class.getName()));
    }

    @Test
    public void profileShowsDefaultUser(){
        waitSeconds(2);
        onView(withId(R.id.myFirstname)).check(matches(withText("bob")));
        onView(withId(R.id.myLastname)).check(matches(withText("minion")));
        onView(withId(R.id.myUsername)).check(matches(withText("bobminion")));
        onView(withId(R.id.myMail)).check(matches(withText("bobminion@gmail.com")));
        onView(withId(R.id.myBirthday)).check(matches(withText("1/1/2000")));
    }

    @Test
    public void profileIsCachedOnLoading(){
        waitSeconds(2);
        Executor mExecutor = Executors.newSingleThreadExecutor();
        AppDatabase localDb = AppDatabase.getInstance(profilePageRule.getActivity().getApplicationContext());
        MusicianDao mdao = localDb.musicianDao();
        mExecutor.execute(() -> {
            result = mdao.loadAllByIds(new String[]{"bobminion@gmail.com"});
        });
        waitSeconds(2);
        assertEquals(1,result.size());
        assertEquals("bobminion",result.get(0).getUserName());
    }

}
