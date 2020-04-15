package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ch.epfl.sdp.musiconnect.RoomDatabase.AppDatabase;
import ch.epfl.sdp.musiconnect.RoomDatabase.MusicianDao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


//@RunWith(AndroidJUnit4.class)
public class RoomDatabaseTest {

    private AppDatabase roomDb;
    private MusicianDao musicianDao;
    private Executor mExecutor = Executors.newSingleThreadExecutor();
    private List<Musician> allUsers = new ArrayList<>();
    private List<Musician> users = new ArrayList<>();



    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    @Before
    public void saveDatabaseContentsBeforeTesting() {
        roomDb = AppDatabase.getInstance(startPageRule.getActivity().getApplicationContext());
        musicianDao = roomDb.musicianDao();
        mExecutor.execute(() -> {
            allUsers = musicianDao.getAll();
            musicianDao.nukeTable();
        });
    }

    @After
    public void putBackDatabaseContents() {
        mExecutor.execute(() -> {
            musicianDao.insertAll(allUsers.toArray(new Musician[allUsers.size()]));
        });
    }


    @Test
    public void databaseIsEmpty(){
        mExecutor.execute(() -> {
            users = musicianDao.getAll();
        });
        waitALittle(2);
        assertTrue(users.isEmpty());
    }

    @Test
    public void musicianIsAddedCorrectly(){
        Musician person1 = new Musician("Sauce", "deSaucisse", "test", "sauce@gmail.com", new MyDate(1990, 10, 25));
        person1.addInstrument(Instrument.BAGPIPES,Level.PROFESSIONAL);
        person1.addInstrument(Instrument.BANJO,Level.BEGINNER);
        person1.setVideoURL("test.com");
        mExecutor.execute(() -> {
            musicianDao.insertAll(person1);
        });
        mExecutor.execute(() -> {
            users = musicianDao.getAll();
        });
        waitALittle(2);
        assertEquals(1,users.size());
        Musician user1 = users.get(0);
        assertEquals(person1.toString(),user1.toString());
    }

    @Test
    public void musicianIsDeletedCorrectly(){
        Musician person1 = new Musician("Sauce", "deSaucisse", "test", "sauce@gmail.com", new MyDate(1990, 10, 25));
        person1.addInstrument(Instrument.BAGPIPES,Level.PROFESSIONAL);
        person1.addInstrument(Instrument.BANJO,Level.BEGINNER);
        person1.setVideoURL("test.com");
        Musician person2 = new Musician("Carson", "Calme", "CallmeCarson", "callmecarson41@gmail.com", new MyDate(1995, 4, 1));

        mExecutor.execute(() -> {
            musicianDao.insertAll(person1);
            musicianDao.insertAll(person2);
        });
        mExecutor.execute(() -> {
            users = musicianDao.getAll();
        });
        waitALittle(2);
        assertEquals(2,users.size());

        mExecutor.execute(() -> {
            musicianDao.delete(person2);
        });
        mExecutor.execute(() -> {
            users = musicianDao.getAll();
        });
        waitALittle(2);
        assertEquals(1,users.size());
        assertEquals(person1.toString(),users.get(0).toString());

    }

    public static void waitALittle(int t) {
        try {
            TimeUnit.SECONDS.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
