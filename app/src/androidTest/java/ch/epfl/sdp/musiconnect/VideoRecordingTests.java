package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.VideoPlayingTests.childAtPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
public class VideoRecordingTests {

    @Rule
    public ActivityTestRule<StartPage> mActivityTestRule = new ActivityTestRule<>(StartPage.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void clickAlert() {
        MapsLocationTest.clickAllow();
    }

    @Test
    public void videoRecordingTests() {
        VideoPlayingTests.goToMyProfilePage();

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btnEditProfile), withText("Edit profile"),
                        childAtPosition(childAtPosition(withClassName(is("android.widget.LinearLayout")),
                                3),0)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btnCaptureVideo), withText("add/Change Video"),
                        childAtPosition(childAtPosition(withClassName(is("android.widget.LinearLayout")),0),2)));
        appCompatButton2.perform(scrollTo(), click());
    }
}
