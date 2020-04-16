package ch.epfl.sdp.musiconnect;

import android.content.Context;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CurrentUserTest {
    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    @Before
    public void stallBefore() {
        CurrentUser.flush();
    }

    @Test
    public void flagNotSet() {
        Context context = startPageRule.getActivity();
        assertFalse(CurrentUser.getInstance(context).getCreatedFlag());
    }

    @Test
    public void flagSet() {
        Context context = startPageRule.getActivity();
        CurrentUser.getInstance(context).setCreatedFlag();
        assertTrue(CurrentUser.getInstance(context).getCreatedFlag());
    }

    @Test
    public void flushTest() {
        Context context = startPageRule.getActivity();
        CurrentUser.getInstance(context).setCreatedFlag();
        assertTrue(CurrentUser.getInstance(context).getCreatedFlag());
        CurrentUser.flush();
        assertFalse(CurrentUser.getInstance(context).getCreatedFlag());
    }
}