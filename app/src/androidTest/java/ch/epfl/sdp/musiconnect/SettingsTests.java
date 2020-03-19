package ch.epfl.sdp.musiconnect;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class SettingsTests {

    @Rule
    public final ActivityTestRule<SettingsPage> settingsPageRule =
            new ActivityTestRule<>(SettingsPage.class);

    /**
     * Helper method to avoid duplicate code
     * @param stringId
     */
    private void openActionsMenu(int stringId) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(stringId)).perform(click());
    }

    @Test
    public void testSettingsClickShouldDoNothing() {
        openActionsMenu(R.string.settings);
        assert(true);
    }

    @Test
    public void testSearchClickFromSettingsShouldDisplayMessage() {
        onView(withId(R.id.search)).perform(click());
        onView(withText(R.string.not_yet_done)).inRoot(withDecorView(not(settingsPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }
}
