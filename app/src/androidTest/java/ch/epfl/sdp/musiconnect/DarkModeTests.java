package ch.epfl.sdp.musiconnect;

import android.content.Context;
import android.content.Intent;
import android.widget.Switch;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.core.content.ContextCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import ch.epfl.sdp.R;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DarkModeTests {
    private UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    /**
     * Get string value from strings.xml file
     * @param id: string id
     * @return: string value
     */
    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        return targetContext.getResources().getString(id);
    }

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
    public void checkColorChangeWhenSwitchButtonIsToggled() {
        TextView title = startPageRule.getActivity().findViewById(R.id.welcomeText);
        UiObject2 toggle = device.findObject(By.textStartsWith(getResourceString(R.string.dark_mode)));
        toggle.click();

        assertThat(title.getCurrentTextColor(), is(ContextCompat.getColor(startPageRule.getActivity().getApplicationContext(), R.color.text_grey)));
    }

    @Test
    public void checkThemeStaysTheSameWhenAppIsRestarted() {
        UiObject2 toggle = device.findObject(By.textStartsWith(getResourceString(R.string.dark_mode)));
        toggle.click();
        startPageRule.finishActivity();

        Intent startPageIntent = new Intent();
        startPageRule.launchActivity(startPageIntent);

        TextView title = startPageRule.getActivity().findViewById(R.id.welcomeText);
        assertThat(title.getCurrentTextColor(), is(ContextCompat.getColor(startPageRule.getActivity().getApplicationContext(), R.color.text_grey)));
    }
}
