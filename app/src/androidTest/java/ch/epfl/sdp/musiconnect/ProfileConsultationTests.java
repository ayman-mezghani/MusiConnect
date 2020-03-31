package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ProfileConsultationTests {
    @Rule
    public final ActivityTestRule<ProfilePage> profilePageRule =
            new ActivityTestRule<>(ProfilePage.class);

    // Before and after methods are used in order to accept tests with intents
    @Before
    public void initIntents() { Intents.init(); }

    @After
    public void releaseIntents() { Intents.release(); }

    @Test
    public void testEditButtonShouldStartNewIntent() {
        onView(withText(R.string.edit_profile_button_text)).perform(click());
        intended(hasComponent(ProfileModification.class.getName()));
    }
}
