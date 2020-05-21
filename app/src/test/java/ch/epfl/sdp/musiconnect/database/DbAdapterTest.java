package ch.epfl.sdp.musiconnect.database;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DbAdapterTest {
    private DbAdapter dbAdapter;
    private Musician musician;
    private Band band;

    private SimplifiedMusician sm;
    private SimplifiedBand sb;
    private Map<String, Object> queryMap;

    private String bandName = "theBand";
    private String firstName = "test";
    private String lastName = "user";
    private String username = "testuser";
    private String emailAddress = "testuser@gmail.com";
    private MyDate birthDate = new MyDate(2000, 1, 1);

    @Before
    public void init() {
        musician = new Musician(firstName, lastName, username, emailAddress, birthDate);
        band = new Band(bandName, musician.getEmailAddress());
        sm = new SimplifiedMusician(musician);
        sb = new SimplifiedBand(band);
        queryMap = new HashMap<>();
        DbSingleton.flush();
    }

    @Test
    public void addMusicianTest() {
        DbSingleton.setDatabase(new MockDatabaseForUT(DbDataType.Musician.toString(), emailAddress, sm, sm.toMap()));
        dbAdapter = DbSingleton.getDbInstance();
        dbAdapter.add(DbDataType.Musician, musician);
    }

    @Test
    public void addBandTest() {
        DbSingleton.setDatabase(new MockDatabaseForUT(DbDataType.Musician.toString(), emailAddress, sb, sb.toMap()));
        dbAdapter = DbSingleton.getDbInstance();
        dbAdapter.add(DbDataType.Band, band);
    }

    @Test
    public void deleteMusicianTest() {
        DbSingleton.setDatabase(new MockDatabaseForUT(DbDataType.Musician.toString(), emailAddress, sm, sm.toMap()));
        dbAdapter = DbSingleton.getDbInstance();
        dbAdapter.delete(DbDataType.Musician, musician);
    }

    @Test
    public void deleteBandTest() {
        DbSingleton.setDatabase(new MockDatabaseForUT(DbDataType.Musician.toString(), emailAddress, sb, sb.toMap()));
        dbAdapter = DbSingleton.getDbInstance();
        dbAdapter.delete(DbDataType.Band, band);
    }

    @Test
    public void updateMusicianTest() {
        DbSingleton.setDatabase(new MockDatabaseForUT(DbDataType.Musician.toString(), emailAddress, sm, sm.toMap()));
        dbAdapter = DbSingleton.getDbInstance();
        dbAdapter.update(DbDataType.Musician, musician);
    }

    @Test
    public void updateBandTest() {
        DbSingleton.setDatabase(new MockDatabaseForUT(DbDataType.Musician.toString(), emailAddress, sb, sb.toMap()));
        dbAdapter = DbSingleton.getDbInstance();
        dbAdapter.update(DbDataType.Band, band);
    }

    @Test
    public void readTest() {
        DbSingleton.setDatabase(new MockDatabaseForUT(DbDataType.Musician.toString(), emailAddress, sm, sm.toMap()));
        dbAdapter = DbSingleton.getDbInstance();
        dbAdapter.read(DbDataType.Musician, musician.getEmailAddress(), new DbCallback() {
            @Override
            public void readCallback(User user) {
                assertEquals(new SimplifiedMusician((Musician) user), new SimplifiedMusician(musician));
            }
        });
    }

    @Test
    public void existsTest() {
        DbSingleton.setDatabase(new MockDatabaseForUT(DbDataType.Musician.toString(), emailAddress, sm, sm.toMap()));
        dbAdapter = DbSingleton.getDbInstance();
        dbAdapter.exists(DbDataType.Musician, musician.getEmailAddress(), new DbCallback() {
            @Override
            public void existsCallback(boolean exists) {
                assertFalse(exists);
            }
        });
    }

    @Test
    public void queryTest() {
        queryMap.put("firstName", "test");
        DbSingleton.setDatabase(new MockDatabaseForUT(DbDataType.Musician.toString(), emailAddress, sm, queryMap));
        dbAdapter = DbSingleton.getDbInstance();
        dbAdapter.query(DbDataType.Musician, queryMap, new DbCallback() {
            @Override
            public void queryCallback(List<User> userList) {
                assertTrue(userList.size() > 0);
            }
        });
    }
}
