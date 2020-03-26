package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
public class VideoRecordingTests {

    @Rule
    public ActivityTestRule<StartPage> mActivityTestRule = new ActivityTestRule<>(StartPage.class);

    @Test
    public void videoRecordingTests() {
        VideoPlayingTests.goToMyProfilePage();

        ViewInteraction appCompatButton = onView(allOf(withText("add/Edit Video"),
                        VideoPlayingTests.childAtPosition(VideoPlayingTests.childAtPosition(withClassName(is("android.widget.LinearLayout")),
                                        0),1),isDisplayed()));
        appCompatButton.perform(click());
    }
}
