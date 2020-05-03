package ch.epfl.sdp.musiconnect;

import android.net.Uri;
import android.widget.VideoView;

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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.testsFunctions.childAtPosition;
import static ch.epfl.sdp.musiconnect.testsFunctions.getCurrentActivity;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

@LargeTest
public class VideoPlayingTests {

    private static boolean setUpIsDone = false;

    public void clickAlerts() {
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



    @Rule
    public ActivityTestRule<StartPage> mActivityTestRule = new ActivityTestRule<>(StartPage.class);


    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase());
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

    public static void goToMyProfilePage() {
        ViewInteraction overflowMenuButton = onView(allOf(withContentDescription("More options"),
                childAtPosition(childAtPosition(withId(R.id.action_bar),1),2), isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction appCompatTextView = onView(allOf(withId(R.id.title), withText("My profile"),
                childAtPosition(childAtPosition(withId(R.id.content),0),0),isDisplayed()));
        appCompatTextView.perform(click());
    }

//    private String packageName = "ch.epfl.sdp.musiconnect";
    @Test
    public void playVideoTest() throws InterruptedException {
        String packageName = mActivityTestRule.getActivity().getPackageName();
        String videoSource = "android.resource://"+packageName+"/"+ R.raw.minion;

        goToMyProfilePage();

        ((MyProfilePage) testsFunctions.getCurrentActivity()).videoUri = Uri.parse(videoSource);

        ViewInteraction videoView = onView(allOf(withId(R.id.videoView),
                childAtPosition(childAtPosition(withClassName(is("android.widget.LinearLayout")),
                        2),0),isDisplayed()));

        VideoView v = getCurrentActivity().findViewById(R.id.videoView);
        Thread.sleep(1000); // waiting util the vidéo is loaded from internet
        assertTrue(v.getDuration() > -1); // test if the video is loaded
    }
}
