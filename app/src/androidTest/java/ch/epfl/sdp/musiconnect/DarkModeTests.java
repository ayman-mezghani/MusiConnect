package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.widget.Switch;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.core.content.ContextCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sdp.R;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DarkModeTests {

    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    @Test
    public void checkDefaultModeIsLight() {
        TextView title = startPageRule.getActivity().findViewById(R.id.welcomeText);

        assertThat(title.getCurrentTextColor(), is(ContextCompat.getColor(startPageRule.getActivity().getApplicationContext(), R.color.text_black)));
    }

    @Test
    public void checkDefaultCheckedStatusIsUnchecked() {
        Switch darkModeSwitch = startPageRule.getActivity().findViewById(R.id.darkModeSwitch);
        assertFalse(darkModeSwitch.isChecked());
    }

    @Test
    public void checkThemeStaysTheSameWhenAppIsRestarted() {
        startPageRule.finishActivity();

        Intent startPageIntent = new Intent();
        startPageRule.launchActivity(startPageIntent);

        TextView title = startPageRule.getActivity().findViewById(R.id.welcomeText);
        assertThat(title.getCurrentTextColor(), is(ContextCompat.getColor(startPageRule.getActivity().getApplicationContext(), R.color.text_black)));
    }

    //@Test
    public void checkColorChangeWhenSwitchButtonIsToggled() {
        Switch darkModeSwitch = startPageRule.getActivity().findViewById(R.id.darkModeSwitch);
        assertFalse(darkModeSwitch.isChecked());
    }
}
