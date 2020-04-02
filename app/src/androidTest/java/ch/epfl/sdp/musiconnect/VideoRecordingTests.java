package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

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

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void videoRecordingTests() {
        MapsLocationTest.clickAllow();
        VideoPlayingTests.goToMyProfilePage();

        ViewInteraction appCompatButton = onView(allOf(withText("add/Edit Video"),
                        VideoPlayingTests.childAtPosition(VideoPlayingTests.childAtPosition(withClassName(is("android.widget.LinearLayout")),
                                        0),1),isDisplayed()));

        //appCompatButton.perform(click());
    }
}
