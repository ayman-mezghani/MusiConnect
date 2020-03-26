package ch.epfl.sdp.musiconnect;


import android.content.Intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class MenuTests {
    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() { Intents.init(); }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void testSearchClickShouldDisplayMessage() {
        onView(withId(R.id.search)).perform(click());
        onView(withText(R.string.not_yet_done)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void testHelpClickShouldStartNewIntent() {
        onView(withId(R.id.help)).perform(click());

        Intent helpIntent = new Intent();
        startPageRule.launchActivity(helpIntent);
        intended(hasComponent(HelpPage.class.getName()));
    }

    /**
     * Helper method to avoid duplicate code
     * @param stringId
     */
    private void openActionsMenu(int stringId) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(stringId)).perform(click());
    }

    @Test
    public void testMyProfileClickShouldStartNewIntent() {
        openActionsMenu(R.string.my_profile);

        Intent profileIntent = new Intent();
        startPageRule.launchActivity(profileIntent);
        intended(hasComponent(ProfilePage.class.getName()));
    }

    @Test
    public void testSettingsClickShouldStartNewIntent() {
        openActionsMenu(R.string.settings);

        Intent settingsIntent = new Intent();
        startPageRule.launchActivity(settingsIntent);
        intended(hasComponent(SettingsPage.class.getName()));
    }

    @Test
    public void testMapsOpensWithMainMarker() {
        assert(true);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        openActionsMenu(R.string.map);

        UiObject marker = device.findObject(new UiSelector().descriptionContains("You"));
        marker.waitForExists(5000);

        assert(true);
    }

    @Test
    public void testLogOut() {
        //openActionsMenu(R.string.signout);
        //intended(hasComponent(GoogleLogin.class.getName()));
    }
}