package ch.epfl.sdp.musiconnect;

import android.content.Context;

import androidx.test.rule.ActivityTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CurrentUserTest {
    @Rule
    public final ActivityTestRule<GoogleLogin> pageRule =
            new ActivityTestRule<>(GoogleLogin.class);

    @BeforeClass
    public static void setMockDB() {
        DbGenerator.setDatabase(new MockDatabase());
    }

    @Before
    public void flushBefore() {
        CurrentUser.flush();
    }

    @Test
    public void flagNotSet() {
        Context context = pageRule.getActivity();
        assertFalse(CurrentUser.getInstance(context).getCreatedFlag());
    }

    @Test
    public void flagSet() {
        Context context = pageRule.getActivity();
        CurrentUser.getInstance(context).setCreatedFlag();
        assertTrue(CurrentUser.getInstance(context).getCreatedFlag());
    }

    @Test
    public void flushTest() {
        Context context = pageRule.getActivity();
        CurrentUser.getInstance(context).setCreatedFlag();
        assertTrue(CurrentUser.getInstance(context).getCreatedFlag());
        CurrentUser.flush();
        assertFalse(CurrentUser.getInstance(context).getCreatedFlag());
    }

    @Test
    public void testsMusician() {
        String firstName = "Espresso";
        String lastName = "Tests";
        String userName = "testsEspresso";
        String emailAddress = "espressotests@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);
        CurrentUser.getInstance(pageRule.getActivity()).setMusician(john);
        assertEquals(john.getEmailAddress(), CurrentUser.getInstance(pageRule.getActivity()).getMusician().getEmailAddress());
    }

    @Test
    public void setBandName() {
        String firstName = "Espresso";
        String lastName = "Tests";
        String userName = "testsEspresso";
        String emailAddress = "espressotests@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);
        john.setTypeOfUser(TypeOfUser.Band);
        CurrentUser.getInstance(pageRule.getActivity()).setMusician(john);
        CurrentUser.getInstance(pageRule.getActivity()).setBandName("bandName");
        assertEquals("bandName", CurrentUser.getInstance(pageRule.getActivity()).getBandName());
    }

    @Test
    public void setBandNameFails() {
        String firstName = "Espresso";
        String lastName = "Tests";
        String userName = "testsEspresso";
        String emailAddress = "espressotests@gmail.com";
        MyDate birthday = new MyDate(1940, 10, 9);
        Musician john = new Musician(firstName, lastName, userName, emailAddress, birthday);
        john.setTypeOfUser(TypeOfUser.Musician);
        CurrentUser.getInstance(pageRule.getActivity()).setMusician(john);
        try
        {
            CurrentUser.getInstance(pageRule.getActivity()).setBandName("bandName");
            Assert.fail("Should have thrown Arithmetic exception");
        }
        catch(IllegalArgumentException e)
        {
            Assert.assertTrue(true);
        }
    }

}