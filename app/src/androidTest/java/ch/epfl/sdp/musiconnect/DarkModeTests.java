package ch.epfl.sdp.musiconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Checkable;
import android.widget.Switch;
import android.widget.TextView;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sdp.musiconnect.StartPage.KEY_ISNIGHTMODE;
import static ch.epfl.sdp.musiconnect.StartPage.MY_PREFERENCES;
import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
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
    public void testDefaultStartThemeIsLight() {
        TextView title = startPageRule.getActivity().findViewById(R.id.welcomeText);

        assertThat(title.getCurrentTextColor(), is(ContextCompat.getColor(startPageRule.getActivity().getApplicationContext(), R.color.text_black)));
    }

    @Test
    public void testPreferencesStayTheSameWhenAppIsRestarted() {
        SharedPreferences sp = startPageRule.getActivity().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        startPageRule.finishActivity();
        Intent startPageIntent = new Intent();
        startPageRule.launchActivity(startPageIntent);

        sp.edit().putBoolean(KEY_ISNIGHTMODE, false).apply();

        Switch darkModeSwitch = startPageRule.getActivity().findViewById(R.id.darkModeSwitch);
        assertFalse(darkModeSwitch.isChecked());
    }

    @Test
    public void testThemeGoesFromLightToDark() {
        SharedPreferences sp = startPageRule.getActivity().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sp.edit().putBoolean(KEY_ISNIGHTMODE, false).apply();

        assertFalse(sp.getBoolean(KEY_ISNIGHTMODE, false));
    }

    public static ViewAction setChecked(final boolean checked) {
        return new ViewAction() {
            @Override
            public BaseMatcher<View> getConstraints() {
                return new BaseMatcher<View>() {
                    @Override
                    public boolean matches(Object item) {
                        return isA(Checkable.class).matches(item);
                    }

                    @Override
                    public void describeMismatch(Object item, Description mismatchDescription) {}

                    @Override
                    public void describeTo(Description description) {}
                };
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                Checkable checkableView = (Checkable) view;
                checkableView.setChecked(checked);
            }
        };
    }

    @Test
    public void testNightModeStateChangeDoesChange() {
        onView(withId(R.id.darkModeSwitch)).perform(scrollTo(), setChecked(true));

        TextView title = startPageRule.getActivity().findViewById(R.id.welcomeText);
        assertThat(title.getCurrentTextColor(), is(ContextCompat.getColor(startPageRule.getActivity().getApplicationContext(), R.color.text_grey)));
    }
}
