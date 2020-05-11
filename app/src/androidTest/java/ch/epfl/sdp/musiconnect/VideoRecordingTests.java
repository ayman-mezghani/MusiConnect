package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.location.MapsLocationFunctions;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.testsFunctions.childAtPosition;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;

@LargeTest
public class VideoRecordingTests {

    @Rule
    public ActivityTestRule<StartPage> mActivityTestRule = new ActivityTestRule<>(StartPage.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private static boolean setUpIsDone = false;

    private void clickAlerts() {
        if (setUpIsDone) {
            return;
        }
        MapsLocationFunctions.clickPermissionAlert();
        setUpIsDone = true;
    }

    @Before
    public void setUp() {
        clickAlerts();
    }

    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase());
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

    @Test
    public void videoRecordingTests() {
        onView(withId(R.id.my_profile)).perform(click());

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
