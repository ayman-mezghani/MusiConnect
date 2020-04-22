package ch.epfl.sdp.musiconnect.database;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DbAdapterTest {
    private DbAdapter dbAdapter;
    private Musician musician;

    private String firstName = "test";
    private String lastName = "user";
    private String username = "testuser";
    private String emailAddress = "testuser@gmail.com";
    private MyDate birthDate = new MyDate(2000,1,1);

    @Before
    public void init() {
        musician = new Musician(firstName, lastName, username, emailAddress, birthDate);
        SimplifiedMusician sm = new SimplifiedMusician(musician);
        DbGenerator.setDatabase(new MockDatabaseForUT(emailAddress, sm, sm.toMap()));
        dbAdapter = DbGenerator.getDbInstance();
    }

    @Test
    public void addTest() {
        dbAdapter.add(musician);
    }

    @Test
    public void deleteTest() {
        dbAdapter.delete(musician);
    }

    @Test
    public void updateTest() {
        dbAdapter.update(musician);
    }

    @Test
    public void readTest() {
        dbAdapter.read(musician.getEmailAddress(), new DbCallback() {
            @Override
            public void readCallback(User user) {
                assertEquals(new SimplifiedMusician((Musician) user), new SimplifiedMusician(musician));
            }
        });
    }

    @Test
    public void existsTest() {
        dbAdapter.exists(musician.getEmailAddress(), new DbCallback() {
            @Override
            public void existsCallback(boolean exists) {
                assertFalse(exists);
            }
        });
    }
}
