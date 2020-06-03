package ch.epfl.sdp.musiconnect.location;


import android.app.ActivityManager;
import android.content.Context;
import android.location.Location;
import android.provider.CalendarContract;

import androidx.core.app.NotificationCompat;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.events.Event;
import ch.epfl.sdp.musiconnect.events.EventCreationPage;
import ch.epfl.sdp.musiconnect.roomdatabase.AppDatabase;
import ch.epfl.sdp.musiconnect.roomdatabase.EventDao;
import ch.epfl.sdp.musiconnect.roomdatabase.MusicianDao;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.testsFunctions.waitSeconds;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class MapsActivityTest {
    private List<Musician> users = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private AppDatabase localDb;
    private MusicianDao musicianDao;
    private EventDao eventDao;
    private Executor mExecutor = Executors.newSingleThreadExecutor();


    @Rule
    public final ActivityTestRule<MapsActivity> mapsActivityRule =
            new ActivityTestRule<>(MapsActivity.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void setMocks() {
        DbSingleton.setDatabase(new MockDatabase(false));
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }

    @Before
    public void initIntents() {
        Intents.init();
        localDb = AppDatabase.getInstance(mapsActivityRule.getActivity().getApplicationContext());
        musicianDao = localDb.musicianDao();
        eventDao = localDb.eventDao();
        mExecutor.execute(() -> {
            musicianDao.nukeTable();
            eventDao.nukeTable();
        });
        waitSeconds(1);
    }

    @After
    public void releaseIntents() {
        Intents.release();
        mExecutor.execute(() -> {
            musicianDao.nukeTable();
            eventDao.nukeTable();
        });
        waitSeconds(1);
    }


    @Test
    public void notificationChannelGeneratesCorrectly() {
        assertTrue(mapsActivityRule.getActivity().createNotificationChannel());
    }

    @Test
    public void alertWarningGeneratesCorrectly(){
        NotificationCompat.Builder notif = mapsActivityRule.getActivity().buildNotification("test");
        assertEquals(NotificationCompat.PRIORITY_MAX,notif.getPriority());
    }

    @Test
    public void testEventAlertShowsAndCancels(){
        try {
            mapsActivityRule.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    mapsActivityRule.getActivity().createAlert(new LatLng(0,0));
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        onView(withText("Do you want to create an event?")).check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).perform(click());
        onView(withText("Do you want to create an event?")).check(doesNotExist());
    }

    @Test
    public void testEventAlertGoesToNewEventPage(){
        try {
            mapsActivityRule.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    mapsActivityRule.getActivity().createAlert(new LatLng(46.5253,6.5606));
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        onView(withId(android.R.id.button1)).perform(click());
        intended(hasComponent(EventCreationPage.class.getName()));
    }

    @Test
    public void testLocationServices(){
        assertEquals(true,mapsActivityRule.getActivity().checkLocationServices());
    }

    @Test
    public void testSetLocation(){
        Location l = new Location("test");
        l.setLongitude(26);
        l.setLatitude(15);
        mapsActivityRule.getActivity().setLocation(l);
        assertTrue(l.getLatitude() == mapsActivityRule.getActivity().setLoc.getLatitude()
                && l.getLongitude() == mapsActivityRule.getActivity().setLoc.getLongitude());

    }

    @Test
    public void testGetLastLocation(){

        mapsActivityRule.getActivity().getLastLocation();
        assertEquals(true,mapsActivityRule.getActivity().updatePos);
        //assertTrue(mapsActivityRule.getActivity().setLoc != null);
        assertTrue(isLocServiceRunning(mapsActivityRule.getActivity().getApplicationContext()));
    }

    private boolean isLocServiceRunning(Context context){
        Class<?> serviceClass = LocationService.class;
        ActivityManager manager = (ActivityManager) context. getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClass.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    @Test
    public void testUpdateProfileList(){
        Location l = new Location("test");
        l.setLongitude(26);
        l.setLatitude(15);
        mapsActivityRule.getActivity().setLocation(l);
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician m = new Musician("gg","Grospardieu","h","reeeeee@gmail.com",birthday);
        mapsActivityRule.getActivity().allUsers.add(m);
        mapsActivityRule.getActivity().updateProfileList();
        assertTrue(mapsActivityRule.getActivity().markers.isEmpty());
        assertTrue(mapsActivityRule.getActivity().profiles.isEmpty());
    }

    @Test
    public void testUpdateEventList(){
        Location l = new Location("test");
        l.setLongitude(26);
        l.setLatitude(15);
        mapsActivityRule.getActivity().setLocation(l);
        Event e = new Event("host@gmail.com","jej");
        mapsActivityRule.getActivity().events.add(e);
        mapsActivityRule.getActivity().updateEvents();
        assertTrue(mapsActivityRule.getActivity().eventNear.isEmpty());
        assertTrue(mapsActivityRule.getActivity().eventMarkers.isEmpty());
    }

    @Test
    public void testsaveToCache(){

        MyDate birthday = new MyDate(1940, 10, 9);
        Musician m = new Musician("gg","Grospardieu","h","reeeeee@gmail.com",birthday);
        mapsActivityRule.getActivity().allUsers.clear();
        mapsActivityRule.getActivity().allUsers.add(m);
        Event e = new Event("host@gmail.com","jej");
        mapsActivityRule.getActivity().eventNear.clear();
        mapsActivityRule.getActivity().eventNear.add(e);
        mapsActivityRule.getActivity().saveToCache();
        waitSeconds(1);

        mExecutor.execute(() -> {
            users = musicianDao.getAll();
            events = eventDao.getAll();
        });

        waitSeconds(1);

        assertEquals(1,users.size());
        Musician user1 = users.get(0);
        assertEquals(m.getFirstName()+m.getLastName()+m.getEmailAddress()+m.getUserName(),
                user1.getFirstName()+user1.getLastName()+user1.getEmailAddress()+user1.getUserName());
        assertEquals(1,events.size());
        assertEquals( e.getEid(),
                 events.get(0).getEid());
    }

    @Test
    public void testloadCache(){
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician m = new Musician("gg","Grospardieu","h","reeeeee@gmail.com",birthday);
        Event e = new Event("host@gmail.com","jej");
        mExecutor.execute(() -> {
            musicianDao.insertAll(m);
            eventDao.insertAll(e);
        });
        waitSeconds(1);
        mapsActivityRule.getActivity().loadCache();
        waitSeconds(1);
        assertTrue(!mapsActivityRule.getActivity().events.isEmpty());
        assertTrue(!mapsActivityRule.getActivity().allUsers.isEmpty());
    }

    @Test
    public void testClearCache(){
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician m = new Musician("gg","Grospardieu","h","reeeeee@gmail.com",birthday);
        Event e = new Event("host@gmail.com","jej");
        mExecutor.execute(() -> {
            musicianDao.insertAll(m);
            eventDao.insertAll(e);
        });
        waitSeconds(1);
        mapsActivityRule.getActivity().clearCache();
        waitSeconds(1);
        mExecutor.execute(() -> {
            users = musicianDao.getAll();
            events = eventDao.getAll();
        });
        waitSeconds(1);
        assertTrue(users.isEmpty());
        assertTrue(events.isEmpty());
    }
}