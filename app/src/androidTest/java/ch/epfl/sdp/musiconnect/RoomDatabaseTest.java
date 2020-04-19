package ch.epfl.sdp.musiconnect;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ch.epfl.sdp.musiconnect.roomdatabase.AppDatabase;
import ch.epfl.sdp.musiconnect.roomdatabase.InstrumentConverter;
import ch.epfl.sdp.musiconnect.roomdatabase.MusicianDao;
import ch.epfl.sdp.musiconnect.roomdatabase.MyDateConverter;
import ch.epfl.sdp.musiconnect.roomdatabase.MyLocationConverter;

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
        waitALittle(5);
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
        waitALittle(5);
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
        waitALittle(5);
        assertEquals(2,users.size());

        mExecutor.execute(() -> {
            musicianDao.delete(person2);
        });
        mExecutor.execute(() -> {
            users = musicianDao.getAll();
        });
        waitALittle(5);
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
        waitALittle(5);
        mExecutor.execute(() -> {
            users = musicianDao.loadAllByIds(new String[]{"sauce@gmail.com"});
        });
        waitALittle(5);
        assertEquals(1,users.size());
        Musician user1 = users.get(0);
        assertEquals(person1.getFirstName()+person1.getLastName()+person1.getEmailAddress()+person1.getUserName()+person1.getAge()+person1.getVideoURL(),
                user1.getFirstName()+user1.getLastName()+user1.getEmailAddress()+user1.getUserName()+user1.getAge()+user1.getVideoURL());
        assertEquals(person1.getJoinDate(),user1.getJoinDate());
        assertEquals(person1.getInstruments(),(user1.getInstruments()));
    }

    @Test
    public void instrumentConverterTest(){
        HashMap<Instrument,Level> map = new HashMap<>();
        for(Instrument i:Instrument.values()){
            for(Level l:Level.values()){

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

    public static void waitALittle(int t) {
        try {
            TimeUnit.SECONDS.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
