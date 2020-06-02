package ch.epfl.sdp.musiconnect;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.pages.StartPage;
import ch.epfl.sdp.musiconnect.roomdatabase.AppDatabase;
import ch.epfl.sdp.musiconnect.roomdatabase.EventDao;
import ch.epfl.sdp.musiconnect.roomdatabase.InstrumentConverter;
import ch.epfl.sdp.musiconnect.roomdatabase.MusicianDao;
import ch.epfl.sdp.musiconnect.roomdatabase.MyDateConverter;
import ch.epfl.sdp.musiconnect.roomdatabase.MyLocationConverter;

import static ch.epfl.sdp.musiconnect.testsFunctions.waitSeconds;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class RoomDatabaseTest {

    private AppDatabase roomDb;
    private MusicianDao musicianDao;
    private EventDao eventDao;
    private Executor mExecutor = Executors.newSingleThreadExecutor();
    private List<Musician> users = new ArrayList<>();

    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    @BeforeClass
    public static void setMocks() {
        DbSingleton.setDatabase(new MockDatabase(false));
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }

    @Before
    public void instantiateTestRoomDatabase() {
        roomDb = AppDatabase.getInstance(startPageRule.getActivity().getApplicationContext());
        musicianDao = roomDb.musicianDao();
        eventDao = roomDb.eventDao();
        mExecutor.execute(() -> {
            musicianDao.nukeTable();
            eventDao.nukeTable();
        });
        waitSeconds(1);
    }


    @After
    public void cleanDatabaseAfterTest() {
        mExecutor.execute(() -> {
            musicianDao.nukeTable();
            eventDao.nukeTable();
        });
        waitSeconds(1);
    }


    @Test
    public void databaseIsEmpty(){
        mExecutor.execute(() -> {
            users = musicianDao.getAll();
        });
        waitSeconds(5);
        assertTrue(users.isEmpty());
    }

    @Test
    public void musicianIsAddedCorrectly(){
        Musician person1 = new Musician("Sauce", "deSaucisse", "test", "sauce@gmail.com", new MyDate(1990, 10, 25));
        person1.addInstrument(Instrument.BAGPIPES,Level.PROFESSIONAL);
        person1.addInstrument(Instrument.BANJO,Level.BEGINNER);
        person1.setVideoURL("test.com");
        person1.setLocation(new MyLocation(40,40));
        mExecutor.execute(() -> {
            musicianDao.insertAll(person1);
        });
        mExecutor.execute(() -> {
            users = musicianDao.getAll();
        });
        waitSeconds(5);
        assertEquals(1,users.size());
        Musician user1 = users.get(0);
        //assertEquals(person1.toString(),user1.toString());
        assertEquals(person1.getFirstName()+person1.getLastName()+person1.getEmailAddress()+person1.getUserName()+person1.getAge()+person1.getVideoURL(),
                user1.getFirstName()+user1.getLastName()+user1.getEmailAddress()+user1.getUserName()+user1.getAge()+user1.getVideoURL());
        assertEquals(person1.getJoinDate(),user1.getJoinDate());
        assertEquals(person1.getInstruments(),(user1.getInstruments()));
        assertEquals(person1.getLocation(),(new MyLocation(40,40)));
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
        waitSeconds(5);
        assertEquals(2,users.size());

        mExecutor.execute(() -> {
            musicianDao.delete(person2);
        });
        mExecutor.execute(() -> {
            users = musicianDao.getAll();
        });
        waitSeconds(5);
        assertEquals(1,users.size());
        Musician user1 = users.get(0);
        assertEquals(person1.getFirstName()+person1.getLastName()+person1.getEmailAddress()+person1.getUserName()+person1.getAge()+person1.getVideoURL(),
                user1.getFirstName()+user1.getLastName()+user1.getEmailAddress()+user1.getUserName()+user1.getAge()+user1.getVideoURL());
        assertEquals(person1.getJoinDate(),user1.getJoinDate());
        assertEquals(person1.getInstruments(),(user1.getInstruments()));
    }


    @Test
    public void canFetchMusicianBasedOnEmail(){
        Musician person1 = new Musician("Sauce", "deSaucisse", "test", "sauce@gmail.com", new MyDate(1990, 10, 25));
        Musician person2 = new Musician("Carson", "Calme", "CallmeCarson", "callmecarson41@gmail.com", new MyDate(1995, 4, 1));
        mExecutor.execute(() -> {
            musicianDao.insertAll(person1);
            musicianDao.insertAll(person2);
        });
        waitSeconds(5);
        mExecutor.execute(() -> {
            users = musicianDao.loadAllByIds(new String[]{"sauce@gmail.com"});
        });
        waitSeconds(5);
        assertEquals(1,users.size());
        Musician user1 = users.get(0);
        assertEquals(person1.getFirstName()+person1.getLastName()+person1.getEmailAddress()+person1.getUserName()+person1.getAge()+person1.getVideoURL(),
                user1.getFirstName()+user1.getLastName()+user1.getEmailAddress()+user1.getUserName()+user1.getAge()+user1.getVideoURL());
        assertEquals(person1.getJoinDate(),user1.getJoinDate());
        assertEquals(person1.getInstruments(),(user1.getInstruments()));
    }

    @Test
    public void fetchingUnknowEmailReturnsEmpty(){
        Musician person1 = new Musician("Sauce", "deSaucisse", "test", "sauce@gmail.com", new MyDate(1990, 10, 25));
        mExecutor.execute(() -> {
            musicianDao.insertAll(person1);
        });
        waitSeconds(2);
        mExecutor.execute(() -> {
            users = musicianDao.loadAllByIds(new String[]{"callmecarson41@gmail.com"});
        });
        waitSeconds(2);
        assertTrue(users.isEmpty());
    }

    @Test
    public void instrumentConverterTest(){
        HashMap<Instrument,Level> map = new HashMap<>();
        for (Instrument i:Instrument.values()){
            for (Level l:Level.values()){
                map.put(i,l);
            }
        }
        assertEquals(map, (InstrumentConverter.toInstrumentMap(InstrumentConverter.toStringList(map))));
    }

    @Test
    public void myDateConverterTest(){
        MyDate date1 = new MyDate(9,9,9,9,9);
        MyDate date2 = new MyDate(666,6,6);

        assertEquals(date1, (MyDateConverter.fromTimestamp(MyDateConverter.dateToTimestamp(date1))));
        assertEquals(date2, (MyDateConverter.fromTimestamp(MyDateConverter.dateToTimestamp(date2))));
    }

    @Test
    public void myLocationConverterTest(){
        MyLocation loc1 = new MyLocation(80,80);
        MyLocation loc2 = new MyLocation(-80,-80);
        String loc3 = "-60.0,60.0";
        String loc4 = "50.0,-70.0";

        assertEquals(loc1, (MyLocationConverter.strToMyLocation(MyLocationConverter.myLocationToString(loc1))));
        assertEquals(loc2, (MyLocationConverter.strToMyLocation(MyLocationConverter.myLocationToString(loc2))));
        assertEquals(loc3,MyLocationConverter.myLocationToString(MyLocationConverter.strToMyLocation(loc3)));
        assertEquals(loc4,MyLocationConverter.myLocationToString(MyLocationConverter.strToMyLocation(loc4)));
    }
}
