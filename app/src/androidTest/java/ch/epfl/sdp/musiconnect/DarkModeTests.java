package ch.epfl.sdp.musiconnect;

import android.content.Context;
import android.content.Intent;
import android.widget.Switch;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.testsFunctions.childAtPosition;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DarkModeTests {
    private UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() { Intents.release(); }

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
    public void checkThemeStaysTheSameWhenAppIsRestarted() {
        startPageRule.finishActivity();

        Intent startPageIntent = new Intent();
        startPageRule.launchActivity(startPageIntent);

        TextView title = startPageRule.getActivity().findViewById(R.id.welcomeText);
        assertThat(title.getCurrentTextColor(), is(ContextCompat.getColor(startPageRule.getActivity().getApplicationContext(), R.color.text_black)));
    }

    @Test
    public void checkColorChangeWhenSwitchButtonIsToggled() {
        ViewInteraction switch_ = onView(
                allOf(withId(R.id.darkModeSwitch), withText("Dark Mode:"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                2)));
        switch_.perform(scrollTo(), click());

        Switch darkModeSwitch = startPageRule.getActivity().findViewById(R.id.darkModeSwitch);
        assertTrue(darkModeSwitch.isChecked());
    }
}
