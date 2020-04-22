package ch.epfl.sdp.musiconnect;

import android.content.Context;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

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
}