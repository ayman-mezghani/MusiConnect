package ch.epfl.sdp.musiconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Switch;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sdp.R;

import static ch.epfl.sdp.musiconnect.StartPage.KEY_ISNIGHTMODE;
import static ch.epfl.sdp.musiconnect.StartPage.MY_PREFERENCES;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DarkModeTests {

    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() { Intents.init(); }

    @After
    public void releaseIntents() { Intents.release(); }

    @Test
    public void testDefaultStartThemeIsDark() {
        TextView title = startPageRule.getActivity().findViewById(R.id.welcomeText);

        assertThat(title.getCurrentTextColor(), is(ContextCompat.getColor(startPageRule.getActivity().getApplicationContext(), R.color.text_grey)));
    }

    @Test
    public void testPreferencesStayTheSameWhenAppIsRestarted() {
        SharedPreferences sp = startPageRule.getActivity().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        startPageRule.finishActivity();
        Intent startPageIntent = new Intent();
        startPageRule.launchActivity(startPageIntent);

        Switch darkModeSwitch = startPageRule.getActivity().findViewById(R.id.darkModeSwitch);
        assertFalse(darkModeSwitch.isChecked());
        assertFalse(sp.getBoolean(KEY_ISNIGHTMODE, false));
    }

    @Test
    public void testPreferencesCanChange() {
        SharedPreferences sp = startPageRule.getActivity().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_ISNIGHTMODE, true).apply();

        assertTrue(sp.getBoolean(KEY_ISNIGHTMODE, false));
    }

    @Test
    public void saveNightModeState() {
        SharedPreferences sp = startPageRule.getActivity().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        sp.edit().putBoolean(KEY_ISNIGHTMODE, true).apply();
    }
}
