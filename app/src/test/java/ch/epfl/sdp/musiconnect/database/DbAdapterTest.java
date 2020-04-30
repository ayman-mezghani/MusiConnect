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
        band = new Band(bandName, musician);
        sm = new SimplifiedMusician(musician);
        sb = new SimplifiedBand(band);
        queryMap = new HashMap<>();
        DbGenerator.flush();
    }

    @Test
    public void addMusicianTest() {
        DbGenerator.setDatabase(new MockDatabaseForUT(DbUserType.Musician.toString(), emailAddress, sm, sm.toMap()));
        dbAdapter = DbGenerator.getDbInstance();
        dbAdapter.add(DbUserType.Musician, musician);
    }

    @Test
    public void addBandTest() {
        DbGenerator.setDatabase(new MockDatabaseForUT(DbUserType.Musician.toString(), emailAddress, sb, sb.toMap()));
        dbAdapter = DbGenerator.getDbInstance();
        dbAdapter.add(DbUserType.Band, band);
    }

    @Test
    public void deleteMusicianTest() {
        DbGenerator.setDatabase(new MockDatabaseForUT(DbUserType.Musician.toString(), emailAddress, sm, sm.toMap()));
        dbAdapter = DbGenerator.getDbInstance();
        dbAdapter.delete(DbUserType.Musician, musician);
    }

    @Test
    public void deleteBandTest() {
        DbGenerator.setDatabase(new MockDatabaseForUT(DbUserType.Musician.toString(), emailAddress, sb, sb.toMap()));
        dbAdapter = DbGenerator.getDbInstance();
        dbAdapter.delete(DbUserType.Band, band);
    }

    @Test
    public void updateMusicianTest() {
        DbGenerator.setDatabase(new MockDatabaseForUT(DbUserType.Musician.toString(), emailAddress, sm, sm.toMap()));
        dbAdapter = DbGenerator.getDbInstance();
        dbAdapter.update(DbUserType.Musician, musician);
    }

    @Test
    public void updateBandTest() {
        DbGenerator.setDatabase(new MockDatabaseForUT(DbUserType.Musician.toString(), emailAddress, sb, sb.toMap()));
        dbAdapter = DbGenerator.getDbInstance();
        dbAdapter.update(DbUserType.Band, band);
    }

    @Test
    public void readTest() {
        DbGenerator.setDatabase(new MockDatabaseForUT(DbUserType.Musician.toString(), emailAddress, sm, sm.toMap()));
        dbAdapter = DbGenerator.getDbInstance();
        dbAdapter.read(DbUserType.Musician, musician.getEmailAddress(), new DbCallback() {
            @Override
            public void readCallback(User user) {
                assertEquals(new SimplifiedMusician((Musician) user), new SimplifiedMusician(musician));
            }
        });
    }

    @Test
    public void existsTest() {
        DbGenerator.setDatabase(new MockDatabaseForUT(DbUserType.Musician.toString(), emailAddress, sm, sm.toMap()));
        dbAdapter = DbGenerator.getDbInstance();
        dbAdapter.exists(DbUserType.Musician, musician.getEmailAddress(), new DbCallback() {
            @Override
            public void existsCallback(boolean exists) {
                assertFalse(exists);
            }
        });
    }

    @Test
    public void queryTest() {
        queryMap.put("firstName", "test");
        DbGenerator.setDatabase(new MockDatabaseForUT(DbUserType.Musician.toString(), emailAddress, sm, queryMap));
        dbAdapter = DbGenerator.getDbInstance();
        dbAdapter.query(DbUserType.Musician, queryMap, new DbCallback() {
            @Override
            public void queryCallback(List<User> userList) {
                assertTrue(userList.size() > 0);
            }
        });
    }
}
